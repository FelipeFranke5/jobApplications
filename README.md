# Job Applications

**Objetivo:** Permite facilitar o acompanhamento de aplicações à vagas
de emprego, com **features** simples: Criação de um novo registro e consulta através
do Banco de Dados. Ao invés de acompanhar cada cadastro por bloco de notas ou Excel,
acessa por um terminal (linha de comando) as funcionalidades disponíveis.

- **Consulta (Query) por nome da empresa:** Permite pesquisar por todas as
vagas (aplicações) cadastradas com o nome de uma empresa específica. Retorna uma lista contendo
informações como o **nome da vaga**, **nome da empresa**, **link da vaga** e **data da aplicação**.

- **Cadastro:** Permite cadastrar uma nova vaga.

## Tecnologias / Dependências

- **Java 17:** Criação do programa que será executado em linha de comando.
- **Maven:** Gerenciamento das dependências e ciclo de vida do projeto. O build é feito por ele, com apoio
do **maven-shade-plugin**, utilizado para criar um arquivo **.jar** contendo as dependências e também
definir o caminho da classe principal que executa o programa.
- **mysql-connector-j:** Permite uso do JDBC para conexão com o banco, incluindo os drivers necessários para conexão com o **MySQL**.
- **MYSQL:** Banco de dados utilizado para persistência dos dados.

## Como executar este projeto

Fique à vontade para fazer o download deste projeto e executá-lo na sua máquina. Será necessário
ter o **Java** instalado com a versão **17+**, sendo apenas a **JRE** ou alguma variante do **JDK**.

Caso não tenha o Java instalado, sugiro o uso dessa versão: **[Java SE Development Kit 17.0.12](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)**.

Também será necessário ter o **Maven** instalado, para que seja possível fazer o **build** do projeto, gerando um
executável **.jar** - Sugiro este link para download: **[Apache Maven 3.9.10](https://maven.apache.org/download.cgi)**.

**Importante:** Como este projeto utiliza Banco de Dados para armazenar as informações das vagas, será necessário também configurar o banco em seu ambiente.
Atualmente, o projeto suporta apenas o **MySQL**, mas futuramente pode existir suporte para outros. Para evitar problemas durante a execução do programa,
configure e inicialize o banco selecionado com **Usuário** e **Senha**. [Segue aqui](https://dev.mysql.com/doc/refman/8.4/en/windows-install-archive.html) um guia para uso do **MySQL no Windows**.

Após fazer a instalação do **Java** e do **Maven**, bem como a inicialização do banco, [baixe este repositório do Github](https://docs.github.com/en/get-started/start-your-journey/downloading-files-from-github) e abra o terminal (CMD ou bash),
executando esses comandos:

- 1º passo: Navegue até a pasta do projeto baixado:
```bash
cd /caminho/para/o/projeto
```

- 2º passo: Execute o comando para fazer o **build** do projeto:
```bash
mvn clean package
```

- 3º passo: Após o **build** ser concluído, execute o comando abaixo para rodar o programa:
```bash
java -jar target/jobapplications-1.0-SNAPSHOT.jar
```

Se tudo ocorrer conforme o esperado, o programa será iniciado no terminal mesmo, permitindo que navegue pelas opções do Menu.

## Para fazer (TODO's)

- Criar testes unitários para garantir o correto funcionamento das **features** disponíveis