package com.project_agenda.agenda.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project_agenda.agenda.entity.Contato;
import lombok.*;


@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EnderecoDTO {

    private Integer id;

    private String nomeRua;

    private Integer numeroRua;

    private String cep;

    @JsonIgnore
    private Contato contato;
}
