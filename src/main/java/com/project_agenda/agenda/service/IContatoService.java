package com.project_agenda.agenda.service;


import com.project_agenda.agenda.entity.Contato;
import com.project_agenda.agenda.entity.Endereco;


import java.util.List;
import java.util.UUID;

public interface IContatoService {


    List<Contato> exibirContatos();

    Contato criarContato(Contato contato);

    Contato atualizarContato(UUID id, Contato contato);

    void excluirContato(UUID id);


}
