package com.project_agenda.agenda.service;


import com.project_agenda.agenda.dto.ContatoDTO;
import com.project_agenda.agenda.dto.ContatoPatchDTO;
import com.project_agenda.agenda.entity.Contato;
import org.springframework.http.ResponseEntity;


import java.util.List;
import java.util.UUID;

public interface IContatoService {


    List<Contato> exibirContatos();

    Contato criarContato(ContatoDTO contatoDTO);

    Contato atualizarContato(UUID id, ContatoPatchDTO contatoPatchDTO);

    Boolean excluirContato(UUID id);

    Contato atualizarInfoContato(UUID id, ContatoPatchDTO contatoPatchDTO);
}
