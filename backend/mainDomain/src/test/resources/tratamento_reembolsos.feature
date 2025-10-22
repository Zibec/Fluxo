# Gustavo

Feature: Tratamento de Reembolsos
  Para garantir a precisão dos meus relatórios e orçamentos, eu como usuário,
  quero registrar reembolsos vinculados a despesas originais.

# -------------------------------------------------------------------------------------
# Regra 1: Vinculação de Transações (O Reembolso DEVE estar ligado a uma despesa)
# -------------------------------------------------------------------------------------

  Scenario: [SUCESSO] Registrar reembolso vinculando corretamente a uma despesa
    Given que registrei uma despesa original com ID "despesa-123" de R$ 80.00 na categoria "Alimentação"
    When eu registrar um reembolso de R$ 20.00 e o vincular à despesa com ID "despesa-123"
    Then o sistema deve criar uma nova transação do tipo "REEMBOLSO"
    And o reembolso criado deve estar vinculado à despesa com ID "despesa-123"

  Scenario: [FALHA] Tentar registrar reembolso sem vincular a uma despesa
    Given que estou na tela de registro de reembolso
    When eu tento registrar um reembolso de R$ 20.00 sem selecionar uma despesa original
    Then o sistema de reembolso deve exibir a mensagem de erro "O ID da despesa original não pode ser nulo"

# -------------------------------------------------------------------------------------
# Regra 2: Lógica Contábil (NÃO é Receita; Subtrai do Gasto da Categoria)
# -------------------------------------------------------------------------------------

  Scenario: [SUCESSO] Reembolso abate corretamente o gasto da categoria (e não conta como receita)
    Given que uma despesa original com ID "despesa-abc" tem o valor de R$ 80.00 na categoria "Alimentação" em "10/2025"
    And que outra despesa com ID "despesa-def" tem o valor de R$ 70.00 na categoria "Alimentação" em "10/2025"
    And o total de receitas do usuário é de R$ 1000.00
    When eu registrar um reembolso de R$ 20.00 vinculado à despesa com ID "despesa-abc"
    Then o total de gastos na categoria "Alimentação" em "10/2025" deve ser de R$ 130.00
    And o total de receitas do usuário deve permanecer R$ 1000.00

  Scenario: [FALHA] Lógica contábil não é aplicada se o reembolso for inválido (ex: valor alto)
    Given que uma despesa original com ID "despesa-ghi" tem o valor de R$ 50.00 na categoria "Lazer" em "10/2025"
    When eu tento registrar um reembolso de R$ 60.00 para a despesa com ID "despesa-ghi"
    Then o sistema de reembolso deve exibir a mensagem de erro "O valor do reembolso não pode ser maior que o da despesa original"
    And o total de gastos na categoria "Lazer" em "10/2025" deve ser de R$ 50.00

# -------------------------------------------------------------------------------------
# Regra 3: Impacto no Orçamento (Libera valor no orçamento da categoria)
# -------------------------------------------------------------------------------------

  Scenario: [SUCESSO] Reembolso restaura corretamente o valor no orçamento
    Given que tenho um orçamento de R$ 500.00 para "Alimentação" em "10/2025"
    And que uma despesa original com ID "despesa-xyz" tem o valor de R$ 400.00 na categoria "Alimentação"
    When eu receber um reembolso de R$ 30.00 vinculado à despesa com ID "despesa-xyz"
    Then o valor disponível no meu orçamento de "Alimentação" em "10/2025" deve ser de R$ 130.00

  Scenario: [FALHA] Orçamento não é alterado se o reembolso for inválido (ex: sem vínculo)
    Given que tenho um orçamento de R$ 300.00 para "Transporte" em "11/2025"
    And que uma despesa original com ID "despesa-tst" tem o valor de R$ 50.00 na categoria "Transporte" em "11/2025"
    When eu tento registrar um reembolso de R$ 20.00 sem selecionar uma despesa original
    Then o sistema de reembolso deve exibir a mensagem de erro "O ID da despesa original não pode ser nulo"
    And o valor disponível no meu orçamento de "Transporte" em "11/2025" deve ser de R$ 250.00

# -------------------------------------------------------------------------------------
# Regra 4: Validação de Valor (Reembolso <= Despesa Original)
# -------------------------------------------------------------------------------------

  Scenario: [SUCESSO] Permitir registrar reembolso com valor igual à despesa
    Given que eu tenho uma despesa original com ID "despesa-789" de R$ 75.00
    When eu tento registrar um reembolso de R$ 75.00 para a despesa com ID "despesa-789"
    Then o sistema deve criar uma nova transação do tipo "REEMBOLSO"

  Scenario: [FALHA] Rejeitar reembolso com valor maior que a despesa
    Given que eu tenho uma despesa original com ID "despesa-456" de R$ 80.00
    When eu tento registrar um reembolso de R$ 90.00 para a despesa com ID "despesa-456"
    Then o sistema de reembolso deve exibir a mensagem de erro "O valor do reembolso não pode ser maior que o da despesa original"