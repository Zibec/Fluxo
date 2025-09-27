#Matias

Feature: Metas de Poupança

  Para alcançar meus objetivos, eu como usuário,
  quero criar metas de poupança e acompanhar meu progresso.

Scenario: Fazer um aporte (contribuição) com saldo suficiente
  Given que eu tenho uma meta de poupança "Viagem de Férias" com saldo atual de R$ 500.00
  And o saldo da minha Conta principal é de R$ 1000.00
  When eu faço um aporte de R$ 200.00 para a meta "Viagem de Férias"
  Then o saldo da meta "Viagem de Férias" deve ser R$ 700.00
  And o saldo da minha Conta principal deve ser R$ 800.00


Scenario: Tentar fazer um aporte com saldo insuficiente
  Given que eu tenho uma meta de poupança "Celular Novo" com saldo atual de R$ 100.00
  And o saldo da minha Conta principal é de R$ 50.00
  When eu tento fazer um aporte de R$ 100.00 para a meta "Celular Novo"
  Then o sistema deve exibir a mensagem de erro "Saldo insuficiente na conta principal"
  And o saldo da meta "Celular Novo" deve permanecer R$ 100.00
