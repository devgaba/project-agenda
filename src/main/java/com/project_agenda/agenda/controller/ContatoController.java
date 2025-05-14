package com.project_agenda.agenda.controller;


import com.project_agenda.agenda.entity.Contato;
import com.project_agenda.agenda.repository.ContatoRepository;
import com.project_agenda.agenda.service.impl.ContatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/contatos")
public class ContatoController {

    @Autowired
    private ContatoService contatoService;

    @GetMapping("/exibir-contatos")
    public List<Contato> exibirContatos(){
        return contatoService.exibirContatos();
    }


    @PostMapping("/criar-contato")
    public Contato criarContato(@RequestBody Contato contato){
        return contatoService.criarContato(contato);
    }


    @PutMapping("/atualizar-contato/{id}")
    public Contato atualizarContato(@PathVariable UUID id, @RequestBody Contato contato) {
        return contatoService.atualizarContato(id, contato);
    }

    @DeleteMapping("/excluir-contato/{id}")
    public void excluirContato(@PathVariable UUID id){
        contatoService.excluirContato(id);
    }
}
