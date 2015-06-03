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

setup:
	@./ant.sh setup -Dwebapp.name=mengdetrening

test:
	@./ant.sh test

servers:
	@docker-compose up

help:
	@echo "FriKomPort docker build system"
	@echo "------------------------------"
	@echo "Options:\n"
	@echo "    build         Build the application (default)"
	@echo "    clean         Clean the build folder"
	@echo "    deploy        Deploy the application to Tomcat"
	@echo "    setup         Setup, prepare and run everything"
	@echo "    test          Run the tests"
	@echo "    servers       Start the Tomcat & Oracle servers"
	@echo "    help          You're looking at it"