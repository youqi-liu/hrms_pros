# HRMS Pro - Enterprise Human Resource Management System

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Vue](https://img.shields.io/badge/Vue-2.7.10-green.svg)](https://vuejs.org/)

[简体中文](README.md) | English

## 📖 Introduction

HRMS Pro is an enterprise-level Human Resource Management System built with Spring Boot and Vue.js, designed to provide comprehensive HR management solutions for enterprises. The system adopts a front-end and back-end separation architecture, covering modules such as personnel management, attendance management, salary management, recruitment management, and more.

### ✨ Key Features

- 👥 **Personnel Management**: Employee information, department management, position management, job title management
- 📊 **Data Management**: Basic data maintenance, data dictionary management
- 📝 **Notice Management**: Notice publishing, notice viewing, message board
- 💰 **Salary Management**: Salary management and inquiry
- 📋 **Leave Management**: Leave application and approval
- 🔐 **Permission Management**: User management, role management, menu management
- 📜 **Log Management**: Operation log recording and query
- 📁 **File Management**: File upload, download, and management

## 🏗️ Technology Stack

### Backend

| Technology | Version | Description |
|------|------|------|
| Spring Boot | 3.1.0 | Core Framework |
| Spring Security | - | Security Framework |
| Spring Data JPA | - | ORM Framework |
| MyBatis Plus | 3.5.3.1 | Persistence Framework |
| MySQL | 8.0+ | Database |
| Redis | - | Cache |
| Druid | 1.2.18 | Connection Pool |
| Redisson | 3.21.3 | Redis Client |
| Hutool | 5.8.19 | Java Utility Library |
| FastJSON2 | 2.0.33 | JSON Processing |
| Apache POI | 3.10 | Excel Import/Export |
| Lombok | - | Code Simplification |

### Frontend

| Technology | Version | Description |
|------|------|------|
| Vue | 2.7.10 | Frontend Framework |
| Vue Router | 3.5.4 | Routing |
| Vuex | 3.6.2 | State Management |
| View Design (iView) | 4.7.0 | UI Components |
| Axios | 0.27.2 | HTTP Client |
| @antv/g2plot | 2.4.19 | Data Visualization |
| XLSX | 0.15.6 | Excel Processing |

### Project Structure

```
hrms_pros/
├── hrms_pro_back/          # Backend Project
│   ├── src/main/
│   │   ├── java/cn/lyp/
│   │   │   ├── basics/     # Basic Components (Exception handling, Security, Utils)
│   │   │   ├── data/       # Data Management Module
│   │   │   ├── rs/         # HR Management Module
│   │   │   └── test/       # Test Module
│   │   └── resources/
│   │       └── application.yml
│   └── pom.xml
├── hrms_pro_front/         # Frontend Project
│   ├── public/
│   ├── src/
│   │   ├── api/           # API Interfaces
│   │   ├── libs/          # Utilities
│   │   ├── router/        # Route Configuration
│   │   ├── store/         # State Management
│   │   └── views/         # Page Components
│   ├── package.json
│   └── vue.config.js
├── s079.sql               # Database Initialization Script
└── README.md
```

## 🚀 Quick Start

### Prerequisites

- JDK 11 or higher
- Node.js 14+ & NPM
- MySQL 8.0+
- Redis 5.0+
- Maven 3.6+

### Installation

#### 1. Clone Repository

```bash
git clone https://github.com/youqi-liu/hrms_pros.git
cd hrms_pros
```

#### 2. Database Setup

```bash
# Create database
mysql -u root -p
CREATE DATABASE hrms_pro DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# Import initialization script
mysql -u root -p hrms_pro < s079.sql
```

#### 3. Backend Configuration and Startup

```bash
cd hrms_pro_back

# Modify configuration file src/main/resources/application.yml
# Configure database connection
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/hrms_pro?useUnicode=true&characterEncoding=utf-8&useSSL=false
    username: root
    password: your_password

# Configure Redis connection
spring:
  data:
    redis:
      host: localhost
      port: 6379
      password: your_redis_password

# Build and run with Maven
mvn clean install
mvn spring-boot:run

# Or run LypApplication.java directly in IDE
```

Backend service runs at: `http://localhost:8081/api/v1`

#### 4. Frontend Configuration and Startup

```bash
cd hrms_pro_front

# Install dependencies
npm install

# Start development server
npm run dev
```

Frontend service runs at: `http://localhost:8080`

#### 5. Production Build

```bash
# Backend build
cd hrms_pro_back
mvn clean package
# Generated jar package in target/ directory

# Frontend build
cd hrms_pro_front
npm run build
# Generated static files in dist/ directory
```

## 📝 Usage

### Default Account

- Admin Account: `admin`
- Default Password: Check database initialization script

### Module Description

1. **Login & Registration**: User login, registration, password reset
2. **Dashboard**: System overview and statistics
3. **Personnel Management**:
   - Department Management: Organizational structure maintenance
   - Employee Management: CRUD operations for employee information
   - Position Management: Position information maintenance
   - Job Title Management: Job title information maintenance
4. **Notice Management**:
   - User Notices: Publish and view notices
   - Message Board: Employee interaction
5. **Salary Management**: Salary entry and inquiry
6. **Leave Management**: Leave application and approval workflow
7. **System Management**:
   - User Management: System user maintenance
   - Role Management: Role permission configuration
   - Menu Management: System menu configuration
   - Dictionary Management: Data dictionary maintenance
   - Log Management: Operation log query

## 🔧 Configuration

### Backend Configuration

Main configuration file: `hrms_pro_back/src/main/resources/application.yml`

```yaml
server:
  port: 8081                    # Service port
  servlet:
    context-path: /api/v1       # Context path

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/hrms_pro
    username: root
    password: your_password
  data:
    redis:
      host: localhost
      port: 6379
      password: your_redis_password

mybatis-plus:
  global-config:
    db-config:
      logic-delete-value: 1      # Logical delete value
      logic-not-delete-value: 0
```

### Frontend Configuration

API interface configuration: `hrms_pro_front/src/api/index.js`

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Code Standards

- Backend: Follow Alibaba Java Development Manual
- Frontend: Follow Vue.js Style Guide
- Commit Messages: Use Conventional Commits

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Contact

- Project URL: [https://github.com/youqi-liu/hrms_pros](https://github.com/youqi-liu/hrms_pros)
- Gitee Mirror: [https://gitee.com/youqi-liu/hrms_pros](https://gitee.com/youqi-liu/hrms_pros)
- Issue Tracker: [https://github.com/youqi-liu/hrms_pros/issues](https://github.com/youqi-liu/hrms_pros/issues)

## ⭐ Star History

If this project helps you, please give it a star!

## 📋 Changelog

### v1.0.0 (2025-11-22)

- ✨ Initial release
- ✅ Complete core functional modules
- 📝 Improve project documentation
