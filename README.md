# BotCamp

## Description

Botcamp is a tool allowing users to make reports regarding a specific topic in their mail inbox. The tool will be able to aggregate mails regarding their subject, and will generate mail reports to clean inboxes.
It is possible to schedule auto cleaning and reporting its mailing inbox.

## Architecture
![Architecture Schema](architecture_schema.drawio.png)

The project will consist in developing a gateway to request Gmail API with Google Java Libraries with Spring Boot, and developing a Botcamp API requesting the gateway, and processing emails.

## Milestones
- [x] Get mails from Gmail
- [ ] Generate reports: it includes the scheduling of automatic report generation
- [ ] Audit feature: Persist reports into database, persist emails. it will be configurable in the scheduled tasks by the user
- [ ] Get mails from more generic API to allow interoperability with non-Gmail users
- [x] Dockerize application
- [ ] Configure deployment on remote server


## Postgres Config

On the folder `./postgres/config/`, you should create two files:

- 'postgres.env'
- 'pgadmin.env'

These two files are mandatory to make the app work. You should define your own Postgres env variables in a .env file so that you can run the postgres docker image. You should define the three following variables in a file name 'postgres.env':

- POSTGRES_PASSWORD
- POSTGRES_USER
- POSTGRES_DB
- PGPORT

For example: you can have:

- POSTGRES_USER=user
- POSTGRES_PASSWORD=password
- POSTGRES_DB=db
- PGPORT=5432

You should define the three following variables in a file name 'pgadmin.env':

- PGADMIN_DEFAULT_EMAIL
- PGADMIN_DEFAULT_PASSWORD
- PGADMIN_LISTEN_PORT

For example, you can have:

- PGADMIN_DEFAULT_EMAIL=root@root.root
- PGADMIN_DEFAULT_PASSWORD=root
- PGADMIN_LISTEN_PORT=80
