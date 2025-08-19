package com.project_agenda.agenda.dto;


import com.project_agenda.agenda.entity.Endereco;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class ContatoDTO {


    private String nome;

    private String email;

    private String telefone;

    private LocalDate dataNascimento;

    private List<EnderecoDTO> enderecoLista;


}
