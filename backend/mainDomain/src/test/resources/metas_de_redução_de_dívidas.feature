Feature: Gestão de Metas de Redução de Dívidas
  Como um usuário focado em quitar uma dívida
  Eu quero criar e acompanhar metas inversas
  Para que eu visualize meu progresso na redução do saldo devedor

  Scenario: Criar uma meta de redução de dívida válida
    Given que existe uma conta do tipo "Cartão de Crédito" com saldo devedor de 5000
    When eu crio uma meta de redução de dívida de 2000 até "31/12/2025"
    Then a meta deve ser salva com status "Em andamento"
    And o progresso inicial deve ser 0

  Scenario: Progresso da meta atualizado após pagamento
    Given que existe uma meta de redução de dívida de 2000 vinculada ao Cartão de Crédito
    And o saldo inicial da dívida era de 5000
    When o saldo atual da dívida passa a ser 4000
    Then o progresso da meta deve ser de 1000
    And o sistema deve registrar que 50% da meta foi atingida

  Scenario: Conclusão da meta de redução de dívida
    Given que existe uma meta de redução de dívida de 2000 vinculada ao Cartão de Crédito
    When o saldo atual da dívida passa a ser 3000
    Then a meta deve ser marcada como "Concluída"
    And o sistema deve gerar uma notificação de parabéns

  Scenario: Tentativa de criar meta para conta que não é dívida
    Given que existe uma conta do tipo "Conta Corrente" com saldo positivo
    When eu tento criar uma meta de redução de dívida vinculada a esta conta
    Then o sistema deve impedir a criação da meta
    And deve exibir uma mensagem de erro "A meta só pode ser criada para contas de dívida"
