package com.project_agenda.agenda.dto;


import com.project_agenda.agenda.entity.Contato;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class EnderecoDTO {

    private Long id;

    private String nomeRua;

    private Long numeroRua;

    private String cep;

    private Contato contato;
}
