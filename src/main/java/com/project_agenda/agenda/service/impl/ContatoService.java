package com.project_agenda.agenda.service.impl;

import com.project_agenda.agenda.dto.ContatoDTO;
import com.project_agenda.agenda.dto.EnderecoDTO;
import com.project_agenda.agenda.entity.Contato;
import com.project_agenda.agenda.entity.Endereco;
import com.project_agenda.agenda.repository.ContatoRepository;
import com.project_agenda.agenda.repository.EnderecoRepository;
import com.project_agenda.agenda.service.IContatoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class ContatoService implements IContatoService {


    //Chamada à classe ContatoRepository
    @Autowired
    private ContatoRepository contatoRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    //Regras de negócio implementadas pela interface IContatoService
    @Override
    public List<Contato> exibirContatos() {

        return contatoRepository.findAll();
    }

    @Override
    public Contato criarContato(ContatoDTO contatoDTO) {

        List<Endereco> enderecoList = new ArrayList<>();



        Contato contatoCriado = Contato.builder()
                .nome(contatoDTO.getNome())
                .email(contatoDTO.getEmail())
                .telefone(contatoDTO.getTelefone())
                .dataNascimento(contatoDTO.getDataNascimento())
                .enderecoLista(enderecoList)
                .build();

        List<Endereco> enderecoListaDTO = contatoDTO.getEnderecoLista().stream().map(enderecoDTO ->
        {
            Endereco end = Endereco.builder()
                    .nomeRua(enderecoDTO.getNomeRua())
                    .numeroRua(enderecoDTO.getNumeroRua())
                    .cep(enderecoDTO.getCep())
                    .contato(contatoCriado)
                    .build();
            return end;

        }).toList();

        enderecoList.addAll(enderecoListaDTO);
        return contatoRepository.save(contatoCriado);
    }

    @Override
    public Contato atualizarContato(UUID id,ContatoDTO contatoDTO) {

        Optional<Contato> contatoAtual = contatoRepository.findById(id);

        if (!contatoAtual.isPresent()){
            ResponseEntity.notFound().build();
        }

        Contato contatoAtualizado = contatoAtual.get();

        if(contatoDTO.getNome() != null){
            contatoAtualizado.setNome(contatoDTO.getNome());
            contatoAtualizado = contatoRepository.save(contatoAtualizado);
        }
        if(contatoDTO.getEmail() != null){
            contatoAtualizado.setEmail(contatoDTO.getEmail());
            contatoAtualizado = contatoRepository.save(contatoAtualizado);
        }
        if(contatoDTO.getTelefone() != null){
            contatoAtualizado.setTelefone(contatoDTO.getTelefone());
            contatoAtualizado = contatoRepository.save(contatoAtualizado);
        }
        if(contatoDTO.getDataNascimento() != null){
            contatoAtualizado.setDataNascimento(contatoDTO.getDataNascimento());
            contatoAtualizado = contatoRepository.save(contatoAtualizado);
        }

        contatoAtualizado = contatoRepository.save(contatoAtualizado);

        return ResponseEntity.ok(contatoAtualizado).getBody();
    }

    @Override
    public void excluirContato(UUID id) {
        contatoRepository.deleteById(id);
    }

    @Override
    public ResponseEntity<Contato> atualizarInfoContato(UUID id, ContatoDTO contatoDTO) {

        Contato contatoAtual = contatoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contato não encontrado"));


        if (contatoDTO.getNome() != null && !contatoDTO.getNome().isEmpty()) {
            contatoAtual.setNome(contatoDTO.getNome());
        }

        if (contatoDTO.getEmail() != null && !contatoDTO.getEmail().isEmpty()) {
            contatoAtual.setEmail(contatoDTO.getEmail());
        }

        if (contatoDTO.getTelefone() != null && !contatoDTO.getTelefone().isEmpty()) {
            contatoAtual.setTelefone(contatoDTO.getTelefone());
        }


        if (contatoDTO.getEnderecoLista() != null && !contatoDTO.getEnderecoLista().isEmpty()) {


            EnderecoDTO endDTO = contatoDTO.getEnderecoLista().get(0);
            Long idEnderecoDTO = endDTO.getId();


            contatoAtual.getEnderecoLista().stream()
                    .filter(endereco -> endereco.getId().equals(idEnderecoDTO))
                    .findFirst()
                    .ifPresent(enderecoExistente -> {
                        // 5. Atualiza os campos do objeto Endereco existente, e não cria um novo.
                        if (endDTO.getNomeRua() != null) {
                            enderecoExistente.setNomeRua(endDTO.getNomeRua());
                        }
                        if (endDTO.getNumeroRua() != null) {
                            enderecoExistente.setNumeroRua(endDTO.getNumeroRua());
                        }
                        if (endDTO.getCep() != null) {
                            enderecoExistente.setCep(endDTO.getCep());
                        }
                    });

            if(endDTO.getId() == null){


                List<Endereco> enderecoListaDTO = contatoDTO.getEnderecoLista().stream().map(enderecoDTO ->
                {
                    Endereco end = Endereco.builder()
                            .nomeRua(enderecoDTO.getNomeRua())
                            .numeroRua(enderecoDTO.getNumeroRua())
                            .cep(enderecoDTO.getCep())
                            .contato(contatoAtual)
                            .build();
                    return end;
                }).toList();

                contatoAtual.getEnderecoLista().addAll(enderecoListaDTO);
            }

        }

        return new ResponseEntity<Contato>(contatoRepository.save(contatoAtual), HttpStatus.OK);
    }
}
