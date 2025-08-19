package com.project_agenda.agenda.controller;



import com.project_agenda.agenda.dto.ContatoDTO;
import com.project_agenda.agenda.dto.EnderecoDTO;
import com.project_agenda.agenda.entity.Contato;
import com.project_agenda.agenda.entity.Endereco;
import com.project_agenda.agenda.repository.ContatoRepository;
import com.project_agenda.agenda.repository.EnderecoRepository;
import com.project_agenda.agenda.service.IContatoService;
import com.project_agenda.agenda.service.impl.ContatoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/contatos")
public class ContatoController {

    @Autowired
    private IContatoService contatoService;

    @Autowired
    private ContatoRepository contatoRepository;

    @GetMapping("/exibir-contatos")
    public List<Contato> exibirContatos(){
        return contatoService.exibirContatos();
    }

    @PostMapping("/criar-contato")
    public ResponseEntity<Contato> criarContato(@RequestBody ContatoDTO contatoDTO){

        Contato contatoCriado = contatoService.criarContato(contatoDTO);

        return new ResponseEntity<>(contatoCriado, HttpStatus.CREATED);
    }

    @PutMapping("/atualizar-contato/{id}")
    public ResponseEntity<ContatoDTO> atualizarContato(@PathVariable UUID id,
                                                    @RequestBody ContatoDTO contatoDTO){

        Contato contatoSalvo = contatoService.atualizarContato(id, contatoDTO);
        contatoRepository.save(contatoSalvo);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/atualizar-info-contato/{id}")
    public ResponseEntity<Contato> atualizarInfoContato(@PathVariable UUID id,
                                                        @RequestBody ContatoDTO contatoDTO){

        Contato contatoSalvo = contatoService.atualizarInfoContato(id, contatoDTO).getBody();

        return ResponseEntity.ok(contatoSalvo);
    }

    @DeleteMapping("/excluir-contato/{id}")
    public void excluirContato(@PathVariable UUID id){
        contatoService.excluirContato(id);
    }
}