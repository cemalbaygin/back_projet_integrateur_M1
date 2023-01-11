# Notes

## Database schema

This project is compliant with the simple schema of database below:

```
USE <YOUR_SCHEMA>;

CREATE TABLE PERSON(

  first_name VARCHAR2(50) NOT NULL,

  last_name VARCHAR2(50) NOT NULL,

  PRIMARY KEY(first_name, LAST_name)

);

INSERT INTO PERSON VALUES ('<YOUR_FIRST_NAME>', '<YOUR_LAST_NAME>');

SELECT * FROM PERSON;
```

You must fill the name of your schema and define the first row with your first and last names

## Project installation

Change the configuration in src/main/resources/application.properties:

```
spring.datasource.url=jdbc:oracle:thin:@im2ag-oracle.univ-grenoble-alpes.fr:1521:IM2AG

spring.datasource.username=<YOUR_USERNAME>

spring.datasource.password=<YOUR_ORACLE_TOKEN>

spring.profiles.active=dev

```

To get your personal token:

https://im2ag-wiki.univ-grenoble-alpes.fr/doku.php?id=environnements:oracle

 Compile the server :

\>mvn clean install

 Launch the server :

\>mvn spring-boot:run

To test the installation, go to this URL :

*http://<URL_DE_LA_VM>:8080/hello*
