package com.project_agenda.agenda.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
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
    private Integer id;

    @Column(name = "NOME_RUA")
    @Schema(description = "Nome da rua", example = "Alameda")
    private String nomeRua;

    @Column(name = "NUMERO_RUA_ENDERECO")
    @Schema(description = "Número da rua", example = "1")
    private Integer numeroRua;

    @Column(name = "CEP_ENDERECO")
    @Pattern(regexp = "^[0-9]{5}-[0-9]{3}$",
            message = "Formato de cep inválido. Utilize o formato: 12345-000")
    @Schema(description = "Cep do local onde reside", example = "12345-000")
    private String cep;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CONTATO")
    @JsonBackReference
    private Contato contato;
}
