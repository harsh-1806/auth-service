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
- [To-do](#To-do)

[//]: # (- [Running Tests]&#40;#running-tests&#41;)

[//]: # (- [Contributing]&#40;#contributing&#41;)

[//]: # (- [License]&#40;#license&#41;)

[//]: # (- [Contact]&#40;#contact&#41;)

## Introduction
AuthService is a microservice responsible for handling user authentication and authorization in the Expense Tracker application. It manages user registration, login, JWT token generation, and validation.
Based on [Lovepreet Singh's](https://github.com/AlphaDecodeX) YT Playlist.

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
    git clone https://github.com/harsh-1806/auth-service.git
    cd auth-service
    ```

2. **Set up PostgreSQL**:
    - Ensure PostgreSQL is running.
    - Create a database for the service.

3. **Configure environment variables**:
    - Add the necessary configuration variables (see Configuration section).

4. **Build and run the application**:
    ```bash
    ./mvnw clean install
    ./mvnw spring-boot:run
    ```

### Configuration
#### Environment Variables
(With Default Values)
```dosini
POSTGRES_HOST=localhost
POSTGRES_PORT=5432
POSTGRES_USER=root
POSTGRES_PASSWORD='password'
POSTGRES_DB=auth_db
KAFKA_HOST=localhost
KAFKA_PORT=9092
AUTH_SERVICE_PORT=9898
```

1. **Clone the repository**:
    ```bash
    git clone https://github.com/harsh-1806/auth-service.git
    cd auth-service
    ```

2. **Set up PostgreSQL**:
    - Ensure PostgreSQL is running.
    - Create a database for the service.

3. **Configure environment variables**:
    - Add the necessary configuration variables (see Configuration section).

### Configuration
#### Environment Variables
(With Default Values)
```plaintext
POSTGRES_HOST=localhost
POSTGRES_PORT=5432
POSTGRES_USER=root
POSTGRES_PASSWORD='password'
POSTGRES_DB=auth_db
KAFKA_HOST=localhost
KAFKA_PORT=9092
AUTH_SERVICE_PORT=9898
```
### Usage
After starting the service, you can use the following endpoints to register and authenticate users.

### API Endpoints
#### User registration
- Endpoint : `api/v1/signup`
- Method : `POST`
- Request Body :
```json
{
    "username": "username51",
    "password": "123345678",
    "first_name": "yourFirstName",
    "last_name": "yourLastName",
    "phone_number": "1234567890",
    "email": "harsh@google.com"
}
```
- Response Body :
```json
{
    "accessToken": "yourAccessToken",
    "token": "yourTokenId",
    "userId": "userId"
}
```
#### User Login
- Endpoint : `api/v1/login`
- Method : `POST`
- Request Body :
```json
{
    "username": "username51",
    "password": "123345678"
}
```
- Response Body :
```json
{
    "accessToken": "yourAccessToken",
    "token": "yourTokenId",
    "userId": "userId"
}
```

## To-do
- [ ] Write some Tests
- [ ] Update End-Point for Profile Pic
- [ ] Standardized DTOs
- [ ] Implement Authorization
- [ ] API Documentation