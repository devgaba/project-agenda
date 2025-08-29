package com.project_agenda.agenda.dto;


import com.project_agenda.agenda.entity.Contato;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class EnderecoDTO {

    private Long id;

    @NotBlank
    private String nomeRua;

    @NotNull
    private Long numeroRua;

    @Pattern(regexp = "\\d{5}-\\d{3}", message = "CEP inválido. Use o formato XXXXX-XXX")
    private String cep;

    private Contato contato;
}
