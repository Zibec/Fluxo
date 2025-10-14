Feature: Rastreamento de Investimentos
  #O sistema precisa de um job agendado para buscar a taxa Selic diária de uma API externa confiável.

  Scenario: Buscar taxa Selic com sucesso
    Given que o job agendado é executado
    When o sistema consulta a API externa do Banco Central
    Then a taxa Selic diária é armazenada no sistema

  Scenario: Falha ao consultar a API externa
    Given que o job agendado é executado
    When o sistema consulta a API externa do Banco Central
    But a API não está disponível
    Then a texa Selic não é atualizada naquele dia
    And o sistema deve registrar um log de erro

  #Para cada investimento "Tesouro Selic", aplicar rendimento diário automaticamente.
  Scenario: Atulização de rendimento bem-sucedida
    Given que existe um investimento do tipo "Tesouro Selic" com valor atual de 1000
    And a taxa selic diária é de 0.01 (1%)
    When o job de atualização de rendimento é executado
    Then o valor atualizado do investimento deve ser 1010

  Scenario: Tentativa de aplicar rendimento sem taxa Selic disponível
    Given que existe um investimento do tipo "Tesouro Selic" com valor atual de 1000
    And não há taxa Selic disponível no sistema
    When o job de atualização de rendimento é executado
    Then o investimento não deve ser atualizado
    And o sistema deve registrar um log de falha

  #Sempre que o rendimento é aplicado, registrar histórico com data e valor.
  Scenario: Registro de histórico após atualização
    Given que existe um investimento do tipo "Tesouro Selic" com valor atual de 1000
    And a taxa selic diária é de 0.01 (1%)
    When o job de atualização de rendimento é executado
    Then o valor atualizado do investimento deve ser 1010
    And deve existir um registro no histórico com a data atual e o valor 1010

  Scenario: Falha ao registrar histórico
    Given que existe um investimento do tipo "Tesouro Selic" com valor atual de 1000
    And a taxa selic diária é de 0.01 (1%)
    When o job de atualização de rendimento é executado
    But ocorre uma falha no registro de histórico
    Then o valor atualizado do investimento deve ser 1010
    And o sistema deve gerar um log de erro indicando falha ao registrar histórico