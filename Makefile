install:
	@mvn clean install -DskipTests -U -fae

test:
	@mvn test

clean:
	@mvn clean
	@./config/cleanup.sh


dbup:
	@docker-compose up postgres pgadmin

dbdown:
	@docker-compose rm -fsv postgres
	@docker-compose rm -fsv pgadmin


dbupd:
	@docker-compose up postgres pgadmin -d

build: install
	@docker-compose build

logs:
	@docker-compose logs -f

prune:
	@docker system prune -a -f
	@docker image prune -f

run: down build up

rund: down build upd


up:
	@docker-compose up

upd:
	@docker-compose up -d

down:
	@docker-compose down


.PHONY: postgres