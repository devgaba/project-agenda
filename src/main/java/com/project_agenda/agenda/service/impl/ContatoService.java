package com.project_agenda.agenda.service.impl;

import com.project_agenda.agenda.dto.ContatoDTO;
import com.project_agenda.agenda.dto.EnderecoDTO;
import com.project_agenda.agenda.entity.Contato;
import com.project_agenda.agenda.entity.Endereco;
import com.project_agenda.agenda.exception.EmailExistenteException;
import com.project_agenda.agenda.repository.ContatoRepository;
import com.project_agenda.agenda.repository.EnderecoRepository;
import com.project_agenda.agenda.service.IContatoService;
import com.project_agenda.agenda.utils.BeanCopyUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

import static com.project_agenda.agenda.utils.BeanCopyUtils.copiarPropriedadesNaoNulas;


@Service
public class ContatoService implements IContatoService {


    @Autowired
    private ContatoRepository contatoRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Override
    public List<Contato> exibirContatos() {

        List<Contato> contatos = contatoRepository.findAll();

        if(!contatos.isEmpty()){
            return contatos;
        }
        return null;
    }

    @Override
    @Transactional
    public ContatoDTO criarContato(ContatoDTO contatoDTO) {

        Optional<Contato> email = contatoRepository.findByEmail(contatoDTO.getEmail());

        if(email.isEmpty()){

            List<Endereco> enderecos = new ArrayList<>();
            List<EnderecoDTO> enderecosDtoVisualizar = new ArrayList<>();
            Contato contatoCriado = Contato.builder()
                    .nome(contatoDTO.getNome())
                    .email(contatoDTO.getEmail())
                    .telefone(contatoDTO.getTelefone())
                    .dataNascimento(contatoDTO.getDataNascimento())
                    .enderecoLista(enderecos)
                    .build();

            enderecos.addAll(contatoDTO.getEnderecoLista().stream().map(enderecoDTO -> {
                return Endereco.builder()
                        .nomeRua(enderecoDTO.getNomeRua())
                        .numeroRua(enderecoDTO.getNumeroRua())
                        .cep(enderecoDTO.getCep())
                        .contato(contatoCriado)
                        .build();

            }).toList());

            contatoRepository.save(contatoCriado);

            for(Endereco end : enderecos){
                EnderecoDTO addEnderecoDto = EnderecoDTO.builder()
                        .id(end.getId())
                        .nomeRua(end.getNomeRua())
                        .numeroRua(end.getNumeroRua())
                        .cep(end.getCep())
                        .build();
                enderecosDtoVisualizar.add(addEnderecoDto);

            }

            return ContatoDTO.builder()
                    .nome(contatoCriado.getNome())
                    .email(contatoCriado.getEmail())
                    .telefone(contatoCriado.getTelefone())
                    .dataNascimento(contatoCriado.getDataNascimento())
                    .enderecoLista(enderecosDtoVisualizar)
                    .build();
        }

        return null;
    }

    @Override
    public Boolean excluirContato(UUID id) {

        if(!contatoRepository.existsById(id)){
            return false;
        }
        contatoRepository.deleteById(id);
        return true;
    }

    @Override
    public ContatoDTO atualizarInfoContato(UUID id, ContatoDTO contatoDTO) throws Exception {

        boolean verificarExistenciaContato = contatoRepository.existsById(id);

        if(!verificarExistenciaContato){
            return null;
        }
        else{
            Contato contatoAtual = contatoRepository.getReferenceById(id);
            Optional<Contato> verificar = contatoRepository.findByEmail(contatoDTO.getEmail());

            if(verificar.isPresent() && !verificar.get().getId().equals(id)){
                throw new EmailExistenteException("Email já cadastrado");
            }
            else{
                copiarPropriedadesNaoNulas(contatoDTO, contatoAtual,"enderecoLista");
                BeanCopyUtils.atualizarEnderecos(contatoAtual, contatoDTO.getEnderecoLista());
                Contato contatoSalvo = contatoRepository.save(contatoAtual);


                List<EnderecoDTO> enderecos = new ArrayList<>();
                contatoAtual.getEnderecoLista().forEach(e -> enderecos.add(
                        EnderecoDTO.builder()
                                .id(e.getId())
                                .nomeRua(e.getNomeRua())
                                .numeroRua(e.getNumeroRua())
                                .cep(e.getCep())
                                .contato(contatoAtual)
                                .build()));

                return ContatoDTO.builder()
                        .nome(contatoAtual.getNome())
                        .email(contatoAtual.getEmail())
                        .telefone(contatoAtual.getTelefone())
                        .dataNascimento(contatoAtual.getDataNascimento())
                        .enderecoLista(enderecos).build();
            }
        }


    }
}
