# Just a simple GNU make wrapper around the Dockerized ant processes.
# Why make? Well, we're pretty much just using it as a big switch-case block, so it's not really used for the build.
# It has little in the terms of dependencies, and is already available on most *nix-based systems. We're using it
# as a simple alternative to having a bunch of bash scripts for all the tasks. There's no logic in this file, 
# only mappings between the Docker containers and parametrizations... --henhed

.PHONY: build clean deploy setup test servers help

build:
	@./ant.sh -Dwebapp.name=mengdetrening

clean:
	@./ant.sh clean -Dwebapp.name=mengdetrening

deploy:
	@./ant.sh clean deploy war -Dwebapp.name=mengdetrening 

migrate:
	@./ant.sh db-create db-prepare -Dwebapp.name=mengdetrening

seed:
	@./ant.sh db-load -Dwebapp.name=mengdetrening

seed-users:
	@./ant.sh db-load-users -Dwebapp.name=mengdetrening

setup:
	@./ant.sh setup -Dwebapp.name=mengdetrening

test:
	@./ant.sh test

servers:
	@docker-compose up -d

start: servers
	@./ant.sh clean compile db-create db-prepare db-load-users deploy -Dwebapp.name=mengdetrening

stop:
	@docker-compose stop
	@docker-compose rm -f
	@rm logs/app/* &>/dev/null
	@rm logs/tomcat/* &>/dev/null
	@rm logs/mails/* &>/dev/null

help:
	@echo "FriKomPort docker build system"
	@echo "------------------------------"
	@echo "Options:\n"
	@echo "    build         Build the application (default)"
	@echo "    clean         Clean the build folder"
	@echo "    deploy        Deploy the application to Tomcat"
	@echo "    seed          Prepare DB & seed default data"
	@echo "    setup         Setup, prepare and run everything"
	@echo "    test          Run the tests"
	@echo "    servers       Start the Tomcat & Oracle servers"
	@echo "    start         Start the servers, build the application & seed the DB"
	@echo "    stop          Teardown and remove the server containers"
	@echo "    help          You're looking at it"