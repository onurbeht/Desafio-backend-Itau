package com.bruno.desafio_itau.domain.entity;

import java.time.LocalDateTime;

public class Transacao {

    private Double valor;
    private LocalDateTime dataHora;

    public Transacao() {
    }

    private Transacao(Double valor, LocalDateTime dataHora) {
        this.valor = valor;
        this.dataHora = dataHora;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Double valor;
        private LocalDateTime dataHora;

        public Builder valor(Double valor) {
            this.valor = valor;
            return this;
        }
        
        public Builder dataHora(LocalDateTime dataHora) {
            this.dataHora = dataHora;
            return this;
        }

        public Transacao build() {
            return new Transacao(valor, dataHora);
        }

    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    @Override
    public String toString() {
        return "Transacao [Valor()=" + getValor() + ", DataHora()=" + getDataHora() + "]";
    }

    

}
