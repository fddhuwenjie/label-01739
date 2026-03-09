-- 设置编码
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    instance_name VARCHAR(100) NOT NULL COMMENT '实例名',
    school_name VARCHAR(100) NOT NULL COMMENT '学校名称',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    role INT DEFAULT 0 COMMENT '角色 0-学生 1-管理员',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_instance_school (instance_name, school_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 订单表
CREATE TABLE IF NOT EXISTS sys_order (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_no VARCHAR(50) NOT NULL COMMENT '订单编号',
    title VARCHAR(200) COMMENT '订单标题',
    language VARCHAR(50) COMMENT '语言',
    `database` VARCHAR(50) COMMENT '数据库',
    frontend VARCHAR(50) COMMENT '大前端',
    frontend_framework VARCHAR(50) COMMENT '前端框架',
    crawler VARCHAR(50) COMMENT '爬虫',
    big_data VARCHAR(50) COMMENT '大数据',
    machine_learning VARCHAR(50) COMMENT '机器学习',
    terminal VARCHAR(50) COMMENT '三端',
    my_no VARCHAR(50) COMMENT '我的编号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    instance_name VARCHAR(100) COMMENT '实例名',
    school_name VARCHAR(100) COMMENT '学校名称',
    dev_date DATE COMMENT '开发日期',
    attachment_path VARCHAR(500) COMMENT '附件路径',
    attachment_name VARCHAR(200) COMMENT '附件原名',
    requirement TEXT COMMENT '需求描述',
    status INT DEFAULT 0 COMMENT '状态 0-待接单 1-已接单 2-开发中 3-开发完成 4-修改完成',
    total_price DECIMAL(10,2) DEFAULT 0 COMMENT '总价',
    settled INT DEFAULT 0 COMMENT '是否结算 0-未结算 1-已结算',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_order_no (order_no),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 插入管理员账号（忽略重复）
-- 密码 admin123 的MD5值: 0192023a7bbd73250516f069df18b500
INSERT IGNORE INTO sys_user (instance_name, school_name, password, role) VALUES ('admin', 'admin', '0192023a7bbd73250516f069df18b500', 1);

-- 插入测试学生账号
-- 密码 123456 的MD5值: e10adc3949ba59abbe56e057f20f883e
INSERT IGNORE INTO sys_user (instance_name, school_name, password, role) VALUES ('张三', '清华大学', 'e10adc3949ba59abbe56e057f20f883e', 0);
INSERT IGNORE INTO sys_user (instance_name, school_name, password, role) VALUES ('李四', '北京大学', 'e10adc3949ba59abbe56e057f20f883e', 0);

-- 插入测试订单数据
INSERT IGNORE INTO sys_order (order_no, title, language, `database`, frontend, frontend_framework, terminal, my_no, user_id, instance_name, school_name, dev_date, requirement, status, total_price, settled) VALUES
('ORD202402010001', '学生成绩管理系统', 'Java', 'MySQL', 'Web', 'Vue', 'PC端', 'MY001', 2, '张三', '清华大学', '2024-02-15', '开发一个学生成绩管理系统，包含学生信息管理、成绩录入、成绩查询、统计分析等功能', 3, 500.00, 1),
('ORD202402010002', '在线商城系统', 'Java', 'MySQL', 'Web', 'Vue', 'PC端', 'MY002', 2, '张三', '清华大学', '2024-02-20', '电商平台，包含商品管理、购物车、订单管理、支付功能', 2, 800.00, 0),
('ORD202402010003', '图书馆管理系统', 'Python', 'MySQL', 'Web', 'Vue', 'PC端', 'MY003', 3, '李四', '北京大学', '2024-02-18', '图书借阅管理系统，包含图书入库、借阅、归还、逾期提醒等功能', 1, 600.00, 0),
('ORD202402010004', '新闻爬虫系统', 'Python', 'MongoDB', NULL, NULL, NULL, 'MY004', 3, '李四', '北京大学', '2024-02-25', '爬取主流新闻网站数据，进行数据清洗和存储', 0, 0.00, 0),
('ORD202402010005', '人脸识别考勤系统', 'Python', 'MySQL', 'Web', 'Vue', 'PC端', 'MY005', 2, '张三', '清华大学', '2024-03-01', '基于人脸识别的考勤系统，支持人脸录入、识别打卡、考勤统计', 4, 1200.00, 1),
('ORD202402010006', '医院挂号预约系统', 'Java', 'MySQL', 'Web', 'Vue', 'PC端', 'MY006', 2, '张三', '清华大学', '2024-03-05', '医院在线挂号系统，支持科室选择、医生排班、预约挂号', 0, 0.00, 0),
('ORD202402010007', '外卖点餐小程序', 'Java', 'MySQL', '小程序', 'UniApp', '小程序端', 'MY007', 3, '李四', '北京大学', '2024-03-08', '外卖点餐微信小程序，包含商家入驻、菜品管理、下单支付', 1, 900.00, 0),
('ORD202402010008', '企业OA办公系统', 'Java', 'MySQL', 'Web', 'Vue', 'PC端', 'MY008', 2, '张三', '清华大学', '2024-03-10', '企业办公自动化系统，包含审批流程、公告通知、日程管理', 2, 1500.00, 0),
('ORD202402010009', '在线考试系统', 'Java', 'MySQL', 'Web', 'React', 'PC端', 'MY009', 3, '李四', '北京大学', '2024-03-12', '在线考试平台，支持题库管理、随机组卷、自动阅卷', 3, 700.00, 1),
('ORD202402010010', '物流配送管理系统', 'Java', 'MySQL', 'Web', 'Vue', 'PC端', 'MY010', 2, '张三', '清华大学', '2024-03-15', '物流配送系统，包含订单管理、路线规划、配送跟踪', 4, 850.00, 1),
('ORD202402010011', '微博数据爬虫', 'Python', 'MongoDB', NULL, NULL, NULL, 'MY011', 3, '李四', '北京大学', '2024-03-18', '爬取微博热搜数据，进行情感分析和可视化展示', 0, 0.00, 0),
('ORD202402010012', '智能停车场系统', 'Java', 'MySQL', 'Web', 'Vue', 'PC端', 'MY012', 2, '张三', '清华大学', '2024-03-20', '停车场管理系统，支持车牌识别、计费管理、车位查询', 1, 1100.00, 0),
('ORD202402010013', '电影推荐系统', 'Python', 'MySQL', 'Web', 'Vue', 'PC端', 'MY013', 3, '李四', '北京大学', '2024-03-22', '基于协同过滤的电影推荐系统，包含用户画像、推荐算法', 2, 950.00, 0),
('ORD202402010014', '校园二手交易平台', 'Java', 'MySQL', 'App', 'Flutter', '移动端', 'MY014', 2, '张三', '清华大学', '2024-03-25', '校园二手物品交易APP，支持发布商品、在线聊天、交易管理', 3, 1000.00, 0),
('ORD202402010015', '疫情数据可视化', 'Python', 'MySQL', 'Web', 'Vue', 'PC端', 'MY015', 3, '李四', '北京大学', '2024-03-28', '疫情数据爬取与可视化大屏展示，包含地图、趋势图', 4, 600.00, 1),
('ORD202402010016', '酒店预订管理系统', 'Java', 'MySQL', 'Web', 'Vue', 'PC端', 'MY016', 2, '张三', '清华大学', '2024-04-01', '酒店在线预订系统，包含房型管理、预订、入住退房', 0, 0.00, 0),
('ORD202402010017', '股票数据分析系统', 'Python', 'MySQL', 'Web', 'React', 'PC端', 'MY017', 3, '李四', '北京大学', '2024-04-05', '股票行情数据采集与分析，K线图展示、技术指标计算', 1, 1300.00, 0),
('ORD202402010018', '在线教育直播平台', 'Java', 'MySQL', 'Web', 'Vue', 'PC端', 'MY018', 2, '张三', '清华大学', '2024-04-08', '在线教育平台，支持直播授课、录播回放、作业提交', 2, 2000.00, 0),
('ORD202402010019', '智能家居控制系统', 'Python', 'SQLite', 'App', 'React Native', '移动端', 'MY019', 3, '李四', '北京大学', '2024-04-10', '智能家居APP，支持设备控制、场景联动、定时任务', 3, 1400.00, 1),
('ORD202402010020', '招聘求职网站', 'Java', 'MySQL', 'Web', 'Vue', 'PC端', 'MY020', 2, '张三', '清华大学', '2024-04-12', '招聘网站，包含企业发布职位、求职者投递简历、在线沟通', 4, 1100.00, 0),
('ORD202402010021', '音乐播放器APP', 'Kotlin', 'SQLite', 'App', NULL, '移动端', 'MY021', 3, '李四', '北京大学', '2024-04-15', '安卓音乐播放器，支持本地音乐、在线搜索、歌词显示', 0, 0.00, 0),
('ORD202402010022', '大数据日志分析', 'Java', 'HBase', NULL, NULL, NULL, 'MY022', 2, '张三', '清华大学', '2024-04-18', '基于Hadoop的日志分析系统，包含数据采集、清洗、统计', 1, 1800.00, 0),
('ORD202402010023', '车牌识别系统', 'Python', 'MySQL', 'Web', 'Vue', 'PC端', 'MY023', 3, '李四', '北京大学', '2024-04-20', '基于深度学习的车牌识别，支持多种车牌类型识别', 2, 1600.00, 0),
('ORD202402010024', '社区团购小程序', 'Java', 'MySQL', '小程序', 'UniApp', '小程序端', 'MY024', 2, '张三', '清华大学', '2024-04-22', '社区团购微信小程序，包含团长管理、商品发布、订单配送', 3, 1200.00, 1),
('ORD202402010025', '健身房会员系统', 'Java', 'MySQL', 'Web', 'Vue', 'PC端', 'MY025', 3, '李四', '北京大学', '2024-04-25', '健身房管理系统，包含会员管理、课程预约、教练排班', 4, 750.00, 1);
