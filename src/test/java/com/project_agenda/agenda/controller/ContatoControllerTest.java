package com.project_agenda.agenda.controller;

import com.project_agenda.agenda.dto.ContatoDTO;
import com.project_agenda.agenda.entity.Contato;
import com.project_agenda.agenda.entity.Endereco;
import com.project_agenda.agenda.repository.ContatoRepository;
import com.project_agenda.agenda.service.IContatoService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;

// 🏆 NOVO PADRÃO: Substitui @MockBean
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

// Para manipulação de JSON e serialização
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContatoController.class)
class ContatoControllerTest {

    @Autowired
    private MockMvc mock;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IContatoService contatoService;

    @MockitoBean
    private ContatoRepository contatoRepository;

    private final UUID ID_VALIDO = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private final UUID ID_INVALIDO = UUID.fromString("22222222-2222-2222-2222-222222222222");

    private Contato criarContatoSimulado(String nome, UUID id) {
        return Contato.builder()
                .id(id)
                .nome(nome)
                .email(nome.toLowerCase() + "@email.com")
                .telefone("(61) 99999-9999")
                .dataNascimento(LocalDate.of(2000, 1, 1))
                .enderecoLista(List.of(Endereco.builder().id(1).nomeRua("Rua " + nome).build()))
                .build();
    }

    private ContatoDTO criarContatoDto(String nome) {
        return ContatoDTO.builder()
                .nome(nome)
                .email(nome.toLowerCase() + "@email.com")
                .telefone("(61) 99999-9999")
                .dataNascimento(LocalDate.of(2000, 1, 1))
                .build();
    }

    // -------------------------------------------------------------------------
    //                              1. GET /exibir-contatos
    // -------------------------------------------------------------------------
    @Test
    void deveBuscarContatos_Retorna200_Sucesso() throws Exception{

        Contato contatoUm = criarContatoSimulado("Gabriel", ID_VALIDO);
        Contato contatoDois = criarContatoSimulado("Adriano", ID_INVALIDO);

        when(contatoService.exibirContatos()).thenReturn(List.of(contatoUm, contatoDois));

        mock.perform(get("/contatos/exibir-contatos") // Caminho completo
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.['Lista de Contatos']", hasSize(2)))
                .andExpect(jsonPath("$.['Lista de Contatos'][0].nome").value("Gabriel"))
                .andExpect(jsonPath("$.['Lista de Contatos'][1].nome").value("Adriano"));
    }

    @Test
    void deveBuscarContatos_Retorna404_NenhumEncontrado() throws Exception {
        when(contatoService.exibirContatos()).thenReturn(null);

        mock.perform(get("/contatos/exibir-contatos")
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.Mensagem").value("Não Há Informações sobre contatos!"));
    }

    // -------------------------------------------------------------------------
    //                              2. POST /criar-contato
    // -------------------------------------------------------------------------

    @Test
    void deveCriarContato_Retorna201_Sucesso() throws Exception {

        ContatoDTO novoContatoDTO = criarContatoDto("NovoContato");
        when(contatoService.criarContato(any(ContatoDTO.class))).thenReturn(novoContatoDTO);

        mock.perform(post("/contatos/criar-contato")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoContatoDTO)))

                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mensagem").value("Contato criado com sucesso!"))
                .andExpect(jsonPath("$.dados.nome").value("NovoContato"));
    }

    @Test
    void deveCriarContato_Retorna302_Existente() throws Exception {

        ContatoDTO contatoExistenteDTO = criarContatoDto("Existente");
        when(contatoService.criarContato(any(ContatoDTO.class))).thenReturn(null);

        mock.perform(post("/contatos/criar-contato")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contatoExistenteDTO)))

                .andExpect(status().isFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.Mensagem")
                        .value("Contato existente! Verifique as informações e tente novamente"));
    }

    // -------------------------------------------------------------------------
    //                              3. PATCH /atualizar-info-contato/{id}
    // -------------------------------------------------------------------------
    @Test
    void deveAtualizarInfoContato_Retorna200_Sucesso() throws Exception {

        ContatoDTO dtoPatch = ContatoDTO.builder().nome("Nome Atualizado").build();
        ContatoDTO dtoSalvo = criarContatoDto("Nome Atualizado");

        when(contatoService.atualizarInfoContato(eq(ID_VALIDO), any(ContatoDTO.class))).thenReturn(dtoSalvo);

        mock.perform(patch("/contatos/atualizar-info-contato/{id}", ID_VALIDO)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoPatch)))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.Mensagem").value("Informações atualizadas com sucesso"))
                .andExpect(jsonPath("$.['Contato Atualizado'].nome").value("Nome Atualizado"));
    }

    @Test
    void deveAtualizarInfoContato_Retorna404_Inexistente() throws Exception {

        ContatoDTO dtoPatch = ContatoDTO.builder().nome("Nome Atualizado").build();
        when(contatoService.atualizarInfoContato(eq(ID_INVALIDO), any(ContatoDTO.class))).thenReturn(null);

        mock.perform(patch("/contatos/atualizar-info-contato/{id}", ID_INVALIDO)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoPatch)))

                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.Mensagem").value("Contato Inexistente"));
    }

    // -------------------------------------------------------------------------
    //                              4. DELETE /excluir-contato/{id}
    // -------------------------------------------------------------------------

    @Test
    void deveExcluirContato_Retorna200_Sucesso() throws Exception {

        Contato contatoRemanescente = criarContatoSimulado("Remanescente", ID_VALIDO);

        when(contatoService.excluirContato(eq(ID_VALIDO))).thenReturn(true);
        when(contatoRepository.findAll()).thenReturn(List.of(contatoRemanescente));

        mock.perform(delete("/contatos/excluir-contato/{id}", ID_VALIDO)
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk()) // ⬅️ Espera 200
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.Mensagem").value("Contato excluído com sucesso"))
                .andExpect(jsonPath("$.['Lista de Contatos Atualizada']", hasSize(1)));
    }

    @Test
    void deveExcluirContato_Retorna404_Inexistente() throws Exception {

        when(contatoService.excluirContato(eq(ID_INVALIDO))).thenReturn(false);
        when(contatoRepository.findAll()).thenReturn(Collections.emptyList());

        mock.perform(delete("/contatos/excluir-contato/{id}", ID_INVALIDO)
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.Mensagem").value("Contato não encontrado"));
    }
}