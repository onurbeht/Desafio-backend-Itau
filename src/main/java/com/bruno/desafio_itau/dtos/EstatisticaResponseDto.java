package com.bruno.desafio_itau.dtos;

public record EstatisticaResponseDto(
    Long count,
    Double sum,
    Double avg,
    Double max,
    Double min
) {

}
