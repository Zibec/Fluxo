#language: pt
#Landim

Funcionalidade: Automação e Agendamento de transações
    Esta funcionalidade automatiza a criação de transações e gerencia contas futuras.

#Contas bancarias
Cenário: Criação de transações bancárias
    Dado que existe uma transação para o usuário pagar que é debitada do seu cartao no dia "16/09/2025"
    Quando o usuário precisa registrar essa transação para o dia "16/09/2025" 
    E o dia atual é "10/09/2025"
    Então o usuário verá que o sistema salvou como "agendada" para o dia "16/09/2025" uma transferência que será realizada

Cenário: Cancelamento de transações bancárias
    Dado que existe uma transação para o usuário efetuar no dia "16/09/2025" np valor de "R$ 600,00"
    Quando o usuário quer cancelar essa transação que iria ser paga no dia "16/09/2025"
    E não existe mais essa transação
    Então o sistema irá excluir esse pagamento


Cenário: Atualização de transações bancárias
    Dado que existe uma transação para o usuário pagar no dia "16/09/2025" no valor de "R$ 600,00"
    Quando o usuario tem essa transação que será paga no dia "16/09/2025" no valor de "R$ 600,00"
    E necessita atualizar o dia e/ou o valor da transaferência para "17/09/2025" no novo valor de "R$ 550,00"
    Então o sistema irá atualizar esse pagamento

# assinaturas de contas

Cenário: Criação de assinatura mensal
    Dado que a data atual é "17/10/2025"
    Quando o usuário cria uma assinatura mensal "Netflix" para o dia "17" iniciando em "17/10/2025"
    Então deve ser criada uma data de transação no dia "17/10/2025" 
    E a próxima data de transação deve ser "17/11/2025"

Cenário: Assinatura em mês curtos
    Dado que existe uma data de vencimento de assinatura para o dia "29/02/2025"
    Quando o usuário tem essa assinatura para pagar no dia "29/02/2025"
    Então o sistema botará o pagamento agendado para o dia "28/02/2025"

Cenário: Cancelamento de assinatura
    Dado que existe uma assinatura da "Netflix" que vence todo mês no dia "17" e esta ativa
    Quando o usuário quer cancelar essa assinatura 
    Então o status deve ser "cancelada"
    E não deve existir próxima data de execução

Cenário: Evita duplicidade ao executar duas vezes
    Dado que o proximo dia de pagamento é "17/11/2025" 
    E ja existe a transação desse pagamento agendada para o dia "17/11/2025"
    Quando o agendamento tentar executar novamente em 17/11/2025
    Então nenhuma nova transação deve ser criada