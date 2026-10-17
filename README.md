# Bank-Software-Testing
Atividade para a disciplina Teste de Software 2026.2 da Universidade Federal Rural de Pernambuco.

## EclEmma

### Cobertura de código
  O objetivo era conseguir pelo menos 85% de cobertura em ambos, onde foi cumprido com sucesso através do relatório do Junit.

 #### Linhas
 <img width="1095" height="447" alt="image" src="https://github.com/user-attachments/assets/c2164f00-52fc-461d-b246-35c1cfd31d50" />
 
 #### Decisões
 <img width="1060" height="446" alt="image" src="https://github.com/user-attachments/assets/835612a2-989f-4808-987a-71220b77833c" />

## Pitest Mutantes
  Foi possível cobrir quase todos os mutantes que surgiram nessa execução(Cada execução do pitest gera mutantes diferentes), mas na maior parte dos testes a pontuação se mantém. Lembrando que apenas foi testado o pacote negócio, e 4 testes ficaram com a tag @ignore devido a bugs encontrado na funcionalidade, e o teste de mutantes só executa se todos os testes unitários resultarem em sucesso. 

<img width="800" height="600" alt="pitest mutant report" src="https://github.com/user-attachments/assets/df5c36bf-6484-4504-8dbb-a34312454339" />

## Resultado da execução da suite de testes
### Resumo da Suíte (`TesteBanco`)
- **Total de Testes Executados:** 89
- **Sucessos:** 85
- **Erros (`Errors`):** 0
- **Falhas (`Failures`):** 4
