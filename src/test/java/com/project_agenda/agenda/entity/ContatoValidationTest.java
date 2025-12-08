package com.project_agenda.agenda.entity;


import com.project_agenda.agenda.dto.ContatoDTO;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.validation.Validator;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ContatoValidationTest {


    @Autowired
    private Validator validator;

    @Test
    void deveFalhar_SeOCampoNomeEstiverVazio(){
        ContatoDTO contatoDTO = ContatoDTO.builder()
                .nome("")
                .email("gabriel@gmail.com")
                .telefone("(61) 99209-6800")
                .dataNascimento(LocalDate.of(2000,1,1))
                .enderecoLista(new ArrayList<>())
                .build();

        Set<ConstraintViolation<ContatoDTO>> violations = validator.validate(contatoDTO);

        assertThat(violations).isNotEmpty();
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("nome");

    }

    @Test
    void devefalhar_SeOCampoEmail_EstiverVazio(){
        ContatoDTO contatoDTO = ContatoDTO.builder()
                .nome("Gabriel")
                .email(null)
                .telefone("(61) 99209-6800")
                .dataNascimento(LocalDate.of(2000,1,1))
                .enderecoLista(new ArrayList<>())
                .build();

        Set<ConstraintViolation<ContatoDTO>> violations = validator.validate(contatoDTO);

        assertThat(violations).isNotEmpty();
        assertThat(violations).hasSize(1);

        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("email");
    }

    @Test
    void deveFalhar_SeOCampoEmail_EstiverInvalido(){
        ContatoDTO contatoDTO = ContatoDTO.builder()
                .nome("Gabriel")
                .email("gabriel_gmail.com") // E-mail inválido
                .telefone("(61) 99209-6800")
                .dataNascimento(LocalDate.of(2000,1,1))
                .enderecoLista(new ArrayList<>())
                .build();

        Set<ConstraintViolation<ContatoDTO>> violations = validator.validate(contatoDTO);

        assertThat(violations).isNotEmpty();

        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .contains("Formato de email inválido.");

    }


}
