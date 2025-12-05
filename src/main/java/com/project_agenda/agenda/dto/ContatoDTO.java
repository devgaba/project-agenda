package com.project_agenda.agenda.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Email;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContatoDTO {

    @NotBlank(message = "O campo 'nome' não deve estar vazio.")
    private String nome;

    @NotBlank(message = "O campo 'email' não deve estar vazio.")
    @Email(message = "Formato de email inválido.")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$",
            message = "O e-mail deve ser válido.")
    private String email;

    @NotBlank(message = "O campo 'telefone' não deve estar vazio.")
    @Pattern(regexp = "^\\([1-9][1-9]\\)\\s9[0-9]{4}-[0-9]{4}$",
            message = "Formato de telefone inválido.")
    private String telefone;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento;

    private List<EnderecoDTO> enderecoLista;


}
