package com.bruno.desafio_itau.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bruno.desafio_itau.dtos.TransacaoRequestDto;
import com.bruno.desafio_itau.service.TransacaoService;

import org.springframework.web.bind.annotation.GetMapping;

import com.bruno.desafio_itau.dtos.EstatisticaResponseDto;

@RestController
@RequestMapping("/transacao")
public class TransacaoController {

    private final TransacaoService transacaoService;

    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    @PostMapping()
    public ResponseEntity<Void> receberTransacao(@RequestBody TransacaoRequestDto request) {
        
        transacaoService.criarTransacao(request);
        
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> apagarTransacoes() {
        
        transacaoService.apagarTransacoes();

        return ResponseEntity.ok().build();
    }

    @GetMapping("/estatistica")
    public ResponseEntity<EstatisticaResponseDto> estatisticas() {
        return ResponseEntity.ok(transacaoService.estatisticas());
    }
    

}
