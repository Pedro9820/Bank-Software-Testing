package br.ufrpe.poo.banco.negocio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import br.ufrpe.poo.banco.exceptions.ClienteJaPossuiContaException;
import br.ufrpe.poo.banco.exceptions.ClienteNaoPossuiContaException;

/**
 * Classe de teste respons�vel por testar as condi��es dos m�todos
 * adicionarConta e removerConta da classe Cliente.
 * 
 * @author Aluno
 * 
 */
public class TesteCliente {

	/**
	 * Testa a inser��o de uma nova conta vinculada ao cliente
	 * 
	 * @throws ClienteJaPossuiContaException
	 */
	@Test
	public void adicionaContasSucesso() throws ClienteJaPossuiContaException {
		Cliente c1 = new Cliente("nome", "123");
		c1.adicionarConta("2");
		c1.adicionarConta("1");
		assertEquals(c1.getContas().size(),2,0);
		assertEquals(c1.procurarConta("2"), 0);
		assertEquals(c1.procurarConta("1"), 1);
	}

	/**
	 * Testa a condi��o da tentativa de adicionar uma conta j� existente � lista de
	 * contas do cliente
	 * 
	 * @throws ClienteJaPossuiContaException
	 */
	@Test(expected = ClienteJaPossuiContaException.class)
	public void adicionarContaJaExistente() throws ClienteJaPossuiContaException {
		Cliente c1 = new Cliente("nome", "123");
		c1.adicionarConta("1"); 
		c1.adicionarConta("2"); 
		c1.adicionarConta("3"); 
		c1.adicionarConta("4"); 
		c1.adicionarConta("3"); // tentativa de adicionar a mesma conta
	}

	/**
	 * Teste a remo��o de uma conta da lista de contas do cliente
	 * 
	 * @throws ClienteJaPossuiContaException
	 * @throws ClienteNaoPossuiContaException
	 */
	@Test
	public void removerContaCliente() throws ClienteJaPossuiContaException, ClienteNaoPossuiContaException {
		Cliente c1 = new Cliente("nome", "123");
		c1.adicionarConta("1"); 
		c1.adicionarConta("2"); 
		c1.removerConta("2"); 
		c1.removerConta("1"); 
		assertEquals(c1.procurarConta("1"), -1);
		assertEquals(c1.procurarConta("2"), -1);
	}

	/**
	 * Testa a remo��o de uma determinada conta que n�o est� vinculada ao cliente
	 * 
	 * @throws ClienteNaoPossuiContaException
	 */
	@Test(expected = ClienteNaoPossuiContaException.class)
	public void removerContaClienteSemContaTest() throws ClienteNaoPossuiContaException {
		Cliente c1 = new Cliente("nome", "123");
		c1.removerConta("1"); 
	}
	
	/**
	 * Testa a verificação se um cliente é igual a outro
	 * 
	 * @author Pedro
	 */
	@Test
	public void equals_ClientsIguais_Sucesso() {
		Cliente c1 = new Cliente("Pedro", "12345678911");
		
		assertTrue(c1.equals(c1));
		
	}
	
	/**
	 * Verifica a comparação entre 2 clientes distintos.
	 * 
	 * @author Pedro
	 */
	@Test
	public void equals_ClientsDiferentes_Sucesso() {
		Cliente c1 = new Cliente("Pedro", "12345678911");
		Cliente c2 = new Cliente("Paulo", "12345678910");
		
		assertFalse(c1.equals(c2));
		
	}
	
	/**
	 * Verifica a comparação de cliente com outro objeto
	 * 
	 * @author Pedro
	 */
	@Test
	public void equals_ClientsComConta_Sucesso() {
		Cliente c1 = new Cliente("Pedro", "12345678911");
		Conta c2 = new Conta("1", 100);
		
		assertFalse(c1.equals(c2));
		
	}
	
	/**
	 * Verifica o retorno do método getNome
	 * @author Pedro
	 */
	@Test
	public void getNome() {
		Cliente c = new Cliente("Pedro", "12345678911");
		assertEquals("O retorno do nome não está acontecendo corretamente", "Pedro", c.getNome());
		
	}
	
	/**
	 * Verifica o retorno do método setNome
	 * @author Pedro
	 */
	@Test
	public void setNome() {
		Cliente c = new Cliente("Pedro", "12345678911");
		c.setNome("Paulo");
		assertEquals("A atualização do nome não está acontecendo corretamente", "Paulo", c.getNome());
		
	}
	
	/**
	 * Verifica o retorno do método getNome
	 * @author Pedro
	 */
	@Test
	public void getCPF() {
		Cliente c = new Cliente("Pedro", "12345678911");
		assertEquals("O retorno do cpf não está acontecendo corretamente", "12345678911", c.getCpf());
		
	}
	
	/**
	 * Verifica o retorno do método setNome
	 * @author Pedro
	 */
	@Test
	public void setCPF() {
		Cliente c = new Cliente("Pedro", "12345678911");
		c.setCpf("12345678910");
		assertEquals("A atualização do cpf não está acontecendo corretamente", "12345678910", c.getCpf());
		
	}
	
	/**
	 * Verifica o apagamento das contas do cliente
	 * @throws ClienteJaPossuiContaException 
	 * @author Pedro
	 * 
	 */
	@Test
	public void removerTodasContas_ExemploValido_Sucesso() throws ClienteJaPossuiContaException {
		Cliente c = new Cliente("Pedro", "12345678911");
		Conta conta = new Conta("1", 123);
		
		c.adicionarConta(conta.getNumero());
		
		c.removerTodasAsContas();
		assertNull("A remoção de todas as contas do cliene não está funcionando corretamente.", c.getContas());
		
	}
	
	
	/**
	 * Consulta o numero da conta na lista de contas do cliente
	 * @throws ClienteJaPossuiContaException 
	 * @author Pedro
	 * 
	 */
	@Test
	public void consultarNumeroConta() throws ClienteJaPossuiContaException {
		Cliente c = new Cliente("Pedro", "12345678911");
		Conta conta = new Conta("1", 123);
		
		c.adicionarConta(conta.getNumero());
		assertEquals("A consulta do numero da conta da lista do cliente  não está funcionando corretamente.", conta.getNumero(), c.consultarNumeroConta(0));
		
	}

}
