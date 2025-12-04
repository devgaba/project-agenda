package com.project_agenda.agenda.utils;

import com.project_agenda.agenda.dto.EnderecoDTO;
import com.project_agenda.agenda.entity.Contato;
import com.project_agenda.agenda.entity.Endereco;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.*;

public class BeanCopyUtils {

    public static void copiarPropriedadesNaoNulas(Object dtoInserido, Object entidadeSalva, String... ignorar){
        String[] propriedadesNulas = obterPropriedadesNulas(dtoInserido);

        List<String> propriedadeASeremIgnoradas = new ArrayList<>(Arrays.asList(propriedadesNulas));
        propriedadeASeremIgnoradas.addAll(Arrays.asList(ignorar));
        BeanUtils.copyProperties(dtoInserido, entidadeSalva, propriedadeASeremIgnoradas.toArray(new String[0]));
    }

    public static String[] obterPropriedadesNulas(Object dtoInserido){
        final BeanWrapper dto = new BeanWrapperImpl(dtoInserido);
        PropertyDescriptor[] camposNulos = dto.getPropertyDescriptors();

        Set<String> atributosNulos = new HashSet<>();
        for(PropertyDescriptor campo : camposNulos){
            Object propriedadeCampo = dto.getPropertyValue(campo.getName());

            if(propriedadeCampo == null){
                atributosNulos.add(campo.getName());
            }
        }
        String[] campos = new String[atributosNulos.size()];
        return atributosNulos.toArray(campos);
    }

    public static void atualizarEnderecos(Contato contato, List<EnderecoDTO> enderecosDTO){
        if(enderecosDTO == null || enderecosDTO.isEmpty()){
            return;
        }
        List<Endereco> enderecosAtuais = contato.getEnderecoLista();
        for(EnderecoDTO enderecoDTO : enderecosDTO){
            if(enderecoDTO.getId() != null){
                Optional<Endereco> enderecoExistente = enderecosAtuais.stream()
                        .filter(e -> enderecoDTO.getId().equals(e.getId()))
                        .findFirst();

                if(enderecoExistente.isPresent()){
                    Endereco endereco = enderecoExistente.get();
                    copiarPropriedadesNaoNulas(enderecoDTO, endereco);
                }
                else {
                    adicionarEndereco(contato, enderecoDTO);
                }
            }
            else{
                adicionarEndereco(contato, enderecoDTO);
            }

        }
    }

    private static void adicionarEndereco(Contato contato, EnderecoDTO novoEnderecoDTO){

        List<Endereco> listaEnderecoContato = contato.getEnderecoLista();

        Endereco novo = Endereco.builder()
                .nomeRua(novoEnderecoDTO.getNomeRua())
                .numeroRua(novoEnderecoDTO.getNumeroRua())
                .cep(novoEnderecoDTO.getCep())
                .contato(contato)
                .build();



        if(contato.getEnderecoLista() == null){
            contato.setEnderecoLista(new ArrayList<>());
        }

        novoEnderecoDTO.setContato(contato);
        listaEnderecoContato.add(novo);
    }
}
