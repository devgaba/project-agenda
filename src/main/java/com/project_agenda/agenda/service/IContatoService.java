package com.project_agenda.agenda.service;


import com.project_agenda.agenda.dto.ContatoDTO;
import com.project_agenda.agenda.entity.Contato;


import java.util.List;
import java.util.UUID;

public interface IContatoService {


    List<Contato> exibirContatos();

    ContatoDTO criarContato(ContatoDTO contatoDTO);

    Boolean excluirContato(UUID id);

    ContatoDTO atualizarInfoContato(UUID id, ContatoDTO contatoDTO) throws Exception;
}
