package com.bruno.desafio_itau.dtos;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;

public record TransacaoRequestDto(
        @NotNull @PositiveOrZero Double valor,
        @NotNull @PastOrPresent @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC") LocalDateTime dataHora) {

}
