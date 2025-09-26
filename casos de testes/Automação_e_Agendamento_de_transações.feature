#Landim

Feature: Automação e Agendamento de transações
    Esta funcionalidade automatiza a criação de transações e gerencia contas futuras.

#Contas bancarias
Scenario: Criação de transações bancárias
    Given que existe uma transação para o usuário pagar que é debitada do seu cartao no dia "16/09/2025"
    When o usuário precisa registrar essa transação para o dia "16/09/2025"
    And o dia atual é "10/09/2025"
    Then o usuário verá que o sistema salvou como "agendada" para o dia "16/09/2025" uma transferência que será realizada

Scenario: Cancelamento de transações bancárias
    Given que existe uma transação para o usuário efetuar no dia "16/09/2025" np valor de "R$ 600,00"
    When o usuário quer cancelar essa transação que iria ser paga no dia "16/09/2025"
    And não existe mais essa transação
    Then o sistema irá excluir esse pagamento


Scenario: Atualização de transações bancárias
    Given que existe uma transação para o usuário pagar no dia "16/09/2025" no valor de "R$ 600,00"
    When o usuario tem essa transação que será paga no dia "16/09/2025" no valor de "R$ 600,00"
    And necessita atualizar o dia e/ou o valor da transaferência para "17/09/2025" no novo valor de "R$ 550,00"
    Then o sistema irá atualizar esse pagamento

# assinaturas de contas

Scenario: Criação de assinatura mensal
    Given que a data atual é "17/10/2025"
    When o usuário cria uma assinatura mensal "Netflix" para o dia "17" iniciando em "17/10/2025"
    Then deve ser criada uma data de transação no dia "17/10/2025" 
    And a próxima data de transação deve ser "17/11/2025"

Scenario: Assinatura em mês curtos
    Given que existe uma data de vencimento de assinatura para o dia "29/02/2025"
    When o usuário tem essa assinatura para pagar no dia "29/02/2025"
    Then o sistema botará o pagamento agendado para o dia "28/02/2025"

Scenario: Cancelamento de assinatura
    Given que existe uma assinatura da "Netflix" que vence todo mês no dia "17" e esta ativa
    When o usuário quer cancelar essa assinatura 
    Then o status deve ser "cancelada"
    And não deve existir próxima data de execução

Scenario: Evita duplicidade ao executar duas vezes
    Given que o proximo dia de pagamento é "17/11/2025" 
    And ja existe a transação desse pagamento agendada para o dia "17/11/2025"
    When o agendamento tentar executar novamente em 17/11/2025
    Then nenhuma nova transação deve ser criada