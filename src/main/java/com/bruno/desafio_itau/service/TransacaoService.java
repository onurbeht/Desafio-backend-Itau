package com.bruno.desafio_itau.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.springframework.stereotype.Service;

import com.bruno.desafio_itau.domain.entity.Transacao;
import com.bruno.desafio_itau.dtos.TransacaoRequestDto;

@Service
public class TransacaoService {

    private final NavigableMap<LocalDateTime, List<Transacao>> historico;
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

    public TransacaoService(TreeMap<LocalDateTime, List<Transacao>> historico) {
        this.historico = historico;
    }

    public Transacao criarTransacao(TransacaoRequestDto request) {
        Transacao transacao = Transacao.builder().valor(request.valor()).dataHora(LocalDateTime.parse(request.dataHora(), formatter)).build();

        salvarTransacao(transacao);

        return transacao;
    }

    public void salvarTransacao(Transacao transacao) {
        historico.computeIfAbsent(transacao.getDataHora().truncatedTo(ChronoUnit.MINUTES), k -> new ArrayList<Transacao>()).add(transacao);
    }

    public void apagarTransacoes() {
        historico.clear();
        System.out.println(historico.toString());
    }

}
