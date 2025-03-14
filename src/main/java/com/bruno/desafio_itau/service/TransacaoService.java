package com.bruno.desafio_itau.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.bruno.desafio_itau.domain.entity.Transacao;
import com.bruno.desafio_itau.dtos.EstatisticaResponseDto;
import com.bruno.desafio_itau.dtos.TransacaoRequestDto;

@Service
public class TransacaoService {

    private final NavigableMap<LocalDateTime, TreeSet<Transacao>> historico;

    public TransacaoService(TreeMap<LocalDateTime, TreeSet<Transacao>> historico) {
        this.historico = historico;
    }

    public Transacao criarTransacao(TransacaoRequestDto request) {
        Transacao transacao = Transacao.builder().valor(request.valor()).dataHora(request.dataHora()).build();

        salvarTransacao(transacao);

        return transacao;
    }

    public void salvarTransacao(Transacao transacao) {
        historico.computeIfAbsent(transacao.getDataHora().truncatedTo(ChronoUnit.MINUTES),
                k -> new TreeSet<Transacao>(
                        Comparator.comparing(Transacao::getDataHora).thenComparing(Transacao::getValor)))
                .add(transacao);

        System.out.println("HISTORICO -> " + historico.toString());
    }

    public void apagarTransacoes() {
        historico.clear();
        System.out.println(historico.toString());
    }

    public EstatisticaResponseDto estatisticas() {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime pastMinute = currentTime.minusSeconds(60);

        System.out.println("Current -> " + currentTime);
        System.out.println("Past -> " + pastMinute);

        // retonar todas as chaves+valores que forem >= pastMinute e < currentTime + 1
        // minuto
        SortedMap<LocalDateTime, TreeSet<Transacao>> transacoes = historico
                .subMap(pastMinute.truncatedTo(ChronoUnit.MINUTES),
                        currentTime.truncatedTo(ChronoUnit.MINUTES).plusMinutes(1));

        if (transacoes.isEmpty()) {
            return new EstatisticaResponseDto(0L, 0.0, 0.0, 0.0, 0.0);
        }

        List<Transacao> transacoesUltimoMinuto = new ArrayList<>();

        for (TreeSet<Transacao> ts : transacoes.values()) {
            for (Transacao tr : ts) {
                if (tr.getDataHora().isAfter(pastMinute) && tr.getDataHora().isBefore(currentTime)) {
                    transacoesUltimoMinuto.add(tr);
                }
            }
        }

        if (transacoesUltimoMinuto.isEmpty()) {
            return new EstatisticaResponseDto(0L, 0.0, 0.0, 0.0, 0.0);
        }

        // para cada Transacao na lista > pega o valor de cada transacao > faz as
        // operações: count, sum, avg, min, max
        DoubleSummaryStatistics est = transacoesUltimoMinuto.stream()
                .collect(Collectors.summarizingDouble(Transacao::getValor));

        return new EstatisticaResponseDto(est.getCount(), est.getSum(),
                est.getAverage(), est.getMax(), est.getMin());

    }

}
