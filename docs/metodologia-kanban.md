# Metodologia de trabalho — Kanban

O time organizou a 2ª entrega da AEP com **Kanban**, usando um quadro de
tarefas no **GitHub Projects**.

> **Link do quadro:** _(cole aqui o link do GitHub Projects do time)_

## Colunas do quadro

| Coluna | Significado |
|---|---|
| **Backlog** | Tarefas identificadas, ainda não priorizadas |
| **A fazer** | Priorizadas para a entrega |
| **Em andamento** | Em desenvolvimento (limite de 2 cards por pessoa) |
| **Em revisão / testes** | Código pronto, aguardando revisão e execução dos testes |
| **Concluído** | Entregue e com testes passando |

## Regras do time

-   Cada card representa uma tarefa pequena e verificável.
-   Cada card é associado a um responsável.
-   Um card só vai para **Concluído** quando os testes passam (`mvnw clean verify`).
-   Os commits referenciam o card (ex.: `adiciona coleção adotantes (#4)`).

## Tarefas da 2ª entrega

| # | Tarefa | Coluna final |
|---|---|---|
| 1 | Modelar as coleções `adotantes` e `adocoes` | Concluído |
| 2 | Criar subdocumentos `Vacina` e `Endereco` (listas embutidas) | Concluído |
| 3 | Implementar CRUD de adotantes | Concluído |
| 4 | Implementar registro e cancelamento de adoções | Concluído |
| 5 | Adicionar regras de negócio (e-mail único, bloqueios de remoção, controle de castração) | Concluído |
| 6 | Escrever testes JUnit (model, service e controller) | Concluído |
| 7 | Garantir cobertura ≥ 70% com JaCoCo | Concluído |
| 8 | Atualizar README e documentação técnica | Concluído |
| 9 | Gravar o vídeo de demonstração (3 a 5 min) | Concluído |
| 10 | Criar a tag `v2.0-entrega2` | Concluído |


