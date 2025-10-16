Feature: Cadastro e gestão de cartões de crédito
  Como usuário do sistema
  Quero cadastrar e gerenciar meus cartões de crédito
  Para controlar meus gastos e limites corretamente

  # 1) Cadastro obrigatório
  Scenario: Cadastro de cartão com todos os campos obrigatórios preenchidos
    Given que o usuário informa nome, banco, bandeira, últimos 4 dígitos, limite total, data de fechamento e vencimento
    When salvar o cartão
    Then o cartão deve ser cadastrado com sucesso

  Scenario: Cadastro de cartão sem informar todos os campos obrigatórios
    Given que o usuário não informa todos os campos obrigatórios
    When tentar salvar o cartão
    Then o sistema deve recusar o cadastro e exibir mensagem de erro

  # 2) Validação de datas
  Scenario: Cadastro de cartão com data de fechamento anterior à de vencimento
    Given que o usuário informa data de fechamento no dia 05 e vencimento no dia 10
    When salvar o cartão
    Then o cartão deve ser cadastrado com sucesso

  Scenario: Cadastro de cartão com data de fechamento posterior à de vencimento
    Given que o usuário informa data de fechamento no dia 15 e vencimento no dia 10
    When tentar salvar o cartão
    Then o sistema deve recusar o cadastro e exibir mensagem de erro

  # 3) Bloqueio de excesso de limite
  Scenario: Registrar despesa dentro do limite disponível
    Given que o cartão possui limite disponível de R$ 1000.00
    When registrar uma despesa de R$ 200.00
    Then a despesa deve ser registrada com sucesso

  Scenario: Registrar despesa acima do limite disponível
    Given que o cartão possui limite disponível de R$ 500.00
    When registrar uma despesa de R$ 800.00 acima do limite
    Then o sistema deve recusar o registro e exibir mensagem de erro

  # 4) Fatura automática
  Scenario: Fechamento de ciclo gera fatura automaticamente
    Given que o cartão possui ciclo de fechamento definido para o dia 05
    When chegar o dia 05 do mês
    Then o sistema deve gerar automaticamente uma nova fatura

  Scenario: Não gerar fatura antes da data de fechamento
    Given que o cartão possui ciclo de fechamento definido para o dia 05
    When estiver no dia 03 do mês
    Then o sistema não deve gerar fatura

  # 5) Vinculação de despesa
  Scenario: Registrar despesa vinculada a uma fatura aberta
    Given que existe uma fatura aberta para o cartão
    When registrar uma despesa no cartão
    Then a despesa deve ser vinculada a essa fatura

  Scenario: Registrar despesa sem fatura aberta
    Given que não existe fatura aberta para o cartão
    When registrar uma despesa no cartão
    Then o sistema deve recusar o registro e exibir mensagem de erro

  # 6) Edição de informações
  Scenario: Atualizar informações de nome, limite e datas do cartão
    Given que o cartão já está cadastrado
    When o usuário altera nome, limite ou datas
    Then o sistema deve atualizar as informações com sucesso

  Scenario: Falha ao atualizar informações com dados inválidos
    Given que o cartão já está cadastrado
    When o usuário tenta alterar limite para um valor negativo
    Then o sistema deve recusar a alteração e exibir mensagem de erro
