#language: pt

Funcionalidade: Metas de Poupança

  Para alcançar meus objetivos, eu como usuário,
  quero criar metas de poupança e acompanhar meu progresso.

Cenário: Fazer um aporte (contribuição) com saldo suficiente
  Dado que eu tenho uma meta de poupança "Viagem de Férias" com saldo atual de R$ 500,00
  E o saldo da minha Conta principal é de R$ 1.000,00
  Quando eu faço um aporte de R$ 200,00 para a meta "Viagem de Férias"
  Então o saldo da meta "Viagem de Férias" deve ser R$ 700,00
  E o saldo da minha Conta principal deve ser R$ 800,00

Cenário: Tentar fazer um aporte com saldo insuficiente
  Dado que eu tenho uma meta de poupança "Celular Novo" com saldo atual de R$ 100,00
  E o saldo da minha Conta principal é de R$ 50,00
  Quando eu tento fazer um aporte de R$ 100,00 para a meta "Celular Novo"
  Então o sistema deve exibir a mensagem de erro "Saldo insuficiente na conta principal"
  E o saldo da meta "Celular Novo" deve permanecer R$ 100,00