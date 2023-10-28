install:
	@mvn clean install -DskipTests -U -fae

test:
	@mvn test

clean:
	@mvn clean

build: install
	@docker-compose build

logs:
	@docker-compose logs -f

prune:
	@docker system prune -a -f
	@docker image prune -f

run: install
	@docker-compose down
	@docker-compose build
	@docker-compose up

restart: install
	@docker-compose build
	@docker-compose up -d

up:
	@docker-compose up

upd:
	@docker-compose up -d

down:
	@docker-compose down