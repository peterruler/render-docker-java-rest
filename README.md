## Prequesites

- Java 8 - https://openjdk.org/install/
- Maven - https://maven.apache.org/install.html
- Git SCM installed - https://git-scm.com/downloads
- Github account (also possible to login to render.com) - https://github.com
- IDE like free IntelliJ Community Edition - https://www.jetbrains.com/idea/download
- PGAdmin - https://www.pgadmin.org/download
- Docker Desktop or Linux install of Docker - https://www.docker.com/products/docker-desktop

## DEMO on render.com in FREE PLAN with 10 Minutes startuptime

- https://spring-boot-rest.onrender.com/swagger-ui.html
- payed plan can be chosen when installing the docker container
- can be free cloud PAAS with easy CI/CD!
- Need a github or gitlab repo for deployment

## deploy Nodejs to render Free Webservice/PAAS hosting

- Tutorial nodejs deployment: https://www.youtube.com/watch?v=39ngI2PF43Q 

## Github

- Used to deploy CI/CD this repository (possible to fork this repo then use to deploy)
- Used to login to render

## Connection string for postgres db
- Tutorial postgres on render.com: https://www.youtube.com/watch?v=KKoMffBhvQo
- APPLICATION WILL RUN WHEN CONNECTION STRING HOST & PASSWORD IS SET.
- MUST BE BUILT WITH CORRECT CONNECTIONSTRING
- in render webinterface choose postgres type and create a db
- for Java you choose docker type and connect to your repo with the content of this repo!
- After you created a db on render or any other host you can set the CONNECTIONSTRING in your application.properties and install the db tables.
- In `src/main/resources/application.properties` add the following line:
- `spring.datasource.url=jdbc:postgresql://<SUBDOMAIN-HOST>.frankfurt-postgres.render.com/java_database_name?currentSchema=public&user=java_database_name_user&password=<DBPASSWORD>`
- WHILE REPLACING `<SUBDOMAIN-HOST>` and `<DBPASSWORD>` with your settings!

## render.com setup

- Choose: free plan -> new webservice -> docker -> Choose this repo from the dropdown in render webinterface

## dot env file, each key-value pair on a new line
- create a `.env` file in the root of this project with the following content:
- Replace `<SUBDOMAIN-HOST>` and `<DBPASSWORD` with your db credentials:

```
PORT=8080
JDBC_DATABASE_URL=jdbc:postgresql://<SUBDOMAIN-HOST>.frankfurt-postgres.render.com/java_database_name
username=java_database_name_user
password=<DBPASSWORD>
```

## render.com env variable, add while deploying to render.com

- in webinterface please add `PORT` with value of `8080` to the environment variables in render.com settings
- otherwise the app won't be reachable!
 
## db schema

- with pgadmin app (download & install) connect to your native render postgresdb with host, username & password received from db installation on render.

- `CREATE DATABASE java_database_name` Add a schema in pgadmin `public` and add tables to this schema.

```set transaction read write;
  DROP TABLE IF EXISTS Project CASCADE;
  DROP TABLE IF EXISTS Issue CASCADE;
  CREATE TABLE Project (
  id BIGSERIAL PRIMARY KEY,
  client_id VARCHAR ( 255 ) NOT NULL,
  title VARCHAR ( 255 ) NOT NULL,
  active boolean NOT NULL
  );
  CREATE TABLE Issue (
  id BIGSERIAL PRIMARY KEY,
  client_id VARCHAR ( 255 ) NOT NULL,
  project_id BIGINT NOT NULL,
  done boolean NOT NULL,
  title VARCHAR ( 255 ) NOT NULL,
  due_date date NOT NULL,
  priority VARCHAR(5) NOT NULL
  );
  ```
## Deploy project to render

- init fresh repository (`<USERNAME>` replace with your github username):
```
sudo rm -R .git
git init
git add .
git remote add origin git@github.com:<USERNAME>/render-docker-java-rest
git commit -m "initial commit"
git push -u origin master
```
- or checkout the project:
```
git clone https://github.com/peterruler/render-docker-java-rest.git
```

## build project

- `mvn clean install`
- In order to run the docker container on render.com you need a built `*.jar` file that is existant in your git repository.
- build on deployment is possible but may take long time.

## build & run

- `sudo chmod +x mvnw`
- `sudo ./mvnw package && java -jar target/java-getting-started-1.0.jar`

## run project

- `mvn spring-boot:run`
or
- `java -jar target/java-getting-started-1.0.jar`

## call app
 - `http://localhost:8080`
 -  Swagger api docu: `http://localhost:8080/swagger-ui.html`

## Free port 8080

- `lsof -t -i tcp:8080 | xargs kill`

## sample calls

- POST: http://localhost:8080/api/projects
- payload:
- `{
  "id": 1,
  "client_id": "2222",
  "title": "Bar",
  "active": false
  }`
- GET: http://localhost:8080/api/projects


## install docker on windows/macos

- download & install docker desktop

## build docker

- build docker image: `sudo docker build -t spring-boot-rest-docker .`

## run docker container

- run docker container: `docker run -p 8080:8080 spring-boot-rest-docker`