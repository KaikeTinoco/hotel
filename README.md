# 🏨 Hotel API --- Sistema de Gestão de Hóspedes

API backend desenvolvida para gerenciar hóspedes de um hotel. A
aplicação permite **cadastro de hóspedes, controle de check-in e
check-out, cálculo automático de estadia e geração de relatórios**.

O projeto foi desenvolvido com foco em **boas práticas de APIs REST,
arquitetura em camadas e integração com banco de dados**.

------------------------------------------------------------------------

# 🚀 Tecnologias Utilizadas

-   ☕ **Java 17**
-   🌱 **Spring Boot 3**
-   🗄️ **Spring Data JPA**
-   🐘 **PostgreSQL**
-   🧪 **H2 Database** (ambiente de testes)
-   📦 **Maven**
-   🐳 **Docker**
-   🐳 **Docker Compose**
-   📑 **SpringDoc / OpenAPI 3 (Swagger)**

------------------------------------------------------------------------

# 📖 Sobre o Projeto

A **Hotel API** é um sistema backend responsável por gerenciar o fluxo
de hóspedes em um hotel.

A aplicação permite:

-   registrar hóspedes
-   realizar check-in e check-out
-   calcular automaticamente o valor da estadia
-   consultar hóspedes dentro ou fora do hotel
-   gerar relatórios com histórico de gastos

O projeto foi desenvolvido como **prática de desenvolvimento backend com
Java e Spring Boot**, aplicando conceitos como:

-   Arquitetura em camadas
-   APIs REST
-   Persistência de dados com JPA
-   Containerização com Docker
-   Documentação de API com Swagger

------------------------------------------------------------------------

# ⚙️ Funcionalidades

-   👤 **Gestão de hóspedes**
    -   Cadastro de novos hóspedes
    -   Busca por nome, documento ou telefone
    -   Remoção de hóspedes
-   🏨 **Check-in e Check-out**
    -   Registro de entrada no hotel
    -   Registro de saída
    -   Controle de permanência
-   💰 **Cálculo automático da estadia**
    -   Tarifas diferentes para dias úteis e finais de semana
    -   Cobrança adicional para veículos
    -   Taxa para check-out tardio
-   🔎 **Busca dinâmica**
    -   Filtro por nome
    -   Filtro por documento
    -   Filtro por telefone
-   📊 **Relatórios**
    -   Hóspedes atualmente no hotel
    -   Hóspedes que já fizeram check-out
    -   Histórico de gastos da estadia
-   🐳 **Containerização**
    -   Execução simplificada com Docker e Docker Compose
-   📑 **Documentação da API**
    -   Interface interativa com Swagger

------------------------------------------------------------------------

# 🧱 Estrutura do Projeto

O projeto segue uma **arquitetura em camadas**, comum em aplicações
Spring Boot.

    src
     └── main
         └── java
             └── controller
             └── service
             └── repository
             └── entity
             └── dto
             └── config

### Responsabilidade das Camadas

-   **Controller** → Recebe as requisições HTTP e expõe os endpoints da
    API
-   **Service** → Contém a lógica de negócio da aplicação
-   **Repository** → Responsável pela comunicação com o banco de dados
-   **Entity** → Representação das tabelas no banco de dados
-   **DTO** → Objetos de transferência de dados entre camadas
-   **Config** → Configurações da aplicação

------------------------------------------------------------------------

# ▶️ Como Executar o Projeto

## Pré-requisitos

Antes de iniciar, você precisa ter instalado:

-   **Java 17+**
-   **Maven**
-   **Docker**
-   **Docker Compose**

------------------------------------------------------------------------

# 🐳 Executando com Docker (Recomendado)

### 1️⃣ Clonar o repositório

``` bash
git clone https://github.com/KaikeTinoco/hotel.git
cd hotel
```

------------------------------------------------------------------------

### 2️⃣ Criar o arquivo de variáveis de ambiente

Crie um arquivo `.env` na raiz do projeto:

    DB_USERNAME=seu_usuario
    DB_PASSWORD=sua_senha

------------------------------------------------------------------------

### 3️⃣ Build e execução da aplicação

``` bash
docker-compose up --build
```

Isso irá:

-   construir as imagens Docker
-   iniciar o banco de dados
-   iniciar a API

------------------------------------------------------------------------

### 4️⃣ Acessar a aplicação

API disponível em:

    http://localhost:8081

Documentação interativa (Swagger):

    http://localhost:8081/swagger-ui.html

------------------------------------------------------------------------

# 🔗 Endpoints da API

## 👤 Gestão de Hóspedes

  ----------------------------------------------------------------------------
Método                  Endpoint                     Descrição
  ----------------------- ---------------------------- -----------------------
POST                    /guests/cadastro             Cadastra um novo
hóspede

GET                     /guests/buscaHospedes        Busca hóspedes por
nome, documento ou
telefone

GET                     /guests/buscaTodosHospedes   Lista todos os hóspedes
cadastrados

DELETE                  /guests/deletarHospedes      Remove um hóspede pelo
ID
  ----------------------------------------------------------------------------

------------------------------------------------------------------------

## 🏨 Check-in e Check-out

Método   Endpoint                    Descrição
  -------- --------------------------- -----------------------------------
POST     /checkin/cadastrarCheckin   Registra o check-in de um hóspede
DELETE   /checkin/deletarCheckin     Remove um registro de check-in
POST     /checkout/criarCheckout     Cria um registro de check-out

------------------------------------------------------------------------

## 📊 Relatórios

  -------------------------------------------------------------------------------------
Método                  Endpoint                              Descrição
  ----------------------- ------------------------------------- -----------------------
GET                     /checkout/buscarHospedesDentroHotel   Lista hóspedes
atualmente no hotel

GET                     /checkout/buscarHospedesForaHotel     Lista hóspedes que já
realizaram check-out
  -------------------------------------------------------------------------------------

------------------------------------------------------------------------

# 💰 Regras de Precificação

O valor da estadia é calculado automaticamente com base nas seguintes
regras:

-   📅 **Dias úteis:** R\$120,00 por diária
-   🎉 **Finais de semana (sábado e domingo):** R\$150,00 por diária
-   🚗 **Estacionamento:** Acréscimo de **R\$20,00 por dia**
-   ⏰ **Check-out tardio:** Caso o check-out seja realizado **após
    16:30**, será cobrada **uma diária adicional**

------------------------------------------------------------------------

# 👨‍💻 Autor

Desenvolvido por **Kaike Tinoco**

-   GitHub: https://github.com/KaikeTinoco
