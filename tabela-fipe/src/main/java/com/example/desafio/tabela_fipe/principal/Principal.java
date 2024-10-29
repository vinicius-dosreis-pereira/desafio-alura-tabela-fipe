package com.example.desafio.tabela_fipe.principal;

import com.example.desafio.tabela_fipe.model.Veiculo;
import com.example.desafio.tabela_fipe.model.Dados;
import com.example.desafio.tabela_fipe.model.Modelos;
import com.example.desafio.tabela_fipe.service.ConsumoApi;
import com.example.desafio.tabela_fipe.service.ConversorDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConversorDados conversorDados = new ConversorDados();

    private final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";

    public void exibeMenu() {
        var menu = """
          *** OPÇÕES ***
          Carro
          Moto    
          Caminhão 
          
          Digite uma das opções para consulta:  
          
          """;

        System.out.println(menu);
        var escolha = leitura.nextLine();
        String endereco;
        if (escolha.toLowerCase().contains("car")) {
            endereco = URL_BASE + "carros/marcas";
        } else if (escolha.toLowerCase().contains("mot")) {
            endereco = URL_BASE + "motos/marcas";
        } else {
            endereco = URL_BASE + "caminhoes/marcas";
        }

        var json = consumo.obterDados(endereco);
        var marcas = conversorDados.obterLista(json, Dados.class);
        marcas.stream()
                .sorted(Comparator.comparing(Dados::nome))
                .forEach(System.out::println);
//      listando as marcas por nome

//      marcas.stream().sorted(Comparator.comparing(Dados::codigo)).forEach(System.out::println);
//      listando as marcas por codigo

        System.out.println("Informe o código da marca para consulta");
        var codigoMarca = leitura.nextLine();

        endereco += "/" + codigoMarca + "/modelos";
        json = consumo.obterDados(endereco);
        var modeloLista = conversorDados.obterDados(json, Modelos.class);

        System.out.println("\nModelos dessa marca: ");
        modeloLista.modelos().stream()
                .sorted(Comparator.comparing(Dados::nome))
                .forEach(System.out::println);
//        listando os modelos por nome


//        modeloLista.modelos().stream().sorted(Comparator.comparing(Dados::codigo)).forEach(System.out::println);
//        listando os modelos por codigo

        System.out.println("\nInforme um trecho do nome do carro que deseja procurar: ");
        var nomeVeiculo = leitura.nextLine();

        List<Dados> modelosBuscados = modeloLista.modelos().stream()
                .filter(m -> m.nome().toLowerCase().contains(nomeVeiculo.toLowerCase()))
                .collect(Collectors.toList());

        modelosBuscados.forEach(System.out::println);
//        listando os modelos buscados por nome

//        modelosBuscados.stream().sorted(Comparator.comparing(Dados::codigo)).forEach(System.out::println);
//        listando os modelos por codigo

        System.out.println("Digite por favor o código do modelo para buscar os valores de avaliação");
        var codigoModelo = leitura.nextLine();

        endereco = endereco + "/" + codigoModelo + "/anos";
        json = consumo.obterDados(endereco);
        List<Dados> anos = conversorDados.obterLista(json, Dados.class);
        List<Veiculo> veiculos = new ArrayList<>();

        for (int i = 0; i < anos.size(); i++) {
            var enderecoAnos = endereco + "/" + anos.get(i).codigo();
            json = consumo.obterDados(enderecoAnos);
            Veiculo veiculo = conversorDados.obterDados (json, Veiculo.class);
            veiculos.add(veiculo);
        }

        System.out.println("\nTodos os veiculos filtrados com avaliações por ano: ");
        veiculos.forEach(System.out::println);

        System.out.println("\nMuito obrigado por utilizar o sistema!");

    }

}
