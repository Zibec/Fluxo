#Landim

Feature: Automação e Agendamento de transações
    Esta funcionalidade automatiza a criação de transações e gerencia contas futuras.

#Contas bancarias
    Scenario: Criação de transações bancárias
        Given que existe uma transação para o usuário pagar que é debitada do seu cartao no dia "16/09/2025"
        When o usuário precisa registrar essa transação para o dia "16/09/2025"
        And o dia atual é "10/09/2025"
        Then o usuário verá que o sistema salvou como "agendada" para o dia "16/09/2025" uma transferência que será realizada

    Scenario: Criação de transação com data no passado (rejeição)
        Given que a data atual é "20/09/2025"
        When o usuário tenta agendar uma transação para o dia "10/09/2025"
        Then o sistema não deve salvar o agendamento
        And deve informar que a data é inválida por estar no passado

    Scenario: Cancelamento de transações bancárias
        Given que existe uma transação para o usuário efetuar no dia "16/09/2025" no valor de "R$ 600,00"
        When o usuário quer cancelar essa transação que iria ser paga no dia "16/09/2025"
        Then o sistema irá excluir esse pagamento

    Scenario: Cancelamento de transação já executada (rejeição)
        Given que existe uma transação que já foi executada no dia "16/09/2025"
        When o usuário tenta cancelar essa transação executada
        Then o sistema deve informar que não é possível cancelar uma transação já executada

    Scenario: Atualização de transações bancárias
        Given que existe uma transação para o usuário pagar no dia "16/09/2025" no valor de "R$ 600,00"
        When o usuario tem essa transação que será paga no dia "16/09/2025" no valor de "R$ 600,00"
        And necessita atualizar o dia e/ou o valor da transaferência para "17/09/2025" no novo valor de "R$ 550,00"
        Then o sistema irá atualizar esse pagamento

    Scenario: Atualização de transação para data no passado (rejeição)
        Given que existe uma transação agendada para o dia "16/09/2025" no valor de "R$ 600,00"
        And a data atual é "20/09/2025"
        When o usuário tenta atualizar a data dessa transação para "10/09/2025"
        Then o sistema não deve permitir a atualização
        And deve informar que a nova data é inválida por estar no passado

# assinaturas de contas

    Scenario: Criação de assinatura mensal
        Given que a data atual é "17/10/2025"
        When o usuário cria uma assinatura mensal "Netflix" para o dia "17" iniciando em "17/10/2025"
        Then deve ser criada uma data de transação no dia "17/10/2025"
        And a próxima data de transação deve ser "17/11/2025"

    Scenario: Próxima data avança até novembro após duas cobranças
        Given existe uma assinatura mensal "Netflix" configurada para o dia "17" com próxima data "17/09/2025"
        And a próxima data de transação é "17/09/2025"
        When o sistema executa a cobrança no dia "17/09/2025"
        And o sistema executa a cobrança no dia "17/10/2025"
        Then a próxima data de transação deve ser "17/11/2025"

    Scenario: Assinatura em mês curtos
        Given que existe uma data de vencimento de assinatura para o dia "29/02/2025"
        When o usuário tem essa assinatura para pagar no dia "29/02/2025"
        Then o sistema botará o pagamento agendado para o dia "28/02/2025"

    Scenario: Atualiza próxima data em mês curto após execução
        Given existe uma assinatura mensal configurada para o dia "31" com próxima data "31/01/2025"
        When o sistema executa a cobrança no dia "31/01/2025"
        Then a próxima data de transação deve ser "28/02/2025"  # ou 29/02 em ano bissexto

    Scenario: Cancelamento de assinatura
        Given que existe uma assinatura da "Netflix" que vence todo mês no dia "17" e esta ativa
        When o usuário quer cancelar essa assinatura
        Then o status deve ser "cancelada"
        And não deve existir próxima data de execução

    Scenario: Cancelamento de assinatura inexistente ou já cancelada
        Given que não existe uma assinatura ativa chamada "Netflix" (inexistente ou status "cancelada")
        When o usuário tenta cancelar essa assinatura
        Then o sistema deve informar que não há assinatura ativa para cancelar

    Scenario: Evita duplicidade ao executar duas vezes
        Given que o próximo dia de pagamento é "17/11/2025"
        And já existe a transação desse pagamento agendada para o dia "17/11/2025"
        When o agendamento tentar executar novamente em 17/11/2025
        Then nenhuma nova transação deve ser criada

    Scenario: Cria transação no novo ciclo (sem duplicidade)
        Given que o próximo dia de pagamento é "17/12/2025"
        And não existe transação agendada para o dia "17/12/2025"
        When o agendamento executar em "17/12/2025"
        Then deve ser criada exatamente uma transação para o dia "17/12/2025"
