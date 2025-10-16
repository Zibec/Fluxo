Feature: Gestão de Perfis Familiares
  #Ao criar uma nova despesa, deve ser obrigatório selecionar qual perfil realizou o gasto. A transação no banco deve conter referência ao perfil que a realizou.
  Scenario: Registrar gasto associado a um perfil com sucesso
    Given que existe um perfil "Filho"
    When eu registro uma nova despesa de 200.0 reais para "Cinema", e seleciono o perfil Filho
    Then a transação deve ser registrada no sistema no perfil Filho

  Scenario: Tentativa de registrar gasto sem selecionar perfil
    Given que existem perfis cadastrados
    When eu registro uma nova despesa de 150.0 reais para "Restaurante", mas não seleciono nenhum perfil
    Then o sistema deve impedir o registro da transacao no sistema
    And deve exibir uma mensagem informando que é obrigatório a seleção de um perfil