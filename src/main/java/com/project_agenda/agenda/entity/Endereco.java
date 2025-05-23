package com.project_agenda.agenda.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "TB_ENDERECO")
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
    private String nomeRua;

    @Column(name = "NUMERO_RUA_ENDERECO")
    private Long numeroRua;

    @Column(name = "CEP_ENDERECO")
    private String cep;

    @ManyToOne
    @JoinColumn(name = "ID_CONTATO")
    @JsonBackReference
    private Contato contato;



    public String getNomeRua(){return nomeRua;}

    public void setNomeRua(String nomeRua){this.nomeRua=nomeRua;}

    public Long getNumeroRua(){return numeroRua;}

    public void setNumeroRua(Long numeroRua){this.numeroRua = numeroRua;}

    public String getCep(){return cep;}

    public void setCep(String cep){this.cep = cep;}

    public Contato getContato() {return contato;}

    public void setContato(Contato contato) {this.contato = contato;}

}
