#language: pt

Funcionalidade: Automação e Agendamento de transações
    Esta funcionalidade automatiza a criação de transações e gerencia contas futuras.

Cenário: Criação de transações bancárias
    Dado que existe uma conta para o usuário pagar que vence nesse dia
    Quando o usuário precisa fazer uma transação para o dia "16/09/2025" 
    Então o usuário verá que o sistema criou para o dia "16/09/2025" que a tranerencia será realiada e anotada

Cenário