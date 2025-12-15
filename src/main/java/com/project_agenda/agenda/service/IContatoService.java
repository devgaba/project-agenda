package com.project_agenda.agenda.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.project_agenda.agenda.dto.ContatoDTO;
import com.project_agenda.agenda.entity.Contato;
import com.project_agenda.agenda.exception.RecursoNaoEncontradoException;


import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface IContatoService {


    List<ContatoDTO> exibirContatos();

    ContatoDTO criarContato(ContatoDTO contatoDTO);

    Boolean excluirContato(UUID id);

    ContatoDTO substituirContato(UUID id, ContatoDTO contatoDTO) throws RecursoNaoEncontradoException, IOException;
    ContatoDTO atualizarInfoContato(UUID id, ContatoDTO contatoDTO) throws Exception;
}
