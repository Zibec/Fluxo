#Landim

Feature: Gestão de Orçamentos
    Esta funcionalidade ajuda o usuário a ter um melhor controle de gastos mensais
    para suas categorias existentes.

# história 1.1
Scenario: Adicionar um limite de gastos com sucesso
    Given que existe a categoria "Alimentação" para um usuário autenticado como "Gabriel"
    When o usuário define um orçamento de "R$ 100,00" para a categoria "Alimentação" no mês "09/2025"
    Then o usuário deve ver o orçamento salvo para "Alimentação" em "09/2025" com valor "R$ 100,00"

Scenario: Impedir a criação de limite duplicado
    Given que existe a categoria "Alimentação" para um usuário autenticado como "Gabriel"
    And existe um orçamento de "R$ 100,00" para "Alimentação" em "09/2025"
    When o usuário tenta definir um orçamento de "R$ 120,00" para "Alimentação" em "09/2025"
    Then o sistema deve retornar "Já existe um orçamento para esta categoria neste mês"
    And o orçamento não deve ser salvo

Scenario: Atualizar um limite de gastos
    Given que existe um orçamento na categoria "Alimentação" para o mês "09/2025" de "R$ 100,00"
    When o usuário atualiza esse orçamento para "R$ 600,00"
    Then o sistema deve retornar "Atualizado com sucesso"
    And o usuário deve ver o orçamento salvo com valor "R$ 600,00"

# história 1.2
Scenario: Notificar ao atingir 80% do limite
    Given que existe uma categoria "Alimentação" com gasto limite de "R$ 100,00" para o mês "09/2025"
    And o gasto acumulado do usuário para "Alimentação" em "09/2025" é de "R$ 79,00"
    When o usuário registra uma despesa de "R$ 1,00" na categoria "Alimentação" em "09/2025"
    Then o sistema deve enviar ao usuário uma notificação "Você atingiu 80% do limite definido"

Scenario: Não notificar abaixo de 80%
    Given que existe uma categoria "Alimentação" com gasto limite de "R$ 100,00" para o mês "09/2025"
    And o gasto acumulado do usuário para "Alimentação" em "09/2025" é de "R$ 79,99"
    When o usuário registra uma despesa de "R$ 0,00" na categoria "Alimentação" em "09/2025"
    Then o sistema não deve notificar o usuário

Scenario: Notificar ao atingir 100% do limite
    Given que existe uma categoria "Alimentação" com gasto limite de "R$ 100,00" para o mês "09/2025"
    And o gasto acumulado do usuário para "Alimentação" em "09/2025" é de "R$ 99,00"
    When o usuário registra uma despesa de "R$ 1,00" na categoria "Alimentação" em "09/2025"
    Then o sistema deve enviar ao usuário uma notificação "Você atingiu 100% do limite definido"

Scenario: Notificar ao exceder 100% do limite
    Given que existe uma categoria "Alimentação" com gasto limite de "R$ 100,00" para o mês "09/2025"
    And o gasto acumulado do usuário para "Alimentação" em "09/2025" é de "R$ 100,00"
    When o usuário registra uma despesa de "R$ 1,00" na categoria "Alimentação" em "09/2025"
    Then o sistema deve enviar ao usuário uma notificação "Você excedeu o limite desta categoria"

# história 1.3
Scenario: Aceitar quando o valor do orçamento é positivo
    Given que existe a categoria "Alimentação" para um usuário autenticado como "Gabriel"
    When o usuário define um orçamento de "R$ 50,00" para a categoria "Alimentação" no mês "09/2025"
    Then o usuário deve ver o orçamento salvo com valor "R$ 50,00"

Scenario: Rejeitar quando o valor do orçamento é negativo
    Given que existe a categoria "Alimentação" para um usuário autenticado como "Gabriel"
    When o usuário tenta definir um orçamento de "R$ -50,00" para a categoria "Alimentação" no mês "09/2025"
    Then o sistema deve retornar "Valor do orçamento deve ser maior que zero"
    And o orçamento não deve ser aplicado
