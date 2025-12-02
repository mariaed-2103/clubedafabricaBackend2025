# 🖥️ **README – BACKEND (Spring Boot + JPA + H2/SQL Server)**

```markdown
# 🖥️ Backend — Clube da Fábrica

Este é o backend oficial do **Clube da Fábrica**, um sistema completo de gestão de produtos, pedidos, usuários e fluxo operacional.  

O backend foi desenvolvido em **Java 17 + Spring Boot 3**, usando **JPA/Hibernate**, DTOs, validação, autenticação e banco configurável (H2 ou SQL Server).

---

## 🚀 Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3**
- **Spring Web**
- **Spring Data JPA**
- **Spring Security**
- **Validation API**
- **Lombok**
- **H2 Database** (dev)
- **SQL Server** (produção)
- **JWT Authentication**

---

## 🧱 Funcionalidades Principais

### 🔐 **Autenticação**
- Login de usuário  
- Cadastro de usuário com validação  
- Cadastro de admin usando `adminCode`  
- Tokens JWT  

### 👤 **Usuários**
- CRUD completo  
- Ativar / desativar usuário  
- Relação 1:N com pedidos  

### 🛍️ **Produtos**
- Cadastro  
- Listagem  
- Relacionamento com categorias  
- Controle de estoque  

### 🧺 **Pedidos**
- Criação de pedidos  
- Retirada com data e hora  
- Consulta por usuário  
- Consulta geral (admin)  
- Status: `PENDENTE`, `AGUARDANDO`, `CONCLUIDO`, `CANCELADO`  
- Notificações somem ao concluir/cancelar  

### 📦 **OrderItems**
- Relação N:N entre pedidos e produtos  
- Cálculo automático do total 

🔗 Principais Endpoints
👤 Auth
POST /auth/login
POST /auth/register
POST /auth/register-admin

🛍️ Produtos
GET /products
POST /products
PUT /products/{id}
DELETE /products/{id}

📦 Pedidos
POST /orders
GET /orders/user/{id}
GET /orders
PATCH /orders/status/{id}

✔️ Padrões Adotados

Arquitetura REST

DTOs para resposta limpa

Services contendo toda a lógica

Entities separadas do frontend

Validações usando @NotNull, @Email, @Size, etc

Segurança com JWT

CORS configurado manualmente
