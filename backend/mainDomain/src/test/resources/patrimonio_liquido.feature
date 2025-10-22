# Gustavo

Feature: Visão de Patrimônio Líquido

  Para ter uma visão clara da minha saúde financeira e sua evolução, eu como usuário,
  quero consolidar meus ativos (contas, investimentos) e passivos (dívidas).
# Cálculo Consolidado
Scenario: Calcular patrimônio com múltiplos ativos e passivos
  Given que eu tenho uma "Conta Corrente" com saldo de R$ 5000.00
  And tenho uma "Conta Poupança" com saldo de R$ 10000.00
  And tenho um "Investimento em Ações" com valor de R$ 15000.00
  And tenho um "Cartão de Crédito" com uma dívida de R$ 3000.00
  When eu solicitar o meu patrimônio líquido
  Then o valor total do patrimônio deve ser R$ 27000.00

  Scenario: Falha ao calcular patrimônio com dados ausentes (Exceção)
    Given que eu tenho uma "Conta Corrente" com saldo de R$ 2000.00
    And um "Investimento Antigo" com valor não definido (nulo)
    Then o sistema de patrimônio deve lançar uma exceção com a mensagem "Valor invalido"

# Snapshot Mensal
Scenario: Gerar snapshot do patrimônio no fim do mês
  Given que hoje é o último dia do mês
  And meu patrimônio líquido atual é de R$ 35000.00
  When o processo automático de snapshot for executado
  Then um registro histórico do patrimônio deve ser salvo com a data de hoje e o valor de R$ 35000.00

  Scenario: Não gerar snapshot fora do fim do mês
    Given que hoje não é o último dia do mês
    And meu patrimônio líquido atual é de R$ 30000.00
    When o processo automático de snapshot for executado
    Then nenhum registro histórico de patrimônio deve ser salvo


  # Geração de Gráfico

Scenario: Visualização do gráfico de evolução com dados históricos
 Given que eu tenho os seguintes snapshots de patrimônio:
   | Data       | Valor       |
   | 2025-07-31 | R$ 25000.00 |
   | 2025-08-31 | R$ 27000.00 |
   | 2025-09-30 | R$ 26500.00 |
  When eu acessar a tela de evolução do patrimônio
  Then um gráfico de linhas deve ser exibido com os dados do histórico

  Scenario: Tentar visualizar gráfico sem histórico de patrimônio
    Given que eu sou um novo usuário sem nenhum snapshot de patrimônio
    When eu acessar a tela de evolução do patrimônio
    Then o sistema deve exibir a mensagem "O histórico de patrimônio ainda não foi gerado"