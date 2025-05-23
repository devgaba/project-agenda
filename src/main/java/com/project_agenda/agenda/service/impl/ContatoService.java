package com.project_agenda.agenda.service.impl;

import com.project_agenda.agenda.entity.Contato;
import com.project_agenda.agenda.entity.Endereco;
import com.project_agenda.agenda.repository.ContatoRepository;
import com.project_agenda.agenda.repository.EnderecoRepository;
import com.project_agenda.agenda.service.IContatoService;
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
    public Contato criarContato(Contato contato) {

        return contatoRepository.save(contato);
    }

    @Override
    public Contato atualizarContato(UUID id,Contato contato) {

        Optional<Contato> contatoAtual = contatoRepository.findById(id);

        if (!contatoAtual.isPresent()){
            ResponseEntity.notFound().build();
        }

        Contato contatoAtualizado = contatoAtual.get();

        if(contato.getNome() != null){
            contatoAtualizado.setNome(contato.getNome());
            contatoAtualizado = contatoRepository.save(contatoAtualizado);
        }
        if(contato.getEmail() != null){
            contatoAtualizado.setEmail(contato.getEmail());
            contatoAtualizado = contatoRepository.save(contatoAtualizado);
        }
        if(contato.getTelefone() != null){
            contatoAtualizado.setTelefone(contato.getTelefone());
            contatoAtualizado = contatoRepository.save(contatoAtualizado);
        }
        if(contato.getDataNascimento() != null){
            contatoAtualizado.setDataNascimento(contato.getDataNascimento());
            contatoAtualizado = contatoRepository.save(contatoAtualizado);
        }

        contatoAtualizado = contatoRepository.save(contatoAtualizado);

        return ResponseEntity.ok(contatoAtualizado).getBody();
    }

    @Override
    public void excluirContato(UUID id) {
        contatoRepository.deleteById(id);
    }
}
