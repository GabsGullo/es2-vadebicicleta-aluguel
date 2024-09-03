package com.es2.vadebicicleta.es2.vadebicicleta.aluguel;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.api.CiclistaController;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.api.CiclistaConverter;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.api.CartaoDeCreditoConverter;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.CartaoDeCredito;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Ciclista;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.CiclistaOutDTO;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.CiclistaInPutDTO;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.RegistoCartaoCiclistaDTO;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service.CiclistaService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CiclistaControllerTest {

    @Mock
    private CiclistaService ciclistaService;

    @Mock
    private CiclistaConverter ciclistaConverter;

    @Mock
    private CartaoDeCreditoConverter cartaoDeCreditoConverter;

    @InjectMocks
    private CiclistaController ciclistaController;

    @Test
    void testGetCiclista() {
        // Dados de teste
        Ciclista ciclista = new Ciclista();
        CiclistaOutDTO ciclistaOutDTO = new CiclistaOutDTO();

        // Configurando o comportamento dos mocks
        when(ciclistaService.getById(1)).thenReturn(ciclista);
        when(ciclistaConverter.entityToOutDTO(ciclista)).thenReturn(ciclistaOutDTO);

        // Chamando o método a ser testado
        ResponseEntity<CiclistaOutDTO> response = ciclistaController.getCiclista(1);

        // Verificando o resultado
        assertEquals(ciclistaOutDTO, response.getBody());
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }

    @Test
    void testPostCiclista() {
        // Dados de teste
        RegistoCartaoCiclistaDTO registro = new RegistoCartaoCiclistaDTO();
        Ciclista ciclista = new Ciclista();
        CiclistaOutDTO ciclistaOutDTO = new CiclistaOutDTO();

        // Configurando o comportamento dos mocks
        when(ciclistaConverter.inPostDtoToEntity(registro.getCiclista())).thenReturn(ciclista);
        when(cartaoDeCreditoConverter.inDtoToEntity(registro.getCartaoDeCredito())).thenReturn(new CartaoDeCredito());
        when(ciclistaService.register(any(Ciclista.class), any(CartaoDeCredito.class))).thenReturn(ciclista);
        when(ciclistaConverter.entityToOutDTO(ciclista)).thenReturn(ciclistaOutDTO);

        // Chamando o método a ser testado
        ResponseEntity<CiclistaOutDTO> response = ciclistaController.postCiclista(registro);

        // Verificando o resultado
        assertEquals(ciclistaOutDTO, response.getBody());
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }

    @Test
    void testPutCiclista() {
        // Dados de teste
        CiclistaInPutDTO ciclistaInPutDTO = new CiclistaInPutDTO();
        Ciclista ciclista = new Ciclista();
        CiclistaOutDTO ciclistaOutDTO = new CiclistaOutDTO();

        // Configurando o comportamento dos mocks
        when(ciclistaConverter.inPutDtoToEntity(ciclistaInPutDTO)).thenReturn(ciclista);
        when(ciclistaService.update(ciclista, 1)).thenReturn(ciclista);
        when(ciclistaConverter.entityToOutDTO(ciclista)).thenReturn(ciclistaOutDTO);

        // Chamando o método a ser testado
        ResponseEntity<CiclistaOutDTO> response = ciclistaController.putCiclista(ciclistaInPutDTO, 1);

        // Verificando o resultado
        assertEquals(ciclistaOutDTO, response.getBody());
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }

    @Test
    void testAtivarCiclista() {
        // Dados de teste
        Ciclista ciclista = new Ciclista();
        CiclistaOutDTO ciclistaOutDTO = new CiclistaOutDTO();

        // Configurando o comportamento dos mocks
        when(ciclistaService.activate(1)).thenReturn(ciclista);
        when(ciclistaConverter.entityToOutDTO(ciclista)).thenReturn(ciclistaOutDTO);

        // Chamando o método a ser testado
        ResponseEntity<CiclistaOutDTO> response = ciclistaController.ativarCiclista(1);

        // Verificando o resultado
        assertEquals(ciclistaOutDTO, response.getBody());
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }
}
