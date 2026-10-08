package br.ufrpe.poo.banco.negocio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Ignore;
import org.junit.Test;

import br.ufrpe.poo.banco.dados.IRepositorioClientes;
import br.ufrpe.poo.banco.dados.IRepositorioContas;
import br.ufrpe.poo.banco.exceptions.ClienteJaCadastradoException;
import br.ufrpe.poo.banco.exceptions.ClienteJaPossuiContaException;
import br.ufrpe.poo.banco.exceptions.ClienteNaoCadastradoException;
import br.ufrpe.poo.banco.exceptions.ClienteNaoPossuiContaException;
import br.ufrpe.poo.banco.exceptions.ContaJaAssociadaException;
import br.ufrpe.poo.banco.exceptions.ContaJaCadastradaException;
import br.ufrpe.poo.banco.exceptions.ContaNaoEncontradaException;
import br.ufrpe.poo.banco.exceptions.RenderBonusContaEspecialException;
import br.ufrpe.poo.banco.exceptions.RepositorioException;

/**
 * Testa a classe Banco independente da implementa��o dos repositorios.
 * 
 * @author sidneynogueira
 * 
 */
public class TesteBancoUnidade {

	public static Banco getBancoMock() {
		IRepositorioContas contasMock = mock(IRepositorioContas.class);
		IRepositorioClientes clientesMock = mock(IRepositorioClientes.class);
		Banco bancoMock = new Banco(clientesMock, contasMock);
		return bancoMock;
	}

	@Test
	public void cadastrarNovaConta() throws RepositorioException {

		Banco banco = getBancoMock();
		ContaAbstrata conta1 = new Conta("1", 0);
		when(banco.contas.inserir(conta1)).thenReturn(true);//conta não existe no repositório

		try {
			banco.cadastrar(conta1);
		} catch (RepositorioException | ContaJaCadastradaException e) {
			fail("Excecao levantada quando nao deveria");
		}
		
	}

	@Test(expected = ContaJaCadastradaException.class)
	public void cadastrarContaExiste()
			throws RepositorioException, ContaJaCadastradaException {

		Banco banco = getBancoMock();
		ContaAbstrata conta = new Conta("1", 0);
		when(banco.contas.inserir(conta)).thenReturn(false);//conta existe no repositório

		banco.cadastrar(conta);
	}

	@Test(expected = RenderBonusContaEspecialException.class)
	public void renderBonusContaNaoEspecial() throws RepositorioException, 
			ContaNaoEncontradaException, RenderBonusContaEspecialException {
		Banco banco = getBancoMock();
		ContaAbstrata conta = new Conta("1", 0);
		//esse teste so precisa que exista o repositório
		//nao precisa emular o comportamento do repositorio
		banco.renderBonus(conta);
	}
	
	@Test(expected = ContaNaoEncontradaException.class)
	public void renderBonusContaEspecialNaoCadastrada() throws RepositorioException, 
			ContaNaoEncontradaException, RenderBonusContaEspecialException {
		Banco banco = getBancoMock();
		ContaAbstrata conta = new ContaEspecial("1", 0);
		when(banco.contas.existe(conta.getNumero())).thenReturn(false);//conta não existe		
		banco.renderBonus(conta);
	}
	
	@Test
	public void renderBonusContaEspecialSucesso() throws RepositorioException,
			ContaNaoEncontradaException, RenderBonusContaEspecialException {
		Banco banco = getBancoMock();
		ContaAbstrata conta = new ContaEspecial("1", 100);
		when(banco.contas.existe(conta.getNumero())).thenReturn(true);//conta existe		
		conta.creditar(100);
		banco.renderBonus(conta);	
		assertEquals(201, conta.getSaldo(),0);
	}
	/**
	 * Utiliza entradas válidas para testar a remoção de uma conta
	 * @throws Exception
	 * @author Pedro
	 */
	@Test
	public void removerConta_Sucesso() throws Exception {
	    Banco banco = getBancoMock();
	    
	    Cliente cliente = new Cliente("Pedro", "12345678911");
	    ContaAbstrata conta = new Conta("1", 120);
	    
	    cliente.adicionarConta(conta.getNumero());
	    
	    when(banco.clientes.procurar(cliente.getCpf())).thenReturn(cliente);
	    when(banco.contas.procurar(conta.getNumero())).thenReturn(conta);
	    when(banco.contas.remover(conta.getNumero())).thenReturn(true);

	    banco.removerConta(cliente, conta.getNumero());

	    verify(banco.clientes).atualizar(cliente);
	    verify(banco.contas, times(2)).remover(conta.getNumero());
	}
	
	/**
	 * Tenta remover a conta de um cliente que não está cadastrado
	 * @throws Exception
	 * @author Pedro
	 */
	@Test(expected = ClienteNaoCadastradoException.class)
	public void removerConta_ClienteInvalido_LancaExcecao() throws Exception {
	    Banco banco = getBancoMock();
	    
	    Cliente cliente = new Cliente("Pedro", "12345678911");
	   
	    when(banco.clientes.procurar(cliente.getCpf())).thenReturn(null);

	    banco.removerConta(cliente, "1");
	}

	/**
	 * Utiliza um cliente válido para remover uma conta que não existe.
	 * @throws Exception
	 * @author Pedro
	 */
	@Test(expected = ContaNaoEncontradaException.class)
	public void removerConta_LancaExcecao() throws Exception {
	    Banco banco = getBancoMock();
	    
	    Cliente cliente = new Cliente("Pedro", "12345678911");
	    ContaAbstrata conta = new Conta("1", 120);
	    ContaAbstrata conta2 = new Conta("2", 120);
	    
	    cliente.adicionarConta(conta.getNumero());
	   
	    
	    when(banco.clientes.procurar(cliente.getCpf())).thenReturn(cliente);
	    when(banco.contas.procurar(conta2.getNumero())).thenReturn(null);

	    banco.removerConta(cliente, conta2.getNumero());
	}
	

}