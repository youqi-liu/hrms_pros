# HRMS Pro - 企业人力资源管理系统

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Vue](https://img.shields.io/badge/Vue-2.7.10-green.svg)](https://vuejs.org/)

## 📖 项目介绍

HRMS Pro 是一款基于 Spring Boot + Vue.js 开发的企业级人力资源管理系统，旨在为企业提供完整的人力资源管理解决方案。系统采用前后端分离架构，功能涵盖人事管理、考勤管理、薪资管理、招聘管理等多个模块。

### ✨ 主要功能

- 👥 **人事管理**：员工信息管理、部门管理、职位管理、职称管理
- 📊 **数据管理**：基础数据维护、数据字典管理
- 📝 **公告管理**：公告发布、公告查看、留言板
- 💰 **薪资管理**：工资管理、工资查询
- 📋 **请假管理**：请假申请、请假审批
- 🔐 **权限管理**：用户管理、角色管理、菜单管理
- 📜 **日志管理**：操作日志记录与查询
- 📁 **文件管理**：文件上传、下载、管理

## 🏗️ 技术架构

### 后端技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 3.1.0 | 核心框架 |
| Spring Security | - | 安全框架 |
| Spring Data JPA | - | ORM框架 |
| MyBatis Plus | 3.5.3.1 | 持久层框架 |
| MySQL | 8.0+ | 数据库 |
| Redis | - | 缓存中间件 |
| Druid | 1.2.18 | 数据库连接池 |
| Redisson | 3.21.3 | Redis客户端 |
| Hutool | 5.8.19 | Java工具类库 |
| FastJSON2 | 2.0.33 | JSON处理 |
| Apache POI | 3.10 | Excel导入导出 |
| Lombok | - | 代码简化 |

### 前端技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 2.7.10 | 前端框架 |
| Vue Router | 3.5.4 | 路由管理 |
| Vuex | 3.6.2 | 状态管理 |
| View Design (iView) | 4.7.0 | UI组件库 |
| Axios | 0.27.2 | HTTP客户端 |
| @antv/g2plot | 2.4.19 | 数据可视化 |
| XLSX | 0.15.6 | Excel处理 |

### 项目结构

```
hrms_pros/
├── hrms_pro_back/          # 后端项目
│   ├── src/main/
│   │   ├── java/cn/lyp/
│   │   │   ├── basics/     # 基础组件（异常处理、安全配置、工具类等）
│   │   │   ├── data/       # 数据管理模块
│   │   │   ├── rs/         # 人事管理模块
│   │   │   └── test/       # 测试模块
│   │   └── resources/
│   │       └── application.yml
│   └── pom.xml
├── hrms_pro_front/         # 前端项目
│   ├── public/
│   ├── src/
│   │   ├── api/           # API接口
│   │   ├── libs/          # 工具库
│   │   ├── router/        # 路由配置
│   │   ├── store/         # 状态管理
│   │   └── views/         # 页面组件
│   ├── package.json
│   └── vue.config.js
├── s079.sql               # 数据库初始化脚本
└── README.md
```

## 🚀 快速开始

### 环境要求

- JDK 11 或更高版本
- Node.js 14+ & NPM
- MySQL 8.0+
- Redis 5.0+
- Maven 3.6+

### 安装步骤

#### 1. 克隆项目

```bash
git clone https://github.com/youqi-liu/hrms_pros.git
cd hrms_pros
```

#### 2. 数据库配置

```bash
# 创建数据库
mysql -u root -p
CREATE DATABASE hrms_pro DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 导入初始化脚本
mysql -u root -p hrms_pro < s079.sql
```

#### 3. 后端配置与启动

```bash
cd hrms_pro_back

# 修改配置文件 src/main/resources/application.yml
# 配置数据库连接信息
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/hrms_pro?useUnicode=true&characterEncoding=utf-8&useSSL=false
    username: root
    password: your_password

# 配置 Redis 连接
spring:
  data:
    redis:
      host: localhost
      port: 6379
      password: your_redis_password

# 使用 Maven 构建并运行
mvn clean install
mvn spring-boot:run

# 或者使用 IDE 直接运行 LypApplication.java
```

后端服务默认运行在：`http://localhost:8081/api/v1`

#### 4. 前端配置与启动

```bash
cd hrms_pro_front

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

前端服务默认运行在：`http://localhost:8080`

#### 5. 生产环境构建

```bash
# 后端打包
cd hrms_pro_back
mvn clean package
# 生成的 jar 包位于 target/ 目录

# 前端打包
cd hrms_pro_front
npm run build
# 生成的静态文件位于 dist/ 目录
```

## 📝 使用说明

### 默认账号

- 管理员账号：`admin`
- 默认密码：请查看数据库初始化脚本

### 功能模块说明

1. **登录注册**：支持用户登录、注册、密码重置
2. **首页看板**：展示系统概览和统计数据
3. **人事管理**：
   - 部门管理：组织架构维护
   - 员工管理：员工信息的增删改查
   - 职位管理：职位信息维护
   - 职称管理：职称信息维护
4. **公告管理**：
   - 用户公告：发布和查看公告
   - 留言板：员工留言互动
5. **薪资管理**：工资信息录入和查询
6. **请假管理**：请假申请、审批流程
7. **系统管理**：
   - 用户管理：系统用户维护
   - 角色管理：角色权限配置
   - 菜单管理：系统菜单配置
   - 字典管理：数据字典维护
   - 日志管理：操作日志查询

## 🔧 配置说明

### 后端配置

主要配置文件：`hrms_pro_back/src/main/resources/application.yml`

```yaml
server:
  port: 8081                    # 服务端口
  servlet:
    context-path: /api/v1       # 上下文路径

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
      logic-delete-value: 1      # 逻辑删除值
      logic-not-delete-value: 0
```

### 前端配置

API 接口配置：`hrms_pro_front/src/api/index.js`

## 🤝 参与贡献

欢迎贡献代码，请遵循以下步骤：

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 提交 Pull Request

### 代码规范

- 后端：遵循阿里巴巴 Java 开发手册
- 前端：遵循 Vue.js 风格指南
- 提交信息：使用语义化提交信息（Conventional Commits）

## 📄 开源协议

本项目采用 MIT 协议开源，详见 [LICENSE](LICENSE) 文件。

## 📞 联系方式

- 项目地址：[https://github.com/youqi-liu/hrms_pros](https://github.com/youqi-liu/hrms_pros)
- Gitee 镜像：[https://gitee.com/youqi-liu/hrms_pros](https://gitee.com/youqi-liu/hrms_pros)
- Issue 反馈：[https://github.com/youqi-liu/hrms_pros/issues](https://github.com/youqi-liu/hrms_pros/issues)

## ⭐ Star History

如果这个项目对你有帮助，欢迎 Star 支持！

## 📋 更新日志

### v1.0.0 (2025-11-22)

- ✨ 初始版本发布
- ✅ 完成核心功能模块开发
- 📝 完善项目文档
