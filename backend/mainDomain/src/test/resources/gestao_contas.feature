Feature: Cadastro e gestão de contas
  Como usuário do sistema
  Quero cadastrar e gerenciar minhas contas
  Para acompanhar saldos, depósitos e retiradas

  # Cadastro obrigatório
  Scenario: Cadastro de conta com todos os campos obrigatórios preenchidos
    Given que o usuário informa nome, tipo e saldo inicial
    When salvar a conta
    Then a conta deve ser cadastrada com sucesso

  Scenario: Cadastro de conta sem informar todos os campos obrigatórios
    Given que o usuário não informa todos os campos obrigatórios
    When tentar salvar a conta
    Then o sistema deve recusar o cadastro e exibir a mensagem de erro

  # Unicidade de nome/tipo
  Scenario: Cadastro de conta com nome único no mesmo banco
    Given que já existe uma conta chamada "Corrente Banco X"
    When o usuário cadastra uma conta chamada "Poupança Banco X"
    Then a nova conta deve ser cadastrada com sucesso

  Scenario: Cadastro de conta duplicada com mesmo nome e banco
    Given que já existe uma conta chamada "Corrente Banco X"
    When o usuário tenta cadastrar outra conta chamada "Corrente Banco X"
    Then o sistema deve recusar o cadastro e exibir a mensagem de erro

  # Saldo inicial válido
  Scenario: Cadastro de conta com saldo inicial maior ou igual a zero
    Given que o usuário informa saldo inicial de R$ 100,00
    When salvar a conta
    Then a conta deve ser cadastrada com sucesso

  Scenario: Cadastro de conta com saldo inicial negativo
    Given que o usuário informa saldo inicial de -R$ 50,00
    When tentar salvar a conta
    Then o sistema deve recusar o cadastro e exibir a mensagem de erro

  # Depósito válido
  Scenario: Realizar depósito com valor positivo
    Given que o usuário possui uma conta com saldo de R$ 500,00
    When realiza um depósito de R$ 200,00
    Then o saldo da conta deve ser atualizado para R$ 700,00

  Scenario: Realizar depósito com valor menor ou igual a zero
    Given que o usuário possui uma conta com saldo de R$ 500,00
    When tenta realizar um depósito de R$ 0,00
    Then o sistema deve recusar a alteração e exibir a mensagem de erro

  # Retirada válida
  Scenario: Realizar retirada dentro do saldo disponível
    Given que o usuário possui uma conta com saldo de R$ 300,00
    When realiza uma retirada de R$ 100,00
    Then o saldo da conta deve ser atualizado para R$ 200,00

  Scenario: Realizar retirada acima do saldo disponível
    Given que o usuário possui uma conta com saldo de R$ 300,00
    When tenta realizar uma retirada de R$ 500,00
    Then o sistema deve recusar a alteração e exibir a mensagem de erro

