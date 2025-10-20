Feature: Gestão de Metas de Redução de Dívidas
  Como um usuário que deseja quitar uma dívida
  Quero criar e acompanhar uma meta inversa
  Para que eu possa visualizar o quanto já paguei e quanto ainda falta quitar

  Background:
    Given que o usuário possui uma conta com saldo "1500.00"

  Scenario: Criar uma meta inversa válida
    When o usuário cria uma meta inversa com nome "Quitar Cartão" e valor alvo "1000.00"
    Then a meta deve ser criada com status "ATIVA"
    And o valor amortizado inicial deve ser 0.00

  Scenario: Realizar um aporte válido
    Given que existe uma meta inversa ativa de "1000.00"
    When o usuário realiza um aporte de "400.00" para amortizar a dívida
    Then o valor amortizado deve ser "400.00"
    And o progresso deve ser "0.40"

  Scenario: Concluir meta ao atingir o valor da dívida
    Given que existe uma meta inversa ativa de "1000.00"
    When o usuário realiza um aporte de "1000.00"
    Then o status da meta deve mudar para "CONCLUIDA"
    And o progresso deve ser "1.00"

  Scenario: Realizar aporte inválido (valor nulo)
    Given que existe uma meta inversa ativa de "1000.00"
    When o usuário tenta realizar um aporte de valor nulo
    Then o sistema deve lançar uma exceção com a mensagem "Valor do aporte deve ser um número positivo."

  Scenario: Realizar aporte inválido (valor negativo)
    Given que existe uma meta inversa ativa de "1000.00"
    When o usuário tenta realizar um aporte de "-50.00"
    Then o sistema deve lançar uma exceção com a mensagem "Valor do aporte deve ser um número positivo."

  Scenario: Verificar progresso proporcional
    Given que existe uma meta inversa ativa de "1200.00"
    When o usuário realiza um aporte de "300.00"
    Then o progresso deve ser "0.25"
