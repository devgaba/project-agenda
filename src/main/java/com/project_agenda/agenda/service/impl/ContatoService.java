package com.project_agenda.agenda.service.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project_agenda.agenda.dto.ContatoDTO;
import com.project_agenda.agenda.dto.EnderecoDTO;
import com.project_agenda.agenda.entity.Contato;
import com.project_agenda.agenda.entity.Endereco;
import com.project_agenda.agenda.exception.EmailExistenteException;
import com.project_agenda.agenda.exception.RecursoNaoEncontradoException;
import com.project_agenda.agenda.repository.ContatoRepository;
import com.project_agenda.agenda.repository.EnderecoRepository;
import com.project_agenda.agenda.service.IContatoService;
import io.swagger.v3.core.util.Json;
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

        Contato contatoExistente = contatoRepository.findById(id).orElseThrow(
                () -> new RecursoNaoEncontradoException("O contato com ID " + id + " não foi encontrado ou não existe.")
        );
        try{
            contatoRepository.delete(contatoExistente);
            return true;
        }
        catch (RecursoNaoEncontradoException e){
            throw new RecursoNaoEncontradoException("O contato com ID " + id +" não foi encontrado ou não existe." );
        }
    }

    @Override
    @Transactional
    public ContatoDTO substituirContato(UUID id, ContatoDTO contatoDTO) throws RecursoNaoEncontradoException{

        try{
            return substituiContato(id, contatoDTO);

        }catch(RecursoNaoEncontradoException e){
            throw new RecursoNaoEncontradoException("Contato não encontrado.");
        }
    }

    @Override
    @Transactional
    public ContatoDTO atualizarInfoContato(UUID id, ContatoDTO contatoDTO) throws Exception {

        try{
            return atualizarContatoComDto(id, contatoDTO);
        }
        catch (RecursoNaoEncontradoException e){
            throw new RecursoNaoEncontradoException(e.getMessage());
        }
        catch (IOException e){
            throw new RuntimeException("Falha ao processar dados de Contato.");
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

    private ContatoDTO substituiContato(UUID id, ContatoDTO contatoDTO){
        Contato contatoExistente = contatoRepository.findById(id).orElseThrow(
                () -> new RecursoNaoEncontradoException("Contato não encontrado ou inexistente."));

        List<Endereco> enderecosAtualizados = contatoDTO.getEnderecoLista().stream().map(
                e -> Endereco.builder()
                        .nomeRua(e.getNomeRua())
                        .numeroRua(e.getNumeroRua())
                        .cep(e.getCep())
                        .contato(contatoExistente)
                        .build()).toList();

        contatoExistente.setNome(contatoDTO.getNome());
        contatoExistente.setEmail(contatoDTO.getEmail());
        contatoExistente.setTelefone(contatoDTO.getTelefone());
        contatoExistente.setDataNascimento(contatoDTO.getDataNascimento());
        contatoExistente.getEnderecoLista().clear();
        contatoExistente.getEnderecoLista().addAll(enderecosAtualizados);


        ContatoDTO contatoRepresentacao = new ContatoDTO();
        BeanUtils.copyProperties(contatoExistente, contatoRepresentacao);
        return contatoRepresentacao;
    }

    private ContatoDTO atualizarContatoComDto(UUID id, ContatoDTO contatoDTO) throws IOException {


        Contato contatoExistente = contatoRepository.findById(id).orElseThrow(
                () -> new RecursoNaoEncontradoException("O ID não foi encontrado ou não existe."));

        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ContatoDTO contTeste = new ContatoDTO();
        BeanUtils.copyProperties(contatoDTO, contTeste, "enderecoLista");

        String jsonDTO = mapper.writeValueAsString(contTeste);
        Contato contatoDesserializado = mapper.readerForUpdating(contatoExistente)
                .readValue(jsonDTO, Contato.class);

        JsonNode verificarCampoEnderecos = mapper.readTree(jsonDTO);
        JsonNode campo = verificarCampoEnderecos.get("enderecoLista");

        if(campo != null){
            for(EnderecoDTO end : contatoDTO.getEnderecoLista()){
                atualizarEnderecoDoContato(id, end.getId(), end);
            }
        }
        contatoRepository.save(contatoDesserializado);

        return ContatoDTO.builder().id(contatoExistente.getId())
                .nome(contatoExistente.getNome())
                .email(contatoExistente.getEmail())
                .telefone(contatoExistente.getTelefone())
                .dataNascimento(contatoExistente.getDataNascimento())
                .enderecoLista(contatoExistente.getEnderecoLista().stream().map(
                        e -> EnderecoDTO.builder()
                                .id(e.getId())
                                .nomeRua(e.getNomeRua())
                                .numeroRua(e.getNumeroRua())
                                .cep(e.getCep()).build()).toList()).build();
    }

    private void atualizarEnderecoDoContato(UUID id, Integer enderecoId, EnderecoDTO enderecoDTO) throws IOException{
        Contato contatoExistente = contatoRepository.findById(id).orElseThrow(
                () -> new RecursoNaoEncontradoException("Contato não encontrado."));

        boolean verificarIdEndereco = contatoExistente.getEnderecoLista()
                .stream().anyMatch(e -> e.getId().equals(enderecoId));

        if(verificarIdEndereco && verificarCamposEndereco(enderecoDTO)){
            excluirEnderecoDoContato(id, enderecoId, enderecoDTO);
        } else if (verificarIdEndereco && !verificarCamposEndereco(enderecoDTO)) {
            Endereco enderecoExistente = contatoExistente.getEnderecoLista().stream()
                    .filter( e -> e.getId().equals(enderecoId))
                    .findFirst()
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Endereço não encontrado."));


            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            String jsonDTO = mapper.writeValueAsString(enderecoDTO);

            Endereco enderecoAtualizado = mapper.readerForUpdating(enderecoExistente)
                    .readValue(jsonDTO, Endereco.class);
        }
        else{
            adicionarEndereco(id, enderecoDTO);
        }
        contatoRepository.save(contatoExistente);
    }

    private void excluirEnderecoDoContato(UUID id, Integer enderecoId, EnderecoDTO enderecoDTO){
        Contato contatoExistente = contatoRepository.findById(id).orElseThrow(
                () -> new RecursoNaoEncontradoException("O ID não foi encontrado ou não existe"));

        if (verificarCamposEndereco(enderecoDTO)){
            contatoExistente.getEnderecoLista().removeIf(e -> e.getId().equals(enderecoId));
        }
        else{
            throw new RecursoNaoEncontradoException("ID do endereço inexistente.");
        }
        contatoRepository.save(contatoExistente);
    }

    private void adicionarEndereco(UUID id, EnderecoDTO enderecoDTO) throws JsonProcessingException {
        Contato contatoExistente = contatoRepository.findById(id).orElseThrow(
                () -> new RecursoNaoEncontradoException("O ID não foi encontrado ou não existe"));
        Endereco enderecoCriado = new Endereco();

        if(enderecoDTO.getId() == null){
                enderecoCriado.setNomeRua(enderecoDTO.getNomeRua());
                enderecoCriado.setNumeroRua(enderecoDTO.getNumeroRua());
                enderecoCriado.setCep(enderecoDTO.getCep());
                enderecoCriado.setContato(contatoExistente);
        }

        contatoExistente.getEnderecoLista().add(enderecoCriado);
        contatoRepository.save(contatoExistente);
    }

    private boolean verificarCamposEndereco(EnderecoDTO enderecoDTO){
        boolean idExistente = enderecoDTO.getId() != null;
        boolean ruaInexistente = enderecoDTO.getNomeRua() == null || enderecoDTO.getNomeRua().trim().isEmpty();
        boolean numeroInexistente = enderecoDTO.getNumeroRua() == null;
        boolean cepInexistente = enderecoDTO.getCep() == null || enderecoDTO.getCep().trim().isEmpty();

        return idExistente && ruaInexistente && numeroInexistente && cepInexistente;
    }
}
