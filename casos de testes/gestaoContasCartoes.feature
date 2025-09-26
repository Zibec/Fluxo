#Lucas

Feature: Gestão de Contas e Cartões
  Esta funcionalidade gerencia as diferentes fontes de dinheiro e crédito do usuário.

  # História 5.1: Cadastro e Controle de Cartões de Crédito
  Scenario: Cadastrar cartão de crédito com sucesso
    Given que estou logado no sistema
    And acesso a página de gestão de contas e cartões
    When eu cadastro um cartão de crédito com limite de "5000"
    Then o sistema deve salvar os dados do cartão
    And exibir a mensagem "Cartão cadastrado com sucesso."

  Scenario: Calcular limite disponível após nova despesa
    Given que tenho um cartão de crédito com limite total de "5000"
    And despesas na fatura aberta somando "1200"
    When eu realizo uma nova compra de "300"
    Then o sistema deve recalcular o limite disponível como "3500"

  Scenario: Fechamento automático da fatura
    Given que tenho uma fatura em aberto com despesas
    And a data de fechamento é hoje
    When o processo de fechamento de fatura é executado
    Then o sistema deve consolidar o valor a pagar
    And criar uma nova fatura para o próximo período

  # História 5.2: Cadastro de Contas Bancárias Correntes
  Scenario: Cadastrar conta corrente com sucesso
    Given que estou logado no sistema
    And acesso a página de gestão de contas
    When eu cadastro uma conta corrente no banco "001" agência "1234" número "56789" com saldo inicial "2000"
    Then o sistema deve salvar os dados da conta
    And exibir a mensagem "Conta corrente cadastrada com sucesso."

  Scenario: Atualizar saldo da conta corrente após depósito
    Given que tenho uma conta corrente com saldo "2000"
    When eu registro um depósito de "500"
    Then o saldo da conta deve ser atualizado para "2500"

  Scenario: Atualizar saldo da conta corrente após retirada
    Given que tenho uma conta corrente com saldo "2000"
    When eu registro uma retirada de "800"
    Then o saldo da conta deve ser atualizado para "1200"

  # História 5.3: Cadastro de Contas Poupança
  Scenario: Cadastrar conta poupança com sucesso
    Given que estou logado no sistema
    And acesso a página de gestão de contas
    When eu cadastro uma conta poupança no banco "104" agência "9999" número "12345" com saldo inicial "3000"
    Then o sistema deve salvar os dados da conta
    And exibir a mensagem "Conta poupança cadastrada com sucesso."

  Scenario: Registrar depósito em conta poupança
    Given que tenho uma conta poupança com saldo "3000"
    When eu registro um depósito de "1000"
    Then o saldo da conta deve ser atualizado para "4000"

  Scenario: Registrar retirada em conta poupança
    Given que tenho uma conta poupança com saldo "3000"
    When eu registro uma retirada de "500"
    Then o saldo da conta deve ser atualizado para "2500"

  # História 5.4: Cadastro de Contas Digitais/Carteiras
  Scenario: Cadastrar carteira digital com sucesso
    Given que estou logado no sistema
    And acesso a página de gestão de contas
    When eu cadastro uma carteira digital "PayPal" com saldo inicial "800"
    Then o sistema deve salvar os dados da carteira
    And exibir a mensagem "Carteira digital cadastrada com sucesso."

  Scenario: Atualizar saldo da carteira digital após entrada
    Given que tenho uma carteira digital "PayPal" com saldo "800"
    When eu registro uma entrada de "200"
    Then o saldo da carteira deve ser atualizado para "1000"

  Scenario: Atualizar saldo da carteira digital após saída
    Given que tenho uma carteira digital "PayPal" com saldo "800"
    When eu registro uma saída de "300"
    Then o saldo da carteira deve ser atualizado para "500"
