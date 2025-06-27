# Digital Banking Application

A full-stack application for managing customers and bank accounts.

![Home Page](Screenshot%202025-06-22%20155649.png)

## Technologies

- **Backend**: Spring Boot 3, Spring Data JPA, Spring Security, PostgreSQL
- **Frontend**: Angular 20

## Installation

### Backend Setup

1. Navigate to the backend directory:

   ```bash
   cd backend
   ```

2. Build and run the Spring Boot application:

   ```bash
   ./mvnw spring-boot:run
   ```

   > Note: Make sure PostgreSQL is running and configured in `application.yml`

### Frontend Setup

1. Navigate to the client directory:

   ```bash
   cd client
   ```

2. Install dependencies:

   ```bash
   npm install
   ```

3. Run the Angular application:

   ```bash
   npm start
   ```

4. Access the application at: `http://localhost:4200`

## Features

- Create and manage customers
- Create different types of bank accounts (Current, Savings)
- View account details and balances
- View customer information

## Usage

### Home Page

![Home Page](Screenshot%202025-06-22%20155649.png)
Central dashboard with quick access to customer and account management.

### Customers Management

![Customers Page](Screenshot%202025-06-22%20155701.png)

- Create new customers with name and email
- View customer list with their associated accounts
- Delete customers

### Accounts Management

![Accounts Page](Screenshot%202025-06-22%20155709.png)

- Create new bank accounts for existing customers
- Select account type (Current Account, etc.)
- Set initial balance
- View all accounts with details like balance, owner, and status
