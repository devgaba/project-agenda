package com.project_agenda.agenda.service.impl;

import com.project_agenda.agenda.dto.ContatoDTO;
import com.project_agenda.agenda.dto.ContatoPatchDTO;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

        List<Contato> contatos = contatoRepository.findAll();

        if(!contatos.isEmpty()){
            return contatoRepository.findAll();
        }
        return null;
    }

    @Override
    public Contato criarContato(ContatoDTO contatoDTO) {

        Optional<Contato> email = contatoRepository.findByEmail(contatoDTO.getEmail());

        if(email.isEmpty()){
            List<Endereco> enderecoList = new ArrayList<>();
            Contato contatoCriado = Contato.builder()
                    .nome(contatoDTO.getNome())
                    .email(contatoDTO.getEmail())
                    .telefone(contatoDTO.getTelefone())
                    .dataNascimento(LocalDate.parse(contatoDTO.getDataNascimento(),
                            DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                    .enderecoLista(enderecoList)
                    .build();

            List<Endereco> enderecoListaDTO = contatoDTO.getEnderecoLista().stream().map(enderecoDTO -> {
                Endereco end = Endereco.builder()
                        .nomeRua(enderecoDTO.getNomeRua())
                        .numeroRua(enderecoDTO.getNumeroRua())
                        .cep(enderecoDTO.getCep()).contato(contatoCriado)
                        .build();
                return end;

            }).toList();
            enderecoList.addAll(enderecoListaDTO);

            return contatoRepository.save(contatoCriado);
        }

        return null;
    }

    @Override
    public Contato atualizarContato(UUID id, ContatoPatchDTO contatoPatchDTO) {

        Optional<Contato> contatoAtual = contatoRepository.findById(id);

        if (contatoAtual.isPresent()) {
            Contato contatoAtualizado = contatoAtual.get();

            if (contatoPatchDTO.getNome() != null) {
                contatoAtualizado.setNome(contatoPatchDTO.getNome());
                contatoAtualizado = contatoRepository.save(contatoAtualizado);
            }
            if (contatoPatchDTO.getEmail() != null) {
                contatoAtualizado.setEmail(contatoPatchDTO.getEmail());
                contatoAtualizado = contatoRepository.save(contatoAtualizado);
            }
            if (contatoPatchDTO.getTelefone() != null) {
                contatoAtualizado.setTelefone(contatoPatchDTO.getTelefone());
                contatoAtualizado = contatoRepository.save(contatoAtualizado);
            }
            if (contatoPatchDTO.getDataNascimento() != null) {
                contatoAtualizado.setDataNascimento(LocalDate.parse(contatoPatchDTO.getDataNascimento(),
                        DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                contatoAtualizado = contatoRepository.save(contatoAtualizado);
            }

            contatoAtualizado = contatoRepository.save(contatoAtualizado);
            return ResponseEntity.ok(contatoAtualizado).getBody();
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
    public Contato atualizarInfoContato(UUID id, ContatoPatchDTO contatoPatchDTO) {

        boolean verificarExistenciaContato = contatoRepository.existsById(id);


        if(!verificarExistenciaContato){
            return null;
        }

            Contato contatoAtual = contatoRepository.getReferenceById(id);

            if (contatoPatchDTO.getNome() != null && !contatoPatchDTO.getNome().isEmpty()) {
                contatoAtual.setNome(contatoPatchDTO.getNome());
            }

            if (contatoPatchDTO.getEmail() != null && !contatoPatchDTO.getEmail().isEmpty()) {
                contatoAtual.setEmail(contatoPatchDTO.getEmail());
            }

            if (contatoPatchDTO.getTelefone() != null && !contatoPatchDTO.getTelefone().isEmpty()) {
                contatoAtual.setTelefone(contatoPatchDTO.getTelefone());
            }

            if (contatoPatchDTO.getDataNascimento() != null && !contatoPatchDTO.getDataNascimento().isEmpty()) {
                contatoAtual.setDataNascimento(LocalDate.parse(contatoPatchDTO.getDataNascimento(),
                        DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            }

            if (contatoPatchDTO.getEnderecoLista() != null && !contatoPatchDTO.getEnderecoLista().isEmpty()) {


                EnderecoDTO endDTO = contatoPatchDTO.getEnderecoLista().get(0);
                Long idEnderecoDTO = endDTO.getId();


                contatoAtual.getEnderecoLista().stream().filter(endereco -> endereco.getId()
                                .equals(idEnderecoDTO))
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

                if (endDTO.getId() == null) {


                    List<Endereco> enderecoListaDTO = contatoPatchDTO.getEnderecoLista()
                            .stream().map(enderecoDTO -> {

                                Endereco end = Endereco.builder()
                                        .nomeRua(enderecoDTO.getNomeRua())
                                        .numeroRua(enderecoDTO.getNumeroRua())
                                        .cep(enderecoDTO.getCep()).contato(contatoAtual)
                                        .build();
                                return end;

                            }).toList();

                    contatoAtual.getEnderecoLista().addAll(enderecoListaDTO);
                }

            }
        return contatoRepository.save(contatoAtual);
    }
}
