package com.bruno.desafio_itau.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bruno.desafio_itau.dtos.TransacaoRequestDto;
import com.bruno.desafio_itau.service.TransacaoService;


@RestController
@RequestMapping("/transacao")
public class TransacaoController {

    private final TransacaoService transacaoService;

    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    @PostMapping()
    public ResponseEntity<?> receberTransacao(@RequestBody TransacaoRequestDto request) {
        
        transacaoService.criarTransacao(request);
        
        return ResponseEntity.status(201).build();
    }

    

}
