# Spring Boot Demo Application

This is a demo Spring Boot application designed for testing and experimenting with new ideas and features. The application is containerized using Docker and includes a `docker-compose` setup to run the application alongside a MongoDB instance.

## Features
- **Spring Boot Application**: Built for testing and learning.
- **Dockerized Setup**: Simplified environment management using Docker.
- **MongoDB Instance**: MongoDB runs in a container for persistence and testing.

## Prerequisites
- Docker and Docker Compose must be installed on your system.
- Java 17+ is required for running the application locally or for building the project.
- Gradle Wrapper (`./gradlew`) is included in the repository.

## Setup and Usage

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd <repository-folder>

2. **Build the application**: Before running the containers, build the application using the Gradle wrapper.
   ```bash
   ./gradlew build

3. **Run the application and MongoDB using Docker Compose**: Start the application and MongoDB containers.
   ```bash
   docker-compose up --build

4. **Access the application**:
   1. The application will be available at http://localhost:8080.
   2. MongoDB will be running at localhost:27017.


## Generating REST API Documentation

This project uses Spring REST Docs to generate API documentation. To generate the documentation:

1. **Run the tests** (if not already run during the build):
   ```bash
   git clone <repository-url>
   cd <repository-folder>

2. Generate the **REST Docs** using the asciidoctor Gradle task: 
   ```bash
   ./gradlew build

3. After running the above command, the generated documentation will be available in the **build/docs/asciidoc** folder. Open the **index.html** file in your browser to view it.


## Cleanup
   ```bash
   docker-compose down

