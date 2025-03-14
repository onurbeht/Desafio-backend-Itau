Este projeto é uma implementação do desafio backend, Itaú Unibanco - Desafio de Programação..

Ler o arquivo DESAFIO.MD, onde haverá as informações sobre o desafio.

## 🚀 Tecnologias Utilizadas

- **[Java 21](https://www.oracle.com/br/java/technologies/downloads/)**
- **[Spring Boot 3.4.3](https://spring.io/projects/spring-boot)**
- **[Spring Web](https://docs.spring.io/spring-boot/reference/web/index.html)**
- **[Maven](maven.apache.org)** (gerenciamento de dependências e build)
- **[Swagger](https://springdoc.org/)** (Documentação da API)
- **[JUnit 5](https://junit.org/junit5/)** (Testes unitários)
- **[Mockito](https://site.mockito.org/)** (Testes unitários)

## 🛠 Configuração e Execução

### Pré-requisitos

Antes de iniciar, certifique-se de ter instalado:

- [JDK 21](https://www.oracle.com/br/java/technologies/downloads/#java21)
- [Maven](https://maven.apache.org/)

### Instalação e Execução

1. Clone o repositório:

   ```bash
   git clone https://github.com/onurbeht/Desafio-backend-Itau.git
   ```

   Entre na pasta do repositório local:

   ```bash
   cd Desafio-backend-Itau
   ```

2. Instale as dependências, compile e execute a aplicação:

   ```bash
   mvn install
   ```

   ```bash
   mvn spring-boot:run
   ```

3. Para testar, acesse a aplicação, você pode usar um client API(Ex: Postman, Insomnia) na URL padrão http://localhost:8080/api **ou pelo Swagger - http://localhost:8080/api/swagger-ui/index.html** :

   Salvar uma transacao.
   Envie o JSON no body da requisição

   ```
   POST - http://localhost:8080/api/transacao
   {
     "valor": 10.0,
     "dataHora": "yyyy-MM-dd'T'HH:mm:ss"
   }

    valor (double): Valor da transação (obrigatório, >= 0).
    dataHora (string, ISO 8601): Data e hora da transação (obrigatório, no passado).

    Retorno esperado:
        - 201 > Criado com sucesso
        - 422 > Dados invalidos(Valor negativo, Data futura)
        - 400 > JSON invalido

   ```

   Apagar as Transferencias

   ```
   DELETE - http://localhost:8080/api/transacao

   Retorno esperado:
        - 200 > Transações deletadas

   ```

   Estatisticas das transaferencias(ultimos 60 segundos)

   ```
   GET - http://localhost:8080/api/transacao/estatistica

    Retorno esperado:
        - 200 > OK
        json:
        {
            count: Número de transações nos últimos 60 segundos.
            sum: Soma dos valores das transações.
            avg: Média dos valores das transações.
            min: Menor valor das transações.
            max: Maior valor das transações.
        }

   ```

## 📜 Licença

Este projeto é desenvolvido apenas para fins educacionais e desafios de backend.

---

## Feito por **[Bruno Oliveira](https://www.linkedin.com/in/bruno-oliveira-9593aa186/)**
