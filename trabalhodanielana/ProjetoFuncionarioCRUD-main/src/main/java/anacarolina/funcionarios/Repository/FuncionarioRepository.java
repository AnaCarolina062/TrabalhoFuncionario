package anacarolina.funcionarios.Repository;

import anacarolina.funcionarios.Model.EnderecoModel;
import anacarolina.funcionarios.Model.FuncionarioModel;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class FuncionarioRepository {

    private final String caminhoCSV = "funcionarios.csv";
    private List<FuncionarioModel> funcionarios = new ArrayList<>(); // A lista agora será recarregada a cada operação

    public FuncionarioRepository() {
        carregarCSV();
    }

    public void adicionar(FuncionarioModel f) {
        funcionarios.add(f);
        salvarCSV();
        carregarCSV();
    }

    public void remover(String matricula) {
        funcionarios.removeIf(f -> f.getMatricula().equals(matricula));
        salvarCSV();
        carregarCSV();
    }

    public FuncionarioModel buscarPorMatricula(String matricula) {
        return funcionarios.stream()
                .filter(f -> f.getMatricula().equals(matricula))
                .findFirst()
                .orElse(null);
    }

    public List<FuncionarioModel> listarTodos() {
        carregarCSV();
        return Collections.unmodifiableList(funcionarios);
    }

    public List<FuncionarioModel> filtrarPorCargo(String cargo) {
        carregarCSV();
        return funcionarios.stream()
                .filter(f -> f.getCargo().equalsIgnoreCase(cargo))
                .collect(Collectors.toList());
    }

    public List<FuncionarioModel> filtrarPorFaixaSalarial(BigDecimal min, BigDecimal max) {
        carregarCSV();
        return funcionarios.stream()
                .filter(f -> f.getSalario().compareTo(min) >= 0 && f.getSalario().compareTo(max) <= 0)
                .collect(Collectors.toList());
    }

    public Map<String, Double> calcularMediaSalarialPorCargo() {
        carregarCSV();
        return funcionarios.stream()
                .collect(Collectors.groupingBy(FuncionarioModel::getCargo,
                        Collectors.averagingDouble(f -> f.getSalario().doubleValue())));
    }

    public Map<String, List<FuncionarioModel>> agruparPorCidade() {
        carregarCSV();
        return funcionarios.stream()
                .collect(Collectors.groupingBy(f -> f.getEndereco().getCidade()));
    }

    public Map<String, List<FuncionarioModel>> agruparPorEstado() {
        carregarCSV();
        return funcionarios.stream()
                .collect(Collectors.groupingBy(f -> f.getEndereco().getEstado()));
    }

    private void carregarCSV() {
        if (!Files.exists(Paths.get(caminhoCSV))) return;

        funcionarios.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(caminhoCSV))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] campos = linha.split(";");

                EnderecoModel endereco = new EnderecoModel(
                        campos[7], campos[8], campos[9], campos[10],
                        campos[11], campos[12], campos[13]
                );

                FuncionarioModel funcionario = new FuncionarioModel(
                        campos[0],
                        campos[1],
                        campos[2],
                        LocalDate.parse(campos[3]),
                        campos[4],
                        new BigDecimal(campos[5]),
                        LocalDate.parse(campos[6]),
                        endereco
                );
                funcionarios.add(funcionario);
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar CSV: " + e.getMessage());
        }
    }

    private void salvarCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoCSV))) {
            for (FuncionarioModel f : funcionarios) {
                writer.write(f.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar CSV: " + e.getMessage());
        }
    }

    public List<FuncionarioModel> filtrarPorLocal(String local) {
        carregarCSV(); // Recarrega os dados antes de aplicar o filtro
        return funcionarios.stream()
                .filter(f -> f.getEndereco().getCidade().contains(local) ||
                        f.getEndereco().getEstado().contains(local))
                .collect(Collectors.toList());
    }
}
