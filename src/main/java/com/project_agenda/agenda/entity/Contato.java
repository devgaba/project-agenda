package com.project_agenda.agenda.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
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


    //Getters e Setters
    /*public String getNome(){ return nome;}

    public void setNome(String nome){this.nome = nome;}

    public String getEmail(){ return email;}

    public void setEmail(String email){this.email = email;}

    public String getTelefone(){return telefone;}

    public void setTelefone(String telefone){this.telefone = telefone;}

    public LocalDate getDataNascimento(){return dataNascimento;}

    public void setDataNascimento(LocalDate dataNascimento){this.dataNascimento = dataNascimento;}
*/}

