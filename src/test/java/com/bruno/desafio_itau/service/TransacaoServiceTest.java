package com.bruno.desafio_itau.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bruno.desafio_itau.domain.entity.Transacao;
import com.bruno.desafio_itau.dtos.EstatisticaResponseDto;
import com.bruno.desafio_itau.dtos.TransacaoRequestDto;

@ExtendWith(MockitoExtension.class)
public class TransacaoServiceTest {

    @Mock
    NavigableMap<LocalDateTime, TreeSet<Transacao>> historico;

    TransacaoService transacaoService;

    Transacao transacao;
    TransacaoRequestDto transacaoRequestDto;

    @BeforeEach
    void setUp() {
        historico = new TreeMap<>();

        transacaoRequestDto = new TransacaoRequestDto(100.0, LocalDateTime.now());

        transacaoService = new TransacaoService((TreeMap<LocalDateTime, TreeSet<Transacao>>) historico);

    }

    @Test
    @DisplayName("Deve criar uma transação corretamente")
    void deveCriarUmaTransacaoCorretamente() {
        // Arrange
        TransacaoRequestDto novaTransacaoDto = new TransacaoRequestDto(50.0,
                LocalDateTime.now().minusSeconds(1));
        System.out.println(historico.toString());

        // Act
        Transacao novaTransacao = transacaoService.criarTransacao(novaTransacaoDto);

        // Assert
        assertNotNull(novaTransacao);
        assertEquals(novaTransacaoDto.valor(), novaTransacao.getValor());
        assertEquals(novaTransacaoDto.dataHora().truncatedTo(ChronoUnit.MINUTES),
                novaTransacao.getDataHora().truncatedTo(ChronoUnit.MINUTES));
    }

    @Test
    @DisplayName("Deve salvar uma transação corretamente")
    void deveSalvarUmaTransacaoCorretamente() {
        // Arrange
        Transacao transacaoParaSalvar = Transacao.builder().valor(200.0).dataHora(LocalDateTime.now().plusSeconds(1))
                .build();

        // Act
        transacaoService.salvarTransacao(transacaoParaSalvar);

        // Assert
        assertTrue(historico.size() == 1);
        assertEquals(historico.get(transacaoParaSalvar.getDataHora().truncatedTo(ChronoUnit.MINUTES)).getFirst(),
                transacaoParaSalvar);

    }

    @Test
    @DisplayName("Deve apagar todas as transações")
    void deveApagarTodasTransacoes() {
        // Act
        transacaoService.apagarTransacoes();

        // Assert
        assertTrue(historico.size() == 0);
    }

    @Test
    @DisplayName("Deve retornar estatísticas corretamente")
    void deveRetornarEstatisticasCorretamente() {
        // Arrange
        LocalDateTime currentTime = LocalDateTime.now();
        TreeSet<Transacao> transacoes = new TreeSet<>(Comparator.comparing(Transacao::getDataHora));
        transacoes.add(Transacao.builder().valor(10.0).dataHora(currentTime.minusSeconds(1)).build());
        transacoes.add(Transacao.builder().valor(20.0).dataHora(currentTime.minusSeconds(2)).build());
        transacoes.add(Transacao.builder().valor(30.0).dataHora(currentTime.minusSeconds(3)).build());

        historico.put(currentTime.truncatedTo(ChronoUnit.MINUTES), transacoes);
        // Act
        EstatisticaResponseDto estatisticas = transacaoService.estatisticas();

        // Assert
        assertNotNull(estatisticas);
        assertEquals(3, estatisticas.count());
        assertEquals(60.0, estatisticas.sum());
        assertEquals(20.0, estatisticas.avg());
        assertEquals(30.0, estatisticas.max());
        assertEquals(10.0, estatisticas.min());
    }

    @Test
    @DisplayName("Deve retornar estatísticas zeradas se não houver transações")
    void deveRetornarEstatisticasZeradasSeNaoHouverTransacoes() {
        // Arrange
        historico.clear();
        // Act
        EstatisticaResponseDto estatisticas = transacaoService.estatisticas();

        // Assert
        assertNotNull(estatisticas);
        assertEquals(0, estatisticas.count());
        assertEquals(0.0, estatisticas.sum());
        assertEquals(0.0, estatisticas.avg());
        assertEquals(0.0, estatisticas.max());
        assertEquals(0.0, estatisticas.min());
    }

}
