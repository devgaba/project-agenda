package com.project_agenda.agenda.controller;


import com.project_agenda.agenda.entity.Contato;
import com.project_agenda.agenda.service.impl.ContatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contato")
public class ContatoController {

    @Autowired
    private ContatoService contatoService;

    @GetMapping
    public List<Contato> exibirContatos(){
        return contatoService.exibirContatos();
    }

    /*@PostMapping
    public Contato criarContato(@RequestBody Contato contato){
        return contatoService.criarContato();
    }*/
}
