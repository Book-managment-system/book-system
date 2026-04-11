# 📚 Book Management System

An online bookstore system that supports two types of users: **Administrators** and **Customers**. The system manages books, publishers, stock levels, orders, sales transactions, shopping carts, and user accounts.

## 🏗️ Architecture

This is a full-stack web application built with:

- **Frontend**: Next.js (TypeScript) - Modern React framework for server-side rendering
- **Backend**: Spring Boot (Java) - RESTful API with JWT authentication
- **Database**: PostgreSQL 16 - Relational database for data persistence
- **Containerization**: Docker & Docker Compose - Easy deployment and development

## 🚀 Features

### For Customers
- 🛒 Browse and search books
- 🛍️ Shopping cart management
- 📦 Place and track orders
- 👤 User account management
- 💳 Order history and transactions

### For Administrators
- 📖 Book inventory management (CRUD operations)
- 📚 Publisher management
- 📊 Stock level monitoring
- 🔍 Order and sales tracking
- 👥 User management

## 🛠️ Technology Stack

### Frontend
- **Framework**: Next.js
- **Language**: TypeScript 
- **Styling**: CSS/Tailwind 
- **HTTP Client**: Axios/Fetch API

### Backend
- **Framework**: Spring Boot
- **Language**: Java 
- **Security**: JWT (JSON Web Tokens)
- **ORM**: Spring Data JPA
- **Database**: PostgreSQL

### DevOps
- **Containerization**: Docker
- **Orchestration**: Docker Compose
- **Database**: PostgreSQL 16 Alpine

## 📋 Prerequisites

Before running this application, ensure you have the following installed:

- [Docker](https://www.docker.com/get-started) (v20.10+)
- [Docker Compose](https://docs.docker.com/compose/install/) (v2.0+)
- [Git](https://git-scm.com/downloads)

## 🔧 Installation & Setup

### 1. Clone the Repository

```bash
git clone https://github.com/Book-managment-system/book-system.git
cd book-system
```

### 2. Environment Configuration

Create a `.env` file in the root directory using the provided example:

```bash
cp .env.example .env
```

Edit the `.env` file with your configuration:

```env
# PostgreSQL Configuration
POSTGRES_DB=bookstore
POSTGRES_USER=admin
POSTGRES_PASSWORD=your_secure_password
POSTGRES_PORT=5433

# Spring Boot Configuration
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/bookstore
SPRING_DATASOURCE_USERNAME=admin
SPRING_DATASOURCE_PASSWORD=your_secure_password
SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.postgresql.Driver

# JWT Configuration
JWT_SECRET=your_super_secret_jwt_key_here
JWT_ACCESS_TOKEN_EXPIRATION_MINUTES=60
JWT_REFRESH_TOKEN_EXPIRATION_DAYS=7

# SQL Initialization
SPRING_SQL_INIT_MODE=always
SPRING_SQL_INIT_PLATFORM=postgresql
SPRING_SQL_INIT_SEPARATOR=;

# Next.js Configuration
NEXT_PUBLIC_BACKEND_URL=http://localhost:8080
BACKEND_URL=http://backend:8080
```

### 3. Build and Run with Docker Compose

```bash
# Build and start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop all services
docker-compose down

# Stop and remove volumes (clean database)
docker-compose down -v
```

### 4. Access the Application

Once all containers are running:

- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8080
- **Database**: localhost:5433 (from host machine)

## 📂 Project Structure

```
book-system/
├── backend/                    # Spring Boot application
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/          # Java source code
│   │   │   └── resources/     # Application properties, schema.sql
│   │   └── test/              # Unit and integration tests
│   ├── Dockerfile
│   └── pom.xml                # Maven dependencies
│
├── frontend/                   # Next.js application
│   ├── src/
│   │   ├── app/               # Next.js app directory
│   │   ├── components/        # React components
│   │   └── utils/             # Utility functions
│   ├── Dockerfile
│   └── package.json
│
├── .env.example               # Environment variables template
├── docker-compose.yml         # Docker orchestration
├── alldb_backup.sql          # Database backup/seed file
└── README.md
```

## 🗄️ Database

The application uses PostgreSQL 16 with the following key tables:

- **Users** - Customer and admin accounts
- **Books** - Book inventory
- **Publishers** - Publisher information
- **Orders** - Customer orders
- **OrderItems** - Order line items
- **ShoppingCart** - User shopping carts
- **Transactions** - Sales records
- **Stock** - Inventory levels

The database schema is automatically initialized on first run via `schema.sql`.

## 🔐 Authentication

The system uses JWT (JSON Web Token) for authentication:

- **Access Tokens**: Short-lived tokens for API requests (default: 15 minutes)
- **Refresh Tokens**: Long-lived tokens for obtaining new access tokens (default: 7 days)

## 🐳 Docker Services

### PostgreSQL Database
- **Image**: postgres:16-alpine
- **Container**: book-system-db
- **Port**: 5433:5432
- **Health Check**: Automatic readiness check

### Spring Boot Backend
- **Build Context**: ./backend
- **Container**: book-system-backend
- **Port**: 8080:8080
- **Depends On**: postgres (healthy)

### Next.js Frontend
- **Build Context**: ./frontend
- **Container**: book-system-frontend
- **Port**: 3000:3000
- **Depends On**: backend

## 🧪 Development

### Running Locally Without Docker

#### Backend
```bash
cd backend
./mvnw spring-boot:run
```

#### Frontend
```bash
cd frontend
npm install
npm run dev
```

### Database Restore

To restore the database from backup:

```bash
# Copy backup into container
docker cp alldb_backup.sql book-system-db:/tmp/

# Restore database
docker exec -i book-system-db psql -U admin -d bookstore < /tmp/alldb_backup.sql
```



