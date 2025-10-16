Feature: Atualização de Perfil do Usuário
  Como um usuário
  Eu quero poder atualizar as informações e a senha do meu perfil
  Para manter meus dados sempre corretos e seguros

  # Confirmação de Identidade
  Scenario: Atualizar e-mail com senha atual correta (sucesso)
    Given que estou logado no sistema
    And informo minha senha atual corretamente
    When altero meu e-mail para "novoemail@dominio.com"
    Then o sistema deve atualizar o e-mail com sucesso

  Scenario: Falha ao atualizar e-mail com senha atual incorreta
    Given que estou logado no sistema
    And informo uma senha atual incorreta
    When tento alterar meu e-mail para "novoemail@dominio.com"
    Then o sistema deve exibir uma mensagem de erro de autenticação

  # Validação de Unicidade - Username
  Scenario: Atualizar username para um nome único (sucesso)
    Given que estou logado no sistema
    And informo minha senha atual corretamente
    When altero meu username para "usuario_unico123"
    Then o sistema deve salvar o novo username

  Scenario: Falha ao atualizar username já existente
    Given que estou logado no sistema
    And informo minha senha atual corretamente
    When altero meu username para "usuario_existente"
    Then o sistema deve exibir uma mensagem de erro de nome já em uso

  # Validação de Formato - E-mail
  Scenario: Atualizar e-mail com formato válido (sucesso)
    Given que estou logado no sistema
    And informo minha senha atual corretamente
    When altero meu e-mail para "valido@dominio.com"
    Then o sistema deve salvar o novo e-mail

  Scenario: Falha ao atualizar e-mail com formato inválido
    Given que estou logado no sistema
    And informo minha senha atual corretamente
    When altero meu e-mail para "email-invalido"
    Then o sistema deve exibir uma mensagem de erro de formato inválido

  # Validação de Unicidade - E-mail
  Scenario: Atualizar e-mail para um endereço único (sucesso)
    Given que estou logado no sistema
    And informo minha senha atual corretamente
    When altero meu e-mail para "unico@dominio.com"
    Then o sistema deve salvar o novo e-mail

  Scenario: Falha ao atualizar e-mail já existente
    Given que estou logado no sistema
    And informo minha senha atual corretamente
    When altero meu e-mail para "email_existente@dominio.com"
    Then o sistema deve exibir uma mensagem de erro de e-mail já cadastrado

  # Verificação da Senha Atual
  Scenario: Alterar senha com senha atual correta (sucesso)
    Given que estou logado no sistema
    And informo a senha atual correta "senha123"
    When informo a nova senha "novaSenha456"
    And confirmo a nova senha "novaSenha456"
    Then o sistema deve atualizar a senha com sucesso

  Scenario: Falha ao alterar senha com senha atual incorreta
    Given que estou logado no sistema
    And informo a senha atual incorreta "errada"
    When informo a nova senha "novaSenha456"
    And confirmo a nova senha "novaSenha456"
    Then o sistema deve exibir uma mensagem de erro de senha atual inválida

  # Proibição de Senha Antiga
  Scenario: Alterar senha para uma nova senha diferente (sucesso)
    Given que estou logado no sistema
    And informo a senha atual correta "senha123"
    When informo a nova senha "senhaNova456"
    And confirmo a nova senha "senhaNova456"
    Then o sistema deve atualizar a senha com sucesso

  Scenario: Falha ao tentar alterar senha para a mesma senha atual
    Given que estou logado no sistema
    And informo a senha atual correta "senha123"
    When informo a nova senha "senha123"
    And confirmo a nova senha "senha123"
    Then o sistema deve exibir uma mensagem de erro de senha repetida
