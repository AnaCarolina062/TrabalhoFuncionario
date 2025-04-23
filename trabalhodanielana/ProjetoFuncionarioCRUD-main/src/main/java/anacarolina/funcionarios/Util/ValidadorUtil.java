package anacarolina.funcionarios.Util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

public class ValidadorUtil {

    public static boolean validarMatricula(String matricula) {
        return matricula.matches("\\d{6}");
    }

    public static boolean validarNome(String nome) {
        return nome != null && nome.trim().length() >= 3;
    }

    public static boolean validarCPF(String cpf) {
        return cpf.matches("\\d{11}"); // Simples, sem verificação de dígitos válidos
    }

    public static boolean validarDataNascimento(LocalDate data) {
        return Period.between(data, LocalDate.now()).getYears() >= 18;
    }

    public static boolean validarSalario(BigDecimal salario) {
        return salario != null && salario.compareTo(BigDecimal.ZERO) > 0;
    }

    public static boolean validarCEP(String cep) {
        return cep.matches("\\d{5}-?\\d{3}");
    }
}