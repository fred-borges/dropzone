# 📦 DropZone

Um sistema simples e eficiente de transferência de ficheiros.

Com o **DropZone**, é possível fazer o upload de um ficheiro e gerar um **código de transferência** que permite a outra pessoa aceder aos ficheiros através da página de receção.

As transferências ficam disponíveis durante **24 horas**, após as quais expiram automaticamente.

## 🌐 Aplicação Online

📤 **Enviar ficheiros:**
https://dropzone-p52u.onrender.com/

📥 **Receber ficheiros:**
https://dropzone-p52u.onrender.com/receive

---

## 🛠️ Tecnologias Utilizadas

* **Java** (Spring Boot)
* **Thymeleaf**
* **HTML5 & CSS3**
* **Tailwind CSS**
* **JPA / Hibernate**
* **Lombok**
* **PostgreSQL**
* **Supabase**
* **Supabase Storage**
* **Git & GitHub**
* **Render**

---

## 🚀 Como Executar o Projeto

### 📋 Pré-requisitos

Antes de começar, certifique-se de que tem instalado na sua máquina:

* **Java 21** ou superior
* **Maven** (opcional, já que o projeto inclui o Maven Wrapper `./mvnw`)
* Uma conta no **Supabase**
* Um **Bucket de Storage** criado no Supabase
* Uma base de dados **PostgreSQL** ativa

---

### ⚙️ Configuração das Variáveis de Ambiente

O projeto utiliza a biblioteca `dotenv-java` para gerir dados sensíveis através de variáveis de ambiente.

1. Na raiz do projeto, crie um ficheiro chamado `.env`.
2. Pode utilizar o `.env.example` como base.
3. Adicione as credenciais necessárias:

```env
DB_URL=jdbc:postgresql://seu-host:porta/sua-base-de-dados
DB_USERNAME=seu_usuario_postgres
DB_PASSWORD=sua_senha_postgres

SUPABASE_URL=https://supabase.co
SUPABASE_SECRET_KEY=sua_chave_secreta_do_supabase
SUPABASE_BUCKET=dropzone-fil
```

> ⚠️ Nunca partilhe ou faça commit das suas credenciais reais no GitHub.

---

### 💻 Passo a Passo para Rodar Localmente

Abra o terminal e execute os seguintes comandos:

1. **Clonar o repositório:**

```bash
git clone https://github.com/fred-borges/dropzone.git
```

2. **Entrar na pasta do projeto:**

```bash
cd dropzone
```

3. **Executar a aplicação:**

```bash
./mvnw spring-boot:run
```

A aplicação estará disponível no navegador em:

```text
http://localhost:8080
```

---

## ⚙️ Funcionalidades Atuais

* **📤 Upload de Ficheiros:** Envio de ficheiros para a plataforma.
* **🔑 Código de Transferência:** Geração de um código associado à transferência.
* **📥 Receção através de Código:** Permite introduzir um código para aceder aos ficheiros disponíveis.
* **📋 Listagem de Ficheiros:** Apresentação dos ficheiros associados ao código introduzido.
* **⏳ Expiração Automática:** As transferências ficam disponíveis durante **24 horas** e expiram automaticamente após esse período.
* **📱 Design Responsivo:** Interface adaptada para diferentes tamanhos de ecrã.
* **🎨 Interface com Tailwind CSS:** Frontend desenvolvido com Tailwind CSS para uma experiência simples e responsiva.

---

## 🚀 Próximos Passos (Em Desenvolvimento)

Algumas funcionalidades planeadas para futuras versões:

* **🔐 Autenticação:** Implementação de autenticação de utilizadores.
* **👤 Sistema de Utilizadores:** Criação e gestão de contas.
* **📊 Dashboard:** Área pessoal para gestão das transferências.
* **📧 Envio por Email:** Possibilidade de enviar informações da transferência por email.
* **🔗 Links de Partilha:** Geração de links para facilitar o acesso às transferências.
* **📱 QR Codes:** Utilização de QR Codes para facilitar a partilha das transferências.

---

## 🎯 Objetivo do Projeto

O **DropZone** é um projeto pessoal desenvolvido de forma independente.

O principal objetivo foi aprender e colocar em prática o desenvolvimento de uma aplicação desde o início, explorando diferentes tecnologias e conceitos de desenvolvimento backend e frontend.

Durante o desenvolvimento foram trabalhados conceitos como:

* APIs REST
* Spring Boot
* JPA / Hibernate
* PostgreSQL
* Supabase
* Supabase Storage
* Upload de ficheiros
* Arquitetura de aplicações
* Tratamento de erros
* Git e GitHub
* Desenvolvimento de interfaces responsivas
* Deployment de aplicações

---

## ✒️ Autor

Desenvolvido com 💻 por **Frederico Borges**.

Este projeto foi criado como parte do meu percurso de aprendizagem em desenvolvimento de software, com o objetivo de ganhar experiência prática através da construção de um projeto próprio.

Se gostou deste projeto ou tem alguma sugestão, sinta-se à vontade para abrir uma *Issue* ou enviar um *Pull Request* no repositório.

---

⭐ **Se o projeto foi útil ou interessante, considere deixar uma estrela no repositório!**
