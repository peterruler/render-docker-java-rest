## DEMO on render.com in FREE PLAN with 10 Minutes startuptime
- https://spring-boot-rest.onrender.com/swagger-ui.html
- payed plan can be chosen when installing the docker container
- can be free cloud PAAS with easy CI/CD!
- Need a github or gitlab repo for deployment

## Connection string for postgres db
- APPLICATION WILL RUN WHEN CONNECTION STRING HOST & PASSWORD IS SET.
- MUST BE BUILT WITH CORRECT CONNECTIONSTRING
- in render webinterface choose postgres type and create a db
- for Java you choose docker type and connect to your repo with the content of this repo!
- After you created a db on render or any other host you can set the CONNECTIONSTRING in your application.properties and install the db tables.
- In `src/main/resources/application.properties` add the following line:
- `spring.datasource.url=jdbc:postgresql://<SUBDOMAIN-HOST>.frankfurt-postgres.render.com/java_database_name?currentSchema=public&user=java_database_name_user&password=<DBPASSWORD>`
- WHILE REPLACE `<SUBDOMAIN-HOST>` and `<DBPASSWORD>` with your settings!

## dot env file, each key-value pair on a new line
- create a `.env` file in the root of this project with the following content:
- `PORT=8080`

- `JDBC_DATABASE_URL=jdbc:postgresql://<SUBDOMAIN-HOST>.frankfurt-postgres.render.com/java_database_name`

- `username=java_database_name_user`

- `password=<DBPASSWORD>`

## render.com env variable, add while deploying to render.com

- in webinterface please add `PORT` with value of `8080` to the environment variables in render.com settings
- otherwise the app won't be reachable!
 
## db schema

- with pgadmin app (download & install) connect to your native render postgresdb with host, username & password received from db installation on render.

- `CREATE DATABASE java_database_name` Add a schema in pgadmin `public` and add tables to this schema.

- `set transaction read write;
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
  );`

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