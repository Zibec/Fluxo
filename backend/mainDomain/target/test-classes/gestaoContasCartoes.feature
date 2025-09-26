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
  #  Scenario: Cadastrar conta com sucesso
  #    Given que estou logado no sistema
  #    And acesso a página de gestão de contas
  #    When eu cadastro uma conta no banco "001" agência "1234" número "56789" com saldo inicial "2000"
  #    Then o sistema deve salvar os dados da conta
  #   And exibir a mensagem "Conta cadastrada com sucesso."

  #  Scenario: Atualizar saldo da conta após depósito
  #    Given que tenho uma conta com saldo "2000"
   #   When eu registro um depósito de "500"
   #   Then o saldo da conta deve ser atualizado para "2500"

  #  Scenario: Atualizar saldo da conta após retirada
  #    Given que tenho uma conta com saldo "2000"
  #    When eu registro uma retirada de "800"
  #   Then o saldo da conta deve ser atualizado para "1200"

  #  Scenario: Impedir retirada maior que o saldo
  #    Given que tenho uma conta com saldo "500"
   #   When eu tento registrar uma retirada de "800"
   #   Then o sistema deve impedir a operação
   #   And exibir a mensagem "Saldo insuficiente.
