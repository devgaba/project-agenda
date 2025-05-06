package com.project_agenda.agenda.service;

import com.project_agenda.agenda.dto.ContatoDTO;
import com.project_agenda.agenda.entity.Contato;
import lombok.Data;

import java.util.List;
@Data
public interface IContatoService {


    public List<Contato> exibirContatos();

    public Contato criarContato(ContatoDTO contatoDTO);

    Contato criarContato(Contato contato);
}
