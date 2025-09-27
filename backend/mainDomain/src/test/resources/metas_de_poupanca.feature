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

Scenario: Criar uma nova meta de poupança com sucesso
  Given que não existe uma meta de poupança chamada "Carro Novo"
  When o usuário cria uma nova meta de poupança chamada "Carro Novo" com valor alvo de R$ 30000.00 e prazo de 24 meses
  Then uma meta chamada "Carro Novo" deve existir no sistema

Scenario: Concluir uma meta de poupança ao fazer o último aporte
  Given que eu tenho uma meta de poupança "Playstation 6" com valor alvo de R$ 5000.00
  And o saldo atual da meta é de R$ 4900.00
  And o saldo da minha Conta principal é de R$ 200.00
  When eu faço um aporte de R$ 100.00 para a meta "Playstation 6"
  Then o saldo da meta "Playstation 6" deve ser R$ 5000.00
  And o status da meta "Playstation 6" deve ser "CONCLUIDA"

Scenario: Excluir uma meta de poupança existente
  Given que eu tenho uma meta de poupança "Casa Nova" criada no sistema
  When o usuário exclui a meta "Casa Nova"
  Then a meta "Casa Nova" não deve mais existir no sistema