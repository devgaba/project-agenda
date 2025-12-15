package com.project_agenda.agenda.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.project_agenda.agenda.service.OnUpdate;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Email;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContatoDTO {

    @Id
    private UUID id;

    @NotBlank(message = "O campo 'nome' não deve estar vazio.")
    @NotNull(message = "O campo não pode ser nulo")
    private String nome;

    @NotBlank(message = "O campo 'email' não deve estar vazio.")
    @Email(message = "Formato de email inválido.", groups = OnUpdate.class)
    @NotNull(message = "O campo não pode ser nulo")
    /*@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$",
            message = "O e-mail deve ser válido.")*/
    private String email;

    @NotBlank(message = "O campo 'telefone' não deve estar vazio.")
    @Pattern(regexp = "^\\([1-9][1-9]\\)\\s9[0-9]{4}-[0-9]{4}$",
            message = "Formato de telefone inválido.", groups = OnUpdate.class)
    @NotNull(message = "O campo não pode ser nulo")
    private String telefone;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @NotNull(message = "O campo não pode ser nulo")
    private LocalDate dataNascimento;

    @NotNull(message = "O campo não pode ser nulo")
    private List<EnderecoDTO> enderecoLista;


}
