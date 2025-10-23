Feature: Gestão de Contas a Pagar
  Como um usuário com contas a pagar
  Eu quero visualizar e confirmar o pagamento de transações pendentes
  Para que o valor seja debitado do meu saldo e a despesa registrada como paga

  # Transação Única
  Scenario: Criar uma transação única pendente
    Given o usuário possui uma conta com saldo "1000.00"
    When o usuário cria uma transação única "Compra de material" de "200.00"
    Then a transação deve estar registrada com status "Pendente"
    And o saldo da conta deve permanecer "1000.00"

  Scenario: Editar uma transação pendente única
    Given existe uma transação única pendente de "200.00" com descrição "Compra de material"
    When o usuário altera o valor para "250.00"
    Then a transação deve refletir o novo valor "250.00"
    And o status deve permanecer "Pendente"

  Scenario: Deletar uma transação pendente única
    Given existe uma transação única pendente de "250.00"
    And o usuário possui uma conta com saldo "1000.00"
    When o usuário deleta essa transação
    Then a transação não deve mais existir no sistema
    And o saldo da conta deve permanecer "1000.00"

  Scenario: Efetivar pagamento de uma transação única
    Given existe uma transação única pendente de "250.00"
    And o usuário possui uma conta com saldo "1000.00"
    When o usuário marca a transação como "Efetivada"
    Then o status da transação deve ser "Efetivada"
    And o valor da transação deve ser debitado da conta
    And o saldo da conta deve ser "750.00"

  # Notificações
  Scenario: Notificação de transações próximas do vencimento
    Given existe uma transação pendente com vencimento para amanhã
    When o sistema verifica transações pendentes próximas do vencimento
    Then deve ser gerada uma notificação para o usuário

  Scenario: Efetivar transação com saldo insuficiente
    Given: existe uma transação única pendente de "250.00"
    And: o usuário possui uma conta com saldo "200.00"
    When: o usuário marca a transação como "Efetivada"
    Then: o sistema deve recusar a operação e exibir mensagem de erro "Saldo insuficiente para realizar o débito."

  Scenario: Cancelar uma transação
    Given existe uma transação efetivada de "250.00"
    And o usuário possui uma conta com saldo "1000.00"
    When o usuario cancela essa transação
    Then o status da transação deve ser "Cancelada"
    And o saldo da conta deve ser "1250.00"

  Scenario: Cancelar uma transação pendente
    Given existe uma transação única pendente de "250.00"
    And o usuário possui uma conta com saldo "1000.00"
    When o usuario cancela essa transação
    Then o sistema deve recusar a operação e exibir mensagem de erro "Só é possível cancelar transações efetivadas"
    And o saldo da conta deve ser "1000.00"