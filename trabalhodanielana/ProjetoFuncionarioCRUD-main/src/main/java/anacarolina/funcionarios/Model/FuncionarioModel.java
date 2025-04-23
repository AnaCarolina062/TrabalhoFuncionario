package anacarolina.funcionarios.Model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FuncionarioModel {
    private String matricula, nome, cpf, cargo;
    private LocalDate dataNascimento, dataContratacao;
    private BigDecimal salario;
    private EnderecoModel endereco;

    public FuncionarioModel(String matricula, String nome, String cpf, LocalDate dataNascimento,
                            String cargo, BigDecimal salario, LocalDate dataContratacao, EnderecoModel endereco) {
        this.matricula = matricula;
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.cargo = cargo;
        this.salario = salario;
        this.dataContratacao = dataContratacao;
        this.endereco = endereco;
    }

    public String getMatricula() { return matricula; }
    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public String getCargo() { return cargo; }
    public BigDecimal getSalario() { return salario; }
    public LocalDate getDataContratacao() { return dataContratacao; }
    public EnderecoModel getEndereco() { return endereco; }

    public String toCSV() {
        return String.join(";", matricula, nome, cpf, dataNascimento.toString(), cargo,
                salario.toString(), dataContratacao.toString(),
                endereco.getLogradouro(), endereco.getNumero(), endereco.getComplemento(),
                endereco.getBairro(), endereco.getCidade(), endereco.getEstado(), endereco.getCep());
    }

    @Override
    public String toString() {
        return nome + " (" + cargo + ") - " + endereco.getCidade();
    }
}
