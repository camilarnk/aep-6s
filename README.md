# Faunifica 🦜

PoC para **cadastro e consulta de espécies de animais silvestres**, com o objetivo de organizar informações básicas sobre a fauna e facilitar o acesso a dados relacionados à biodiversidade.

## 🌱 Problema

Informações sobre espécies de animais silvestres podem estar dispersas e pouco organizadas, dificultando sua consulta e o acesso a dados básicos sobre a fauna. O Faunifica propõe uma forma simples de centralizar essas informações em uma aplicação.

## 🌎 ODS 15 — Vida Terrestre

O projeto está alinhado à **ODS 15 — Vida Terrestre**, que busca proteger, recuperar e promover o uso sustentável dos ecossistemas terrestres, além de combater a perda de biodiversidade.

O Faunifica contribui de forma indireta para esse objetivo ao organizar e facilitar a consulta de informações sobre espécies silvestres, incluindo dados como nome popular, nome científico, grupo, bioma, nível de risco e população estimada.

## 💡 Prova de Conceito

A primeira versão da PoC consiste em uma API REST para gerenciamento de espécies, permitindo:

* Cadastrar espécies;
* Listar espécies cadastradas;
* Pesquisar espécies por nome popular;
* Consultar uma espécie por seu ID;
* Atualizar dados de uma espécie;
* Excluir uma espécie.

## 🛠️ Tecnologias

* Java 21
* Spring Boot 4.1.0
* Spring Data MongoDB
* MongoDB 8
* Docker
* Maven
* Git e GitHub

## 🗄️ Banco de Dados

O projeto utiliza o **MongoDB**, banco de dados NoSQL orientado a documentos.

O MongoDB é executado através do Docker Compose. Para iniciar o banco:

```bash
docker compose up -d
```

A aplicação utiliza o banco `faunifica` e a collection `especies`.

## ▶️ Execução

### Pré-requisitos

* Java 21
* Maven
* Docker Desktop com suporte ao WSL 2

### 1. Inicie o MongoDB

Na pasta do projeto:

```bash
docker compose up -d
```

### 2. Execute a aplicação

No IntelliJ IDEA ou através do Maven:

```bash
mvn spring-boot:run
```

A API estará disponível, por padrão, em:

```text
http://localhost:8080
```

## 🧪 Testes

O projeto possui testes automatizados utilizando JUnit 5, Mockito e MockMvc.

Para executar os testes e gerar o relatório de cobertura de código com o JaCoCo::

```bash
mvn test
```

O relatório será gerado no arquivo em:

```
target/site/jacoco/index.html
```

Abra o arquivo index.html no navegador para visualizar as estatísticas de cobertura, incluindo a porcentagem de linhas, métodos e classes testadas.

O projeto possui como requisito mínimo uma cobertura de 70%.

## 🔗 Principais endpoints

| Método | Endpoint                    | Descrição                 |
| ------ | --------------------------- | ------------------------- |
| POST   | `/especies`                 | Cadastra uma espécie      |
| GET    | `/especies`                 | Lista as espécies         |
| GET    | `/especies?nomePopular=...` | Pesquisa por nome popular |
| GET    | `/especies/{id}`            | Consulta uma espécie      |
| PUT    | `/especies/{id}`            | Atualiza uma espécie      |
| DELETE | `/especies/{id}`            | Exclui uma espécie        |
