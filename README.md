# 基于SpringBoot的流浪动物领养管理系统

## 项目简介

本项目是一个连接流浪动物救助站与有领养意愿的社会公众的线上管理系统，实现：

- **动物档案管理**：标准化记录救助站内流浪动物的基础信息、健康状况和领养状态；
- **领养申请与审核**：公众在线提交领养申请，系统根据多维条件给出匹配评分，工作人员在线审核；
- **捐赠管理与追溯**：支持资金/物资捐赠，记录使用去向并对外公示；
- **统计分析**：对动物数量、领养申请、捐赠情况等进行数据统计；
- **多角色登录入口**：区分领养者与管理端用户，进入不同的操作页面。

### 核心角色

- **领养者（ADOPTER）**：浏览动物、提交领养申请、查看申请进度、进行爱心捐赠。
- **管理员 / 工作人员（ADMIN/STAFF）**：审核领养申请、维护宠物信息、管理捐赠记录、查看统计报表。

---

## 技术栈

- **后端框架**：Spring Boot 3.3.x
- **ORM**：Spring Data JPA（Hibernate）
- **数据库**：MySQL 8+
- **安全与跨域**：Spring Security（简化版）+ CORS
- **前端技术**：原生 HTML + Bootstrap + 原生 JS（fetch 调用 REST API）
- **构建工具**：Maven
- **Java 版本**：JDK 17+

---

## 项目结构概览

```text
src/main/java/com/example/strayanimal/
├── StrayAnimalApplication.java        # 启动类
├── common/
│   └── BaseEntity.java                # 通用创建/更新时间字段
├── user/                              # 用户模块
│   ├── User.java
│   └── UserRepository.java
├── auth/                              # 简易登录接口
│   └── AuthController.java
├── animal/                            # 动物档案模块
│   ├── Animal.java
│   ├── AnimalRepository.java
│   ├── AnimalService.java
│   └── AnimalController.java
├── adoption/                          # 领养申请与回访模块
│   ├── AdoptionApplication.java
│   ├── AdoptionApplicationRepository.java
│   ├── AdoptionApplicationService.java
│   ├── AdoptionApplicationController.java
│   ├── AdoptionScoreCalculator.java   # 评分规则
│   ├── AdoptionEvaluationUtil.java    # 评分文字解释
│   ├── AdoptionApplicationDTO.java    # 领养者视图 DTO
│   ├── AdoptionAdminDTO.java          # 后台审核视图 DTO
│   ├── AdoptionFollowup.java          # 回访记录
│   ├── AdoptionFollowupRepository.java
│   ├── AdoptionFollowupService.java
│   └── AdoptionFollowupController.java
├── donation/                          # 捐赠与使用去向模块
│   ├── Donation.java
│   ├── DonationRepository.java
│   ├── DonationUsage.java
│   ├── DonationUsageRepository.java
│   ├── DonationService.java
│   └── DonationController.java
├── stats/                             # 统计分析模块
│   ├── DashboardStats.java
│   ├── StatsService.java
│   └── StatsController.java
├── web/
│   └── HomeController.java            # 根路径重定向到登录页
└── config/
    └── SecurityConfig.java            # 基础安全与 CORS 配置
```

前端静态页面位于 `src/main/resources/static/`：

```text
static/
├── login.html          # 登录入口（区分领养者 / 管理端）
├── index.html          # 领养者前台：动物列表、申请、我的申请、捐赠
├── admin.html          # 管理端：领养申请审核
├── pet-admin.html      # 管理端：宠物信息录入与维护
├── donation-admin.html # 管理端：捐赠记录管理
└── stats.html          # 管理端：数据统计分析仪表盘
```

---

## 主要功能点汇总

- **登录与角色分流**
  - 通过 `login.html` 访问 `POST /api/auth/login`，根据 `User.userType` 以及选择的身份进入：
    - 领养者：`index.html`
    - 管理员/工作人员：`stats.html`（再导航到审核/宠物/捐赠管理）
  - 登录信息（用户ID、用户名、角色）保存在 `localStorage`，前台用于自动填充“当前用户ID”。

- **动物档案管理**
  - 录入 / 编辑 / 删除动物信息（`pet-admin.html` 对应 `/api/animals` 系列接口）。
  - 前台 `index.html` 展示所有动物，并用徽章标识状态（待领养 / 领养中 / 已领养 / 特别照顾）。
  - 对已领养或特别照顾的动物按钮禁用，防止重复申请。

- **领养申请与审核**
  - 领养者在 `index.html` 提交申请（`POST /api/adoptions/apply`）。
  - 系统通过 `AdoptionScoreCalculator` 计算 0–100 分的匹配度，并通过 `AdoptionEvaluationUtil` 生成“系统评价”文字。
  - 管理端在 `admin.html` 中按状态查看申请、查看评分 + 系统评价 + 居住环境 + 收入 + 经验等信息，点击“通过/驳回”直接更新状态。
  - 审核通过时自动将该动物状态更新为“已领养”，避免二次领养。
  - 支持对已领养申请做回访记录（电话/上门/线上）。

- **捐赠管理与追溯**
  - 前台 `index.html` 支持资金/物资捐赠（`POST /api/donations`）并公示（`GET /api/donations/public`）。
  - 管理端 `donation-admin.html` 查看所有捐赠，确认收捐、标记已使用等。
  - 后端预留捐赠使用去向记录接口（`/api/donations/{id}/usages`），可扩展到页面中做追溯展示。

- **统计分析**
  - `stats.html` 通过 `GET /api/admin/stats/overview` 展示：
    - 动物总数 / 待领养数 / 已领养数；
    - 领养申请总数 / 已通过数；
    - 捐赠记录条数 / 资金捐赠总额。

---

## 文档一览

项目附带以下文档（位于 `docs/` 目录）：

- `数据库设计文档.md`：数据库概念结构、逻辑结构、数据字典。
- `database.sql`：完整数据库建表脚本及部分初始化数据。
- `API接口文档.md`：所有 REST API 的详细说明及示例。
- `部署说明文档.md`：开发/生产环境部署步骤。
- `操作说明文档.md`：面向“领养者”和“管理员”的使用指南。
- `页面说明文档.md`：各 HTML 页面结构与功能说明。
- `代码说明文档.md`：主要包和类的职责说明、关键业务流程说明。

---

## 快速运行

1. 安装并启动 MySQL，执行 `docs/database.sql` 初始化数据库（或手动建库后由 JPA 自动建表）。
2. 修改 `src/main/resources/application.yml` 中的数据库连接。
3. 在项目根目录执行：

```bash
mvn clean install
mvn spring-boot:run
```

4. 打开浏览器访问：`http://localhost:8080`，进入登录页（`login.html`）。

---

## 更多说明

- 评分规则、系统评价逻辑见 `AdoptionScoreCalculator` 和 `AdoptionEvaluationUtil`。
- 统计逻辑见 `StatsService`，可按需要扩展更多维度。
- 当前登录逻辑为演示用途，仅按用户名识别角色，密码与真正鉴权逻辑可在此基础上增加（如 Spring Security + JWT）。

