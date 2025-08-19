package com.project_agenda.agenda.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "TB_CONTATOS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Contato implements Serializable {

    @Serial
    private static final long SerialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_CONTATO")
    private UUID id;

    @Column(name = "NOME_CONTATO")
    @NotBlank(message = "O campo referente ao nome não pode ser vazio.")
    private String nome;

    @Column(name = "EMAIL_CONTATO")
    @NotBlank
    @Email(message = "E-mail inválido")
    private String email;

    @Column(name = "TELEFONE_CONTATO")
    @NotBlank
    @Pattern(regexp = "\\(\\d{2}\\) \\d{4,5}-\\d{4}", message = "Formato de telefone inválido")
    private String telefone;

    @Column(name = "DATANASC_CONTATO")
    @NotNull
    @PastOrPresent(message="A data de nascimento não pode ser no futuro.")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento;

    @OneToMany(mappedBy = "contato", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @NotNull
    @Size(min = 1, message = "O contato deve ter pelo menos um endereço.")
    private List<Endereco> enderecoLista =  new ArrayList<>();
}