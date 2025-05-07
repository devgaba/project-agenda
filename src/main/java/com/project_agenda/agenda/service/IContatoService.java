package com.project_agenda.agenda.service;

import com.project_agenda.agenda.entity.Contato;


import java.util.List;
import java.util.UUID;

public interface IContatoService {


    public List<Contato> exibirContatos();

    public Contato criarContato(Contato contato);

    public void excluirContato(UUID id);
}
