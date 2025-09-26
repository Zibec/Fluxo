#language: pt

Funcionalidade: Gestão de Orçamentos
    Esta funcionalidade ajuda o usuário a ter um melhor controle de gastos mensais
    para suas categorias existentes.

# história 1.1
Cenário: Adicionar um limite de gastos com sucesso
    Dado que existe a categoria "Alimentação" para um usuário autenticado como "Gabriel"
    Quando o usuário define um orçamento de "R$ 100,00" para a categoria "Alimentação" no mês "09/2025"
    Então o usuário deve ver o orçamento salvo para "Alimentação" em "09/2025" com valor "R$ 100,00"

Cenário: Impedir a criação de limite duplicado
    Dado que existe a categoria "Alimentação" para um usuário autenticado como "Gabriel"
    E existe um orçamento de "R$ 100,00" para "Alimentação" em "09/2025"
    Quando o usuário tenta definir um orçamento de "R$ 120,00" para "Alimentação" em "09/2025"
    Então o sistema deve retornar "Já existe um orçamento para esta categoria neste mês"
    E o orçamento não deve ser salvo

Cenário: Atualizar um limite de gastos
    Dado que existe um orçamento na categoria "Alimentação" para o mês "09/2025" de "R$ 100,00"
    Quando o usuário atualiza esse orçamento para "R$ 600,00"
    Então o sistema deve retornar "Atualizado com sucesso"
    E o usuário deve ver o orçamento salvo com valor "R$ 600,00"

# história 1.2
Cenário: Notificar ao atingir 80% do limite
    Dado que existe uma categoria "Alimentação" com gasto limite de "R$ 100,00" para o mês "09/2025"
    E o gasto acumulado do usuário para "Alimentação" em "09/2025" é de "R$ 79,00"
    Quando o usuário registra uma despesa de "R$ 1,00" na categoria "Alimentação" em "09/2025"
    Então o sistema deve enviar ao usuário uma notificação "Você atingiu 80% do limite definido"

Cenário: Não notificar abaixo de 80%
    Dado que existe uma categoria "Alimentação" com gasto limite de "R$ 100,00" para o mês "09/2025"
    E o gasto acumulado do usuário para "Alimentação" em "09/2025" é de "R$ 79,99"
    Quando o usuário registra uma despesa de "R$ 0,00" na categoria "Alimentação" em "09/2025"
    Então o sistema não deve notificar o usuário

Cenário: Notificar ao atingir 100% do limite
    Dado que existe uma categoria "Alimentação" com gasto limite de "R$ 100,00" para o mês "09/2025"
    E o gasto acumulado do usuário para "Alimentação" em "09/2025" é de "R$ 99,00"
    Quando o usuário registra uma despesa de "R$ 1,00" na categoria "Alimentação" em "09/2025"
    Então o sistema deve enviar ao usuário uma notificação "Você atingiu 100% do limite definido"

Cenário: Notificar ao exceder 100% do limite
    Dado que existe uma categoria "Alimentação" com gasto limite de "R$ 100,00" para o mês "09/2025"
    E o gasto acumulado do usuário para "Alimentação" em "09/2025" é de "R$ 100,00"
    Quando o usuário registra uma despesa de "R$ 1,00" na categoria "Alimentação" em "09/2025"
    Então o sistema deve enviar ao usuário uma notificação "Você excedeu o limite desta categoria"

# história 1.3
Cenário: Aceitar quando o valor do orçamento é positivo
    Dado que existe a categoria "Alimentação" para um usuário autenticado como "Gabriel"
    Quando o usuário define um orçamento de "R$ 50,00" para a categoria "Alimentação" no mês "09/2025"
    Então o usuário deve ver o orçamento salvo com valor "R$ 50,00"

Cenário: Rejeitar quando o valor do orçamento é negativo
    Dado que existe a categoria "Alimentação" para um usuário autenticado como "Gabriel"
    Quando o usuário tenta definir um orçamento de "R$ -50,00" para a categoria "Alimentação" no mês "09/2025"
    Então o sistema deve retornar "Valor do orçamento deve ser maior que zero"
    E o orçamento não deve ser aplicado
