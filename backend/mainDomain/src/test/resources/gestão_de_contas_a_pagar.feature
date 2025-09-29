Feature: Gestão de Contas a Pagar
  Como um usuário com contas agendadas
  Eu quero visualizar e confirmar o pagamento de contas pendentes
  Para que o valor seja debitado e a despesa registrada como paga

  Scenario: Criar nova transação pendente
    Given o usuário possui um saldo de 1000.00
    When o usuário adiciona uma transação "Pendente" de 200.00 com descrição "Compra de material"
    Then a transação deve ser registrada com status "Pendente"
    And o saldo da conta deve continuar 1000.00

  Scenario: Editar uma transação pendente
    Given existe uma transação pendente de 200.00 com descrição "Compra de material"
    When o usuário altera o valor para 250.00
    Then a transação deve refletir o novo valor de 250.00
    And o status permanece "Pendente"

  Scenario: Deletar uma transação pendente
    Given existe uma transação pendente de 250.00 com descrição "Compra de material"
    And o usuário possui um saldo de 1000.00
    When o usuário deleta a transação
    Then a transação não deve mais existir no sistema
    And o saldo da conta permanece 1000.00

  Scenario: Efetivar pagamento de uma transação pendente
    Given existe uma transação pendente de 250.00 com descrição "Compra de material"
    And o usuário tem um saldo de 1000.00
    When o usuário marca a transação como "Efetivada"
    Then o status da transação deve ser "Efetivada"
    And o valor da transação deve ser debitado da conta
    And o saldo da conta deve ser 750.00

  Scenario: Lembrete de transações pendentes próximas da data de vencimento
    Given existe uma transação pendente com data de vencimento para amanhã
    When o sistema verifica transações pendentes próximas do vencimento
    Then deve gerar uma notificação para o usuário
