package br.ufrpe.poo.banco.exceptions;

/**
 * Exceção para quando uma transferencia não pode ser realizada.
 * @author Pedro
 */
public class TransferenciaInvalidaException extends Exception{
	private static final long serialVersionUID = 1L;
	
	public TransferenciaInvalidaException() {
		super("Não foi possivel realizar a transferencia");
	}

}
