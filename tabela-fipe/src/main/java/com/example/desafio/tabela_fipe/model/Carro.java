package com.example.desafio.tabela_fipe.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Carro(
    @JsonAlias("Valor") String valor,
    @JsonAlias("Marca")  String marca,
    @JsonAlias("Modelo")  String modelo,
    @JsonAlias("AnoModelo") Integer anoModelo,
    @JsonAlias("Combustivel") String combustivel,
    @JsonAlias("CodigoFipe") String codigoFipe
) {

}
