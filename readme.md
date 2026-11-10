# 📊 ProjectFlow - Backend

### 🛠️ (frontend ainda em desenvolvimento)

API REST para **gerenciamento de projetos e equipes** com quadro **Kanban**, inspirada em ferramentas como *Jira*, *Trello* e *ClickUp*. Desenvolvida em Java + Spring Boot com persistência híbrida (MySQL + MongoDB).

## Sobre o projeto

O ProjectFlow centraliza o gerenciamento de projetos de pequenas equipes em um **único lugar**, evitando que informações fiquem espalhadas entre *planilhas*, *mensageiros* e *anotações*. 

A API permite organizar o trabalho em:
```
organizações → projetos → quadros Kanban → tarefas, com controle de permissões, comentários, anexos e histórico completo de alterações.
```

## Funcionalidades
- **Usuários e autenticação**: cadastro, login com JWT, logout, edição de perfil e foto de perfil
- **Organizações**: criação, gerenciamento de membros, aceitar/rejeitar solicitações de entrada
- **Permissões por papel**: OWNER, ADMIN e MEMBER, com restrição de funcionalidades conforme o perfil
- **Projetos**: CRUD, status (ativo/finalizado), membros e imagem do projeto
- **Quadro Kanban**: colunas configuráveis e movimentação de tarefas entre colunas
- **Tarefas**: título, descrição, prioridade, prazo, responsável, etiquetas (labels) e checklist
- **Comentários**: conversas por tarefa em ordem cronológica
- **Anexos**: upload, visualização e exclusão de arquivos nas tarefas
- **Histórico de atividades**: auditoria de criação, mudança de status, troca de responsável, comentários e exclusões
- **Notificações**: geradas automaticamente pelo sistema
- **Soft delete**: exclusão lógica (deleted_at) em todas as entidades


## Stack
```
Linguagem:	      Java
Framework:	      Spring Boot
Segurança:	      Spring Security, JWT, hash seguro de senhas
DB relacional:    Spring Data JPA + MySQL
DB NoSQL:	      Spring Data MongoDB + MongoDB
Build:	          Maven
```

## Arquitetura
O backend segue uma **arquitetura em camadas organizada por módulos de domínio (Feature-Based Architecture)**, com princípios de: 
- Clean Architecture simplificada
- Separation of Concerns e SOLID (principalmente SRP e DIP)

Cada módulo é **autocontido**, o que reduz o *acoplamento* entre funcionalidades e facilita a *manutenção* e a *evolução* do código.

## Estrutura de pastas
```bash
src/main/java/br/com/projectflow
├── common/
├── config/
├── exception/
├── infrastructure/
├── security/
├── modules/ # mesma estrutura em test/
│   └── activitylog/
│   ├── attachment/
│   ├── auth/
│   ├── user/
│   ├── organization/
│   ├── project/
│   ├── board/
│   ├── task/
│   ├── comment/
│   ├── notification/
└── ProjectFlowApplication.java 
```

### Estrutura interna de um módulo (exemplo: task):
```bash
task/
├── constants/
├── controller/
├── dto/
├── entity/
├── service/
├── repository/
├── mapper/
├── validator/
└── enums/

# em test/
task/
├── integrationTests/
│   ├── taskController/
│   |   └── TaskBadTests.java
│   |   └── TaskHappyTests.java
├── unitTests/
│   ├── taskChecklistService/
│   |   └── taskChecklistBadTests.java
│   |   └── taskChecklistHappyTests.java
│   ├── taskLabelService/
│   ├── taskService/
└── 
```


## Padrões e boas práticas aplicados
- Layered Architecture e modularização por domínio
- Repository Pattern
- DTO Pattern (entidades nunca são expostas diretamente pela API)
- Mapper Pattern
- Specification Pattern (filtros dinâmicos)
- Tratamento global de exceções


## Persistência híbrida
O projeto usa dois bancos, cada um para o que faz melhor:

- **MySQL:** guarda os dados estruturados e relacionais - usuários, organizações, membros, projetos, quadros, colunas, tarefas, comentários, etiquetas e checklists.

- **MongoDB:** guarda documentos de estrutura flexível e conteúdo binário - imagens (perfil, organização, projeto), anexos, histórico de atividades e notificações. As entidades do MySQL referenciam os documentos do Mongo por ID.


## Segurança
- Autenticação stateless com **JWT**
- Senhas armazenadas com **hash seguro** (nunca em texto puro)
- Autorização por papel (*OWNER, ADMIN, MEMBER*) aplicada na camada de serviço
- Credenciais inválidas bloqueadas, com respostas de erro padronizadas

## Requisitos não funcionais atendidos
- Tempo de resposta inferior a 2s nas operações comuns
- Comunicação exclusivamente via API REST
- Código separado em camadas (apresentação, regras de negócio e acesso a dados)
- Multiplataforma (Windows, Linux e macOS)


## 🐋 Instalação e execução com Docker
O backend (API + MySQL + MongoDB) roda em containers. 

O app desktop (Electron) será executado separadamente e se conectara à API em `http://localhost:8080`. 🛠️ (frontend ainda em desenvolvimento)

### Pré-requisitos
- [Git](https://git-scm.com/)
- [Docker Desktop](https://www.docker.com/products/docker-desktop/) (ou Docker Engine + Compose no Linux)

### Passo a passo

1. Clone o repositório:

   ```bash
   git clone https://github.com/Viniciusrodd/ProjectFlow.git
   cd ProjectFlow
   ```

2. Crie o arquivo `.env` a partir do exemplo:

   ```bash
   # Linux / macOS / Git Bash / PowerShell
   cp .env.example .env

   # CMD (Windows)
   copy .env.example .env
   ```

   Edite o `.env` e troque as senhas/`JWT_SECRET` por seus valores. 
   
   O `JWT_SECRET` é uma *chave secreta*: use uma string aleatória com pelo menos 32 caracteres,
   por exemplo `k8Jx2vQ9mZpL4rT7wB1nYc5dFgH3sA6e`.
   Você pode gerar uma com qualquer *gerador de senhas* ou pelo *terminal*:

   ```bash
   # Linux / macOS / Git Bash
   openssl rand -base64 48
   ```

3. Suba a aplicação:

   ```bash
   docker compose up --build
   ```

A API fica disponível em `http://localhost:8080`.

### Encerrando

```bash
docker compose stop      # para os containers (mantém os dados)
docker compose down      # remove containers e rede (mantém os dados)
docker compose down -v   # remove tudo, incluindo o banco de dados
```