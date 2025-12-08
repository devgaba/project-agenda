package com.project_agenda.agenda.entity;

import com.project_agenda.agenda.dto.ContatoDTO;
import com.project_agenda.agenda.service.impl.ContatoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ContatoTest {

    @Mock
    private ContatoService contatoService;

    @Test
    void deveCriarContato(){
        ContatoDTO contato = new ContatoDTO();
        contato.setNome("Gabriel");
        contato.setEmail("gaba@gmail.com");
        contato.setTelefone("(61) 99209-6800");
        contato.setDataNascimento(LocalDate.of(2000, 1, 1));

        contatoService.criarContato(contato);

        assertThat(contato.getNome()).isEqualTo("Gabriel");
        assertThat(contato.getEmail()).isEqualTo("gaba@gmail.com");
        assertThat(contato.getTelefone()).isEqualTo("(61) 99209-6800");
        assertThat(contato.getDataNascimento()).isEqualTo(LocalDate.of(2000,1,1));

    }

}