package com.project_agenda.agenda.service.impl;

import com.project_agenda.agenda.dto.ContatoDTO;
import com.project_agenda.agenda.entity.Contato;
import com.project_agenda.agenda.repository.ContatoRepository;
import com.project_agenda.agenda.service.IContatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.project_agenda.agenda.entity.Contato.*;

@Service
public class ContatoService implements IContatoService {

    private final ContatoRepository contatoRepository;


    @Autowired
    public ContatoService(ContatoRepository contatoRepository){
        this.contatoRepository = contatoRepository;
    }


    @Override
    public List<Contato> exibirContatos() {
        return contatoRepository.findAll();
    }
    

    @Override
    public Contato criarContato(Contato contato) {
        var con = Contato.builder()
                .nome(contato.getNome())
                .email(contato.getEmail())
                .telefone(contato.getTelefone())
                .dataNascimento(contato.getDataNascimento()).build();
        return contatoRepository.save(contato);
    }



}
