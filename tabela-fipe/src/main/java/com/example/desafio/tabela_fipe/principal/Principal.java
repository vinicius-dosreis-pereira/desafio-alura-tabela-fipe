package com.example.desafio.tabela_fipe.principal;

import com.example.desafio.tabela_fipe.model.Carro;
import com.example.desafio.tabela_fipe.model.Dados;
import com.example.desafio.tabela_fipe.model.Modelos;
import com.example.desafio.tabela_fipe.service.ConsumoApi;
import com.example.desafio.tabela_fipe.service.ConversorDados;

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

        modelosBuscados.stream().sorted(Comparator.comparing(Dados::nome)).forEach(System.out::println);
//        listando os modelos buscados por nome

//        modelosBuscados.stream().sorted(Comparator.comparing(Dados::codigo)).forEach(System.out::println);
//        listando os modelos por codigo

        System.out.println("\nDigite o código do modelo específico que deseja ver: ");
        var codigoVeiculo = leitura.nextLine();

        endereco += "/" + codigoVeiculo + "/anos";
        json = consumo.obterDados(endereco);
        var anosList = conversorDados.obterLista(json, Dados.class);

        System.out.println("\nModelo buscado: ");
        anosList.stream().sorted(Comparator.comparing(Dados::nome).reversed()).forEach(System.out::println);

        System.out.println("\nDigite agora o código específico referente ao ano específico que deseja buscar: ");
        var anoVeiculo = leitura.nextLine();

        endereco += "/" + anoVeiculo;
        json = consumo.obterDados(endereco);
        var anoBuscado = conversorDados.obterDados(json, Carro.class);

        System.out.println("\n" + anoBuscado);

        System.out.println("\nMuito obrigado por utilizar o sistema!");
    }


}
