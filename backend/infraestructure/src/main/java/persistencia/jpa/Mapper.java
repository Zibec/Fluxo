package persistencia.jpa;

import agendamento.Agendamento;
import agendamento.Frequencia;
import cartao.Cartao;
import cartao.CartaoId;
import cartao.CartaoNumero;
import cartao.Cvv;
import categoria.Categoria;
import conta.Conta;
import conta.ContaId;
import conta.ContaService;
import divida.Divida;
import historicoInvestimento.HistoricoInvestimento;
import investimento.Investimento;
import meta.Meta;
import metaInversa.MetaInversa;
import orcamento.Orcamento;
import orcamento.OrcamentoChave;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import patrimonio.Patrimonio;
import perfil.Perfil;
//import persistencia.jpa.agendamento.AgendamentoJpa;
import persistencia.jpa.agendamento.AgendamentoJpa;
import persistencia.jpa.cartao.CartaoJpa;
import persistencia.jpa.cartao.CartaoJpaRepository;
import persistencia.jpa.categoria.CategoriaJpa;
import persistencia.jpa.conta.ContaJpa;
import persistencia.jpa.conta.ContaJpaRepository;
import persistencia.jpa.conta.ContaRepositoryImpl;
import persistencia.jpa.divida.DividaJpa;
import persistencia.jpa.historicoInvestimento.HistoricoInvestimentoJpa;
import persistencia.jpa.investimento.InvestimentoJpa;
import persistencia.jpa.meta.MetaJpa;
import persistencia.jpa.metaInversa.MetaInversaJpa;
import persistencia.jpa.orcamento.OrcamentoJpa;
import persistencia.jpa.patrimonio.PatrimonioJpa;
import persistencia.jpa.perfil.PerfilJpa;
import persistencia.jpa.transacao.TransacaoJpa;
import persistencia.jpa.usuario.UsuarioJpa;
import transacao.FormaPagamentoId;
import transacao.Transacao;
import usuario.DataFormato;
import usuario.Email;
import usuario.Moeda;
import usuario.Usuario;

import java.security.Provider;
import java.time.YearMonth;
import java.util.ArrayList;

@Component
public class Mapper extends ModelMapper {

    @Autowired
    private CartaoJpaRepository cartaoJpaRepository;

    @Autowired
    private ContaJpaRepository contaJpaRepository;

    Mapper() {
        var config = getConfiguration();

        config.setFieldMatchingEnabled(true);
        config.setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);

        addConverter(new AbstractConverter<AgendamentoJpa, Agendamento>() {
            @Override
            protected Agendamento convert(AgendamentoJpa source) {
                return new Agendamento(source.id, source.descricao, source.valor, source.frequencia, source.proximaData, source.perfilId);
            }
        });

        addConverter(new AbstractConverter<ContaJpa, Conta>() {
            @Override
            protected Conta convert(ContaJpa source) {
                ContaId contaId = new ContaId(source.id);
                return new Conta(contaId, source.nome, source.tipo, source.banco, source.saldo);
            }
        });

        addConverter(new AbstractConverter<CartaoJpa, Cartao>() {
            @Override
            protected Cartao convert(CartaoJpa source) {
                CartaoId id = new CartaoId(source.id);
                return new Cartao(id, new CartaoNumero(source.numero), source.titular, source.validade, new Cvv(source.cvv), source.limite, source.dataFechamentoFatura, source.dataVencimentoFatura, source.saldo);
            }
        });

        addConverter(new AbstractConverter<CategoriaJpa, Categoria>() {
            @Override
            protected Categoria convert(CategoriaJpa source) {
                return new Categoria(source.id, source.nome);
            }
        });

        addConverter(new AbstractConverter<DividaJpa, Divida>() {
            @Override
            protected Divida convert(DividaJpa source) {
                return new Divida(source.id, source.nome, source.valorDevedor);
            }
        });

        addConverter(new AbstractConverter<HistoricoInvestimentoJpa, HistoricoInvestimento>() {
            @Override
            protected HistoricoInvestimento convert(HistoricoInvestimentoJpa source) {
                return new HistoricoInvestimento(source.investimentoId, source.valorAtualizado, source.data);
            }
        });

        addConverter(new AbstractConverter<InvestimentoJpa, Investimento>() {
            @Override
            protected Investimento convert(InvestimentoJpa source) {
                return new Investimento(source.id, source.nome, source.descricao, source.valorAtual);
            }
        });

        addConverter(new AbstractConverter<MetaJpa, Meta>() {
            @Override
            protected Meta convert(MetaJpa source) {
                return new Meta(source.id, source.tipo, source.descricao, source.valorAlvo, source.prazo);
            }
        });

        addConverter(new AbstractConverter<MetaInversaJpa, MetaInversa>() {
            @Override
            protected MetaInversa convert(MetaInversaJpa source) {
                return new MetaInversa(source.id, source.nome, source.valorDivida, source.contaAssociadaId,source.dataLimite, source.valorAcumulado, source.status);
            }
        });

        addConverter(new AbstractConverter<OrcamentoJpa, Orcamento>() {
            @Override
            protected Orcamento convert(OrcamentoJpa source) {
                String[] infolist = source.chave.split("-");
                OrcamentoChave chave = new OrcamentoChave(infolist[0], YearMonth.parse(infolist[1]),infolist[2]);
                return new Orcamento(chave, source.limite, source.dataLimite);
            }
        });

        addConverter(new AbstractConverter<PatrimonioJpa, Patrimonio>() {
            @Override
            protected Patrimonio convert(PatrimonioJpa source) {
                return new Patrimonio(source.id, source.data, source.valor);
            }
        });

        addConverter(new AbstractConverter<PerfilJpa, Perfil>() {
            @Override
            protected Perfil convert(PerfilJpa source) {
                return new Perfil(source.id, source.nome);
            }
        });

        addConverter(new AbstractConverter<TransacaoJpa, Transacao>() {
            @Override
            protected Transacao convert(TransacaoJpa source) {
                ContaService contaService = new ContaService(new ContaRepositoryImpl());

                FormaPagamentoId pagamentoId = null;

                if(contaService.obter(source.pagamentoId).isPresent()) {
                    pagamentoId = new ContaId(source.pagamentoId);
                } else {
                    pagamentoId = new CartaoId(source.pagamentoId);
                }
                return new Transacao(source.id, source.origemAgendamentoId, source.descricao, source.valor, source.data, source.status, source.categoriaId, pagamentoId, source.avulsa, source.tipo, source.perfilId);
            }
        });

        addConverter(new AbstractConverter<UsuarioJpa, Usuario>() {
            @Override
            protected Usuario convert(UsuarioJpa source) {
                return new Usuario(source.id, source.username, source.userEmail, source.password, DataFormato.valueOf(source.formatoDataPreferido) , Moeda.valueOf(source.moedaPreferida), source.providerId);
            }
        });

        addConverter(new AbstractConverter<String, Cvv>() {
            @Override
            protected Cvv convert(String source) {
                return new Cvv(source);
            }
        });

        addConverter(new AbstractConverter<Cvv, String>() {
            @Override
            protected String convert(Cvv source) {
                var value = source.getCodigo();
                return value;
            }
        });

        addConverter(new AbstractConverter<String, CartaoNumero>() {
            @Override
            protected CartaoNumero convert(String source) {
                return new CartaoNumero(source);
            }
        });

        addConverter(new AbstractConverter<CartaoNumero, String>() {
            @Override
            protected String convert(CartaoNumero source) {
                var value = source.toString();
                return value;
            }
        });

        addConverter(new AbstractConverter<OrcamentoChave, String>() {
            @Override
            protected String convert(OrcamentoChave source) {
                return source.getUsuarioId() + "-" + source.getAnoMes() + "-" + source.getCategoriaId();
            }
        });

        addConverter(new AbstractConverter<String, Email>() {
            @Override
            protected Email convert(String source) {
                return new Email(source);
            }
        });



    }
}
