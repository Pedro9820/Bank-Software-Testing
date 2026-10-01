package br.ufrpe.poo.banco.negocio;

public class Dispensadora {
	
    private static final int[] VALORES_CEDULAS = {100, 50, 20, 10, 5, 2}; // Valor das cédulas, em ordem decrescente
    private int[] cedulasDisponiveis; // Cédulas disponíveis para cada valor

    public Dispensadora(int[] cedulasInicial) {
        if (cedulasInicial.length != VALORES_CEDULAS.length) {
            throw new IllegalArgumentException("Quantidade inicial de cédulas inválida.");
        }
        this.cedulasDisponiveis = cedulasInicial;
    }
    
    public int[] getCedulasDisponiveis() {
    	return this.cedulasDisponiveis.clone();
    }

    public int[] dispensar(double valor) throws NaoEhPossivelDispensarValorException {
        int[] cedulasUsadas = new int[VALORES_CEDULAS.length];
        int[] cedulasTemporarias = cedulasDisponiveis.clone(); // Cópia para operações
        int valorRestante = (int) valor;

        if (valorRestante >= 100 && cedulasTemporarias[0] > 0) {
            int quantidadeNecessaria = valorRestante / 100;
            int quantidadeUsada = Math.min(quantidadeNecessaria, cedulasTemporarias[0]);
            cedulasUsadas[0] = quantidadeUsada;
            cedulasTemporarias[0] -= quantidadeUsada;
            valorRestante -= quantidadeUsada * 100;
        }

        if (valorRestante >= 50 && cedulasTemporarias[1] > 0) {
            int quantidadeNecessaria = valorRestante / 50;
            int quantidadeUsada = Math.min(quantidadeNecessaria, cedulasTemporarias[1]);
            cedulasUsadas[1] = quantidadeUsada;
            cedulasTemporarias[1] -= quantidadeUsada;
            valorRestante -= quantidadeUsada * 50;
        }

        if (valorRestante >= 20 && cedulasTemporarias[2] > 0) {
            int quantidadeNecessaria = valorRestante / 20;
            int quantidadeUsada = Math.min(quantidadeNecessaria, cedulasTemporarias[2]);
            cedulasUsadas[2] = quantidadeUsada;
            cedulasTemporarias[2] -= quantidadeUsada;
            valorRestante -= quantidadeUsada * 20;
        }

        if (valorRestante >= 10 && cedulasTemporarias[3] > 0) {
            int quantidadeNecessaria = valorRestante / 10;
            int quantidadeUsada = Math.min(quantidadeNecessaria, cedulasTemporarias[3]);
            cedulasUsadas[3] = quantidadeUsada;
            cedulasTemporarias[3] -= quantidadeUsada;
            valorRestante -= quantidadeUsada * 10;
        }

        if (valorRestante >= 5 && cedulasTemporarias[4] > 0) {
            int quantidadeNecessaria = valorRestante / 5;
            int quantidadeUsada = Math.min(quantidadeNecessaria, cedulasTemporarias[4]);
            cedulasUsadas[4] = quantidadeUsada;
            cedulasTemporarias[4] -= quantidadeUsada;
            valorRestante -= quantidadeUsada * 5;
        }

        if (valorRestante >= 2 && cedulasTemporarias[5] > 0) {
            int quantidadeNecessaria = valorRestante / 2;
            int quantidadeUsada = Math.min(quantidadeNecessaria, cedulasTemporarias[5]);
            cedulasUsadas[5] = quantidadeUsada;
            cedulasTemporarias[5] -= quantidadeUsada;
            valorRestante -= quantidadeUsada * 2;
        }

        if (valorRestante > 0) {
            throw new NaoEhPossivelDispensarValorException("Não é possível dispensar o valor solicitado com as cédulas disponíveis.");
        }

        // Apenas atualiza cédulas disponíveis se não houver exceção
        this.cedulasDisponiveis = cedulasTemporarias;

        return cedulasUsadas;
    }
    /*
    public static void main(String[] args) throws NaoEhPossivelDispensarValorException {
        // Exemplo de uso
        int[] cedulasInicial = {10, 10, 10, 0, 0, 10}; // Inicializando com 10 cédulas de cada valor
        Dispensadora dispensadora = new Dispensadora(cedulasInicial);

            int[] resultado = dispensadora.dispensar(280); // Exemplo que não pode ser atendido
            System.out.println("Cédulas usadas: ");
            for (int i = 0; i < resultado.length; i++) {
                System.out.println(VALORES_CEDULAS[i] + " reais: " + resultado[i] + " cédulas");
            }       
       
    }
    */
}

// Definição da exceção personalizada
@SuppressWarnings("serial")
class NaoEhPossivelDispensarValorException extends Exception {
    public NaoEhPossivelDispensarValorException(String mensagem) {
        super(mensagem);
    }
}