# AdotaPet — Documentação Técnica

**Segunda versão \| AEP — Engenharia de Software \| 2026**

## 1. Introdução

O AdotaPet é uma API REST desenvolvida para apoiar o cadastro e o
gerenciamento de animais disponíveis para adoção. A aplicação organiza
informações dos animais, dos possíveis adotantes e dos registros de
adoção.

Na segunda versão, a estrutura foi ampliada: além da coleção de animais,
foram implementadas coleções próprias para adotantes e adoções. Também
foram adicionados subdocumentos para armazenar endereços e informações
de vacinação, além de funcionalidades relacionadas à castração.

O projeto é uma aplicação acadêmica desenvolvida com Java e Spring Boot
e utiliza o MongoDB para persistência dos dados. Sua proposta está
relacionada ao ODS 11 — Cidades e Comunidades Sustentáveis — no contexto
de organização e apoio a iniciativas de adoção responsável.

## 2. Objetivos da segunda versão

A segunda versão teve como objetivos:

- separar os dados de animais, adotantes e adoções em coleções
  específicas;
- permitir o gerenciamento dos dados dos adotantes;
- registrar e consultar adoções;
- controlar a disponibilidade dos animais durante o processo de adoção;
- armazenar endereços e vacinas como subdocumentos;
- registrar a situação de castração dos animais;
- aplicar regras de negócio para evitar inconsistências;
- ampliar os testes automatizados e verificar a cobertura com JaCoCo.

## 3. Tecnologias e ferramentas

| Tecnologia ou ferramenta | Utilização                                          |
|--------------------------|-----------------------------------------------------|
| Java 17                  | Linguagem de programação                            |
| Spring Boot 3.3.4        | Estrutura principal da aplicação                    |
| Spring Web               | Criação dos endpoints REST                          |
| Spring Data MongoDB      | Persistência e consultas no MongoDB                 |
| Spring Validation        | Validação dos dados recebidos                       |
| MongoDB 7                | Banco de dados NoSQL                                |
| Maven                    | Gerenciamento de dependências e execução do projeto |
| JUnit 5 e Mockito        | Testes automatizados                                |
| JaCoCo 0.8.12            | Medição e verificação da cobertura de testes        |
| Docker Compose           | Inicialização do MongoDB em contêiner               |

## 4. Arquitetura da aplicação

A aplicação utiliza uma arquitetura em camadas. Cada camada possui uma
responsabilidade, facilitando a organização e a manutenção do código.

| Camada/pacote | Responsabilidade                                 |
|---------------|--------------------------------------------------|
| `controller`  | Receber requisições HTTP e retornar respostas    |
| `service`     | Executar regras de negócio e coordenar operações |
| `repository`  | Acessar e persistir os documentos no MongoDB     |
| `model`       | Representar as entidades e subdocumentos         |
| `dto`         | Definir os dados recebidos nas requisições       |
| `enums`       | Definir valores fixos, como o status da adoção   |
| `exception`   | Centralizar o tratamento de erros                |

### 4.1 Fluxo de uma requisição

De forma geral, uma requisição segue este fluxo:

1.  O cliente envia uma requisição HTTP para um endpoint.
2.  O Controller recebe a requisição e, quando necessário, valida o DTO.
3.  O Service executa as regras de negócio.
4.  O Repository realiza a operação no MongoDB.
5.  O Controller retorna a resposta HTTP correspondente.

### 4.2 Estrutura de diretórios

``` text
src/
├── main/
│   ├── java/com/aep6s/
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── enums/
│   │   ├── exception/
│   │   ├── model/
│   │   ├── repository/
│   │   └── service/
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/aep6s/
        ├── controller/
        ├── model/
        └── service/
```

## 5. Modelagem e banco de dados

O banco utilizado é `adotapet`. A aplicação trabalha com três coleções
principais:

| Coleção     | Documento representado | Finalidade                                  |
|-------------|------------------------|---------------------------------------------|
| `animais`   | `AnimalModel`          | Armazenar os dados dos animais              |
| `adotantes` | `AdotanteModel`        | Armazenar os dados dos adotantes            |
| `adocoes`   | `AdocaoModel`          | Registrar a relação entre animal e adotante |

### 5.1 Animal

A classe `AnimalModel` representa um animal disponível para adoção.
Entre seus atributos estão nome, espécie, raça, idade, porte, sexo,
descrição, status, situação de castração e data de cadastro.

O animal também possui uma lista de vacinas. Cada item é representado
pela classe `Vacina` e fica armazenado como subdocumento dentro do
documento do animal.

O status é representado pelo enum `StatusAdocao`, com os valores:

- `DISPONIVEL`
- `ADOTADO`

Ao criar um animal, o status inicial é `DISPONIVEL`.

### 5.2 Adotante

A classe `AdotanteModel` armazena nome, e-mail, telefone, data de
cadastro e uma lista de endereços.

Os endereços são representados pela classe `Endereco`, armazenada como
subdocumento na lista `enderecos`. Cada endereço contém rua, número,
cidade e estado.

### 5.3 Adoção

A classe `AdocaoModel` registra os identificadores do animal e do
adotante, a data da adoção e observações opcionais.

A adoção mantém os campos `animalId` e `adotanteId` como referências por
identificador. A aplicação valida a existência dos registros e atualiza
o status do animal durante o processo.

### 5.4 Visão simplificada do relacionamento

``` text
adotapet
│
├── animais
│   ├── dados do animal
│   └── vacinas[]          (subdocumentos)
│
├── adotantes
│   ├── dados do adotante
│   └── enderecos[]        (subdocumentos)
│
└── adocoes
    ├── animalId
    ├── adotanteId
    ├── dataAdocao
    └── observacoes
```

As coleções `animais`, `adotantes` e `adocoes` são documentos separados.
As informações de vacinação e endereço são incorporadas aos respectivos
documentos como subdocumentos.

## 6. Regras de negócio e validações

As regras de negócio são implementadas principalmente nas classes de
serviço.

### 6.1 Adotantes

- O nome e o e-mail são obrigatórios.
- O e-mail deve possuir formato válido.
- É permitido cadastrar apenas um adotante por e-mail, desconsiderando
  diferenças entre letras maiúsculas e minúsculas.
- É necessário informar ao menos um endereço.
- Rua, cidade e estado são obrigatórios; o estado deve possuir duas
  letras.
- Um adotante que possua adoções registradas não pode ser removido.

### 6.2 Animais

- Nome, espécie, porte, sexo e status são campos obrigatórios no modelo.
- A idade não pode ser negativa.
- O status inicial é `DISPONIVEL`.
- A castração pode ser registrada uma única vez pela operação
  específica; a data é preenchida no momento do registro.
- O nome da vacina é obrigatório.
- A data de aplicação da vacina é obrigatória e não pode estar no
  futuro.
- Um animal que esteja adotado não pode ser removido.

### 6.3 Adoções

- O identificador do animal e o identificador do adotante são
  obrigatórios.
- O animal e o adotante precisam existir para que a adoção seja
  registrada.
- Um animal que já esteja adotado não pode receber uma nova adoção.
- Ao registrar a adoção, o status do animal passa para `ADOTADO`.
- Ao cancelar a adoção, o registro é removido e o animal retorna para
  `DISPONIVEL`.

## 7. API REST — Endpoints

A aplicação está configurada para utilizar a porta `8082` por padrão. A
URL-base local é `http://localhost:8082`.

### 7.1 Animais — `/animais`

| Método | Endpoint                       | Descrição                          |
|--------|--------------------------------|------------------------------------|
| POST   | `/animais`                     | Cadastrar um animal                |
| GET    | `/animais`                     | Listar todos os animais            |
| GET    | `/animais/{id}`                | Consultar animal por identificador |
| GET    | `/animais/status/{status}`     | Listar animais por status          |
| GET    | `/animais/castrado/{castrado}` | Filtrar por situação de castração  |
| PUT    | `/animais/{id}`                | Atualizar dados de um animal       |
| POST   | `/animais/{id}/vacinas`        | Adicionar vacina ao animal         |
| PATCH  | `/animais/{id}/castracao`      | Registrar castração                |
| DELETE | `/animais/{id}`                | Remover animal, se permitido       |

### 7.2 Adotantes — `/adotantes`

| Método | Endpoint          | Descrição                            |
|--------|-------------------|--------------------------------------|
| POST   | `/adotantes`      | Cadastrar adotante                   |
| GET    | `/adotantes`      | Listar todos os adotantes            |
| GET    | `/adotantes/{id}` | Consultar adotante por identificador |
| PUT    | `/adotantes/{id}` | Atualizar adotante                   |
| DELETE | `/adotantes/{id}` | Remover adotante, se permitido       |

### 7.3 Adoções — `/adocoes`

| Método | Endpoint                         | Descrição                          |
|--------|----------------------------------|------------------------------------|
| POST   | `/adocoes`                       | Registrar adoção                   |
| GET    | `/adocoes`                       | Listar todas as adoções            |
| GET    | `/adocoes/{id}`                  | Consultar adoção por identificador |
| GET    | `/adocoes/adotante/{adotanteId}` | Listar adoções de um adotante      |
| DELETE | `/adocoes/{id}`                  | Cancelar adoção                    |

### 7.4 Exemplos de requisição

**Cadastro de animal — `POST /animais`**

``` json
{
  "nome": "Thor",
  "especie": "Cachorro",
  "raca": "SRD",
  "idade": 3,
  "porte": "Médio",
  "sexo": "Macho",
  "descricao": "Animal dócil",
  "castrado": false,
  "vacinas": []
}
```

**Cadastro de adotante — `POST /adotantes`**

``` json
{
  "nome": "Maria da Silva",
  "email": "maria@email.com",
  "telefone": "44999999999",
  "enderecos": [
    {
      "rua": "Rua das Flores",
      "numero": "100",
      "cidade": "Maringá",
      "estado": "PR"
    }
  ]
}
```

**Registro de adoção — `POST /adocoes`**

``` json
{
  "animalId": "ID_DO_ANIMAL",
  "adotanteId": "ID_DO_ADOTANTE",
  "observacoes": "Adoção registrada para demonstração"
}
```

Os identificadores dos exemplos devem ser substituídos por IDs
existentes no banco de dados.

### 7.5 Respostas e tratamento de erros

Os Controllers utilizam `ResponseEntity` para definir os códigos HTTP.
Entre os retornos previstos estão:

| Situação                          | Código HTTP       |
|-----------------------------------|-------------------|
| Cadastro realizado                | `201 Created`     |
| Consulta ou atualização realizada | `200 OK`          |
| Remoção ou cancelamento realizado | `204 No Content`  |
| Recurso não encontrado            | `404 Not Found`   |
| Dados de entrada inválidos        | `400 Bad Request` |
| Regra de negócio não atendida     | `409 Conflict`    |

O tratamento centralizado é realizado pela classe
`GlobalExceptionHandler`. As respostas de erro incluem os campos
`timestamp` e `mensagem`.

## 8. Configuração e execução

### 8.1 Pré-requisitos

- Java 17 ou superior compatível com a configuração do projeto;
- Docker Desktop com Docker Compose, ou uma instalação local do MongoDB;
- Git, caso o projeto seja obtido pelo repositório;
- IDE de preferência, como IntelliJ IDEA.

### 8.2 Banco de dados com Docker Compose

Na pasta raiz do projeto, execute:

``` bash
docker compose up -d
```

O arquivo `docker-compose.yml` utiliza a imagem `mongo:7`, disponibiliza
a porta `27017` e mantém os dados em um volume chamado
`adotapet_mongo_data`.

Para parar o contêiner:

``` bash
docker compose down
```

O comando acima não remove o volume de dados. Para remover também os
dados persistidos, seria necessário excluir o volume explicitamente;
faça isso somente se desejar apagar o banco local.

### 8.3 Executar a aplicação

No Windows, na pasta raiz do projeto:

``` bash
mvnw.cmd spring-boot:run
```

Em sistemas Linux ou macOS:

``` bash
./mvnw spring-boot:run
```

Por padrão, a API utiliza:

``` text
http://localhost:8082
```

A porta pode ser alterada pela variável de ambiente `SERVER_PORT`. A
conexão padrão com o MongoDB está configurada em
`src/main/resources/application.properties`:

``` properties
spring.application.name=aep6s
spring.data.mongodb.uri=mongodb://localhost:27017/adotapet
server.port=${SERVER_PORT:8082}
```

## 9. Testes automatizados e cobertura

Os testes automatizados estão organizados por responsabilidade:

| Grupo       | O que é verificado                         |
|-------------|--------------------------------------------|
| Models      | Comportamentos dos modelos e subdocumentos |
| Services    | Regras de negócio e operações da aplicação |
| Controllers | Respostas e funcionamento dos endpoints    |

As ferramentas utilizadas são JUnit 5 e Mockito, disponibilizadas pela
dependência `spring-boot-starter-test`.

### 9.1 Executar os testes

No Windows:

``` bash
mvnw.cmd clean verify
```

No Linux ou macOS:

``` bash
./mvnw clean verify
```

### 9.2 Cobertura com JaCoCo

O JaCoCo está configurado no `pom.xml` para gerar o relatório de
cobertura e verificar o percentual mínimo de linhas cobertas.

A configuração estabelece o mínimo de **70% de cobertura de linhas**
para o conjunto da aplicação (`BUNDLE`). Quando a verificação não atinge
esse limite, a etapa `verify` falha.

No pacote do projeto enviado para análise, o relatório existente
indicava:

- **94 testes executados**;
- **0 falhas**;
- **0 erros**;
- **99% de cobertura de linhas**.

Esses números representam o resultado registrado nos relatórios
incluídos no pacote analisado. Após novas alterações no código ou nos
testes, execute novamente `mvnw.cmd clean verify` para atualizar os
resultados.

O relatório HTML é gerado em:

``` text
target/site/jacoco/index.html
```

## 10. Considerações finais

A segunda versão do AdotaPet ampliou a aplicação ao separar o
gerenciamento de animais, adotantes e adoções, além de incluir
subdocumentos para endereços e vacinas.

A implementação das regras de negócio contribui para manter a
consistência dos registros durante o cadastro, a atualização, a remoção
e o cancelamento de adoções. A organização em camadas também facilita a
manutenção e a evolução da API.

Os testes automatizados e a análise de cobertura com JaCoCo permitem
verificar o comportamento das principais partes da aplicação e
acompanhar o atendimento ao percentual mínimo definido no projeto.

------------------------------------------------------------------------
**Projeto: AdotaPet \| AEP - Engenharia de Software 6S \| 2026.2**
