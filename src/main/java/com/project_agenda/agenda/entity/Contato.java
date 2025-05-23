package com.project_agenda.agenda.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private String nome;

    @Column(name = "EMAIL_CONTATO")
    private String email;

    @Column(name = "TELEFONE_CONTATO")
    private String telefone;

    @Column(name = "DATANASC_CONTATO")
    private LocalDate dataNascimento;

    @OneToMany(mappedBy = "contato", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Endereco> enderecoLista =  new ArrayList<>();
}