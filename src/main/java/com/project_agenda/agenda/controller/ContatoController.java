package com.project_agenda.agenda.controller;

import com.project_agenda.agenda.dto.ContatoDTO;
import com.project_agenda.agenda.entity.Contato;
import com.project_agenda.agenda.exception.EmailExistenteException;
import com.project_agenda.agenda.repository.ContatoRepository;
import com.project_agenda.agenda.service.IContatoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/contatos")
public class ContatoController {

    @Autowired
    private IContatoService contatoService;

    @Autowired
    private ContatoRepository contatoRepository;

    @Operation(description = "Busca e exibe todos os contatos existentes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna a lista de contatos, esteja ela, vazia ou não"),
    })
    @GetMapping("/exibir-contatos")
    public ResponseEntity<List<ContatoDTO>> exibirContatos(){
        List<ContatoDTO> contatos = contatoService.exibirContatos();
        return new ResponseEntity<>(contatos, HttpStatus.OK);
    }

    @Operation(description = "Cria um contato e cadastro no banco de dados")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "201", description = "Retorna o contato criado com sucesso"),
            @ApiResponse(responseCode = "409", description = "O contato não é criado, pois já existe")
    })
    @PostMapping("/criar-contato")
    public ResponseEntity<?> criarContato(@Valid @RequestBody ContatoDTO contatoDTO){
            ContatoDTO contatoCriado = contatoService.criarContato(contatoDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /*
    @Operation(description = "Atualiza um contato baseado no ID fornecido na URL.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna o contato atualizado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Retorna erro ao tentar atualizar o contato.")
    })
    @PutMapping("/atualizar-contato/{id}")
    public ResponseEntity<Map<String, Object>> atualizarContato(@PathVariable UUID id,
                                                       @Valid @RequestBody ContatoDTO contatoDTO){

        ContatoDTO contatoSalvo = contatoService.atualizarContato(id, contatoDTO);
        Map<String, Object> resposta = new HashMap<>();
        if(contatoSalvo == null){
            resposta.put("Mensagem", "Contato Inexistente! Verifique o ID digitado!");
            return new ResponseEntity<>(resposta, HttpStatus.NOT_FOUND);
        }
        else{
            resposta.put("Mensagem", "Contato atualizado com sucesso!");
            resposta.put("Contato Atualizado", contatoSalvo);
        }
        contatoRepository.save(contatoSalvo);
        return new ResponseEntity<> (resposta, HttpStatus.OK);
    }
    */


    @Operation(description = "Atualiza informações específicas de um contato")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna o contato com as informações especificas atualizadas."),
            @ApiResponse(responseCode = "404", description = "Retorna o erro específico ao tentar atualizar a informação desejada.")
    })
    @PatchMapping("/atualizar-info-contato/{id}")
    public ResponseEntity<?> atualizarInfoContato(@Valid @PathVariable UUID id,
                                                         @RequestBody ContatoDTO contatoDTO) throws Exception {
        ContatoDTO contatoSalvo = contatoService.atualizarInfoContato(id, contatoDTO);
        return new ResponseEntity<>(contatoSalvo, HttpStatus.OK);
    }


    @Operation(description = "Remove um contato do banco de dados, baseado no ID fornecido na URL.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "A exclusão foi executado com sucesso."),
            @ApiResponse(responseCode = "404", description = "O ID não foi localizado na lista.")
    })
    @DeleteMapping("/excluir-contato/{id}")
    public ResponseEntity<?> excluirContato(@PathVariable UUID id){

        boolean contatoExcluido = contatoService.excluirContato(id);

        if(!contatoExcluido){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}

