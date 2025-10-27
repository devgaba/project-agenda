import com.project_agenda.agenda.dto.ContatoDTO;
import com.project_agenda.agenda.dto.EnderecoDTO;
import com.project_agenda.agenda.entity.Contato;
import com.project_agenda.agenda.repository.ContatoRepository;
import com.project_agenda.agenda.service.impl.ContatoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ContatoServiceTest {

    @Mock
    private ContatoRepository contatoRepository;

    @InjectMocks
    private ContatoService contatoService;

    @Test
    void deveCriarUmContato(){

        Contato contatoSalvoMock = contatoService.criarContato(new ContatoDTO(
                "Gabriel", "gaba@gmail.com",
                "(61) 99209-6800", "01/01/2000", List.of(EnderecoDTO.builder()
                .id(1L)
                .nomeRua("Gotham")
                .numeroRua(1L)
                .cep("75506-000").build())));

        Mockito.when(contatoRepository.save(any(Contato.class))).thenReturn(contatoSalvoMock);
    }


}
