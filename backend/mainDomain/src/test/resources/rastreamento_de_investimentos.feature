Feature: Rastreamento de Investimentos
  #O sistema precisa de um job agendado para buscar a taxa Selic diária de uma API externa confiável.

  Scenario: Buscar taxa Selic com sucesso
    Given que o job agendado é executado
    When o sistema consulta a API externa do Banco Central
    Then a taxa Selic diária é armazenada no sistema

  Scenario: Falha ao consultar a API externa
    Given que o job agendado é executado
    And a API não está disponível
    When o sistema consulta a API externa do Banco Central
    Then a texa Selic não é atualizada naquele dia
    And o sistema deve registrar um log de erro

  #Para cada investimento "Tesouro Selic", aplicar rendimento diário automaticamente.
  Scenario: Atualização de rendimento bem-sucedida
    Given que existe um investimento do tipo Tesouro Selic com valor atual de 1000
    And a taxa selic diária é de 0.01 (1%)
    When o job de atualização de rendimento é executado
    Then o valor atualizado do investimento deve ser 1010

  Scenario: Tentativa de aplicar rendimento sem taxa Selic disponível
    Given que existe um investimento do tipo Tesouro Selic com valor atual de 1000
    And não há taxa Selic disponível no sistema
    When o job de atualização de rendimento é executado
    Then o investimento não deve ser atualizado
    And o sistema deve registrar um log de falha

  #Sempre que o rendimento é aplicado, registrar histórico com data e valor.
  Scenario: Registro de histórico após atualização
    Given que existe um investimento do tipo Tesouro Selic com valor atual de 1000
    And a taxa selic diária é de 0.01 (1%)
    When o job de atualização de rendimento é executado
    Then deve existir um registro no histórico com a data atual e o valor 1010

  Scenario: Falha ao registrar histórico
    Given que existe um investimento do tipo Tesouro Selic com valor atual de 1000
    And a taxa selic diária é de 0.01 (1%)
    And o banco de dados de histórico está fora do ar
    When o job de atualização de rendimento é executado
    Then o sistema deve gerar um log de erro indicando falha ao registrar histórico

  #O sistema deve poder realizar tanto o resgate parcial, como o resgate total do valor investido para um investimento.
  # Caso o tipo de resgate seja total, o investimento deve ser removido do sistema.

  Scenario: Resgate total bem-sucedido
    Given que existe um investimento do tipo Tesouro Selic com valor atual de 1000
    When realizo o resgate total do valor investido
    Then o investimento deve ser removido do sistema

  Scenario: Falha em etapas anteriores do resgate total
    Given que existe um investimento do tipo Tesouro Selic com valor atual de 1000
    And o banco de dados de histórico está fora do ar
    When realizo o resgate total do valor investido
    Then o investimento não deve ser removido
    And o sistema deve emitir um log de falha

  #Caso o tipo de resgate seja parcial, deve ter seu valor atualizado no sistema.

  Scenario: Resgate parcial bem-sucedido
    Given que existe um investimento do tipo Tesouro Selic com valor atual de 1000
    When realizo o resgate parcial de 500 reais do valor investido
    Then o sistema deve atualizar o valor investido para 500 reais

  Scenario: Tentativa de resgate total em resgate parcial
    Given que existe um investimento do tipo Tesouro Selic com valor atual de 1000
    When realizo o resgate parcial com o valor total investido
    Then o sistema deve impedir a atualização do valor investido
    And exibir aviso de tentativa de resgate total em resgate parcial

  #Caso o tipo de resgate seja total, o histórico de valorização do investimento resgatado, deve ser completamente apagado
  Scenario: Deleção do histórico de valorização bem-sucedido em resgate total
    Given que existe um investimento do tipo Tesouro Selic com valor atual de 1000
    When realizo o resgate total do valor investido
    Then o sistema deve apagar o histórico de valorização daquele investimento

  Scenario: Falha deleção do histórico de valorização em resgate total
    Given que existe um investimento do tipo Tesouro Selic com valor atual de 1000
    And o banco de dados de histórico está fora do ar
    When realizo o resgate total do valor investido
    Then o sistema deve levantar uma exceção referente à falha na deleção
    And o sistema deve emitir um log de falha

  #Caso o tipo de resgate seja parcial, o histórico de valorização deve ser mantido e uma nova entrada com o valor
  #restante investido deve ser adicionada.
  Scenario: Histórico de valorização atualizado com sucesso em resgate parcial
    Given que existe um investimento do tipo Tesouro Selic com valor atual de 1000
    When realizo o resgate parcial de 500 reais do valor investido
    Then o sistema deve atualizar o histórico com uma nova entrada com o valor restante investido de 500 reais

  Scenario: Falha em etapas anteriores à atualização do histórico em resgate parcial
    Given que existe um investimento do tipo Tesouro Selic com valor atual de 1000
    When realizo o resgate parcial, mas uma falha ocorre durante o resgate
    Then o sistema não deve atualizar o histórico com uma nova entrada