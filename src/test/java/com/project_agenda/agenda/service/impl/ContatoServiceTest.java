package com.project_agenda.agenda.service.impl;

import com.project_agenda.agenda.dto.ContatoDTO;
import com.project_agenda.agenda.dto.EnderecoDTO;
import com.project_agenda.agenda.entity.Contato;
import com.project_agenda.agenda.entity.Endereco;
import com.project_agenda.agenda.repository.ContatoRepository;
 // Alterado para o nome da interface/classe
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContatoServiceTest {

    @Mock
    private ContatoRepository contatoRepository;

    @InjectMocks
    private ContatoService contatoService;

    @BeforeEach
    void setUp(){
        UUID idEndereco = UUID.randomUUID();
        UUID idContato = UUID.randomUUID();
        Contato contato = new Contato();
        EnderecoDTO endereco = EnderecoDTO.builder()
                .id(1L)
                .nomeRua("Avenida")
                .numeroRua(2L)
                .cep("72268-000")
                .contato(null)
                .build();

        ContatoDTO contatoDTO = ContatoDTO.builder()
                .nome("Gabriel")
                .email("gabriel@gmail.com")
                .telefone("(61) 99209-6800")
                .dataNascimento(String.valueOf(LocalDate.parse("11/04/2000")))
                .enderecoLista(List.of(endereco))
                .build();

        contato.setId(idContato);
        contato.setNome(contatoDTO.getNome());
        contato.setEmail(contatoDTO.getEmail());
        contato.setTelefone(contatoDTO.getTelefone());
        contato.setEnderecoLista(contatoDTO.getEnderecoLista());
    }


    /*
    @Test
    @DisplayName("Deve criar um ContatoDTO e salvar no repositório")
    void deveCriarContato(){

        // 1. DADOS DE ENTRADA
        ContatoDTO contatoDTO = new ContatoDTO(
                "João da Silva",
                "joao@email.com",
                "11987654321",
                "11/04/2000",
                // Passando 'null' para o 5º argumento (Contato), assumindo
                // que você corrigiu o DTO com um construtor público de 5 args.
                List.of(new EnderecoDTO(1L, "Rua das Flores", 123L, "72268-000", null))
        );


        Contato contatoSimuladoRetorno = mock(Contato.class);
        when(contatoSimuladoRetorno.getNome()).thenReturn(contatoDTO.getNome());

        when(contatoRepository.save(any(Contato.class))).thenReturn(contatoSimuladoRetorno);

        Contato contSalvo = contatoService.criarContato(contatoDTO);

        assertNotNull(contSalvo);
        assertEquals("João da Silva", contSalvo.getNome());


        verify(contatoRepository, times(1)).save(any(Contato.class));
    }
    */

    @Test
    @DisplayName("Deve excluir um contato do repositório baseado no ID")
    void deveExcluirUmContato(){


    }
}