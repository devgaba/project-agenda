package com.project_agenda.agenda.service;


import com.project_agenda.agenda.dto.ContatoDTO;
import com.project_agenda.agenda.entity.Contato;
import com.project_agenda.agenda.entity.Endereco;
import org.springframework.http.ResponseEntity;


import java.util.List;
import java.util.UUID;

public interface IContatoService {


    List<Contato> exibirContatos();

    Contato criarContato(ContatoDTO contatoDTO);

    Contato atualizarContato(UUID id, ContatoDTO contatoDTO);

    void excluirContato(UUID id);

    ResponseEntity<Contato> atualizarInfoContato(UUID id, ContatoDTO contatoDTO);
}
