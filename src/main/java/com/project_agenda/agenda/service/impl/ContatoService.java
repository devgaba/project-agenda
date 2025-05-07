package com.project_agenda.agenda.service.impl;

import com.project_agenda.agenda.entity.Contato;
import com.project_agenda.agenda.repository.ContatoRepository;
import com.project_agenda.agenda.service.IContatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ContatoService implements IContatoService {


    //Chamada à classe ContatoRepository
    @Autowired
    private ContatoRepository contatoRepository;



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
    public void excluirContato(UUID id) {
        contatoRepository.deleteById(id);
    }


}
