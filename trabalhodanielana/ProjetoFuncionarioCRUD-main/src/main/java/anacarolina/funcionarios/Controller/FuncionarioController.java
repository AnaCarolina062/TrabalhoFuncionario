package anacarolina.funcionarios.Controller;

import anacarolina.funcionarios.Model.FuncionarioModel;
import anacarolina.funcionarios.Model.EnderecoModel;
import anacarolina.funcionarios.Repository.FuncionarioRepository;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.util.List;

public class FuncionarioController {

    @FXML private TextField txtMatricula, txtNome, txtCpf, txtCargo, txtSalario;
    @FXML private DatePicker dpNascimento, dpContratacao;
    @FXML private TextField txtLogradouro, txtNumero, txtComplemento, txtBairro, txtCidade, txtEstado, txtCep;
    @FXML private ListView<FuncionarioModel> listFuncionarios;
    @FXML private TableView<FuncionarioModel> tabelaFiltrados;
    @FXML private ComboBox<String> cbFiltroCargo;
    @FXML private TextField txtFiltroCidadeEstado, txtMinSalario, txtMaxSalario;

    @FXML private TableColumn<FuncionarioModel, String> colNome;
    @FXML private TableColumn<FuncionarioModel, String> colCargo;
    @FXML private TableColumn<FuncionarioModel, BigDecimal> colSalario;
    @FXML private TableColumn<FuncionarioModel, String> colCidade;
    @FXML private TableColumn<FuncionarioModel, String> colEstado;

    private FuncionarioRepository repo = new FuncionarioRepository();

    @FXML
    private void initialize() {
        atualizarLista();
        inicializarFiltros();
        configurarTabela();
    }

    private void inicializarFiltros() {
        List<String> cargos = repo.listarTodos().stream()
                .map(FuncionarioModel::getCargo)
                .distinct()
                .sorted()
                .toList();
        cbFiltroCargo.setItems(FXCollections.observableArrayList(cargos));
    }

    private void configurarTabela() {
        colNome.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNome()));
        colCargo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCargo()));
        colSalario.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getSalario()));
        colCidade.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEndereco().getCidade()));
        colEstado.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEndereco().getEstado()));
    }

    @FXML
    private void salvarFuncionario() {
        try {
            if (txtMatricula.getText().isBlank() || txtNome.getText().isBlank() ||
                    txtCpf.getText().isBlank() || dpNascimento.getValue() == null ||
                    txtCargo.getText().isBlank() || txtSalario.getText().isBlank() ||
                    dpContratacao.getValue() == null) {
                mostrarAlerta("Erro", "Preencha todos os campos do funcionário.");
                return;
            }

            BigDecimal salario;
            try {
                salario = new BigDecimal(txtSalario.getText().replace(",", "."));
            } catch (NumberFormatException e) {
                mostrarAlerta("Erro", "Salário inválido. Use ponto como separador decimal.");
                return;
            }

            String cpf = txtCpf.getText().replaceAll("[^\\d]", "");
            if (cpf.length() != 11) {
                mostrarAlerta("Erro", "CPF inválido. Digite 11 números.");
                return;
            }

            if (txtLogradouro.getText().isBlank() || txtNumero.getText().isBlank() ||
                    txtBairro.getText().isBlank() || txtCidade.getText().isBlank() ||
                    txtEstado.getText().isBlank() || txtCep.getText().isBlank()) {
                mostrarAlerta("Erro", "Preencha todos os campos do endereço.");
                return;
            }

            EnderecoModel endereco = new EnderecoModel(
                    txtLogradouro.getText(), txtNumero.getText(), txtComplemento.getText(),
                    txtBairro.getText(), txtCidade.getText(), txtEstado.getText(), txtCep.getText());

            FuncionarioModel funcionario = new FuncionarioModel(
                    txtMatricula.getText(), txtNome.getText(), cpf, dpNascimento.getValue(),
                    txtCargo.getText(), salario, dpContratacao.getValue(), endereco);

            repo.adicionar(funcionario);
            atualizarLista();
            limparCampos();
            inicializarFiltros();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro inesperado", "Ocorreu um erro ao salvar o funcionário.");
        }
    }

    @FXML
    private void removerFuncionario() {
        FuncionarioModel selecionado = listFuncionarios.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            repo.remover(selecionado.getMatricula());
            atualizarLista();
            inicializarFiltros();
        }
    }

    private void atualizarLista() {
        ObservableList<FuncionarioModel> dados = FXCollections.observableArrayList(repo.listarTodos());
        listFuncionarios.setItems(dados);
    }

    private void limparCampos() {
        txtMatricula.clear(); txtNome.clear(); txtCpf.clear(); txtCargo.clear(); txtSalario.clear();
        dpNascimento.setValue(null); dpContratacao.setValue(null);

        txtLogradouro.clear(); txtNumero.clear(); txtComplemento.clear(); txtBairro.clear();
        txtCidade.clear(); txtEstado.clear(); txtCep.clear();
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }


    @FXML
    private void aplicarFiltroCargo() {
        String cargo = cbFiltroCargo.getValue();
        if (cargo != null && !cargo.isEmpty()) {
            List<FuncionarioModel> filtrados = repo.filtrarPorCargo(cargo);
            tabelaFiltrados.setItems(FXCollections.observableArrayList(filtrados));
        } else {
            atualizarListaFiltrada();
        }
    }

    @FXML
    private void aplicarFiltroLocal() {
        String local = txtFiltroCidadeEstado.getText().trim();
        if (!local.isEmpty()) {
            List<FuncionarioModel> filtrados = repo.filtrarPorLocal(local);
            tabelaFiltrados.setItems(FXCollections.observableArrayList(filtrados));
        } else {
            atualizarListaFiltrada();
        }
    }

    @FXML
    private void aplicarFiltroSalario() {
        try {
            BigDecimal min = txtMinSalario.getText().isEmpty() ? BigDecimal.ZERO : new BigDecimal(txtMinSalario.getText().replace(",", "."));
            BigDecimal max = txtMaxSalario.getText().isEmpty() ? BigDecimal.valueOf(Double.MAX_VALUE) : new BigDecimal(txtMaxSalario.getText().replace(",", "."));
            if (min.compareTo(max) > 0) {
                mostrarAlerta("Erro", "O salário mínimo não pode ser maior que o salário máximo.");
                return;
            }
            List<FuncionarioModel> filtrados = repo.filtrarPorFaixaSalarial(min, max);
            tabelaFiltrados.setItems(FXCollections.observableArrayList(filtrados));
        } catch (NumberFormatException e) {
            mostrarAlerta("Erro", "Informe valores numéricos válidos para o salário.");
        }
    }

    private void atualizarListaFiltrada() {
        List<FuncionarioModel> todosFuncionarios = repo.listarTodos();
        tabelaFiltrados.setItems(FXCollections.observableArrayList(todosFuncionarios));
    }
}
