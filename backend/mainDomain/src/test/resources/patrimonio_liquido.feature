# Gustavo

Feature: Visão de Patrimônio Líquido

  Para ter uma visão clara da minha saúde financeira e sua evolução, eu como usuário,
  quero consolidar meus ativos (contas, investimentos) e passivos (dívidas).

Scenario: Calcular patrimônio com múltiplos ativos e passivos
  Given que eu tenho uma "Conta Corrente" com saldo de R$ 5000.00
  And tenho uma "Conta Poupança" com saldo de R$ 10000.00
  And tenho um "Investimento em Ações" com valor de R$ 15000.00
  And tenho um "Cartão de Crédito" com uma dívida de R$ 3000.00
  When eu solicitar o meu patrimônio líquido
  Then o valor total do patrimônio deve ser R$ 27000.00

Scenario: Calcular patrimônio com conta de saldo negativo
  Given que eu tenho uma "Conta Corrente" com saldo de R$ -500.00
  And tenho um "Investimento" com valor de R$ 2000.00
  When eu solicitar o meu patrimônio líquido
  Then o valor total do patrimônio deve ser R$ 1500.00

Scenario: Gerar snapshot do patrimônio no fim do mês
  Given que hoje é o último dia do mês
  And meu patrimônio líquido atual é de R$ 35000.00
  When o processo automático de snapshot for executado
  Then um registro histórico do patrimônio deve ser salvo com a data de hoje e o valor de R$ 35000.00

Scenario: Tentar visualizar gráfico sem histórico de patrimônio
  Given que eu sou um novo usuário sem nenhum snapshot de patrimônio
  When eu acessar a tela de evolução do patrimônio
  Then o sistema deve exibir a mensagem "O histórico de patrimônio ainda não foi gerado"