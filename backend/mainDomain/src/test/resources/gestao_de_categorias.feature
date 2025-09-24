#language: pt

Funcionalidade: Gestão de Categorias

  Para organizar meus gastos, eu como usuário,
  quero poder criar e gerenciar uma lista de categorias.

Cenário: Adicionar uma nova categoria com sucesso
  Dado que o usuário está na tela de gerenciamento de categorias
  Quando o usuário insere o nome "Lazer" e salva
  Então a categoria "Lazer" deve aparecer na lista de categorias

Cenário: Tentar adicionar uma categoria que já existe
  Dado que a categoria "Moradia" já existe na lista
  Quando o usuário tenta criar uma nova categoria com o nome "Moradia"
  Então o sistema deve exibir uma mensagem de erro "Categoria já existe"
  E a lista de categorias não deve ser alterada

Cenário: Deletar uma categoria que não está em uso
  Dado que o usuário tem uma categoria chamada "Extra"
  E não existe nenhuma transação associada à categoria "Extra"
  Quando o usuário escolhe deletar a categoria "Extra"
  Então a categoria "Extra" deve ser removida da lista

Cenário: Tentar deletar uma categoria que está em uso
  Dado que o usuário tem uma categoria chamada "Alimentação"
  E existe pelo menos uma transação associada à categoria "Alimentação"
  Quando o usuário tenta deletar a categoria "Alimentação"
  Então o sistema deve exibir uma mensagem de erro "Categoria não pode ser excluída pois está em uso"
  E a categoria "Alimentação" deve continuar na lista