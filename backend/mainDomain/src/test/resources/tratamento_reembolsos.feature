# Gustavo

Feature: Tratamento de Reembolsos
  Para garantir a precisão dos meus relatórios e orçamentos, eu como usuário,
  quero registrar reembolsos vinculados a despesas originais.

  Scenario: Registrar reembolso vinculando a uma despesa
    Given que registrei uma despesa original com ID "despesa-123" de R$ 80.00 na categoria "Alimentação"
    When eu registrar um reembolso de R$ 20.00 e o vincular à despesa com ID "despesa-123"
    Then o sistema deve criar uma nova transação do tipo "REEMBOLSO"
    And o reembolso criado deve estar vinculado à despesa com ID "despesa-123"

  Scenario: Tentar registrar reembolso sem vincular a uma despesa
    Given que estou na tela de registro de reembolso
    When eu tento registrar um reembolso de R$ 20.00 sem selecionar uma despesa original
    Then o sistema de reembolso deve exibir a mensagem de erro "O ID da despesa original não pode ser nulo"

  Scenario: Reembolso deve abater o gasto da categoria
    Given que uma despesa original com ID "despesa-abc" tem o valor de R$ 80.00 na categoria "Alimentação" em "10/2025"
    And que outra despesa com ID "despesa-def" tem o valor de R$ 70.00 na categoria "Alimentação" em "10/2025"
    When eu registrar um reembolso de R$ 20.00 vinculado à despesa com ID "despesa-abc"
    Then o total de gastos na categoria "Alimentação" em "10/2025" deve ser de R$ 130.00

  Scenario: Reembolso deve restaurar valor no orçamento
    Given que tenho um orçamento de R$ 500.00 para "Alimentação" em "10/2025"
    And que uma despesa original com ID "despesa-xyz" tem o valor de R$ 400.00 na categoria "Alimentação"
    When eu receber um reembolso de R$ 30.00 vinculado à despesa com ID "despesa-xyz"
    Then o valor disponível no meu orçamento de "Alimentação" em "10/2025" deve ser de R$ 130.00

  Scenario: Tentar registrar reembolso com valor maior que a despesa
    Given que eu tenho uma despesa original com ID "despesa-456" de R$ 80.00
    When eu tento registrar um reembolso de R$ 90.00 para a despesa com ID "despesa-456"
    Then o sistema de reembolso deve exibir a mensagem de erro "O valor do reembolso não pode ser maior que o da despesa original"

  Scenario: Reembolso não deve ser contabilizado como receita
    Given que uma despesa original com ID "despesa-l-contabil" tem o valor de R$ 50.00 na categoria "Lazer"
    And o total de receitas do usuário é de R$ 1000.00
    When eu registrar um reembolso de R$ 10.00 vinculado à despesa com ID "despesa-l-contabil"
    Then o total de receitas do usuário deve permanecer R$ 1000.00

  Scenario: Orçamento deve ser efetivamente atualizado após reembolso
    Given que tenho um orçamento de R$ 200.00 para "Transporte" em "11/2025"
    And que uma despesa original com ID "despesa-transp" tem o valor de R$ 50.00 na categoria "Transporte"
    When eu receber um reembolso de R$ 20.00 vinculado à despesa com ID "despesa-transp"
    Then o valor disponível no meu orçamento de "Transporte" em "11/2025" não deve ser R$ 150.00

  Scenario: Tentar registrar reembolso com valor igual à despesa
    Given que eu tenho uma despesa original com ID "despesa-789" de R$ 75.00
    When eu tento registrar um reembolso de R$ 75.00 para a despesa com ID "despesa-789"
    Then o sistema deve criar uma nova transação do tipo "REEMBOLSO"