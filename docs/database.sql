-- ============================================
-- 流浪动物领养管理系统 - 数据库初始化脚本
-- 数据库名称：stray_animal_db
-- 字符集：utf8mb4
-- ============================================

-- 1. 创建数据库
CREATE DATABASE IF NOT EXISTS stray_animal_db
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;

USE stray_animal_db;

-- 2. 用户表
CREATE TABLE `user` (
  `id`           BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
  `username`     VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
  `password`     VARCHAR(255) NOT NULL COMMENT '密码（加密存储）',
  `real_name`    VARCHAR(100) COMMENT '真实姓名',
  `phone`        VARCHAR(20) COMMENT '手机号',
  `email`        VARCHAR(100) COMMENT '邮箱',
  `address`      VARCHAR(255) COMMENT '地址',
  `user_type`    VARCHAR(20) COMMENT '用户类型：ADMIN/STAFF/VOLUNTEER/ADOPTER',
  `status`       VARCHAR(20) DEFAULT 'ENABLED' COMMENT '状态：ENABLED/DISABLED',
  `create_time`  DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  INDEX `idx_username` (`username`),
  INDEX `idx_user_type` (`user_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 3. 动物档案表
CREATE TABLE `animal` (
  `id`               BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '动物ID',
  `name`             VARCHAR(50) NOT NULL COMMENT '动物名称',
  `species`          VARCHAR(20) COMMENT '种类：狗/猫/其他',
  `breed`            VARCHAR(50) COMMENT '品种',
  `gender`           VARCHAR(10) COMMENT '性别',
  `age`              INT COMMENT '年龄（月或年）',
  `weight`           DOUBLE COMMENT '体重（kg）',
  `color`            VARCHAR(100) COMMENT '颜色',
  `health_status`    VARCHAR(200) COMMENT '健康状况',
  `vaccinated`       BIT DEFAULT 0 COMMENT '是否免疫',
  `neutered`         BIT DEFAULT 0 COMMENT '是否绝育',
  `character_feature` VARCHAR(200) COMMENT '性格特征',
  `status`           VARCHAR(20) DEFAULT '待领养' COMMENT '状态：待领养/领养中/已领养/特别照顾',
  `description`      TEXT COMMENT '详细描述',
  `create_time`      DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  INDEX `idx_status` (`status`),
  INDEX `idx_species` (`species`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='动物档案表';

-- 4. 领养申请表
CREATE TABLE `adoption_application` (
  `id`            BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '申请ID',
  `applicant_id`  BIGINT COMMENT '申请人ID',
  `animal_id`     BIGINT COMMENT '动物ID',
  `status`        VARCHAR(20) DEFAULT 'SUBMITTED' COMMENT '状态：SUBMITTED/UNDER_REVIEW/APPROVED/REJECTED/CANCELLED',
  `reason`        TEXT COMMENT '申请理由',
  `home_env`      TEXT COMMENT '居住环境描述',
  `has_other_pet` BIT DEFAULT 0 COMMENT '是否有其他宠物',
  `income_level`  VARCHAR(20) COMMENT '收入水平',
  `experience`    TEXT COMMENT '养宠经验',
  `score`         INT COMMENT '系统匹配评分',
  `reject_reason` TEXT COMMENT '驳回原因',
  `create_time`   DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`   DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  INDEX `idx_applicant` (`applicant_id`),
  INDEX `idx_animal` (`animal_id`),
  INDEX `idx_status` (`status`),
  INDEX `idx_score` (`score`),
  CONSTRAINT `fk_adoption_applicant`
    FOREIGN KEY (`applicant_id`) REFERENCES `user`(`id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_adoption_animal`
    FOREIGN KEY (`animal_id`) REFERENCES `animal`(`id`)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='领养申请表';

-- 5. 捐赠主表
CREATE TABLE `donation` (
  `id`          BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '捐赠ID',
  `donor_id`    BIGINT COMMENT '捐赠人ID',
  `donor_name`  VARCHAR(50) COMMENT '捐赠人姓名（可匿名）',
  `type`        VARCHAR(20) COMMENT '类型：MONEY/ITEM',
  `amount`      DOUBLE COMMENT '金额（资金捐赠）',
  `status`      VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态：PENDING/CONFIRMED/USED/PARTLY_USED',
  `public_flag` BIT DEFAULT 1 COMMENT '是否公开',
  `remark`      TEXT COMMENT '备注',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  INDEX `idx_donor` (`donor_id`),
  INDEX `idx_status` (`status`),
  INDEX `idx_public` (`public_flag`),
  CONSTRAINT `fk_donation_donor`
    FOREIGN KEY (`donor_id`) REFERENCES `user`(`id`)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='捐赠主表';

-- 6. 捐赠使用去向表
CREATE TABLE `donation_usage` (
  `id`          BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '使用记录ID',
  `donation_id` BIGINT COMMENT '所属捐赠ID',
  `animal_id`   BIGINT COMMENT '对应动物ID（可为空）',
  `project_name` VARCHAR(100) COMMENT '项目名称',
  `use_time`    DATETIME COMMENT '使用时间',
  `use_desc`    TEXT COMMENT '使用说明',
  `photo_url`   VARCHAR(255) COMMENT '凭证照片URL',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  INDEX `idx_donation` (`donation_id`),
  INDEX `idx_animal` (`animal_id`),
  CONSTRAINT `fk_usage_donation`
    FOREIGN KEY (`donation_id`) REFERENCES `donation`(`id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_usage_animal`
    FOREIGN KEY (`animal_id`) REFERENCES `animal`(`id`)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='捐赠使用去向表';

-- 7. 回访记录表
CREATE TABLE `adoption_followup` (
  `id`             BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '回访ID',
  `application_id` BIGINT COMMENT '对应申请ID',
  `staff_id`       BIGINT COMMENT '执行工作人员ID',
  `visit_time`     DATETIME COMMENT '回访时间',
  `visit_type`     VARCHAR(20) COMMENT '回访方式：PHONE/HOME/ONLINE',
  `visit_result`   TEXT COMMENT '回访结果',
  `note`           TEXT COMMENT '备注',
  `create_time`    DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`    DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  INDEX `idx_application` (`application_id`),
  INDEX `idx_staff` (`staff_id`),
  CONSTRAINT `fk_followup_application`
    FOREIGN KEY (`application_id`) REFERENCES `adoption_application`(`id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_followup_staff`
    FOREIGN KEY (`staff_id`) REFERENCES `user`(`id`)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='回访记录表';

-- 8. 初始化测试数据（可选）
-- 插入管理员用户
INSERT INTO `user` (`username`, `password`, `real_name`, `user_type`, `status`) VALUES
('admin', 'test', '系统管理员', 'ADMIN', 'ENABLED');

INSERT INTO `user` (username, password, real_name, user_type, status)
VALUES ('adopter1', 'test', '测试领养人', 'ADOPTER', 'ENABLED');

-- 插入测试动物数据

INSERT INTO animal
(name, species, breed, gender, age, weight, color, health_status,
 vaccinated, neutered, character_feature, status, description)
VALUES
('小白', '狗', '金毛', '公', 24, 25.5, '金黄色',
 '健康，已完成免疫', 1, 1,
 '温顺、喜欢和人互动，适合有院子家庭',
 '待领养',
 '救助于市流浪动物救助站门口，现在身体状况良好'),
('小花', '猫', '橘猫', '母', 12, 4.2, '橘白相间',
 '健康，已完成免疫', 1, 1,
 '活泼、亲人、喜欢晒太阳',
 '待领养',
 '从小在救助站长大，对人非常友好，适合有时间陪伴的家庭'),
('小黑', '狗', '中华田园犬', '公', 18, 18.0, '黑色',
 '略有皮肤病，正在治疗中', 1, 0,
 '聪明、黏人，需要有耐心的主人',
 '特别照顾',
 '在工地附近被发现，有轻微皮肤病，需要后续持续治疗'),
('豆豆', '猫', '英短', '母', 8, 3.5, '蓝白',
 '健康，免疫完成', 1, 0,
 '安静、胆小，需要安静环境',
 '待领养',
 '原主人家庭变故送至救助站，适合单身或无小孩家庭'),
('大黄', '狗', '拉布拉多', '公', 30, 30.0, '黄色',
 '健康，体力充沛', 1, 1,
 '精力旺盛、喜欢运动，适合有运动习惯的主人',
 '待领养',
 '公园附近流浪，被救助后恢复良好，非常友好亲人');