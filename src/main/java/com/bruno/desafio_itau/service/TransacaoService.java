package com.bruno.desafio_itau.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.bruno.desafio_itau.domain.entity.Transacao;
import com.bruno.desafio_itau.dtos.EstatisticaResponseDto;
import com.bruno.desafio_itau.dtos.TransacaoRequestDto;

@Service
public class TransacaoService {

    private final NavigableMap<LocalDateTime, List<Transacao>> historico;

    public TransacaoService(TreeMap<LocalDateTime, List<Transacao>> historico) {
        this.historico = historico;
    }

    public Transacao criarTransacao(TransacaoRequestDto request) {
        Transacao transacao = Transacao.builder().valor(request.valor()).dataHora(request.dataHora()).build();

        salvarTransacao(transacao);

        return transacao;
    }

    public void salvarTransacao(Transacao transacao) {
        historico.computeIfAbsent(transacao.getDataHora().truncatedTo(ChronoUnit.MINUTES),
                k -> new ArrayList<Transacao>()).add(transacao);
    }

    public void apagarTransacoes() {
        historico.clear();
        System.out.println(historico.toString());
    }

    public EstatisticaResponseDto estatisticas() {
        LocalDateTime currentTime = LocalDateTime.now();

        List<Transacao> transacoes = historico.get(currentTime.truncatedTo(ChronoUnit.MINUTES));

        if (transacoes == null) {
            return new EstatisticaResponseDto(0L, 0.0, 0.0, 0.0, 0.0);
        }

        // para cada Transacao na lista > pega o valor de cada transacao > faz as
        // operações: count, sum, avg, min, max
        DoubleSummaryStatistics est = transacoes.stream().collect(Collectors.summarizingDouble(Transacao::getValor));

        return new EstatisticaResponseDto(est.getCount(), est.getSum(), est.getAverage(), est.getMax(), est.getMin());

    }

}
