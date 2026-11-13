# Metodologia Kanban — AdotaPet 

Para organizar e acompanhar o desenvolvimento da segunda entrega do projeto AdotaPet, foi utilizada a metodologia Kanban, por meio da ferramenta GitHub Projects.
O quadro foi utilizado para dividir o desenvolvimento em tarefas menores, acompanhar o andamento das atividades e visualizar as etapas concluídas e pendentes.
> **Link do quadro:** _[AdotaPet — Segunda Entrega](https://github.com/users/VihSouza06/projects/1/views/1)_

## Organização do quadro

O quadro Kanban foi organizado em cinco colunas:

| Coluna | Descrição |
|---|---|
| Backlog | Reúne as tarefas planejadas para o desenvolvimento. |
| A fazer | Contém as atividades selecionadas para execução. |
| Em andamento | Apresenta as tarefas que estão sendo desenvolvidas. |
| Em revisão/testes | Reúne as atividades que aguardam verificação e validação. |
| Concluído | Contém as tarefas finalizadas. |

## Planejamento das atividades

As atividades foram divididas em cinco etapas, de acordo com a sequência de desenvolvimento do projeto.

### Etapa 1 — Estrutura e modelagem

| Nº | Atividade | Descrição |
|---|---|---|
| 1 | Preparar o projeto para a segunda versão | Organizar o projeto e preservar a primeira versão. |
| 2 | Criar o model de adotantes | Implementar a classe AdotanteModel. |
| 3 | Criar o model de adoções | Implementar a classe AdocaoModel. |
| 4 | Criar subdocumentos de vacina e endereço | Implementar as classes Vacina e Endereco. |
| 5 | Criar repositories de adotantes e adoções | Implementar AdotanteRepository e AdocaoRepository. |
| 6 | Atualizar estrutura do model de animais | Adicionar informações de vacinação e castração ao AnimalModel. |

### Etapa 2 — Implementação das funcionalidades

| Nº | Atividade                                  | Descrição                                                                |
|----|--------------------------------------------|--------------------------------------------------------------------------|
| 7  | Criar e atualizar DTOs da aplicação        | Implementar e atualizar os DTOs de adotantes, adoções e animais.         |
| 8  | Implementar CRUD de adotantes              | Desenvolver cadastro, consulta, atualização e remoção de adotantes.      |
| 9  | Implementar CRUD de adoções                | Desenvolver registro, consulta, atualização e cancelamento de adoções.   |
| 10 | Atualizar funcionalidades de animais       | Integrar as funcionalidades de animais ao novo processo de adoção.       |

### Etapa 3 — Regras de negócio

| Nº | Atividade | Descrição |
|----|---|---|
| 11 | Implementar validação de e-mail único | Impedir o cadastro de adotantes com e-mails duplicados. |
| 12 | Implementar bloqueios de remoção | Impedir remoções que comprometam registros de adoção. |
| 13 | Implementar regras de registro e cancelamento de adoções | Validar as condições necessárias para realizar e cancelar adoções. |
| 14 | Implementar regras de vacinação e castração | Adicionar validações e controles dessas informações. |

### Etapa 4 — Testes e cobertura

| Nº | Atividade | Descrição |
|----|---|---|
| 15 | Criar testes dos models e subdocumentos | Verificar o comportamento dos models e subdocumentos. |
| 16 | Criar testes dos services | Validar as regras e funcionalidades dos serviços. |
| 17 | Criar testes dos controllers | Verificar o funcionamento dos endpoints. |
| 18 | Garantir cobertura mínima de 70% | Utilizar JaCoCo para analisar a cobertura dos testes automatizados. |

### Etapa 5 — Documentação e finalização

| Nº | Atividade | Descrição |
|----|---|---|
| 19 | Atualizar README e documentação técnica | Documentar o projeto, suas funcionalidades e instruções de execução. |
| 20 | Realizar testes gerais e gravar demonstração | Validar os endpoints e apresentar o funcionamento da aplicação em vídeo. |


