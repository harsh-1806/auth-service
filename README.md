# AuthService - Expense Tracker Application

## Table of Contents
- [Introduction](#introduction)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
- [Configuration](#configuration)
- [Usage](#usage)
- [API Endpoints](#api-endpoints)
- [Running Tests](#running-tests)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

## Introduction
AuthService is a microservice responsible for handling user authentication and authorization in the Expense Tracker application. It manages user registration, login, JWT token generation, and validation.

## Features
- User registration and login
- JWT token generation
- Token validation
- Password hashing

## Technologies Used
- Spring Boot
- PostgreSQL
- JWT
- Docker
- Kafka

## Getting Started

### Prerequisites
- JDK 11 or higher (Used JDK 17)
- Docker (optional, for containerization)
- PostgreSQL
- Kafka (optional, for delegating the UserInfo to UserService)

### Installation

1. **Clone the repository**:
    ```bash
    git clone https://github.com/harsh-1806/expense-tracker-auth-service.git
    cd expense-tracker-auth-service
    ```

2. **Set up PostgreSQL**:
    - Ensure PostgreSQL is running.
    - Create a database for the service.

3. **Configure environment variables**:
    - Create a `.env` file in the root directory.
    - Add the necessary configuration variables (see Configuration section).

4. **Build and run the application**:
    ```bash
    ./mvnw clean install
    ./mvnw spring-boot:run
    ```

### Configuration
Create a `.env` file with the following variables:

```plaintext
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/yourdatabase
SPRING_DATASOURCE_USERNAME=yourusername
SPRING_DATASOURCE_PASSWORD=yourpassword
JWT_SECRET=yourjwtsecret
```
### Usage
After starting the service, you can use the following endpoints to register and log in users.

### API Endpoints
#### User registration
- Endpoint : `api/v1/signup`
- Method : `POST`
- Request Body :
```json

```
