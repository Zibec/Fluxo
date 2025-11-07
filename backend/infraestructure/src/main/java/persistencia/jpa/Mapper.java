package persistencia.jpa;

import agendamento.Agendamento;
import agendamento.Frequencia;
import cartao.*;
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
import persistencia.jpa.agendamento.AgendamentoJpa;
import persistencia.jpa.cartao.CartaoJpa;
import persistencia.jpa.cartao.CartaoJpaRepository;
import persistencia.jpa.cartao.CartaoRepositoryImpl;
import persistencia.jpa.categoria.CategoriaJpa;
import persistencia.jpa.conta.ContaJpa;
import persistencia.jpa.conta.ContaJpaRepository;
import persistencia.jpa.conta.ContaRepositoryImpl;
import persistencia.jpa.divida.DividaJpa;
import persistencia.jpa.fatura.FaturaJpa;
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

import java.time.YearMonth;

@Component
public class Mapper extends ModelMapper {

    @Autowired
    private CartaoJpaRepository cartaoJpaRepository;

    @Autowired
    private ContaJpaRepository contaJpaRepository;

    public Mapper() {
        var config = getConfiguration();
        config.setFieldMatchingEnabled(true);
        config.setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);

        // ====== JPA -> DOMÍNIO ======
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
                return new Conta(contaId, source.nome, source.tipo, source.banco, source.saldo, source.usuarioId);
            }
        });

        addConverter(new AbstractConverter<CartaoJpa, Cartao>() {
            @Override
            protected Cartao convert(CartaoJpa source) {
                CartaoId id = new CartaoId(source.id);
                return new Cartao(id, new CartaoNumero(source.numero), source.titular, source.validade, new Cvv(source.cvv),
                        source.limite, source.dataFechamentoFatura, source.dataVencimentoFatura, source.saldo, source.usuarioId);
            }
        });

        addConverter(new AbstractConverter<FaturaJpa, Fatura>() {
            @Override
            protected Fatura convert(FaturaJpa source) {
                var cartao = new CartaoRepositoryImpl().obterCartaoPorId(new CartaoId(source.cartaoId));
                return new Fatura(cartao, source.dataVencimento);
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
                return new Meta(source.id, source.tipo, source.descricao, source.valorAlvo, source.saldoAcumulado, source.prazo);
            }
        });

        addConverter(new AbstractConverter<MetaInversaJpa, MetaInversa>() {
            @Override
            protected MetaInversa convert(MetaInversaJpa source) {
                return new MetaInversa(source.id, source.nome, source.valorDivida, source.contaAssociadaId,
                        source.dataLimite, source.valorAcumulado, source.status);
            }
        });

        addConverter(new AbstractConverter<OrcamentoJpa, Orcamento>() {
            @Override
            protected Orcamento convert(OrcamentoJpa source) {
                String[] info = source.chave.split("-");
                OrcamentoChave chave = new OrcamentoChave(info[0], YearMonth.parse(info[1]), info[2]);
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
                FormaPagamentoId pagamentoId = contaService.obter(source.pagamentoId).isPresent()
                        ? new ContaId(source.pagamentoId)
                        : new CartaoId(source.pagamentoId);
                return new Transacao(source.id, source.origemAgendamentoId, source.descricao, source.valor,
                        source.data, source.status, source.categoriaId, pagamentoId, source.avulsa, source.tipo, source.perfilId);
            }
        });

        addConverter(new AbstractConverter<UsuarioJpa, Usuario>() {
            @Override
            protected Usuario convert(UsuarioJpa source) {
                return new Usuario(source.id, source.username, source.userEmail,
                        source.password, DataFormato.valueOf(source.formatoDataPreferido),
                        Moeda.valueOf(source.moedaPreferida));
            }
        });

        // ====== DOMÍNIO -> JPA ======

        addConverter(new AbstractConverter<Agendamento, AgendamentoJpa>() {
            @Override
            protected AgendamentoJpa convert(Agendamento source) {
                var jpa = new AgendamentoJpa();
                jpa.id = source.getId();
                jpa.descricao = source.getDescricao();
                jpa.valor = source.getValor();
                jpa.frequencia = source.getFrequencia();
                jpa.proximaData = source.getProximaData();
                jpa.perfilId = source.getPerfilId();
                return jpa;
            }
        });

        addConverter(new AbstractConverter<Conta, ContaJpa>() {
            @Override
            protected ContaJpa convert(Conta source) {
                var jpa = new ContaJpa();
                jpa.id = source.getId().getId();
                jpa.nome = source.getNome();
                jpa.tipo = source.getTipo();
                jpa.banco = source.getBanco();
                jpa.saldo = source.getSaldo();
                jpa.usuarioId = source.getUsuarioId();
                return jpa;
            }
        });

        addConverter(new AbstractConverter<Cartao, CartaoJpa>() {
            @Override
            protected CartaoJpa convert(Cartao source) {
                CartaoJpa jpa = new CartaoJpa();
                jpa.id = source.getId().getId();
                jpa.numero = source.getNumero().getCodigo();
                jpa.titular = source.getTitular();
                jpa.validade = source.getValidade();
                jpa.cvv = source.getCvv().getCodigo();
                jpa.limite = source.getLimite();
                jpa.dataFechamentoFatura = source.getDataFechamentoFatura();
                jpa.dataVencimentoFatura = source.getDataVencimentoFatura();
                jpa.saldo = source.getSaldo();
                jpa.usuarioId = source.getUsuarioId();
                return jpa;
            }
        });

        addConverter(new AbstractConverter<Categoria, CategoriaJpa>() {
            @Override
            protected CategoriaJpa convert(Categoria source) {
                var jpa = new CategoriaJpa();
                jpa.id = source.getId();
                jpa.nome = source.getNome();
                return jpa;
            }
        });

        addConverter(new AbstractConverter<Divida, DividaJpa>() {
            @Override
            protected DividaJpa convert(Divida source) {
                var jpa = new DividaJpa();
                jpa.id = source.getId();
                jpa.nome = source.getNome();
                jpa.valorDevedor = source.getValorDevedor();
                return jpa;
            }
        });

        addConverter(new AbstractConverter<Investimento, InvestimentoJpa>() {
            @Override
            protected InvestimentoJpa convert(Investimento source) {
                var jpa = new InvestimentoJpa();
                jpa.id = source.getId();
                jpa.nome = source.getNome();
                jpa.descricao = source.getDescricao();
                jpa.valorAtual = source.getValorAtual();
                return jpa;
            }
        });

        addConverter(new AbstractConverter<Meta, MetaJpa>() {
            @Override
            protected MetaJpa convert(Meta source) {
                var jpa = new MetaJpa();
                jpa.id = source.getId();
                jpa.tipo = source.getTipo();
                jpa.descricao = source.getDescricao();
                jpa.valorAlvo = source.getValorAlvo();
                jpa.saldoAcumulado = source.getSaldoAcumulado();
                jpa.prazo = source.getPrazo();
                return jpa;
            }
        });

        addConverter(new AbstractConverter<MetaInversa, MetaInversaJpa>() {
            @Override
            protected MetaInversaJpa convert(MetaInversa source) {
                var jpa = new MetaInversaJpa();
                jpa.id = source.getId();
                jpa.nome = source.getNome();
                jpa.valorDivida = source.getValorDivida();
                jpa.contaAssociadaId = source.getContaAssociadaId();
                jpa.dataLimite = source.getDataLimite();
                jpa.valorAcumulado = source.getValorAmortizado();
                jpa.status = source.getStatus();
                return jpa;
            }
        });

        addConverter(new AbstractConverter<Patrimonio, PatrimonioJpa>() {
            @Override
            protected PatrimonioJpa convert(Patrimonio source) {
                var jpa = new PatrimonioJpa();
                jpa.id = source.getId();
                jpa.data = source.getData();
                jpa.valor = source.getValor();
                return jpa;
            }
        });

        addConverter(new AbstractConverter<Perfil, PerfilJpa>() {
            @Override
            protected PerfilJpa convert(Perfil source) {
                var jpa = new PerfilJpa();
                jpa.id = source.getId();
                jpa.nome = source.getNome();
                return jpa;
            }
        });

        addConverter(new AbstractConverter<Usuario, UsuarioJpa>() {
            @Override
            protected UsuarioJpa convert(Usuario source) {
                var jpa = new UsuarioJpa();
                jpa.id = source.getId();
                jpa.username = source.getUsername();
                jpa.userEmail = source.getEmail().getEndereco();
                jpa.password = source.getPassword();
                jpa.formatoDataPreferido = source.getFormatoDataPreferido().name();
                jpa.moedaPreferida = source.getMoedaPreferida().name();
                return jpa;
            }
        });
    }
}
