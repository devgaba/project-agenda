package com.project_agenda.agenda.service.impl;

import com.project_agenda.agenda.dto.ContatoDTO;
import com.project_agenda.agenda.dto.EnderecoDTO;
import com.project_agenda.agenda.entity.Contato;
import com.project_agenda.agenda.entity.Endereco;
import com.project_agenda.agenda.exception.EmailExistenteException;
import com.project_agenda.agenda.repository.ContatoRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContatoServiceTest {

    @InjectMocks
    private ContatoService contatoService;

    @Mock
    private ContatoRepository contatoRepository;

    /*TESTES ABAIXO REFERENTES À EXCLUSÃO DE CONTATO*/
    @Test
    void excluirContato_QuandoExiste_DeveRetornarTrueESimularExclusao() {
        UUID id = UUID.randomUUID();

        when(contatoRepository.existsById(id)).thenReturn(true);

        Boolean resultado = contatoService.excluirContato(id);

        assertTrue(resultado, "A exclusão de um contato existente deve retornar true.");

        verify(contatoRepository, times(1)).deleteById(id);
    }

    @Test
    void naoExcluirContato_QuandoNaoExiste_DeveRetornarFalse(){
        UUID id = UUID.randomUUID();

        when(contatoRepository.existsById(id)).thenReturn(false);

        Boolean resultado = contatoService.excluirContato(id);

        assertFalse(resultado);

        verify(contatoRepository, never()).deleteById(id);

        verify(contatoRepository, times(1)).existsById(id);
    }


    /*TESTES ABAIXO REFERENTES À EXIBIÇÃO DE CONTATOS*/
    @Test
    void exibirContatos_DeveRetornar_ListaDeContatos(){
        UUID id = UUID.randomUUID();

        Contato contatoSimulado = Contato.builder()
                .id(id)
                .nome("Gabriel")
                .email("gaba@gmail.com")
                .telefone("(61) 99394-9087")
                .dataNascimento(LocalDate.of(2005, 6, 12))
                .enderecoLista(null)
                .build();

        when(contatoRepository.findAll()).thenReturn(List.of(contatoSimulado));

        List<Contato> contatos = contatoService.exibirContatos();

        assertNotNull(contatos);
        assertEquals(1, contatos.size());
        assertEquals("Gabriel", contatos.get(0).getNome());

        verify(contatoRepository, times(1)).findAll();

    }

    @Test
    void exibirContato_NaoDeveRetornar_ListaDeContatos(){

        when(contatoRepository.findAll()).thenReturn(Collections.emptyList());

        List<Contato> contatos = contatoService.exibirContatos();

        assertNull(contatos);

        verify(contatoRepository, times(1)).findAll();
    }


    /*TESTES ABAIXO SÃO REFERENTES À CRIAÇÃO DE CONTATOS*/
    @Test
    void deveCriarContato_QuandoEmailNaoExistir_OuEstiverCadastrado(){
        UUID id = UUID.randomUUID();

        Contato contatoSimulado = Contato.builder()
                .id(id)
                .nome("Gabriel")
                .email("gaba@gmail.com")
                .telefone("(61) 99394-9087")
                .dataNascimento(LocalDate.of(2005, 6, 12))
                .enderecoLista(null)
                .build();

        EnderecoDTO enderecoDTO = EnderecoDTO.builder()
                .id(1)
                .nomeRua("Avenida")
                .numeroRua(2)
                .cep("72380-000")
                .build();

        ContatoDTO contatoDTO = ContatoDTO.builder()
                .nome(contatoSimulado.getNome())
                .email(contatoSimulado.getEmail())
                .telefone(contatoSimulado.getTelefone())
                .dataNascimento(contatoSimulado.getDataNascimento())
                .enderecoLista(List.of(enderecoDTO))
                .build();

        when(contatoRepository.findByEmail(contatoSimulado.getEmail())).thenReturn(Optional.empty());
        ContatoDTO contatoSalvo = contatoService.criarContato(contatoDTO);
        assertNotNull(contatoSalvo);

        assertEquals("Gabriel", contatoDTO.getNome());

        verify(contatoRepository, times(1)).findByEmail(contatoDTO.getEmail());

    }

    @Test
    void naoDeveCriarContato_QuandoEmail_EstiverCadastrado(){
        UUID id = UUID.randomUUID();

        EnderecoDTO enderecoDTO = EnderecoDTO.builder()
                .id(1)
                .nomeRua("Avenida")
                .numeroRua(2)
                .cep("72380-000")
                .build();

        ContatoDTO contatoDTO = ContatoDTO.builder()
                .nome("Gabriel")
                .email("gaba@gmail.com")
                .telefone("(61) 99394-9087")
                .dataNascimento(LocalDate.of(2005, 6, 12))
                .enderecoLista(List.of(enderecoDTO))
                .build();

        Contato contatoSimulado = Contato.builder()
                .id(id)
                .nome(contatoDTO.getNome())
                .email(contatoDTO.getEmail())
                .telefone(contatoDTO.getTelefone())
                .dataNascimento(contatoDTO.getDataNascimento())
                .enderecoLista(null)
                .build();

        when(contatoRepository.findByEmail(contatoSimulado.getEmail())).thenReturn(Optional.of(contatoSimulado));

        ContatoDTO contato = contatoService.criarContato(contatoDTO);

        assertNull(contato);
        verify(contatoRepository, times(1)).findByEmail(contatoDTO.getEmail());
        verify(contatoRepository, never()).save(contatoSimulado);
    }

    /*TESTES ABAIXO SÃO REFERENTES À ATUALIZAÇÃO DE CONTATOS*/
    @Test
    void deveRetornarNull_AoTentar_AtualizarContatoInexistente() throws Exception {
        UUID id = UUID.randomUUID();

        when(contatoRepository.existsById(id)).thenReturn(false);

        ContatoDTO resultado = contatoService.atualizarInfoContato(id, any(ContatoDTO.class));

        assertNull(resultado);

        verify(contatoRepository, times(1)).existsById(id);

    }

    @Test
    void deveVerificarExistenciaDoContato_EAtualizar() throws Exception{
        UUID id = UUID.randomUUID();

        Endereco endereco = Endereco.builder()
                .id(1)
                .nomeRua("Avenida")
                .numeroRua(2)
                .cep("72380-000")
                .build();

        Contato contatoSimulado = Contato.builder()
                .id(id)
                .nome("Gabriel")
                .email("gaba@gmail.com")
                .telefone("(61) 99394-9087")
                .dataNascimento(LocalDate.of(2005, 6, 12))
                .enderecoLista(List.of(endereco))
                .build();

        EnderecoDTO enderecoDTO = EnderecoDTO.builder()
                .id(1)
                .nomeRua("Avenida")
                .numeroRua(2)
                .cep("72380-000")
                .build();

        ContatoDTO contatoDTO = ContatoDTO.builder()
                .nome("Fulano")
                .email("fulano@gmail.com")
                .telefone("(61) 99834-9088")
                .dataNascimento(LocalDate.of(2003, 5, 6))
                .enderecoLista(List.of(enderecoDTO))
                .build();

        Contato contatoAtualizado = Contato.builder()
                .nome(contatoDTO.getNome())
                .email(contatoDTO.getEmail())
                .telefone(contatoDTO.getTelefone())
                .dataNascimento(contatoDTO.getDataNascimento())
                .enderecoLista(null)
                .build();


        when(contatoRepository.existsById(id)).thenReturn(true);
        when(contatoRepository.getReferenceById(id)).thenReturn(contatoSimulado);
        when(contatoRepository.findByEmail(contatoDTO.getEmail())).thenReturn(Optional.empty());
        when(contatoRepository.save(any(Contato.class))).thenReturn(contatoAtualizado);

        ContatoDTO contatoRetornado = contatoService.atualizarInfoContato(id, contatoDTO);

        assertNotNull(contatoRetornado);
        assertEquals(contatoAtualizado.getEmail(), contatoRetornado.getEmail());
        assertEquals(contatoAtualizado.getNome(), contatoRetornado.getNome());

        verify(contatoRepository, times(1)).save(any(Contato.class));

    }

    @Test
    void deveVerificarElancarExcecao_EmailExistenteOutroContato(){
        UUID idContatoASerAtualizado = UUID.randomUUID();
        UUID idOutroContato = UUID.randomUUID();


        Contato contatoSimulado = Contato.builder()
                .id(idOutroContato)
                .email("gaba@gmail.com")
                .build();


        ContatoDTO contatoDTO = ContatoDTO.builder()
                .email("gaba@gmail.com")
                .build();


        when(contatoRepository.existsById(idContatoASerAtualizado)).thenReturn(true);
        when(contatoRepository.findByEmail(contatoDTO.getEmail())).thenReturn(Optional.of(contatoSimulado));

        assertThrows(EmailExistenteException.class, () ->{
            contatoService.atualizarInfoContato(idContatoASerAtualizado, contatoDTO);
        });

        verify(contatoRepository, never()).save(any());
    }




}