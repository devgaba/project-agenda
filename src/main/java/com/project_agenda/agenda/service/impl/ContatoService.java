package com.project_agenda.agenda.service.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project_agenda.agenda.dto.ContatoDTO;
import com.project_agenda.agenda.dto.EnderecoDTO;
import com.project_agenda.agenda.entity.Contato;
import com.project_agenda.agenda.entity.Endereco;
import com.project_agenda.agenda.exception.EmailExistenteException;
import com.project_agenda.agenda.exception.RecursoNaoEncontradoException;
import com.project_agenda.agenda.repository.ContatoRepository;
import com.project_agenda.agenda.repository.EnderecoRepository;
import com.project_agenda.agenda.service.IContatoService;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;


@Service
public class ContatoService implements IContatoService {


    @Autowired
    private ContatoRepository contatoRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Override
    public List<ContatoDTO> exibirContatos() {

        return contatoRepository.findAll().stream().map(
                contato -> ContatoDTO.builder()
                        .id(contato.getId())
                        .nome(contato.getNome()).email(contato.getEmail())
                        .telefone(contato.getTelefone()).dataNascimento(contato.getDataNascimento())
                        .enderecoLista(contato.getEnderecoLista().stream().map(
                                endereco -> EnderecoDTO.builder()
                                        .id(endereco.getId())
                                        .nomeRua(endereco.getNomeRua())
                                        .numeroRua(endereco.getNumeroRua())
                                        .cep(endereco.getCep())
                                        .build()).toList())
                        .build()).toList();
    }

    @Override
    @Transactional
    public ContatoDTO criarContato(ContatoDTO contatoDTO) {

        Optional<Contato> email = contatoRepository.findByEmail(contatoDTO.getEmail());

        try{
            if(email.isPresent()){
                throw new EmailExistenteException("O email está cadastrado em outro contato.");
            }
            criarContatoComDto(contatoDTO);
            return ContatoDTO.builder().nome(contatoDTO.getNome())
                    .email(contatoDTO.getEmail()).telefone(contatoDTO.getTelefone())
                    .enderecoLista(contatoDTO.getEnderecoLista()).build();

        } catch (EmailExistenteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean excluirContato(UUID id) throws RecursoNaoEncontradoException {

        if(!contatoRepository.existsById(id)){
            throw new RecursoNaoEncontradoException("O contato com ID " + id +" não foi encontrado ou não existe." );
        }
        contatoRepository.deleteById(id);
        return true;
    }

    @Override
    public ContatoDTO atualizarInfoContato(UUID id, ContatoDTO contatoDTO) throws Exception {

        try{
            return atualizarContatoComDto(id,contatoDTO);

        }
        catch (RecursoNaoEncontradoException e){
            throw new RecursoNaoEncontradoException(e.getMessage());
        }
    }

    private void criarContatoComDto(ContatoDTO contatoDTO){

        List<Endereco> enderecosAdicionados = new ArrayList<>();

        Contato contatoCriado = Contato.builder()
                .nome(contatoDTO.getNome())
                .email(contatoDTO.getEmail())
                .telefone(contatoDTO.getTelefone())
                .dataNascimento(contatoDTO.getDataNascimento())
                .enderecoLista(enderecosAdicionados)
                .build();

        enderecosAdicionados.addAll(contatoDTO.getEnderecoLista().stream().map(
                enderecoDTO -> Endereco.builder().nomeRua(enderecoDTO.getNomeRua())
                        .numeroRua(enderecoDTO.getNumeroRua())
                        .cep(enderecoDTO.getCep())
                        .contato(contatoCriado)
                        .build()).toList());

        contatoRepository.save(contatoCriado);
    }

    private ContatoDTO atualizarContatoComDto(UUID id, ContatoDTO contatoDTO) throws IOException {


        Contato contatoExistente = contatoRepository.findById(id).orElseThrow(
                () -> new RecursoNaoEncontradoException("O ID não foi encontrado ou não existe."));


        ObjectMapper mapeador = new ObjectMapper();
        mapeador.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String dtoParaString = mapeador.writeValueAsString(contatoDTO);
        mapeador.readerForUpdating(contatoExistente).readValue(dtoParaString);

        Contato contatoAtualizado = contatoRepository.save(contatoExistente);

        ContatoDTO dtoVisualizacao = new ContatoDTO();
        BeanUtils.copyProperties(contatoAtualizado, dtoVisualizacao);
        return dtoVisualizacao;

        /*
        return ContatoDTO.builder().nome(contatoAtualizado.getNome())
                .email(contatoAtualizado.getEmail()).telefone(contatoAtualizado.getTelefone())
                .enderecoLista(contatoAtualizado.getEnderecoLista().stream().map(
                        endereco -> EnderecoDTO.builder()
                                .id(endereco.getId()).nomeRua(endereco.getNomeRua())
                                .numeroRua(endereco.getNumeroRua()).cep(endereco.getCep())
                                .build()).toList()).build();

         */
    }
}
