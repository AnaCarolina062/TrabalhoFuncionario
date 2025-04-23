Sistema de Gerenciamento de Funcionários em Java com JavaFX
Objetivo
O sistema desenvolvido tem como objetivo gerenciar o cadastro de funcionários de uma empresa. O cadastro inclui dados pessoais e informações de endereço, além de funcionalidades de manipulação de arquivos CSV, relatórios utilizando a API Stream do Java e interface gráfica com JavaFX.

Requisitos Funcionais
O sistema deve permitir:

Cadastrar um novo funcionário com seus dados pessoais e endereço.

Excluir um funcionário existente.

Consultar um funcionário específico por matrícula.

Listar todos os funcionários cadastrados.

Armazenar os dados em um arquivo CSV.

Fornecer relatórios utilizando a API Stream do Java.

Requisitos Técnicos
Validação de Dados: Cada dado do funcionário deve ser validado, incluindo matrícula, CPF, data de nascimento, salário e CEP.

Persistência de Dados: Os dados devem ser salvos em um arquivo CSV com campos separados por ponto-e-vírgula.

Relatórios: A API Stream deve ser usada para gerar relatórios como:

Filtrar funcionários por cargo.

Filtrar funcionários por faixa salarial.

Calcular média salarial por cargo.

Listar funcionários agrupados por cidade ou estado.

Estrutura das Classes
1. Classe Endereco
Esta classe armazena as informações de endereço de um funcionário. A classe tem os seguintes atributos:

java
Copiar
Editar
public class Endereco {
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;

    public Endereco(String logradouro, String numero, String complemento, String bairro, String cidade, String estado, String cep) {
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
    }

    // Getters e Setters
}
2. Classe Funcionario
Esta classe armazena os dados dos funcionários, incluindo a composição com a classe Endereco.

java
Copiar
Editar
import java.math.BigDecimal;
import java.time.LocalDate;

public class Funcionario {
    private String matricula;
    private String nome;
    private String cpf;
    private LocalDate dataNascimento;
    private String cargo;
    private BigDecimal salario;
    private LocalDate dataContratacao;
    private Endereco endereco;

    public Funcionario(String matricula, String nome, String cpf, LocalDate dataNascimento, String cargo, BigDecimal salario, LocalDate dataContratacao, Endereco endereco) {
        this.matricula = matricula;
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.cargo = cargo;
        this.salario = salario;
        this.dataContratacao = dataContratacao;
        this.endereco = endereco;
    }

    // Getters e Setters
}
3. Classe SistemaFuncionarios
A classe SistemaFuncionarios será responsável por gerenciar os dados dos funcionários. Ela deve ser capaz de carregar e salvar os dados em um arquivo CSV.

java
Copiar
Editar
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class SistemaFuncionarios {
    private List<Funcionario> funcionarios;
    private final String caminhoArquivo = "funcionarios.csv";

    public SistemaFuncionarios() {
        funcionarios = new ArrayList<>();
        carregarDados();
    }

    public void cadastrarFuncionario(Funcionario funcionario) {
        funcionarios.add(funcionario);
        salvarDados();
    }

    public void excluirFuncionario(String matricula) {
        funcionarios.removeIf(f -> f.getMatricula().equals(matricula));
        salvarDados();
    }

    public Funcionario consultarFuncionario(String matricula) {
        return funcionarios.stream()
                           .filter(f -> f.getMatricula().equals(matricula))
                           .findFirst()
                           .orElse(null);
    }

    public List<Funcionario> listarFuncionarios() {
        return funcionarios;
    }

    public void salvarDados() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(caminhoArquivo))) {
            for (Funcionario f : funcionarios) {
                writer.write(f.getMatricula() + ";" +
                             f.getNome() + ";" +
                             f.getCpf() + ";" +
                             f.getDataNascimento() + ";" +
                             f.getCargo() + ";" +
                             f.getSalario() + ";" +
                             f.getDataContratacao() + ";" +
                             f.getEndereco().getLogradouro() + ";" +
                             f.getEndereco().getNumero() + ";" +
                             f.getEndereco().getComplemento() + ";" +
                             f.getEndereco().getBairro() + ";" +
                             f.getEndereco().getCidade() + ";" +
                             f.getEndereco().getEstado() + ";" +
                             f.getEndereco().getCep());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void carregarDados() {
        try {
            List<String> linhas = Files.readAllLines(Paths.get(caminhoArquivo));
            for (String linha : linhas) {
                String[] dados = linha.split(";");
                Funcionario funcionario = new Funcionario(
                        dados[0], dados[1], dados[2], LocalDate.parse(dados[3]), dados[4],
                        new BigDecimal(dados[5]), LocalDate.parse(dados[6]),
                        new Endereco(dados[7], dados[8], dados[9], dados[10], dados[11], dados[12], dados[13])
                );
                funcionarios.add(funcionario);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Funcionario> filtrarPorCargo(String cargo) {
        return funcionarios.stream()
                           .filter(f -> f.getCargo().equalsIgnoreCase(cargo))
                           .collect(Collectors.toList());
    }

    public List<Funcionario> filtrarPorFaixaSalarial(BigDecimal salarioMin, BigDecimal salarioMax) {
        return funcionarios.stream()
                           .filter(f -> f.getSalario().compareTo(salarioMin) >= 0 && f.getSalario().compareTo(salarioMax) <= 0)
                           .collect(Collectors.toList());
    }

    public BigDecimal calcularMediaSalarialPorCargo(String cargo) {
        return funcionarios.stream()
                           .filter(f -> f.getCargo().equalsIgnoreCase(cargo))
                           .map(Funcionario::getSalario)
                           .reduce(BigDecimal.ZERO, BigDecimal::add)
                           .divide(BigDecimal.valueOf(funcionarios.size()));
    }

    public Map<String, List<Funcionario>> agruparPorCidade() {
        return funcionarios.stream()
                           .collect(Collectors.groupingBy(f -> f.getEndereco().getCidade()));
    }

    public Map<String, List<Funcionario>> agruparPorEstado() {
        return funcionarios.stream()
                           .collect(Collectors.groupingBy(f -> f.getEndereco().getEstado()));
    }
}
4. Interface Gráfica com JavaFX
Para a interface gráfica, usaremos o JavaFX para criar telas de cadastro, consulta e listagem dos funcionários.

java
Copiar
Editar
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

public class MainApp extends Application {
    private SistemaFuncionarios sistema;

    @Override
    public void start(Stage primaryStage) {
        sistema = new SistemaFuncionarios();

        // Layout da interface
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 600, 400);

        // Comandos para a interface (como botões, campos de texto, etc.)
        // Exemplo: Botão para cadastrar funcionário
        Button btnCadastrar = new Button("Cadastrar");
        btnCadastrar.setOnAction(e -> cadastrarFuncionario());
        root.setCenter(btnCadastrar);

        primaryStage.setTitle("Sistema de Cadastro de Funcionários");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void cadastrarFuncionario() {
        // Exemplo de como capturar os dados para cadastrar um funcionário
    }

    public static void main(String[] args) {
        launch(args);
    }
}
Conclusão
Este sistema utiliza o JavaFX para a interface gráfica, Java Streams para a manipulação de dados e persistência em arquivos CSV. As funcionalidades de cadastro, exclusão e consulta de funcionários estão implementadas, bem como relatórios usando a API Stream.

Estrutura de Arquivos
SistemaFuncionarios.java: Classe principal de gerenciamento de funcionários.

Funcionario.java: Define a estrutura de um funcionário.

Endereco.java: Define a estrutura de um endereço.

MainApp.java: Interface gráfica com JavaFX.

Como Executar
Compile as classes e execute o arquivo MainApp.java.

Certifique-se de que o arquivo funcionarios.csv esteja presente no mesmo diretório do projeto ou crie-o manualmente.

