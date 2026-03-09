# 录单系统

## How to Run

### Docker 方式（推荐）

```bash
docker-compose up --build -d
```

停止服务：
```bash
docker-compose down
```

重置数据库（修改 schema.sql 后需要执行）：
```bash
docker-compose down -v
docker-compose up --build -d
```

容器名称冲突时，先删除旧容器：
```bash
docker rm -f order-mysql order-backend order-frontend-admin
docker-compose up --build -d
```

### 本地开发

1. 启动 MySQL，创建数据库并执行 `backend/src/main/resources/schema.sql`
2. 启动后端：
```bash
cd backend
mvn spring-boot:run
```
3. 启动前端：
```bash
cd frontend-admin
npm install
npm run dev
```

## Services

| 服务 | 端口 | 说明 |
|------|------|------|
| frontend-admin | 8082 | 管理后台 |
| backend | 8080 | 后端 API |
| mysql | 3306 | 数据库 |

访问地址：http://localhost:8082

## 测试账号

| 角色 | 实例名 | 学校 | 密码 |
|------|--------|------|------|
| 管理员 | admin | admin | admin123 |
| 学生 | 张三 | 清华大学 | 123456 |
| 学生 | 李四 | 北京大学 | 123456 |

普通用户也可通过注册页面自行注册。

## 题目内容

录单系统

一、系统基础环境搭建

初始化 SpringBoot（JDK21）后端项目，集成 MyBatis Plus、MySQL 驱动，配置数据库连接；初始化 Vue3+Element Plus 前端项目，配置路由、Axios 请求拦截器；建立前后端统一的接口规范。

二、用户模块

用户登录/身份标识：前端实现登录页，使用"实例名"+"学校名称"作为身份标识；后端创建用户表，提供用户查询接口。

三、录单功能

前端用 Element Plus 组件实现表单，包含订单标题、语言、数据库、大前端、前端框架、爬虫、大数据、机器学习、三端等下拉选择器，我的编号输入框，开发日期选择器，订单附件上传组件；后端创建订单表，提供订单提交接口。

四、订单管理功能

前端实现订单列表页，包含筛选栏、操作按钮（编辑、撤回）、表格展示；后端提供订单列表查询接口，支持多条件筛选。

五、订单状态流转+价格管理

订单状态：待接单→已接单→开发中→开发完成→修改完成；管理员可接单、填写价格、修改状态。

六、结算管理功能

后端提供结算状态更新接口；前端订单列表展示结算状态，管理员可修改；顶部展示账户余额（已结算订单总价之和）。

七、附件下载功能

前端订单列表提供下载按钮；后端提供文件下载接口。

## 功能特性

- 用户登录/注册（密码 MD5 加密传输）
- JWT 接口鉴权
- 订单录入（支持附件上传）
- 订单列表查询（多条件筛选、分页）
- 订单状态流转管理
- 价格设置与结算管理
- 账户余额统计
- 附件下载

## 项目结构

```
├── backend/                    # SpringBoot 后端
│   ├── src/main/java/com/order/
│   │   ├── common/             # 通用类
│   │   ├── config/             # 配置类（JWT、MyBatis）
│   │   ├── controller/         # 控制器
│   │   ├── dto/                # 数据传输对象
│   │   ├── entity/             # 实体类
│   │   ├── mapper/             # MyBatis Mapper
│   │   ├── service/            # 服务层
│   │   └── util/               # 工具类
│   └── src/main/resources/
│       ├── application.yml     # 配置文件
│       └── schema.sql          # 数据库脚本
├── frontend-admin/             # Vue3 前端
│   └── src/
│       ├── api/                # 接口封装
│       ├── router/             # 路由配置
│       ├── utils/              # 工具函数
│       └── views/              # 页面组件
└── docker-compose.yml          # Docker 编排
```

## 技术栈

- 后端：SpringBoot 3.2 + MyBatis Plus + MySQL 8.0 + JWT
- 前端：Vue 3 + Element Plus + Vite + Axios
- 部署：Docker + Docker Compose + Nginx
