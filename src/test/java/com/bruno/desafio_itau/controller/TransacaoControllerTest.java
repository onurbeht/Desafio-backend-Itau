package com.bruno.desafio_itau.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.bruno.desafio_itau.dtos.EstatisticaResponseDto;
import com.bruno.desafio_itau.dtos.TransacaoRequestDto;
import com.bruno.desafio_itau.service.TransacaoService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransacaoController.class)
public class TransacaoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockitoBean
    TransacaoService transacaoService;

    TransacaoRequestDto transacaoRequestDto;
    EstatisticaResponseDto estatisticaResponseDto;

    @BeforeEach
    void setUp() {
        transacaoRequestDto = new TransacaoRequestDto(100.0, LocalDateTime.now());
        estatisticaResponseDto = new EstatisticaResponseDto(1l, 100.0, 100.0, 100.0, 100.0);
    }

    @Test
    @DisplayName("Deve retornar 201 quando a transação for recebida com sucesso")
    void receberTransacao_DeveRetornar201() throws Exception {
        String json = mapper.writeValueAsString(transacaoRequestDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/transacao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());

        verify(transacaoService, times(1)).criarTransacao(any(TransacaoRequestDto.class));
    }

    @Test
    @DisplayName("Deve retornar 422 quando o valor da transação for inválido")
    void receberTransacao_ComValorInvalido_DeveRetornar422() throws Exception {
        transacaoRequestDto = new TransacaoRequestDto(null, LocalDateTime.now());
        String json = mapper.writeValueAsString(transacaoRequestDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/transacao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("Deve retornar 422 quando a data da transação for inválida")
    void receberTransacao_ComDataInvalida_DeveRetornar422() throws Exception {
        transacaoRequestDto = new TransacaoRequestDto(100.0,
                LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS));
        String json = mapper.writeValueAsString(transacaoRequestDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/transacao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("Deve retornar 400 quando o JSON da transação for inválida")
    void receberTransacao_ComJsonInvalido_DeveRetornar400() throws Exception {
        String json = mapper.writeValueAsString("abc");
        mockMvc.perform(MockMvcRequestBuilders.post("/transacao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar 200 quando as transações forem apagadas com sucesso")
    void apagarTransacoes_DeveRetornar200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/transacao"))
                .andExpect(status().isOk());

        verify(transacaoService, times(1)).apagarTransacoes();
    }

    @Test
    @DisplayName("Deve retornar 200 e as estatísticas quando as estatísticas forem solicitadas")
    void estatisticas_DeveRetornar200Estatistica() throws Exception {
        when(transacaoService.estatisticas()).thenReturn(estatisticaResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/transacao/estatistica"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.sum").value(100))
                .andExpect(MockMvcResultMatchers.jsonPath("$.avg").value(100))
                .andExpect(MockMvcResultMatchers.jsonPath("$.max").value(100))
                .andExpect(MockMvcResultMatchers.jsonPath("$.min").value(100))
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(1));
    }

}
