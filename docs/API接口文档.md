# API接口文档

## 基础信息

- **基础URL**：`http://localhost:8080/api`
- **数据格式**：JSON
- **字符编码**：UTF-8

## 通用响应格式

### 成功响应
```json
{
  "data": {...},
  "message": "操作成功",
  "code": 200
}
```

### 错误响应
```json
{
  "message": "错误信息",
  "code": 400
}
```

## 1. 动物管理接口

### 1.1 获取动物列表

**接口地址**：`GET /api/animals`

**请求参数**：
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | int | 否 | 页码，默认0 |
| size | int | 否 | 每页数量，默认10 |
| status | String | 否 | 状态筛选（待领养/领养中/已领养/特别照顾） |
| species | String | 否 | 种类筛选（狗/猫/其他） |

**请求示例**：
```
GET /api/animals?page=0&size=10&status=待领养&species=狗
```

**响应示例**：
```json
{
  "content": [
    {
      "id": 1,
      "name": "小白",
      "species": "狗",
      "breed": "金毛",
      "gender": "公",
      "age": 24,
      "weight": 25.5,
      "color": "金黄色",
      "healthStatus": "健康",
      "vaccinated": true,
      "neutered": true,
      "characterFeature": "温顺、友好",
      "status": "待领养",
      "description": "这是一只非常温顺的金毛...",
      "createTime": "2024-01-01T10:00:00",
      "updateTime": "2024-01-01T10:00:00"
    }
  ],
  "totalElements": 50,
  "totalPages": 5,
  "size": 10,
  "number": 0
}
```

### 1.2 获取动物详情

**接口地址**：`GET /api/animals/{id}`

**路径参数**：
| 参数名 | 类型 | 说明 |
|--------|------|------|
| id | Long | 动物ID |

**请求示例**：
```
GET /api/animals/1
```

**响应示例**：
```json
{
  "id": 1,
  "name": "小白",
  "species": "狗",
  "breed": "金毛",
  "gender": "公",
  "age": 24,
  "weight": 25.5,
  "color": "金黄色",
  "healthStatus": "健康",
  "vaccinated": true,
  "neutered": true,
  "characterFeature": "温顺、友好",
  "status": "待领养",
  "description": "这是一只非常温顺的金毛...",
  "createTime": "2024-01-01T10:00:00",
  "updateTime": "2024-01-01T10:00:00"
}
```

### 1.3 新增动物档案

**接口地址**：`POST /api/animals`

**请求体**：
```json
{
  "name": "小黑",
  "species": "狗",
  "breed": "拉布拉多",
  "gender": "母",
  "age": 18,
  "weight": 20.0,
  "color": "黑色",
  "healthStatus": "健康",
  "vaccinated": true,
  "neutered": true,
  "characterFeature": "活泼、聪明",
  "status": "待领养",
  "description": "这是一只活泼的拉布拉多..."
}
```

**响应示例**：
```json
{
  "id": 3,
  "name": "小黑",
  ...
}
```

### 1.4 更新动物信息

**接口地址**：`PUT /api/animals/{id}`

**路径参数**：
| 参数名 | 类型 | 说明 |
|--------|------|------|
| id | Long | 动物ID |

**请求体**：同新增接口

### 1.5 删除动物档案

**接口地址**：`DELETE /api/animals/{id}`

**路径参数**：
| 参数名 | 类型 | 说明 |
|--------|------|------|
| id | Long | 动物ID |

## 2. 领养申请接口

### 2.1 提交领养申请

**接口地址**：`POST /api/adoptions/apply`

**请求参数**：
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| applicantId | Long | 是 | 申请人ID |
| animalId | Long | 是 | 动物ID |

**请求体**：
```json
{
  "reason": "我一直想养一只金毛，家里有足够的空间和条件",
  "homeEnv": "独栋别墅，有院子，适合大型犬",
  "hasOtherPet": false,
  "incomeLevel": "中高",
  "experience": "之前养过一只拉布拉多，有丰富的养狗经验"
}
```

**响应示例**：
```json
{
  "id": 1,
  "applicant": {
    "id": 10,
    "username": "zhangsan"
  },
  "animal": {
    "id": 1,
    "name": "小白"
  },
  "status": "SUBMITTED",
  "score": 85,
  "createTime": "2024-01-01T10:00:00"
}
```

### 2.2 查询我的申请列表（领养者端）

**接口地址**：`GET /api/adoptions/my`

**请求参数**：
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| applicantId | Long | 是 | 申请人ID |
| page | int | 否 | 页码，默认0 |
| size | int | 否 | 每页数量，默认10 |

**请求示例**：
```
GET /api/adoptions/my?applicantId=10&page=0&size=10
```

### 2.3 后台查询申请列表（审核列表）

**接口地址**：`GET /api/adoptions/admin`

**请求参数**：
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| status | String | 否 | 状态筛选 |
| page | int | 否 | 页码，默认0 |
| size | int | 否 | 每页数量，默认10 |

**说明**：按匹配评分（score）降序排列

### 2.4 审核申请（通过 / 驳回）

**接口地址**：`PUT /api/adoptions/{id}/status`

**路径参数**：
| 参数名 | 类型 | 说明 |
|--------|------|------|
| id | Long | 申请ID |

**请求参数**：
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| status | String | 是 | 新状态（APPROVED/REJECTED） |
| rejectReason | String | 否 | 驳回原因（当status=REJECTED时） |

**请求示例**：
```
PUT /api/adoptions/1/status?status=APPROVED
```

或

```
PUT /api/adoptions/1/status?status=REJECTED&rejectReason=居住环境不符合要求
```

### 2.5 重新计算历史申请评分（管理员）

> 用于当评分规则调整后，一次性为所有历史申请按新规则重算评分。

**接口地址**：`GET /api/adoptions/admin/recalculate-score` 或 `POST /api/adoptions/admin/recalculate-score`

**说明**：无请求体，无返回体（204 No Content）。

---

## 3. 捐赠管理接口

### 3.1 创建捐赠记录

**接口地址**：`POST /api/donations`

**请求参数**：
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| donorId | Long | 否 | 捐赠人ID（不传则为匿名捐赠） |

**请求体**：
```json
{
  "donorName": "张先生",
  "type": "MONEY",
  "amount": 500.0,
  "publicFlag": true,
  "remark": "希望用于动物医疗费用"
}
```

或物资捐赠：
```json
{
  "donorName": "李女士",
  "type": "ITEM",
  "publicFlag": true,
  "remark": "捐赠狗粮10袋"
}
```

**响应示例**：
```json
{
  "id": 1,
  "donorName": "张先生",
  "type": "MONEY",
  "amount": 500.0,
  "status": "PENDING",
  "publicFlag": true,
  "createTime": "2024-01-01T10:00:00"
}
```

### 3.2 公开捐赠公示列表（前台）

**接口地址**：`GET /api/donations/public`

**请求参数**：
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | int | 否 | 页码，默认0 |
| size | int | 否 | 每页数量，默认10 |

**请求示例**：
```
GET /api/donations/public?page=0&size=10
```

### 3.3 后台查看所有捐赠记录

**接口地址**：`GET /api/donations/admin`

**请求参数**：
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | int | 否 | 页码，默认0 |
| size | int | 否 | 每页数量，默认10 |

### 3.4 更新捐赠状态

**接口地址**：`PUT /api/donations/{id}/status`

**路径参数**：
| 参数名 | 类型 | 说明 |
|--------|------|------|
| id | Long | 捐赠ID |

**请求参数**：
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| status | String | 是 | 新状态（CONFIRMED/USED/PARTLY_USED） |

### 3.5 记录捐赠使用去向

**接口地址**：`POST /api/donations/{donationId}/usages`

**路径参数**：
| 参数名 | 类型 | 说明 |
|--------|------|------|
| donationId | Long | 捐赠ID |

**请求体**：
```json
{
  "animalId": 1,
  "projectName": "冬季保暖计划",
  "useTime": "2024-01-15T10:00:00",
  "useDesc": "用于购买小白（ID:1）的保暖用品和医疗检查",
  "photoUrl": "https://example.com/photo.jpg"
}
```

**说明**：`animalId` 和 `projectName` 至少填一个

### 3.6 查询捐赠使用明细

**接口地址**：`GET /api/donations/{donationId}/usages`

**路径参数**：
| 参数名 | 类型 | 说明 |
|--------|------|------|
| donationId | Long | 捐赠ID |

**请求参数**：
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | int | 否 | 页码，默认0 |
| size | int | 否 | 每页数量，默认10 |

**响应示例**：
```json
{
  "content": [
    {
      "id": 1,
      "donation": {
        "id": 1,
        "donorName": "张先生",
        "amount": 500.0
      },
      "animal": {
        "id": 1,
        "name": "小白"
      },
      "projectName": null,
      "useTime": "2024-01-15T10:00:00",
      "useDesc": "用于购买小白的保暖用品",
      "photoUrl": "https://example.com/photo.jpg",
      "createTime": "2024-01-15T10:00:00"
    }
  ],
  "totalElements": 1
}
```

## 4. 回访记录接口

### 4.1 新增回访记录

**接口地址**：`POST /api/adoptions/{applicationId}/followups`

**路径参数**：
| 参数名 | 类型 | 说明 |
|--------|------|------|
| applicationId | Long | 申请ID |

**请求参数**：
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| staffId | Long | 是 | 执行回访的工作人员ID |

**请求体**：
```json
{
  "visitTime": "2024-02-01T14:00:00",
  "visitType": "PHONE",
  "visitResult": "电话回访，领养人反馈小白适应良好，身体健康，与家人相处融洽",
  "note": "建议3个月后再次回访"
}
```

**响应示例**：
```json
{
  "id": 1,
  "application": {
    "id": 1,
    "animal": {
      "name": "小白"
    }
  },
  "staff": {
    "id": 2,
    "realName": "王志愿者"
  },
  "visitTime": "2024-02-01T14:00:00",
  "visitType": "PHONE",
  "visitResult": "电话回访，领养人反馈小白适应良好...",
  "note": "建议3个月后再次回访",
  "createTime": "2024-02-01T14:00:00"
}
```

### 4.2 查询回访记录列表

**接口地址**：`GET /api/adoptions/{applicationId}/followups`

**路径参数**：
| 参数名 | 类型 | 说明 |
|--------|------|------|
| applicationId | Long | 申请ID |

**请求参数**：
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | int | 否 | 页码，默认0 |
| size | int | 否 | 每页数量，默认10 |

**响应示例**：
```json
{
  "content": [
    {
      "id": 1,
      "visitTime": "2024-02-01T14:00:00",
      "visitType": "PHONE",
      "visitResult": "电话回访，领养人反馈小白适应良好...",
      "note": "建议3个月后再次回访",
      "staff": {
        "id": 2,
        "realName": "王志愿者"
      }
    }
  ],
  "totalElements": 1
}
```

## 5. 登录与角色识别接口

### 5.1 简易登录

> 当前版本为演示级登录，根据用户名在 `user` 表中查询并返回用户ID与角色类型，未做密码校验和会话管理。

**接口地址**：`POST /api/auth/login`

**请求体**：
```json
{
  "username": "adopter1",
  "password": "任意值（当前未校验，可忽略）"
}
```

**响应示例**：
```json
{
  "userId": 1,
  "username": "adopter1",
  "userType": "ADOPTER"
}
```

前端根据 `userType` 决定跳转：

- `ADOPTER` → 前台 `index.html`；
- `ADMIN` / `STAFF` → 管理端首页 `stats.html`。

---

## 6. 统计分析接口

### 6.1 概览统计

**接口地址**：`GET /api/admin/stats/overview`

**返回字段**：
```json
{
  "totalAnimals": 5,
  "animalsWaiting": 3,
  "animalsAdopted": 2,
  "totalApplications": 10,
  "approvedApplications": 6,
  "totalDonations": 20,
  "totalMoneyAmount": 3500.0
}
```

---

## 7. 状态码说明

| 状态码 | 说明 |
|--------|------|
| 200 | 请求成功 |
| 400 | 请求参数错误 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

## 8. 注意事项

1. 所有时间字段格式为：`yyyy-MM-ddTHH:mm:ss`（ISO 8601格式）
2. 分页参数从0开始
3. 所有接口均支持跨域（CORS）
4. 当前版本未实现完整的认证授权，生产环境需要添加JWT或Session认证
5. 文件上传功能（如动物照片、捐赠凭证）需要单独实现文件存储服务
