package com.project_agenda.agenda.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.project_agenda.agenda.entity.Endereco;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Email;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class ContatoDTO {

    @NotBlank(message = "O campo referente ao nome não pode ser vazio.")
    private String nome;

    @NotBlank
    @Email(message = "E-mail inválido")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$",
            message = "O e-mail deve ser válido.")
    private String email;

    @NotBlank
    @Pattern(regexp = "\\(\\d{2}\\) \\d{4,5}-\\d{4}", message = "Formato de telefone inválido")
    private String telefone;

    @NotNull
    @PastOrPresent(message="A data de nascimento não pode ser no futuro.")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento;

    @NotNull
    @Size(min = 1, message = "O contato deve ter pelo menos um endereço.")
    private List<EnderecoDTO> enderecoLista;


}
