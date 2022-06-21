# Bancario API Rest 
Sistema bancario, contento funções de Conta e Transferência

## Tecnologias
- Java 11
- Maven
- Spring Boot
- Spring Data JPA
- Flyway Migration
- H2 Database
- Spring DevTools
- Bena Validation
- Swagger
- Junit
- Lombok

## Diagrama entidade relacional
![image](https://user-images.githubusercontent.com/25377765/174811475-85189482-cd7d-4766-b978-83df9be74a73.png)

## H2 Database
Link: http://localhost:8080/h2
- JDBC URL: jdbc:h2:mem:db_bancario
- user name: meutudo

![image](https://user-images.githubusercontent.com/25377765/174814251-523bd31c-ca65-46f1-84a4-4cf45787d780.png)


## Swagger
Link: http://localhost:8080/swagger-ui/index.html
### transferencia-controller
![image](https://user-images.githubusercontent.com/25377765/174813549-b405bdba-b198-4cc0-aadb-ebde70a46500.png)

### conta-controller
![image](https://user-images.githubusercontent.com/25377765/174813967-09ea36e3-ec86-4a40-907a-31ec12a90638.png)

## Testes
- Utilizado MockMvc para testar os endpoints.
- Utilizado Jacoco para medir a cobertura.
![image](https://user-images.githubusercontent.com/25377765/174815190-481864ea-d750-4f55-8861-8ebc84c29256.png)

