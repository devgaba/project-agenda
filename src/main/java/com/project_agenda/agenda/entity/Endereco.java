package com.project_agenda.agenda.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "TB_ENDERECO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Endereco implements Serializable {

    @Serial
    private static final long SerialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_ENDERECO")
    private Long id;

    @Column(name = "NOME_RUA")
    @NotBlank
    private String nomeRua;

    @Column(name = "NUMERO_RUA_ENDERECO")
    @NotNull
    private Long numeroRua;

    @Column(name = "CEP_ENDERECO")
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "CEP inválido. Use o formato XXXXX-XXX")
    private String cep;

    @ManyToOne
    @JoinColumn(name = "ID_CONTATO")
    @JsonBackReference
    private Contato contato;
}
