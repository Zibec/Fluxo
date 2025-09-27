#Matias

Feature: Gestão de Categorias

  Para organizar meus gastos, eu como usuário,
  quero poder criar e gerenciar uma lista de categorias.

Scenario: Adicionar uma nova categoria que não existe
  Given que não existe uma categoria chamada "Saúde" no sistema
  When o usuário insere o nome "Saúde" e salva
  Then a categoria "Saúde" deve aparecer na lista de categorias

Scenario: Tentar adicionar uma categoria que já existe
  Given: que a categoria "Moradia" já existe na lista
  When o usuário tenta criar uma nova categoria com o nome "Moradia"
  Then o sistema deve exibir uma mensagem de erro "Categoria já existe"
  And a lista de categorias não deve ser alterada

Scenario: Deletar uma categoria que não está em uso
  Given que o usuário tem uma categoria chamada "Extra"
  And não existe nenhuma transação associada à categoria "Extra"
  When o usuário escolhe deletar a categoria "Extra"
  Then a categoria "Extra" deve ser removida da lista

Scenario: Tentar deletar uma categoria que está em uso
  Given que o usuário tem uma categoria chamada "Alimentação"
  And existe pelo menos uma transação associada à categoria "Alimentação"
  When o usuário tenta deletar a categoria "Alimentação"
  Then o sistema deve exibir uma mensagem de erro "Categoria não pode ser excluída pois está em uso"
  And a categoria "Alimentação" deve continuar na lista