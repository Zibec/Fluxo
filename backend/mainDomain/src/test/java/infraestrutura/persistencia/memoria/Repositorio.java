package infraestrutura.persistencia.memoria;

import agendamento.Agendamento;
import agendamento.AgendamentoRepositorio;
import cartao.Cartao;
import cartao.CartaoId;
import cartao.CartaoNumero;
import cartao.CartaoRepositorio;
import categoria.Categoria;
import categoria.CategoriaRepositorio;
import conta.Conta;
import conta.ContaRepositorio;
import divida.Divida;
import divida.DividaRepositorio;
import historicoInvestimento.HistoricoInvestimento;
import historicoInvestimento.HistoricoInvestimentoRepositorio;
import investimento.Investimento;
import investimento.InvestimentoRepositorio;
import meta.Meta;
import meta.MetaRepositorio;
import metaInversa.MetaInversa;
import metaInversa.MetaInversaRepositorio;
import orcamento.Orcamento;
import orcamento.OrcamentoChave;
import orcamento.OrcamentoRepositorio;
import patrimonio.Patrimonio;
import patrimonio.PatrimonioRepositorio;
import perfil.Perfil;
import perfil.PerfilRepository;
import taxaSelic.TaxaSelic;
import taxaSelic.TaxaSelicRepository;
import transacao.Transacao;
import transacao.TransacaoRepositorio;
import usuario.Usuario;
import usuario.UsuarioRepositorio;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.apache.commons.lang3.Validate.notNull;

public class Repositorio implements
        InvestimentoRepositorio,
        AgendamentoRepositorio,
        CartaoRepositorio,
        CategoriaRepositorio,
        ContaRepositorio,
        DividaRepositorio,
        HistoricoInvestimentoRepositorio,
        MetaRepositorio,
        MetaInversaRepositorio,
        OrcamentoRepositorio,
        PatrimonioRepositorio,
        PerfilRepository,
        TaxaSelicRepository,
        TransacaoRepositorio,
        UsuarioRepositorio
{

    /*-----------------------------------------------------------------------*/
    // Investimento
    /*-----------------------------------------------------------------------*/
    private ArrayList<Investimento> investimentos = new ArrayList<Investimento>();

    public void salvar(Investimento investimento){
        investimentos.add(investimento);
    }

    public Investimento obterInvestimento(String investimentoId){
        for (Investimento inv : investimentos){
            if (investimentoId.equals(inv.getId())){
                return inv;
            }
        }
        return null;
    }

    public ArrayList<Investimento> obterTodos(){
        return investimentos;
    }

    public void atualizarInvestimento(String investimentoId, Investimento investimento){

        for (Investimento inv : investimentos){
            if(investimentoId.equals(inv.getId())){
                inv = investimento;
            }
        }
    }

    public void deletarInvestimento(String investimentoId){
        for (Investimento inv : investimentos){
            if (investimentoId.equals(inv.getId())){
                investimentos.remove(inv);
            }
        }

    }

    public void limparInvestimento() {
        investimentos.clear();
    }

    /*-----------------------------------------------------------------------*/
    // Agendamento
    /*-----------------------------------------------------------------------*/

    private final Map<String, Agendamento> agendamento = new ConcurrentHashMap<>();

    public void salvar(Agendamento a) {
        agendamento.put(a.getId(), a);
    }

    public Optional<Agendamento> obterAgendamento(String id) {
        return Optional.ofNullable(agendamento.get(id));
    }

    /*-----------------------------------------------------------------------*/
    // Cartao
    /*-----------------------------------------------------------------------*/

    private Map<CartaoNumero, Cartao> cartoes = new HashMap<>();

    public void salvar(Cartao cartao) {
        notNull(cartao, "O cartão não pode ser nulo");
        cartoes.put(cartao.getNumero(), cartao);
    }

    public Cartao obterCartao(CartaoNumero numero) {
        notNull(numero, "O número do cartão não pode ser nulo");

        var cartao = cartoes.get(numero);
        return cartao;
    }

    public Cartao obterCartaoPorId(CartaoId cartaoId) {
        notNull(cartaoId, "O ID do cartão não pode ser nulo");

        for (Cartao cartao : cartoes.values()) {
            if (cartao.getId().equals(cartaoId)) {
                return cartao;
            }
        }
        return null;
    }

    /*-----------------------------------------------------------------------*/
    // Categoria
    /*-----------------------------------------------------------------------*/

    private final Map<String, Categoria> categorias = new HashMap<>();

    public void salvar(Categoria categoria) {
        notNull(categoria, "A categoria não pode ser nula.");

        //nomes duplicados
        if (obterCategoriaPorNome(categoria.getNome()).isPresent()) {
            throw new IllegalArgumentException("Categoria já existe");
        }
        categorias.put(categoria.getId(), categoria);
    }

    public Optional<Categoria> obterCategoriaPorNome(String nome) {
        notNull(nome, "O nome da categoria não pode ser nulo.");
        return categorias.values().stream()
                .filter(cat -> cat.getNome().equalsIgnoreCase(nome))
                .findFirst();
    }

    public Optional<Categoria> obterCategoria(String id) {
        notNull(id, "O ID da categoria não pode ser nulo.");
        return Optional.ofNullable(categorias.get(id));
    }

    public void deletarCategoria(String id) {
        notNull(id, "O ID da categoria não pode ser nulo.");
        categorias.remove(id);
    }

    public int contagem() {
        return categorias.size();
    }

    /*-----------------------------------------------------------------------*/
    // Conta
    /*-----------------------------------------------------------------------*/

    private final Map<String, Conta> contas = new HashMap<>();

    public void salvar(Conta conta) {
        notNull(conta, "A conta não pode ser nula");
        contas.put(conta.getId().getId(), conta);
    }

    public Optional<Conta> obterConta(String contaId) {
        notNull(contaId, "O ID da conta não pode ser nulo");
        return Optional.ofNullable(contas.get(contaId));
    }

    public boolean contaExistente(String nome) {
        notNull(nome, "O nome da conta não pode ser nulo");
        return contas.values().stream().anyMatch(c -> c.getNome().equals(nome));
    }

    public List<Conta> listarTodasContas() {
        return new ArrayList<>(contas.values());
    }

    public void limparConta() {
        contas.clear();
    }

    /*-----------------------------------------------------------------------*/
    // Divida
    /*-----------------------------------------------------------------------*/

    private final Map<String, Divida> dividas = new HashMap<>();

    public void salvar(Divida divida) {
        notNull(divida, "A dívida não pode ser nula");
        dividas.put(divida.getId(), divida);
    }

    public List<Divida> obterTodosDivida() {
        return new ArrayList<>(dividas.values());
    }

    public void limparDivida() {
        dividas.clear();
    }

    /*-----------------------------------------------------------------------*/
    // HistoricoInvestimento
    /*-----------------------------------------------------------------------*/

    private List<HistoricoInvestimento> historico = new ArrayList<>();
    private boolean status;

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void salvar(HistoricoInvestimento historicoInvestimento){
        if(status){
            historico.add(historicoInvestimento);
        }
        else{
            throw new RuntimeException("Falha ao salvar histórico");
        }

    }

    public List<HistoricoInvestimento> obterTodosHistoricos(){
        return historico;
    }

    public void deletarTodosHistoricosPorId(String investimentoId){

        if(status){
            for(HistoricoInvestimento histInv : historico){
                if(investimentoId.equals(histInv.getInvestimentoId())){
                    historico.remove(histInv);
                }
            }
        }
        else{
            throw new RuntimeException("Falha ao deletar histórico.");
        }

    }

    /*-----------------------------------------------------------------------*/
    // Meta
    /*-----------------------------------------------------------------------*/


    private final Map<String, Meta> metas = new HashMap<>();

    public void salvar(Meta meta) {
        notNull(meta, "A meta não pode ser nula");
        metas.put(meta.getId(), meta);
    }

    public Optional<Meta> obterMeta(String metaId) {
        notNull(metaId, "O ID da meta não pode ser nulo");

        // Retorna um Optional para evitar retornos nulos (NullPointerException)
        return Optional.ofNullable(metas.get(metaId));
    }

    public Optional<Meta> obterMetaPorNome(String nomeMeta) {
        notNull(nomeMeta, "O nome da meta não pode ser nulo");

        return metas.values().stream()
                .filter(meta -> meta.getDescricao().equalsIgnoreCase(nomeMeta))
                .findFirst();
    }

    public void deletarMeta(String metaId) {
        notNull(metaId, "O ID da meta não pode ser nulo");
        metas.remove(metaId);
    }

    /*-----------------------------------------------------------------------*/
    // Meta Inversa
    /*-----------------------------------------------------------------------*/


    private final Map<String, MetaInversa> metasInversas = new HashMap<>();

    public void salvarMetaInversa(MetaInversa metaInversa) {
        notNull(metaInversa, "A meta inversa não pode ser nula");
        metasInversas.put(metaInversa.getId(), metaInversa);
    }

    public Optional<MetaInversa> obterMetaInversa(String metaId) {
        notNull(metaId, "O ID da meta inversa não pode ser nulo");
        return Optional.ofNullable(metasInversas.get(metaId));
    }

    public Optional<MetaInversa> obterMetaInversaPorNome(String nomeMeta) {
        notNull(nomeMeta, "O nome da meta inversa não pode ser nulo");

        return metasInversas.values().stream()
                .filter(meta -> meta.getNome().equalsIgnoreCase(nomeMeta))
                .findFirst();
    }

    public void deletarMetaInversa(String metaId) {
        notNull(metaId, "O ID da meta inversa não pode ser nulo");
        metasInversas.remove(metaId);
    }

    public void limparMetaInversa() {
        metasInversas.clear();
    }

    /*-----------------------------------------------------------------------*/
    // Orçamento
    /*-----------------------------------------------------------------------*/

    private Map<OrcamentoChave, Orcamento> orcamentos = new HashMap<>();

    public void salvarOrcamento(OrcamentoChave chave, Orcamento orcamento) {
        notNull(chave, "A achave não pode ser nula");
        notNull(orcamento, "O orcamento não pode ser nulo");

        if(orcamentos.putIfAbsent(chave,orcamento) != null){
            throw new IllegalStateException("Já existe um orçamento para essa chave");
        }
    }

    public void atualizarOrcamento(OrcamentoChave chave, Orcamento orcamento) {
        notNull(chave, "A achave não pode ser nula");
        notNull(orcamento, "O orcamento não pode ser nulo");

        if(orcamentos.replace(chave,orcamento) == null){
            throw new IllegalStateException("Não existe um orçamento para essa chave");
        }
    }

    public Optional<Orcamento> obterOrcamento(OrcamentoChave chave) {
        notNull(chave, "A chave do orcamento não pode ser nula");
        return Optional.ofNullable((orcamentos.get(chave)));
    }

    public void limparOrcamento() {
        orcamentos.clear();
    }

    /*-----------------------------------------------------------------------*/
    // Patrimonio
    /*-----------------------------------------------------------------------*/

    private final Map<String, Patrimonio> snapshots = new HashMap<>();

    public void salvarPatrimonio(Patrimonio snapshot) {
        notNull(snapshot, "O snapshot não pode ser nulo");
        snapshots.put(snapshot.getId(), snapshot);
    }

    public List<Patrimonio> obterTodosPatrimonios() {
        return new ArrayList<>(snapshots.values());
    }

    public void limparPatrimonio() {
        snapshots.clear();
    }

    /*-----------------------------------------------------------------------*/
    // Perfil
    /*-----------------------------------------------------------------------*/

    private ArrayList<Perfil> perfis = new ArrayList<Perfil>();

    public void salvarPerfil(Perfil perfil){
        perfis.add(perfil);
    }

    public Perfil obterPerfil(String id){
        for (Perfil per : perfis){
            if (id.equals(per.getId())){
                return per;
            }
        }

        return null;
    }

    public ArrayList<Perfil> obterTodosPerfis(){
        return perfis;
    }

    public void alterarPerfil(String id,Perfil perfil){
        for (Perfil per : perfis){
            if (id.equals(per.getId())){
                per = perfil;
                return;
            }
        }

        throw new RuntimeException("Perfil não encontrado.");
    }

    public void deletarPerfil(String id){
        for (Perfil perf : perfis){
            if (id.equals(perf.getId())){
                perfis.remove(perf);
            }
        }
    }

    /*-----------------------------------------------------------------------*/
    // TaxaSelic
    /*-----------------------------------------------------------------------*/

    private TaxaSelic taxaSelic;

    public void salvar(TaxaSelic taxaSelic){
        this.taxaSelic = taxaSelic;
    }

    public TaxaSelic obterTaxaSelic(){
        return taxaSelic;
    }

    /*-----------------------------------------------------------------------*/
    // Transacao
    /*-----------------------------------------------------------------------*/

    private final Map<String, Transacao> transacao = new ConcurrentHashMap<>();

    private final Map<String, String> idxAgendamentoData = new ConcurrentHashMap<>();

    public void salvarTransacao(Transacao t) {
        if (t.getPerfilId() == null){
            throw new RuntimeException("É obrigatório a seleção de um perfil.");
        }
        transacao.put(t.getId(), t);
        if (t.getOrigemAgendamentoId() != null) {
            idxAgendamentoData.put(chave(t.getOrigemAgendamentoId(), t.getData()), t.getId());
        }
    }

    public Optional<Transacao> encontrarTransacaoPorAgendamentoEData(String agendamentoId, LocalDate data) {
        String id = idxAgendamentoData.get(chave(agendamentoId, data));
        return Optional.ofNullable(id).map(transacao::get);
    }

    public boolean existeTransacaoPorCategoriaId(String categoriaId) {
        Objects.requireNonNull(categoriaId, "ID da Categoria não pode ser nulo");
        // Percorre a lista de transações e para no primeiro que encontrar com o ID
        return transacao.values().stream()
                .anyMatch(transacao -> categoriaId.equals(transacao.getCategoriaId()));
    }

    public List<Transacao> listarTodasTransacoes() {
        return List.copyOf(transacao.values());
    }

    private static String chave(String agendamentoId, LocalDate data) {
        return agendamentoId + "#" + data;
    }

    public Optional<Transacao> buscarTransacaoPorId(String id) {
        Objects.requireNonNull(id, "ID da transação não pode ser nulo");
        return Optional.ofNullable(transacao.get(id));
    }

    public void atualizarTransacao(Transacao t) {
        salvarTransacao(t);
    }

    public void excluirTransacao(String id) {
        Objects.requireNonNull(id, "O ID da transação não pode ser nulo");

        Transacao removida = transacao.remove(id);
        if (removida == null) {
            throw new NoSuchElementException("Transação com ID " + id + " não encontrada para exclusão.");
        }

        // Remove também do índice auxiliar, se existir
        if (removida.getOrigemAgendamentoId() != null) {
            idxAgendamentoData.remove(chave(removida.getOrigemAgendamentoId(), removida.getData()));
        }

    }

    public Optional<Transacao> obterTransacaoPorId(String id) {
        Objects.requireNonNull(id, "O ID da transação não pode ser nulo");
        // 'transacao' é o nome do seu Map principal, então usamos ele para buscar pelo ID.
        return Optional.ofNullable(transacao.get(id));
    }

    public void limparTransacao() {
        transacao.clear();
        idxAgendamentoData.clear();
    }

    /*-----------------------------------------------------------------------*/
    // Usuario
    /*-----------------------------------------------------------------------*/


    private final Map<String, Usuario> usuarios = new HashMap<>();

    public void salvarUsuario(Usuario usuario) {
        notNull(usuario, "O usuario não pode ser nulo");
        usuarios.put(usuario.getId(), usuario);
    }

    public void deletarUsuario(String id) {
        notNull(id, "O ID do Usuario não pode ser nulo");
        usuarios.remove(id);
    }

    public Optional<Usuario> obterUsuario(String contaId) {
        notNull(contaId, "O ID do Usuario não pode ser nulo");
        return Optional.ofNullable(usuarios.get(contaId));
    }

    public boolean emailExistente(String email) {
        notNull(email, "O email não pode ser nulo");
        return usuarios.values().stream().anyMatch(u -> u.getEmail().getEndereco().equals(email));
    }

    public boolean usernameExistente(String username) {
        notNull(username, "O username não pode ser nulo");
        return usuarios.values().stream().anyMatch(u -> u.getUsername().equals(username));
    }

}
