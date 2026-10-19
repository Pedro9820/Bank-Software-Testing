# Bank-Software-Testing
Atividade para a disciplina Teste de Software 2026.2 da Universidade Federal Rural de Pernambuco.
**Aluno:** Pedro Henrique Apolinario da Silva

## Aviso
Todos os testes criados receberam a tag @author Pedro, para facilitar a visualização do código escrito e mudanças. As classes alteradas foram as seguintes:
- Banco
- Cliente
- ContaAbstrata
- ContaImposto

## EclEmma

### Cobertura de código
  O objetivo era conseguir pelo menos 85% de cobertura em ambos, onde foi cumprido com sucesso através do relatório do Junit.

 <div align="center">
    
 #### Linhas
 <img alt="cobertura de linhas" src="imagens/cobertura de linhas.png" />
 
 #### Decisões
 <img alt="cobertura de decisão" src="imagens/cobertura de decisões.png" />
 </div>
 
## Pitest Mutantes
  Foi possível cobrir quase todos os mutantes que surgiram nessa execução(Cada execução do pitest gera mutantes diferentes), mas na maior parte dos testes a pontuação se mantém. Lembrando que apenas foi testado o pacote negócio, e 4 testes ficaram com a tag @ignore devido a bugs encontrado na funcionalidade, e o teste de mutantes só executa se todos os testes unitários resultarem em sucesso. 


<img alt="pitest mutant report" src="imagens/cobertura de mutantes.png" />

## Resultado da execução da suíte de testes
### Resumo da Suíte (`TesteBanco`)
- **Total de Testes Executados:** 89
- **Sucessos:** 85
- **Erros (`Errors`):** 0
- **Falhas (`Failures`):** 4

### Registro de falhas 

1. **`transferir_ContasIguais_LevantamentoExcecao`**
   - **Falha:** A transferência entre uma conta e ela mesma não levanta exceção.
   - **Issue:** [#4](https://github.com/Pedro9820/Bank-Software-Testing/issues/4)

2. **`transferir_ContasInexistentes_LevantamentoExcecao`**
   - **Falha:** A transferência com contas inexistentes falha sem retornar erro.
   - **Issue:** [#3](https://github.com/Pedro9820/Bank-Software-Testing/issues/3)

3. **`creditar_VerificarPersistencia_Falha`**
   - **Falha:** A alteração de saldo via crédito não é salva no arquivo/repositório após a execução.
   - **Issue:** [#2](https://github.com/Pedro9820/Bank-Software-Testing/issues/2)

4. **`removerCliente_ClienteMultiplasContas_RemocaoContasSucesso`**
   - **Falha:** Ocorre falha na remoção quando o cliente possui múltiplas contas associadas.
   - **Issue:** [#1](https://github.com/Pedro9820/Bank-Software-Testing/issues/1)
