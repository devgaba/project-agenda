package com.project_agenda.agenda.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContatoDTO {


    private String nome;
    private Long idade;


}
