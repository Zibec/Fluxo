# Padrões de Projeto

 ### Observer:

    Classes modificadas: 
        - TaxaSelicService 
        - InvestimentoService

    Classes criadas: 
        - Observer (Interface)

### Iterator:

    Classes Modificadas:
        - AgendamentoController
        - AgendamentoImpl
        - AgendamentoRepositorio
        - AgendamentoService

    Classes criadas:
        - PagedAgendamentosIterator
        - agendamentoIterable

### Proxy:
   
   Classes Modificadas:
   
        - FluxoApplication 
        
            - Ajuste na configuração de Beans para que o Spring injete um CategoriaRepositorioProxy no lugar do repositório real.

            - O CategoriaRepository continua sendo instanciado, mas agora é encapsulado pelo Proxy, que intercepta as chamadas antes de delegá-las ao repositório real.

    Classes criadas:
        - CategoriaRepositorioProxy
