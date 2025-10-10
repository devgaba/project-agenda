package com.project_agenda.agenda.controller;

import com.project_agenda.agenda.dto.ContatoDTO;
import com.project_agenda.agenda.dto.ContatoPatchDTO;
import com.project_agenda.agenda.dto.EnderecoDTO;
import com.project_agenda.agenda.entity.Contato;
import com.project_agenda.agenda.entity.Endereco;
import com.project_agenda.agenda.repository.ContatoRepository;
import com.project_agenda.agenda.repository.EnderecoRepository;
import com.project_agenda.agenda.service.IContatoService;
import com.project_agenda.agenda.service.impl.ContatoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/contatos")
public class ContatoController {

    @Autowired
    private IContatoService contatoService;

    @Autowired
    private ContatoRepository contatoRepository;

    @Operation(description = "Busca e exibe todos os contatos existentes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna todos os contatos existentes"),
            @ApiResponse(responseCode = "404", description = "Não existem contatos")
    })
    @GetMapping("/exibir-contatos")
    public ResponseEntity<List<Contato>> exibirContatos(){

        List<Contato> contatos = contatoService.exibirContatos();
        if(contatos == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(contatos, HttpStatus.OK);
    }

    @Operation(description = "Cria um contato e cadastro no banco de dados")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "201", description = "Retorna o contato criado com sucesso"),
            @ApiResponse(responseCode = "302", description = "O contato não é criado, pois já existe")
    })
    @PostMapping("/criar-contato")
    public ResponseEntity<String> criarContato(@Valid @RequestBody ContatoDTO contatoDTO){

        Contato contatoCriado = contatoService.criarContato(contatoDTO);

        if(contatoCriado == null){
            return new ResponseEntity<String>
                    ("Contato existente! Verifique as informações e tente novamente",
                    HttpStatus.FOUND);
        }

        return new ResponseEntity<>("Contato criado com sucesso!", HttpStatus.CREATED);
    }

    @Operation(description = "Atualiza um contato baseado no ID fornecido na URL.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna o contato atualizado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Retorna erro ao tentar atualizar o contato.")
    })
    @PutMapping("/atualizar-contato/{id}")
    public ResponseEntity<String> atualizarContato(@PathVariable UUID id,
                                                       @Valid @RequestBody ContatoPatchDTO contatoPatchDTO){


        Contato contatoSalvo = contatoService.atualizarContato(id, contatoPatchDTO);
        if(contatoSalvo == null){
            return new ResponseEntity<String>
                    ("Contato inexistente! Verifique o ID digitado!",
                            HttpStatus.NOT_FOUND);
        }
        contatoRepository.save(contatoSalvo);
        return new ResponseEntity<String> ("Contato atualizado com sucesso!", HttpStatus.OK);
    }
    @Operation(description = "Atualiza informações específicas de um contato")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna o contato com as informações especificas atualizadas."),
            @ApiResponse(responseCode = "404", description = "Retorna o erro específico ao tentar atualizar a informação desejada.")
    })
    @PatchMapping("/atualizar-info-contato/{id}")
    public ResponseEntity<String> atualizarInfoContato(@PathVariable UUID id,
                                                        @Valid @RequestBody ContatoPatchDTO contatoPatchDTO){

        Contato contatoSalvo = contatoService.atualizarInfoContato(id, contatoPatchDTO);
        if(contatoSalvo == null){
            return new ResponseEntity<String>
                    ("Contato inexistente! Verifique o ID digitado!",
                            HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>("Informações atualizadas com sucesso", HttpStatus.OK);
    }
    @Operation(description = "Remove um contato do banco de dados, baseado no ID fornecido na URL.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna a lista de contatos atualizada e com o contato removido."),
            @ApiResponse(responseCode = "404", description = "Retorna erro ao tentar remover o contato da lista.")
    })
    @DeleteMapping("/excluir-contato/{id}")
    public ResponseEntity<String> excluirContato(@PathVariable UUID id){

        boolean contatoExcluido = contatoService.excluirContato(id);
        if(!contatoExcluido){
            return new ResponseEntity<>("Contato não encontrado", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Contato excluído com sucesso", HttpStatus.OK);
    }
}

