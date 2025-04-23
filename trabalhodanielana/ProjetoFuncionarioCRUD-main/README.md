# 📋 Sistema de Cadastro de Funcionários

Aplicação desktop feita com **JavaFX**, usando o padrão **MVC**, com persistência de dados em **CSV** e geração de relatórios com **Stream API**.

---

## 📁 Estrutura do Projeto

```
src/
├── main/
│   ├── java/
│   │   └── elieldm.funcionarios/
│   │       ├── Controller/
│   │       │   └── FuncionarioController.java
│   │       ├── Model/
│   │       │   ├── EnderecoModel.java
│   │       │   └── FuncionarioModel.java
│   │       ├── Repository/
│   │       │   └── FuncionarioRepository.java
│   │       ├── Util/
│   │       │   └── ValidadorUtil.java
│   │       └── Main.java
│   └── resources/
│       └── elieldm.funcionarios.View/
│           └── FuncionarioView.fxml
├── funcionarios.csv
```

---

## 🔧 Funcionalidades

- Cadastro de funcionário com endereço completo
- Interface feita em JavaFX (FXML)
- Dados persistidos em arquivo `.csv`
- Filtros na tabela:
  - Por **cargo**
  - Por **cidade** ou **estado**
  - Por **faixa salarial**
- Relatórios gerados com **Stream API**
- Validações de CPF, matrícula, salário, CEP, idade mínima, etc.

---

## ▶️ Como Executar

1. Clone este repositório:

```bash
git clone https://github.com/seu-usuario/sistema-funcionarios-javafx.git
cd sistema-funcionarios-javafx
```

2. Abra com sua IDE (IntelliJ ou Eclipse com suporte a JavaFX)
3. Execute a classe `Main.java`

> ⚠️ Certifique-se de ter o Java 17+ instalado e JavaFX configurado na IDE.

---

## 🧪 Tecnologias Usadas

- Java 17
- JavaFX
- FXML
- Stream API
- Padrão MVC
- Armazenamento em CSV

---

## 🧠 Validações

- CPF: apenas números, deve conter 11 dígitos
- Matrícula: 6 dígitos
- Salário: `BigDecimal`, maior que zero
- Idade mínima: 18 anos
- CEP: formato aceito `00000-000` ou `00000000`

---

## 👨‍💻 Autor

Desenvolvido por **Eliel D. M.**  
📧 Contato: [linkedin.com/in/elieldm](https://linkedin.com/in/elieldm)

---

## 📄 Licença

Este projeto é livre para fins educacionais.  
Se usar ou modificar, mantenha os créditos. 👍
