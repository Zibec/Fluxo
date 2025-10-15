Feature: Gestão de Metas de Redução de Dívidas
  Como um usuário focado em quitar uma dívida
  Eu quero criar e acompanhar metas inversas
  Para que eu visualize meu progresso na redução do saldo devedor

  Scenario: Criação de meta de quitação válida
    When o usuário cria uma meta de quitação para reduzir a dívida em "2000" até "2025-12-31"
    Then a meta deve ser registrada com status "Ativa"
    And o saldo inicial da dívida deve ser armazenado como "5000"

  Scenario: Criação de meta de quitação inválida (valor negativo)
    When o usuário tenta criar uma meta de quitação com valor de redução "-500"
    Then o sistema deve rejeitar a criação da meta

  Scenario: Atualização de progresso da meta
    Given existe uma meta de quitação ativa com objetivo de "2000" e saldo inicial de "5000"
    When o saldo atual da dívida é reduzido para "3500"
    Then o progresso deve ser calculado como "1500"
    And a meta deve continuar com status "Ativa"

  Scenario: Conclusão automática da meta
    Given existe uma meta de quitação ativa com objetivo de "2000" e saldo inicial de "5000"
    When o saldo atual da dívida é reduzido para "2800"
    Then o progresso deve ser "2200"
    And o status da meta deve ser alterado para "Concluída"

  Scenario: Vinculação de pagamento extra à meta
    Given existe uma meta de quitação ativa para uma conta de cartão de crédito
    When o usuário registra uma transação marcada como "pagamento extra"
    Then o valor da transação deve ser somado ao progresso da meta
    And o sistema deve atualizar o percentual de conclusão

  Scenario: Exclusão de meta existente
    Given existe uma meta de quitação ativa
    When o usuário exclui a meta
    Then a meta não deve mais existir no sistema