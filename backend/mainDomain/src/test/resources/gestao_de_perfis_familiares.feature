Feature: Gestão de Perfis Familiares
  #A conta principal pode criar, editar e excluir perfis secundários.
  Scenario: Criar, editar e excluir perfil com sucesso
    Given que estou logado no usuário
    When eu crio um novo perfil chamado "Filho"
    Then o perfil "Filho" deve estar disponível a lista de perfis
    When eu edito o perfil "Filho" para "Filho Mais Velho"
    Then o nome atualizado "Filho Mais Velho" deve estar na lista de perfis
    When eu excluo o perfil "Filho Mais Velho"
    Then o perfil não deve mais aparecer na lista de perfis

  Scenario: Tentativa de criar perfil a partir de conta secundária
    Given que estou logado em um perfil secundário
    When eu tento criar um novo perfil chamado "Filho Mais Novo"
    Then o sistema deve impedir a operação
    And deve exibir a mensagem "Apenas a conta principal pode gerenciar perfis"

  #Ao criar uma nova despesa, deve ser obrigatório selecionar qual perfil realizou o gasto. A transação no banco deve conter referência ao perfil_id.
  Scenario: Registrar gasto associado a um perfil
    Given que existe um perfil chamado "Filho"
    When eu registro uma nova despesa de "R$200,00" para "Cinema"
    And seleciono o perfil "Filho"
    Then a transação deve ser registrada no banco de dados
    And deve conter o campo perfil_id correspondente ao perfil "Filho"

  Scenario: Tentativa de registrar gasto sem selecionar perfil
    Given que existem perfis cadastrados
    When eu registro uma nova despesa de "R$150,00" para "Restaurante"
    But não seleciono nenhum perfil
    Then o sistema deve impedir o registro no banco de dados
    And deve exibir a mensagem "É obrigatório a seleção de um perfil"