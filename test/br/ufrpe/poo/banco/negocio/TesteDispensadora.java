package br.ufrpe.poo.banco.negocio;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import org.junit.Test;

public class TesteDispensadora {
	
	/**
	 * Testa se os parametros do construtor sao passados corretamente
	 */
	@Test
	public void construtorInicializaCorretamente() {
			int[] cedulas = new int[] {100, 50, 20, 10, 5, 2};
			Dispensadora dp = new Dispensadora(cedulas);
			assertArrayEquals(cedulas, dp.getCedulasDisponiveis());
		
	}
	
	/**
	 * Testa se os parametros do construtor sao verificados corretamente
	 */
	@Test(expected = IllegalArgumentException.class)
	public void construtorInicializaIncorretamente() {
			int[] cedulas = new int[] {100, 50, 10, 5, 2};
			new Dispensadora(cedulas);
	}
	
	
	/**
	 * Testa se as celulas são salvas corretamente
	 */
	@Test
	public void retornaCelulasDisponiveis() {
			int[] cedulas = new int[] {100, 50, 20, 10, 5, 2};
			Dispensadora dp = new Dispensadora(cedulas);
			assertArrayEquals(dp.getCedulasDisponiveis(), cedulas);
	}
	
	/**
	 * Testa se a dispensa de um valor válido funciona
	 */
	@Test
	public void dispensarEsgotaCedulas(){
		Dispensadora dp = new Dispensadora(new int[]{100, 50, 20, 10, 5, 2});
		try{
			dp.dispensar(75);
			dp.dispensar(150);
			dp.dispensar(20);
			dp.dispensar(5);
			dp.dispensar(200);
			dp.dispensar(5);
			dp.dispensar(100);
		}catch(NaoEhPossivelDispensarValorException e) {
			fail("Excessão não deveria ter sido levantada");
		}
	}
	
	/**
	 * Testa se a dispensa avisa não ser possivel continuar operação
	 * @throws NaoEhPossivelDispensarValorException 
	 */
	@Test(expected = NaoEhPossivelDispensarValorException.class)
	public void dispensarCelulasFaltantes() throws NaoEhPossivelDispensarValorException{
		Dispensadora dp = new Dispensadora(new int[]{0, 0, 0, 0, 0, 1});
        dp.dispensar(4);
	
}
	@Test
    public void dispensarValorValidoCalculaEQuantidadeEAtualizaEstoque() throws NaoEhPossivelDispensarValorException {
        
        Dispensadora dp = new Dispensadora(new int[]{10, 10, 10, 10, 10, 10});

        int[] resultado = dp.dispensar(187);
        int[] esperado = new int[]{1, 1, 1, 1, 1, 1};

        assertArrayEquals(esperado, resultado);

        int[] estoqueEsperado = new int[]{9, 9, 9, 9, 9, 9};
        assertArrayEquals(estoqueEsperado, dp.getCedulasDisponiveis());
    }
	
	@Test(expected = NaoEhPossivelDispensarValorException.class)
    public void dispensarValorImpossivelComCedulasAtuaisLancaExcecao() throws NaoEhPossivelDispensarValorException {
        // Dispensadora possui apenas cédulas de 100, 50 e 20
        Dispensadora dp = new Dispensadora(new int[]{10, 10, 10, 0, 0, 0});
        dp.dispensar(15); // Impossível dispensar 15 reais sem cédulas de 10, 5 ou 2
    }
	
	// --- TESTES DE VALOR EXATO (Mata mutantes que trocam >= por >) ---

	@Test
	public void dispensarValorExatoCinquenta() throws NaoEhPossivelDispensarValorException {
	    Dispensadora dp = new Dispensadora(new int[]{5, 5, 5, 5, 5, 5});
	    int[] resultado = dp.dispensar(50);
	    // Deve usar exatamente 1 nota de 50
	    assertArrayEquals(new int[]{0, 1, 0, 0, 0, 0}, resultado);
	}

	@Test
	public void dispensarValorExatoVinte() throws NaoEhPossivelDispensarValorException {
	    Dispensadora dp = new Dispensadora(new int[]{5, 5, 5, 5, 5, 5});
	    int[] resultado = dp.dispensar(20);
	    // Deve usar exatamente 1 nota de 20
	    assertArrayEquals(new int[]{0, 0, 1, 0, 0, 0}, resultado);
	}

	@Test
	public void dispensarValorExatoDez() throws NaoEhPossivelDispensarValorException {
	    Dispensadora dp = new Dispensadora(new int[]{5, 5, 5, 5, 5, 5});
	    int[] resultado = dp.dispensar(10);
	    // Deve usar exatamente 1 nota de 10
	    assertArrayEquals(new int[]{0, 0, 0, 1, 0, 0}, resultado);
	}

	// --- TESTES DE ESTOQUE ZERADO (Mata mutantes na verificação cedulasTemporarias > 0) ---

	@Test
	public void dispensarCinquentaSemNotasDeCinquentaDisponiveis() throws NaoEhPossivelDispensarValorException {
	    // 0 notas de 50 disponíveis -> deve pular a nota de 50 e usar duas de 20 e uma de 10
	    Dispensadora dp = new Dispensadora(new int[]{10, 0, 10, 10, 10, 10});
	    int[] resultado = dp.dispensar(50);
	    assertArrayEquals(new int[]{0, 0, 2, 1, 0, 0}, resultado);
	}

	@Test
	public void dispensarVinteSemNotasDeVinteDisponiveis() throws NaoEhPossivelDispensarValorException {
	    // 0 notas de 20 disponíveis -> deve pular a nota de 20 e usar duas de 10
	    Dispensadora dp = new Dispensadora(new int[]{10, 10, 0, 10, 10, 10});
	    int[] resultado = dp.dispensar(20);
	    assertArrayEquals(new int[]{0, 0, 0, 2, 0, 0}, resultado);
	}

	@Test
	public void dispensarDezSemNotasDeDezDisponiveis() throws NaoEhPossivelDispensarValorException {
	    // 0 notas de 10 disponíveis -> deve pular a nota de 10 e usar duas de 5
	    Dispensadora dp = new Dispensadora(new int[]{10, 10, 10, 0, 10, 10});
	    int[] resultado = dp.dispensar(10);
	    assertArrayEquals(new int[]{0, 0, 0, 0, 2, 0}, resultado);
	}
	
}
