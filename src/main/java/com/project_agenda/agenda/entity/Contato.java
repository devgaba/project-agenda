package com.project_agenda.agenda.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.springframework.format.annotation.DateTimeFormat;

import javax.xml.transform.Source;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Contato implements Serializable {

    @Serial
    private static final long SerialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_CONTATO")
    private UUID id;

    @Column(name = "NOME_CONTATO")
    @Schema(description = "Nome do contato", example = "Fulano")
    private String nome;

    @Column(name = "EMAIL_CONTATO")
    @Schema(description = "Email do contato", example = "fulano@gmail.com")
    private String email;

    @Column(name = "TELEFONE_CONTATO")
    @Pattern(regexp = "^\\([1-9][1-9]\\)\\s9[0-9]{4}-[0-9]{4}$",
            message = "Formato de telefone inválido.")
    @Schema(description = "Telefone do contato", example = "(61) 98956-7896")
    private String telefone;

    @Column(name = "DATANASC_CONTATO")
    @PastOrPresent(message="A data de nascimento não pode ser no futuro.")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento;

    @OneToMany(mappedBy = "contato", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Schema(description = "Lista de endereços do contato", example = "")
    private List<Endereco> enderecoLista =  new ArrayList<>();

}