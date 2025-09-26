Feature: Configurações da Conta de Usuário
  Permite ao usuário personalizar sua experiência e gerenciar sua segurança.

  # História 12.1: Gerenciamento de Dados Pessoais e Segurança
  Scenario: Atualizar e-mail com sucesso
    Given que estou logado no sistema
    And acesso a página de configurações de cartao
    When eu altero meu e-mail para "novo_email@teste.com"
    And o e-mail não está em uso por outro usuário
    Then o sistema deve salvar o novo e-mail
    And exibir uma mensagem de sucesso "E-mail atualizado com sucesso."

  Scenario: Atualizar e-mail já existente
    Given que estou logado no sistema
    And acesso a página de configurações de cartao
    When eu altero meu e-mail para "email_existente@teste.com"
    And o e-mail já está em uso por outro usuário
    Then o sistema deve recusar a alteração
    And exibir a mensagem "E-mail já cadastrado."

  Scenario: Alterar senha com sucesso
    Given que estou logado no sistema
    And acesso a página de configurações de cartao
    When eu informo minha senha atual "senha_atual"
    And informo a nova senha "NovaSenha123!"
    And confirmo a nova senha
    Then o sistema deve validar a senha atual
    And armazenar a nova senha com criptografia forte
    And exibir a mensagem "Senha alterada com sucesso."

  Scenario: Alterar senha com senha atual incorreta
    Given que estou logado no sistema
    And acesso a página de configurações de cartao
    When eu informo uma senha atual incorreta "senha_errada"
    And tento cadastrar a nova senha "NovaSenha123!"
    Then o sistema deve recusar a alteração
    And exibir a mensagem "Senha atual inválida."

  # História 12.2: Configurações de Moeda e Regionalização
  Scenario: Definir moeda e formato de data preferidos
    Given que estou logado no sistema
    And acesso a página de configurações de cartao
    When eu seleciono a moeda "USD"
    And seleciono o formato de data "MM/dd/yyyy"
    Then o sistema deve salvar as preferências
    And exibir a mensagem "Configurações atualizadas."
    And todos os valores monetários exibidos devem estar em dólares
    And todas as datas exibidas devem seguir o formato "MM/dd/yyyy"
