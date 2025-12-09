package com.project_agenda.agenda.utils;

import com.project_agenda.agenda.dto.ContatoDTO;
import com.project_agenda.agenda.dto.EnderecoDTO;
import com.project_agenda.agenda.entity.Contato;
import com.project_agenda.agenda.entity.Endereco;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class BeanCopyUtilsTest {

    @Autowired
    private BeanCopyUtils beanCopyUtils;



    @Test
    void copiarPropriedadesNaoNulas() {


        Endereco end = Endereco.builder()
                .id(1)
                .nomeRua("Teste")
                .numeroRua(3)
                .cep("72682-680")
                .contato(null)
                .build();

        Contato contato = Contato.builder()
                .id(UUID.randomUUID())
                .nome("Gabriel")
                .email("gaba@gmail.com")
                .telefone("(61) 99029-6800")
                .dataNascimento(LocalDate.of(2000,2,4))
                .enderecoLista(List.of(end))
                .build();

        end.setContato(contato);

        Contato contatoSimulado = Contato.builder()
                .id(UUID.randomUUID())
                .nome(null)
                .email("gabHenri@gmail.com")
                .telefone(null)
                .dataNascimento(LocalDate.of(2000,4,11))
                .enderecoLista(null)
                .build();

        BeanCopyUtils.copiarPropriedadesNaoNulas(contatoSimulado, contato);


        assertEquals(contatoSimulado.getEmail(), contato.getEmail());
        assertEquals(contatoSimulado.getDataNascimento(), contato.getDataNascimento());
    }

    @Test
    void obterPropriedadesNulas(){
        Contato contatoSimulado = Contato.builder()
                .id(UUID.randomUUID())
                .nome(null)
                .email("teste@example.com")
                .telefone(null)
                .dataNascimento(null)
                .enderecoLista(null)
                .build();

        List<String> atributosNulosEsperados = Arrays.asList(
                "nome",
                "telefone",
                "dataNascimento",
                "enderecoLista"
        );

        String[] resultado = BeanCopyUtils.obterPropriedadesNulas(contatoSimulado);
        List<String> listaResultados = Arrays.asList(resultado);
        Collections.sort(listaResultados);
        Collections.sort(atributosNulosEsperados);

        assertEquals(atributosNulosEsperados.size(), resultado.length);
        assertEquals(atributosNulosEsperados, listaResultados);
    }

    @Test
    void testAtualizarEnderecos_listaVazia() {
        Endereco end = Endereco.builder()
                .id(2)
                .nomeRua("AVENIDA")
                .numeroRua(2)
                .cep("72658-000")
                .build();

        Contato contato = Contato.builder()
                .id(UUID.randomUUID())
                .nome("TESTE")
                .email("teste@example.com")
                .telefone("(61) 94568-0000")
                .dataNascimento(LocalDate.of(2000,4,11))
                .enderecoLista(List.of(end))
                .build();

        List<EnderecoDTO> enderecoDTOS =  new ArrayList<>();

        BeanCopyUtils.atualizarEnderecos(contato, enderecoDTOS);

        assertEquals(new ArrayList<>(), enderecoDTOS);
    }



    /*
    @Test
    void testAtualizarEnderecos_atualizaEnderecoExistente() {
        Endereco e = new Endereco();
        e.setId(1);

        Contato contato = new Contato();
        contato.getEnderecoLista().add(e);

        EnderecoDTO dto = new EnderecoDTO();
        dto.setId(1);

        List<EnderecoDTO> dtoList = List.of(dto);

        assertDoesNotThrow(() ->
                BeanCopyUtils.atualizarEnderecos(contato, dtoList)
        );
    }

    @Test
    void testAtualizarEnderecos_adicionaNovoEndereco() {
        Contato contato = new Contato();

        EnderecoDTO dto = new EnderecoDTO();
        dto.setId(99);

        List<EnderecoDTO> dtoList = List.of(dto);

        assertDoesNotThrow(() ->
                BeanCopyUtils.atualizarEnderecos(contato, dtoList)
        );
    }


     */
}