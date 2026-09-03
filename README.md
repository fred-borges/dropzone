# 📦 DropZone

Um sistema simples e eficiente de transferência de arquivos. Com o **DropZone**, você pode fazer o upload de um arquivo, definir um código de segurança opcional e gerar um link único junto ao código para que outra pessoa possa realizar o download de forma rápida e segura.

## 🛠️ Tecnologias Utilizadas

- **Java** (Spring Boot)
- **HTML5 & CSS3**
- **Supabase** (Armazenamento de arquivos / Storage)
- **PostgreSQL** (Banco de dados)

## 🚀 Como Executar o Projeto

### 📋 Pré-requisitos

Antes de começar, certifique-se de que tem instalado na sua máquina:
- **Java 21** ou superior
- **Maven** (opcional, já que o projeto inclui o Maven Wrapper `./mvnw`)
- Uma conta no **Supabase** (com um Bucket de Storage criado)
- Uma base de dados **PostgreSQL** ativa

---

### ⚙️ Configuração das Variáveis de Ambiente

O projeto utiliza a biblioteca `dotenv-java` para gerir dados sensíveis de forma segura.

1. Na raiz do projeto, crie um ficheiro chamado `.env` (pode usar o `.env.example` como base).
2. Adicione as suas credenciais da base de dados e do Supabase:

```env
DB_URL=jdbc:postgresql://seu-host:porta/sua-base-de-dados
DB_USERNAME=seu_usuario_postgres
DB_PASSWORD=sua_senha_postgres

SUPABASE_URL=https://supabase.co
SUPABASE_SECRET_KEY=sua_chave_secreta_do_supabase
SUPABASE_BUCKET=dropzone-fil
```

---

### 💻 Passo a Passo para Rodar Localmente

Abra o seu terminal e execute os seguintes comandos:

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

A aplicação estará disponível no seu navegador em `http://localhost:8080`.


## ⚙️ Funcionalidades Atuais

- **Upload de Ficheiros:** Envio rápido e simples de ficheiros para a plataforma.
- **Armazenamento Seguro:** Integração direta com o Supabase Storage para guardar os arquivos salvos.
- **Geração de Códigos:** Criação de um código de acesso para o ficheiro carregado.
- **Histórico:** Listagem visível dos ficheiros que já foram enviados para o sistema.

## 🚀 Próximos Passos (Em Desenvolvimento)

- **Geração de Links Únicos:** Criar links diretos que, junto ao código, permitem o download imediato.
- **Associação Estrita:** Vincular cada ficheiro diretamente ao código de segurança definido para garantir que apenas quem tem o código consegue aceder ao ficheiro.

---

## ✒️ Autor

Desenvolvido com 💻 por **Frederico Borges**.

Se gostou deste projeto ou quer contribuir, sinta-se à vontade para abrir uma *Issue* ou enviar um *Pull Request* no repositório!
