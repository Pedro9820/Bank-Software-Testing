package br.ufrpe.poo.banco.negocio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import br.ufrpe.poo.banco.exceptions.AtualizacaoNaoRealizadaException;
import br.ufrpe.poo.banco.exceptions.ClienteJaCadastradoException;
import br.ufrpe.poo.banco.exceptions.ClienteJaPossuiContaException;
import br.ufrpe.poo.banco.exceptions.ClienteNaoCadastradoException;
import br.ufrpe.poo.banco.exceptions.ClienteNaoPossuiContaException;
import br.ufrpe.poo.banco.exceptions.ContaJaAssociadaException;
import br.ufrpe.poo.banco.exceptions.ContaJaCadastradaException;
import br.ufrpe.poo.banco.exceptions.ContaNaoEncontradaException;
import br.ufrpe.poo.banco.exceptions.InicializacaoSistemaException;
import br.ufrpe.poo.banco.exceptions.RenderBonusContaEspecialException;
import br.ufrpe.poo.banco.exceptions.RenderJurosPoupancaException;
import br.ufrpe.poo.banco.exceptions.RepositorioException;
import br.ufrpe.poo.banco.exceptions.SaldoInsuficienteException;
import br.ufrpe.poo.banco.exceptions.ValorInvalidoException;

public class TesteBanco {

	private static Banco banco;
	private static String NOME = "Pedro";
	private static String CPF = "123456778911";

	@Before
	public void apagarArquivos() throws IOException, RepositorioException,
			InicializacaoSistemaException {
		
		BufferedWriter bw = new BufferedWriter(new FileWriter("clientes.dat"));
		bw.close();
		bw = new BufferedWriter(new FileWriter("contas.dat"));
		bw.close();
		
		Banco.instance = null;
		banco = Banco.getInstance();
	}

	/**
	 * Verifica o cadastramento de uma nova conta.
	 * 
	 */
	@Test
	public void contaCadastradaPersistidaCorretamente() throws RepositorioException,
			ContaJaCadastradaException {

		ContaAbstrata conta1 = new Conta("1", 100);
		banco.cadastrar(conta1);
		ContaAbstrata conta2 = banco.procurarConta("1");
		assertEquals(conta1.getNumero(), conta2.getNumero());
		assertEquals(conta1.getSaldo(), conta2.getSaldo(), 0);
	}

	/**
	 * Verifica que nao e permitido cadastrar duas contas com o mesmo numero.
	 * 
	 */
	@Test(expected = ContaJaCadastradaException.class)
	public void tentativaCadastrarContaJaExistente() throws RepositorioException,
			ContaJaCadastradaException {

		Conta c1 = new Conta("1", 200);
		Conta c2 = new Conta("1", 300);
		banco.cadastrar(c1);
		banco.cadastrar(c2);
	}

	/**
	 * Verifica se o credito esta sendo executado corretamente em uma conta
	 * corrente.
	 * 
	 */
	@Test
	public void testeCreditarSaldoSuficiente() throws RepositorioException,
			ContaJaCadastradaException, ValorInvalidoException {

		ContaAbstrata conta = new Conta("1", 100);
		banco.cadastrar(conta);
		banco.creditar(conta, 100);
		conta = banco.procurarConta("1");
		assertEquals(200, conta.getSaldo(), 0);
	}

	/**
	 * Verifica que a operacao de debito em conta corrente esta acontecendo
	 * corretamente.
	 * 
	 */
	@Test
	public void testeDebitarSaldoSuficiente() throws RepositorioException,
			SaldoInsuficienteException, 
			ContaJaCadastradaException, ValorInvalidoException {

		ContaAbstrata conta = new Conta("1", 50);
		banco.cadastrar(conta);
		banco.debitar(conta, 50);
		conta = banco.procurarConta("1");
		assertEquals(0, conta.getSaldo(), 0);
	}
	
	/**
	 * Verifica que a operacao de debito em conta corrente esta acontecendo
	 * corretamente.
	 * @author Pedro
	 */
	@Test(expected = ValorInvalidoException.class)
	public void debitar_ValorInvalido_LevantarExcessao() throws RepositorioException,
			SaldoInsuficienteException, 
			ContaJaCadastradaException, ValorInvalidoException {

		ContaAbstrata conta = new Conta("1", 50);
		banco.cadastrar(conta);
		banco.debitar(conta, -50);
	}
	
	

	/**
	 * Verifica que a transferencia entre contas correntes e realizada com
	 * sucesso.
	 * 
	 */
	@Test
	public void transferirEntreContasCorrentesSaldoSuficiente() throws RepositorioException,
			SaldoInsuficienteException, ContaJaCadastradaException, ValorInvalidoException {

		ContaAbstrata conta1 = new Conta("1", 100);
		ContaAbstrata conta2 = new Conta("2", 200);
		banco.cadastrar(conta1);
		banco.cadastrar(conta2);
		banco.transferir(conta1, conta2, 50);
		conta1 = banco.procurarConta("1");
		conta2 = banco.procurarConta("2");
		assertEquals(50, conta1.getSaldo(), 0);
		assertEquals(250, conta2.getSaldo(), 0);
	}

	/**
	 * Verifica que render juros de uma conta poupanca funciona corretamente
	 * 
	 */
	@Test
	public void renderJurosContaPoupanca() throws RepositorioException,
	ContaJaCadastradaException {

		Poupanca poupanca = new Poupanca("20", 100);
		banco.cadastrar(poupanca);
		double saldoSemJuros = poupanca.getSaldo();
		double saldoComJuros = saldoSemJuros + (saldoSemJuros * 0.008);
		poupanca.renderJuros(0.008);
		assertEquals(saldoComJuros, poupanca.getSaldo(), 0);
	}
	
	/**
	 * Cadastrar um cliente, uma conta e o associa a ela.
	 * @throws ClienteNaoCadastradoException
	 * @throws ContaNaoEncontradaException
	 * @throws ClienteJaPossuiContaException
	 * @throws ContaJaAssociadaException
	 * @throws RepositorioException
	 * @throws ContaJaCadastradaException
	 * @throws ClienteJaCadastradoException
	 * @author Pedro
	 */
	@Test
	public void associarConta_EntradasValidas_AssociacaoComSucesso() throws ClienteNaoCadastradoException, ContaNaoEncontradaException, ClienteJaPossuiContaException, ContaJaAssociadaException, RepositorioException, ContaJaCadastradaException, ClienteJaCadastradoException {
		Conta c = new Conta("2", 100);
		banco.cadastrar(c);
		String cpf = "12345678911";
		Cliente cliente = new Cliente("Pedro", cpf);
		banco.cadastrarCliente(cliente);
		banco.associarConta(cliente.getCpf(), c.getNumero());;
		assertTrue("A associação de contas não esta funcionando corretamente",banco.procurarCliente(cpf).getContas().contains(c.getNumero()));
	}
	
	/**
	 * Cadastrar um cliente e tenta associar uma conta que nao existe.
	 * @throws ClienteNaoCadastradoException
	 * @throws ContaNaoEncontradaException
	 * @throws ClienteJaPossuiContaException
	 * @throws ContaJaAssociadaException
	 * @throws RepositorioException
	 * @throws ContaJaCadastradaException
	 * @throws ClienteJaCadastradoException
	 * @author Pedro
	 */
	@Test(expected = ContaNaoEncontradaException.class)
	public void asssociarConta_contaAssociadaInexsistente_LevantarExceçao() throws ClienteNaoCadastradoException, ContaNaoEncontradaException, ClienteJaPossuiContaException, ContaJaAssociadaException, RepositorioException, ContaJaCadastradaException, ClienteJaCadastradoException {
		String cpf = "12345678911";
		Cliente cliente = new Cliente("Pedro", cpf);
		banco.cadastrarCliente(cliente);
		banco.associarConta(cliente.getCpf(), "");
	}
	
	/**
	 * Cadastra uma conta e em seguida tenta associar ela a um cliente que não existe.
	 * @throws ClienteNaoCadastradoException
	 * @throws ContaNaoEncontradaException
	 * @throws ClienteJaPossuiContaException
	 * @throws ContaJaAssociadaException
	 * @throws RepositorioException
	 * @throws ContaJaCadastradaException
	 * @throws ClienteJaCadastradoException
	 * @author Pedro
	 */
	@Test(expected = ClienteNaoCadastradoException.class)
	public void associarConta_ClienteInexistente_LevantamentoExceçao() throws ClienteNaoCadastradoException, ContaNaoEncontradaException, ClienteJaPossuiContaException, ContaJaAssociadaException, RepositorioException, ContaJaCadastradaException, ClienteJaCadastradoException {
		Conta c = new Conta("2", 100);
		banco.cadastrar(c);
		banco.associarConta("999", c.getNumero());
	}
	
	/**
	 * Cadastra 2 clientes e 1 uma conta, e tenta associar ela a ambos.
	 * @throws ClienteNaoCadastradoException
	 * @throws ContaNaoEncontradaException
	 * @throws ClienteJaPossuiContaException
	 * @throws ContaJaAssociadaException
	 * @throws RepositorioException
	 * @throws ContaJaCadastradaException
	 * @throws ClienteJaCadastradoException
	 * @author Pedro
	 */
	@Test(expected = ContaJaAssociadaException.class)
	public void associarConta_contaAssociadaMultiplosClientes_LevanamentoExceçao() throws ClienteNaoCadastradoException, ContaNaoEncontradaException, ClienteJaPossuiContaException, ContaJaAssociadaException, RepositorioException, ContaJaCadastradaException, ClienteJaCadastradoException {
		Conta c = new Conta("2", 100);
		banco.cadastrar(c);
		
		Cliente cliente = new Cliente("Pedro", "12345678911");
		banco.cadastrarCliente(cliente);
		
		
		Cliente cliente2 = new Cliente("Paulo", "12345678910");
		banco.cadastrarCliente(cliente2);
		
		
		banco.associarConta(cliente.getCpf(), c.getNumero());
		banco.associarConta(cliente2.getCpf(), c.getNumero());
		
	}
	
	/**
	 * Cadastra um usuário e o remove do sistema.
	 * @throws RepositorioException
	 * @throws ClienteJaCadastradoException
	 * @throws ClienteNaoCadastradoException
	 * @throws ContaNaoEncontradaException
	 * @throws ClienteNaoPossuiContaException
	 * @author Pedro
	 */
	
	@Test
	public void removerCliente_ExemplosValidos_RemocaoComSucesso() throws RepositorioException, ClienteJaCadastradoException, ClienteNaoCadastradoException, ContaNaoEncontradaException, ClienteNaoPossuiContaException {
		Cliente c = new Cliente(NOME, CPF);
		banco.cadastrarCliente(c);
		banco.removerCliente(c.getCpf());
		
		assertNull("A remoção de clientes não está funcionando corretamente.",banco.procurarCliente(c.getCpf()));
	}
	
	/**
	 * Cadastra um usuário e o remove do sistema.
	 * @throws RepositorioException
	 * @throws ClienteJaCadastradoException
	 * @throws ClienteNaoCadastradoException
	 * @throws ContaNaoEncontradaException
	 * @throws ClienteNaoPossuiContaException
	 * @author Pedro
	 */
	
	@Test(expected = ClienteNaoCadastradoException.class)
	public void removerCliente_ClienteInexistente_LevantamentoExcecao() throws RepositorioException, ClienteJaCadastradoException, ClienteNaoCadastradoException, ContaNaoEncontradaException, ClienteNaoPossuiContaException {
		banco.removerCliente(CPF);
		
	}
	
	/**
	 * Cadastra um usuário e o remove do sistema.
	 * @throws RepositorioException
	 * @throws ClienteJaCadastradoException
	 * @throws ClienteNaoCadastradoException
	 * @throws ContaNaoEncontradaException
	 * @throws ClienteNaoPossuiContaException
	 * @author Pedro
	 * @throws ContaJaAssociadaException 
	 * @throws ClienteJaPossuiContaException 
	 * @throws ContaJaCadastradaException 
	 */
	@Ignore("Falha conhecida registrada na Issue #1")
	@Test
	public void removerCliente_ClienteMultiplasContas_RemocaoContasSucesso() throws RepositorioException, ClienteJaCadastradoException, ClienteNaoCadastradoException, ContaNaoEncontradaException, ClienteNaoPossuiContaException, ClienteJaPossuiContaException, ContaJaAssociadaException, ContaJaCadastradaException {
		Cliente c = new Cliente(NOME, CPF);
		banco.cadastrarCliente(c);
		
		Conta c1 = new Conta("1", 100);
		Conta c2 = new Conta("2", 200);
		
		banco.cadastrar(c1);
		banco.cadastrar(c2);
		
		banco.associarConta(c.getCpf(), c1.getNumero());
		banco.associarConta(c.getCpf(), c2.getNumero());
		
		banco.removerCliente(c.getCpf());
		
		assertNull("O cliente deve ser removido",banco.procurarCliente(c.getCpf()));
		assertNull("A conta C1 deve ser removida.",banco.procurarConta(c1.getNumero()));
		assertNull("A conta C2 deve ser removida",banco.procurarConta(c2.getNumero()));
			
		
	}
	/**
	 *  Cria um cliente e uma conta, associa os dois e depois faz a remoção da conta
	 * @throws RepositorioException
	 * @throws ContaNaoEncontradaException
	 * @throws ClienteNaoPossuiContaException
	 * @throws ClienteNaoCadastradoException
	 * @throws ContaJaCadastradaException
	 * @throws ClienteJaPossuiContaException
	 * @throws ContaJaAssociadaException
	 * @throws ClienteJaCadastradoException
	 * @author Pedro
	 */
	@Test
	public void removerConta_ExemplosValidos_RemocaoComSucesso() throws RepositorioException, ContaNaoEncontradaException, ClienteNaoPossuiContaException, ClienteNaoCadastradoException, ContaJaCadastradaException, ClienteJaPossuiContaException, ContaJaAssociadaException, ClienteJaCadastradoException {
		Conta conta = new Conta("1", 100);
		Cliente c = new Cliente(NOME, CPF);
		
		banco.cadastrarCliente(c);
		banco.cadastrar(conta);
		banco.associarConta(c.getCpf(), conta.getNumero());
		
		banco.removerConta(c, conta.getNumero());
		
		assertNull("A conta deve ser removida", banco.procurarConta(conta.getNumero()));
		assertFalse("A conta removida não deve mais ser associada ao cliente", banco.procurarCliente(c.getCpf()).getContas().contains(conta.getNumero()));
	}
	
	/**
	 * Cria um cliente e tenta remover uma conta ao qual ele não possui
	 * @throws RepositorioException
	 * @throws ContaNaoEncontradaException
	 * @throws ClienteNaoPossuiContaException
	 * @throws ClienteNaoCadastradoException
	 * @throws ContaJaCadastradaException
	 * @throws ClienteJaPossuiContaException
	 * @throws ContaJaAssociadaException
	 * @throws ClienteJaCadastradoException
	 * @author Pedro
	 */
	@Test(expected = ContaNaoEncontradaException.class)
	public void removerConta_ContaInexistente_LevantamentoExcecao() throws RepositorioException, ContaNaoEncontradaException, ClienteNaoPossuiContaException, ClienteNaoCadastradoException, ContaJaCadastradaException, ClienteJaPossuiContaException, ContaJaAssociadaException, ClienteJaCadastradoException {
		Cliente c = new Cliente(NOME, CPF);
		banco.cadastrarCliente(c);
		
		banco.removerConta(c, "123");
	}
	
	/**
	 * Cria um cliente e tenta remover uma conta ao qual ele não possui
	 * @throws RepositorioException
	 * @throws ContaNaoEncontradaException
	 * @throws ClienteNaoPossuiContaException
	 * @throws ClienteNaoCadastradoException
	 * @throws ContaJaCadastradaException
	 * @throws ClienteJaPossuiContaException
	 * @throws ContaJaAssociadaException
	 * @throws ClienteJaCadastradoException
	 * @author Pedro
	 */
	@Test(expected = ClienteNaoPossuiContaException.class)
	public void removerConta_ContaAssociadaOutroCliente_LevantamentoExcecao() throws RepositorioException, ContaNaoEncontradaException, ClienteNaoPossuiContaException, ClienteNaoCadastradoException, ContaJaCadastradaException, ClienteJaPossuiContaException, ContaJaAssociadaException, ClienteJaCadastradoException {
		Cliente c1 = new Cliente(NOME, CPF);
		Cliente c2 = new Cliente("cliente2", "12345678910");
		Conta conta = new Conta("1", 100);
		banco.cadastrar(conta);
		
		banco.cadastrarCliente(c1);
		banco.cadastrarCliente(c2);
		
		banco.associarConta(c1.getCpf(), conta.getNumero());
		
		banco.removerConta(c2, conta.getNumero());
	}
	
	/**
	 * Cria um cliente e tenta remover uma conta de um cliente inexistente.
	 * @throws RepositorioException
	 * @throws ContaNaoEncontradaException
	 * @throws ClienteNaoPossuiContaException
	 * @throws ClienteNaoCadastradoException
	 * @throws ContaJaCadastradaException
	 * @throws ClienteJaPossuiContaException
	 * @throws ContaJaAssociadaException
	 * @throws ClienteJaCadastradoException
	 * @author Pedro
	 */
	@Test(expected = ClienteNaoCadastradoException.class)
	public void removerConta_ClienteInexsitente_LevantamentoExcecao() throws RepositorioException, ContaNaoEncontradaException, ClienteNaoPossuiContaException, ClienteNaoCadastradoException, ContaJaCadastradaException, ClienteJaPossuiContaException, ContaJaAssociadaException, ClienteJaCadastradoException {
		Cliente c1 = new Cliente(NOME, CPF);
		Conta conta = new Conta("1", 100);
		banco.cadastrar(conta);
		
		banco.removerConta(c1, conta.getNumero());
	}
	
	/**
	 * Cria uma conta especial, credita um valor e calcula o bonus gerado.
	 * @throws RepositorioException
	 * @throws ContaJaCadastradaException
	 * @throws RenderBonusContaEspecialException
	 * @throws ContaNaoEncontradaException
	 * @author Pedro
	 */
	@Test
	public void renderBonus_ExemplosValidos_Sucesso() throws RepositorioException, ContaJaCadastradaException, RenderBonusContaEspecialException, ContaNaoEncontradaException {
		ContaEspecial conta = new ContaEspecial("1", 100);
		conta.creditar(100);
		
		banco.cadastrar(conta);
		
		banco.renderBonus(conta);
		
		assertEquals("Bonus não foi calculado corretamente", 201, banco.procurarConta(conta.getNumero()).getSaldo(), 0.1);
		
	}
	
	/**
	 * Verifica se é possivel render bonus em uma conta não especial
	 * @throws RepositorioException
	 * @throws ContaJaCadastradaException
	 * @throws RenderBonusContaEspecialException
	 * @throws ContaNaoEncontradaException
	 * @author Pedro
	 */
	@Test(expected = RenderBonusContaEspecialException.class)
	public void renderBonus_ContaComum_LevantamentoExcecao() throws RepositorioException, ContaJaCadastradaException, RenderBonusContaEspecialException, ContaNaoEncontradaException {
		Conta conta = new Conta("1", 100);
		
		banco.cadastrar(conta);
		banco.renderBonus(conta);
		
		
	}
	
	@Test(expected = ContaNaoEncontradaException.class)
	public void renderBonus_ContaInexsitente_LevantamentoExcecao() throws RenderBonusContaEspecialException, RepositorioException, ContaNaoEncontradaException {
		ContaEspecial conta = new ContaEspecial("1", 100);
		
		banco.renderBonus(conta);
		
	}
	
	/**
	 * Cria uma conta poupança e rende juros
	 * @throws RepositorioException
	 * @throws ContaJaCadastradaException
	 * @throws RenderBonusContaEspecialException
	 * @throws ContaNaoEncontradaException 
	 * @throws RenderJurosPoupancaException 
	 * @author Pedro
	 *
	 */
	@Test
	public void renderJuros_ExemplosValidos_Sucesso() throws RepositorioException, ContaJaCadastradaException, ContaNaoEncontradaException, RenderJurosPoupancaException {
		Poupanca conta = new Poupanca("1", 100);
		
		banco.cadastrar(conta);
		
		banco.renderJuros(conta);
		
		assertEquals("Bonus não foi calculado corretamente", 150, banco.procurarConta(conta.getNumero()).getSaldo(), 0.1);
		
	}
	
	/**
	 * Verifica se é possivel render juros em uma conta nao poupança
	 * @throws RepositorioException
	 * @throws ContaJaCadastradaException
	 * @throws RenderJurosPoupancaException 
	 * @throws ContaNaoEncontradaException
	 * @author Pedro
	 * 
	 */
	@Test(expected = RenderJurosPoupancaException.class)
	public void renderJuros_ContaComum_LevantamentoExcecao() throws RepositorioException, ContaJaCadastradaException, ContaNaoEncontradaException, RenderJurosPoupancaException {
		Conta conta = new Conta("1", 100);
		
		banco.cadastrar(conta);
		banco.renderJuros(conta);
		
		
	}
	/**
	 * Tenta render juros em uma conta que não existe
	 * @throws RepositorioException
	 * @throws ContaNaoEncontradaException
	 * @throws RenderJurosPoupancaException
	 * @author Pedro
	 */
	
	@Test(expected = ContaNaoEncontradaException.class)
	public void renderJuros_ContaInexistente_LevanemntoExcecao() throws RepositorioException, ContaNaoEncontradaException, RenderJurosPoupancaException {
		Poupanca conta = new Poupanca("1", 100);
		
		banco.renderJuros(conta);
		
	}
	
	/**
	 * Utiliza o banco para creditar o valor em uma conta
	 * @throws RepositorioException
	 * @throws ValorInvalidoException
	 * @throws ContaJaCadastradaException 
	 * @author Pedro
	 */
	@Test
	public void creditar_ExemplosValidos_Sucesso() throws RepositorioException, ValorInvalidoException, ContaJaCadastradaException {
		Conta c = new Conta("1", 100);
		banco.cadastrar(c);
		banco.creditar(c, 10);
		
		assertEquals("O creditamento não está calculando corretamente", 110, banco.procurarConta(c.getNumero()).getSaldo(), 0.1);
	}
	
	/**
	 * Verifica o crédito de um valor negativo através de um banco
	 * @throws RepositorioException
	 * @throws ValorInvalidoException
	 * @author Pedro
	 */
	@Test(expected = ValorInvalidoException.class)
	public void creditar_ValorNegativo_LevantamentoExcecao() throws RepositorioException, ValorInvalidoException {
		Conta c = new Conta("1", 100);
		banco.creditar(c, -10);
		
	}
	
	/**
	 * Verifica o crédito de um valor nulo através de um banco
	 * @throws RepositorioException
	 * @throws ValorInvalidoException
	 * @throws ContaJaCadastradaException 
	 * @author Pedro
	 */
	@Test
	public void creditar_ValorNulo_Sucesso() throws RepositorioException, ValorInvalidoException, ContaJaCadastradaException {
		Conta c = new Conta("1", 100);
		banco.cadastrar(c);
		banco.creditar(c, 0);
		
		assertEquals("O creditamento não está calculando corretamente", 100, banco.procurarConta(c.getNumero()).getSaldo(), 0.1);
		
	}
	
	/**
	 * 
	 * @throws RepositorioException
	 * @throws ClienteJaCadastradoException
	 * @throws AtualizacaoNaoRealizadaException
	 * @author PedroPC
	 */
	@Test
	public void atualizarCliente_ExemplosValidos_Sucesso() throws RepositorioException, ClienteJaCadastradoException, AtualizacaoNaoRealizadaException {
		Cliente c = new Cliente("Pedro", "12345678911");
		banco.cadastrarCliente(c);
		
		c = banco.procurarCliente(c.getCpf());
		c.setNome("Paulo");
		
		banco.atualizarCliente(c);
		assertEquals("Atualização de dados do cliente não funciona corretamente", "Paulo", banco.procurarCliente(c.getCpf()).getNome());
		
	}
	
	/**
	 * 
	 * @throws RepositorioException
	 * @throws ClienteJaCadastradoException
	 * @throws AtualizacaoNaoRealizadaException
	 * @author PedroPC
	 */
	@Test(expected = AtualizacaoNaoRealizadaException.class)
	public void atualizarCliente_ClienteInexistente_LevanamentoExcecao() throws RepositorioException, ClienteJaCadastradoException, AtualizacaoNaoRealizadaException {
		Cliente c = new Cliente("Pedro", "12345678911");
		banco.atualizarCliente(c);
		
	}
	
	/**
	 * Verifica o cadastro de um cliente no banco
	 * @throws RepositorioException
	 * @throws ClienteJaCadastradoException
	 * @author Pedro
	 */
	@Test
	public void cadastrarCliente_ExemploValido_Sucesso() throws RepositorioException, ClienteJaCadastradoException {
		Cliente c = new Cliente("Pedro", "12345678911");
		banco.cadastrarCliente(c);
		
		assertEquals("O cadastro pelo banco não funciona corretamente", c, banco.procurarCliente(c.getCpf()));
		
	}
	
	/**
	 * Verifica o cadastro de um cliente repetido no banco
	 * @throws RepositorioException
	 * @throws ClienteJaCadastradoException
	 * @author Pedro
	 */
	@Test(expected = ClienteJaCadastradoException.class)
	public void cadastrarCliente_ClienteExistente_LevantamentoExcecao() throws RepositorioException, ClienteJaCadastradoException {
		Cliente c = new Cliente("Pedro", "12345678911");
		Cliente c2 = new Cliente("Paulo", "12345678911");
		banco.cadastrarCliente(c);
		banco.cadastrarCliente(c2);
		
		
	}



}
