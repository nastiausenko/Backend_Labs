# Expense Tracker API

This is a backend RESTful API application for tracking personal expenses, built with **Spring Boot**.  
It supports CRUD operations for expense entries and includes **currency conversion** using real-time rates.

## Features 

- **Category Management**: Create, retrieve, list (public and user-specific), and delete categories with access control.
- **Record Management**: Add, retrieve, delete, and list records filtered by user and/or category, with validation of user and category existence.
- **Currency Conversion:** Supports real-time currency conversion using exchange rates from multiple providers, with caching for performance optimization.
- **Security & Access Control**: User authentication and authorization to ensure only owners can modify or delete their data.

## Run the Project

>  **NOTE:** To run this project locally, you must have [Docker](https://www.docker.com/) and **docker-compose** installed on your machine.

1. Clone the repository:

```
git clone https://github.com/nastiausenko/Backend_Labs.git
```

2.	Build and run with Docker Compose:

```
docker-compose up --build
```

3.	The API will be available at: [http://localhost:8080/api/v1](http://localhost:8080/api/v1)

### [Swagger UI](http://localhost:8080/swagger-ui/index.html#/)


