# Gustavo

Feature: Tratamento de Reembolsos

  Para garantir a precisão dos meus relatórios e orçamentos, eu como usuário,
  quero registrar reembolsos vinculados a despesas originais.

Scenario: Registrar reembolso vinculando a uma despesa
  Given que eu tenho uma despesa original de R$ 80.00 na categoria "Alimentação"
  When eu registrar um reembolso de R$ 20.00 e o vincular à despesa original
  Then um reembolso de R$ 20.00 deve ser criado e associado à despesa original

Scenario: Tentar registrar reembolso sem vincular a uma despesa
  Given que estou na tela de registro de reembolso
  When eu tento registrar um reembolso de R$ 20.00 sem selecionar uma despesa original
  Then o sistema deve exibir a mensagem de erro "A vinculação a uma despesa original é obrigatória"

Scenario: Reembolso deve abater o gasto da categoria
  Given que o meu gasto total na categoria "Alimentação" é de R$ 150.00
  And uma das despesas originais nesta categoria é de R$ 80.00
  When eu registrar um reembolso de R$ 20.00 vinculado à despesa de R$ 80.00
  Then o total de gastos na categoria "Alimentação" deve ser de R$ 130.00

Scenario: Reembolso deve restaurar valor no orçamento
  Given que tenho um orçamento de R$ 500.00 para "Alimentação"
  And já gastei R$ 400.00, com R$ 100.00 disponíveis
  And uma das despesas é de R$ 80.00
  When eu receber um reembolso de R$ 30.00 vinculado à despesa de R$ 80.00
  Then o valor disponível no meu orçamento de "Alimentação" deve ser de R$ 130.00

Scenario: Tentar registrar reembolso com valor maior que a despesa
  Given que eu tenho uma despesa original de R$ 80.00
  When eu tento registrar um reembolso de R$ 90.00 para essa despesa
  Then o sistema deve exibir a mensagem de erro "O valor do reembolso não pode ser maior que o da despesa original"