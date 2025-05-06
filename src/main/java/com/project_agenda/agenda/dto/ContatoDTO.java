package com.project_agenda.agenda.dto;


import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class ContatoDTO {



    private UUID idContato;

    private String nome;

    private String email;

    private String telefone;

    private LocalDate dataNascimento;
}
