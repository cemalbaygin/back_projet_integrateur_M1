
## Sommaire

 - [Schéma de base de données](#schéma-de-base-de-données)
 - [Installation du projet](#installation-du-projet)
 - [Diagramme UML du projet](#diagramme-uml-du-projet)
 - [Rapport du projet](#rapport-du-projet)
 - [Tous nos documents de conception sur Notion] (#tous-nos-documents-de-conception-sur-notion)

## Schéma de base de données

Ce projet est conforme au schéma simple de base de données ci-dessous :

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

Vous devez renseigner le nom de votre schéma et définir la première ligne avec vos nom et prénom

## Installation du projet

Changez la configuration dans src/main/resources/application.properties:

```
spring.datasource.url=jdbc:oracle:thin:@im2ag-oracle.univ-grenoble-alpes.fr:1521:IM2AG

spring.datasource.username=<YOUR_USERNAME>

spring.datasource.password=<YOUR_ORACLE_TOKEN>

spring.profiles.active=dev

```

Pour récupérer votre token personnel:

https://im2ag-wiki.univ-grenoble-alpes.fr/doku.php?id=environnements:oracle

Pour compiler le server :
 
```
\>mvn clean install
```

Pour lancer le server :
 
```
\>mvn spring-boot:run
```

Pour tester l'installation, aller sur l'URL :

*http://<URL_DE_LA_VM>:8080/hello*

## Diagramme UML du projet

![Diagramme UML ](https://user-images.githubusercontent.com/121033914/216333398-f3ac8033-d464-4978-935e-af58a0612b66.jpg)

## Rapport du projet
[Rapport du projet](https://github.com/cemalbaygin/back_projet_integrateur_M1/files/10569314/Projet_BDI_G_9.pdf)

## Tous nos documents de conception sur Notion

https://jelly-conga-3a7.notion.site/Notre-projet-int-grateur-da1887951b0f4aff994be91284d0caa4
