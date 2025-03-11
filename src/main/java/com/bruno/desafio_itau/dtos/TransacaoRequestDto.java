package com.bruno.desafio_itau.dtos;

public record TransacaoRequestDto(
    Double valor,
    String dataHora
) {

}
