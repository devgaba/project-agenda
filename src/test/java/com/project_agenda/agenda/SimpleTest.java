package com.project_agenda.agenda;

import com.project_agenda.agenda.dto.ContatoDTO;
import com.project_agenda.agenda.dto.EnderecoDTO;
import com.project_agenda.agenda.entity.Contato;
import com.project_agenda.agenda.repository.ContatoRepository;
import com.project_agenda.agenda.service.impl.ContatoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class SimpleTest {

    @Mock
    private ContatoRepository contatoRepository;
    private ContatoService contatoService;

    @BeforeEach
    public void init(){
        contatoRepository = mock(ContatoRepository.class);
        contatoService = mock(ContatoService.class);
    }

    @Test
    @DisplayName("Deve criar um contatoDTO")
    void deveCriarContato(){

        EnderecoDTO end = EnderecoDTO.builder()
                .id(1L)
                .nomeRua("Avenida")
                .numeroRua(1L)
                .cep("72268-000")
                .build();

        ContatoDTO contato = new ContatoDTO();
        contato.setNome("Username");
        contato.setEmail("email@email.com");
        contato.setTelefone("(61) 01234-5680");
        contato.setDataNascimento("11/04/2000");
        contato.setEnderecoLista(List.of(end));

        contatoService.criarContato(contato);
    }
}
