-- 说明：
-- 1. 索引命名规则：索引类型_表名_tbl_列名.

-- 建库
DROP
    DATABASE IF EXISTS `bcosteaching`;
CREATE
    DATABASE `bcosteaching`;

USE
    bcosteaching;

DROP TABLE IF EXISTS `achieve`;
create table `achieve`
(
    id           int unsigned auto_increment comment 'Primary Key'
        primary key,
    create_time  datetime         default CURRENT_TIMESTAMP null comment '创建时间',
    update_time  datetime         default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    edu_id       char(255)                                  not null comment '教育记录ID',
    title        varchar(255)                               not null comment '成就标题',
    acquire_time date                                       not null comment '获得时间',
    type         varchar(255)                               not null comment '类型',
    remark       varchar(255)                               null comment '备注',
    certify_uri  varchar(255)                               null comment '证明资料',
    audit_status tinyint unsigned default '2'               null comment '审核状态0/1/2',
    feedback     varchar(255)                               null comment '审核反馈'
)
    comment '成就记录' charset = utf8mb3;


DROP TABLE IF EXISTS `alliance_node`;
create table `alliance_node`
(
    id          int unsigned auto_increment comment 'Primary Key'
        primary key,
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    node_id     int unsigned                       not null comment '联盟点编号',
    name        varchar(255)                       not null comment '联盟点名称',
    type        tinyint unsigned                   not null comment '联盟点类型0/1',
    constraint name
        unique (name),
    constraint node_id
        unique (node_id)
)
    comment '联盟点' charset = utf8mb3;



DROP TABLE IF EXISTS `course`;
create table `course`
(
    id          int unsigned auto_increment comment 'Primary Key'
        primary key,
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    course_id   char(255)                          not null comment '课程编号',
    name        varchar(255)                       not null comment '课程名称',
    credit      tinyint unsigned                   not null comment '学分',
    constraint course_id
        unique (course_id)
)
    comment '联盟点课程' charset = utf8mb3;


DROP TABLE IF EXISTS `course_score`;
create table `course_score`
(
    id          int unsigned auto_increment comment 'Primary Key'
        primary key,
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    edu_id      char(255)                          not null comment '教育记录ID',
    course_id   char(255)                          not null comment '课程编号',
    score       tinyint unsigned                   null comment '成绩',
    certify_uri varchar(255)                       null comment '证明资料'
)
    comment '课程成绩表' charset = utf8mb3;


DROP TABLE IF EXISTS `edu_record`;
create table `edu_record`
(
    id               int unsigned auto_increment comment 'Primary Key'
        primary key,
    create_time      datetime          default CURRENT_TIMESTAMP null comment '创建时间',
    update_time      datetime          default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    edu_id           char(255)                                   not null comment '教育记录ID',
    person_id        char(255)                                   not null comment '身份证号',
    type             varchar(4)                                  not null comment '教育类型',
    begin_time       smallint unsigned                           not null comment '入学时间',
    end_time         smallint unsigned                           null comment '毕业时间',
    node_id          int unsigned                                not null comment '联盟点编号',
    node_name        varchar(255)                                not null comment '联盟点名称',
    major_name       varchar(255)                                not null comment '专业名称',
    acquire_credit   smallint unsigned default '0'               null comment '获得学分',
    graduate_credit  smallint unsigned default '0'               null comment '毕业学分',
    certify_uri      varchar(255)                                null comment '证明资料',
    edu_tx_hash      varchar(255)                                null comment '教育经历上链交易哈希',
    courses_tx_hash  varchar(255)                                null comment '课程信息上链交易哈希',
    achieves_tx_hash varchar(255)                                null comment '成就信息上链交易哈希',
    constraint edu_id
        unique (edu_id)
)
    comment '教育记录' charset = utf8mb3;


DROP TABLE IF EXISTS `major`;
create table `major`
(
    id              int unsigned auto_increment comment 'Primary Key'
        primary key,
    create_time     datetime          default CURRENT_TIMESTAMP null comment '创建时间',
    update_time     datetime          default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    major_id        char(255)                                   not null comment '专业编号',
    name            varchar(255)                                not null comment '专业名称',
    type            varchar(255)                                not null comment '教育类型',
    graduate_credit smallint unsigned default '0'               null comment '毕业学分',
    constraint major_id
        unique (major_id)
)
    comment '专业' charset = utf8mb3;


DROP TABLE IF EXISTS `node_user`;
create table `node_user`
(
    id          int unsigned auto_increment comment 'Primary Key'
        primary key,
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    account     char(20)                           not null comment '账号',
    name        varchar(255)                       not null comment '姓名',
    password    varchar(255)                       not null comment '密码',
    salt        varchar(255)                       not null comment '盐',
    phone       char(11)                           null comment '手机号码',
    email       varchar(50)                        null comment '邮箱',
    role        varchar(20)                        null comment '角色',
    constraint account
        unique (account),
    constraint email
        unique (email),
    constraint phone
        unique (phone)
)
    comment '联盟点用户' charset = utf8mb3;


DROP TABLE IF EXISTS `permission`;
create table `permission`
(
    id              int unsigned auto_increment comment 'Primary Key'
        primary key,
    create_time     datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time     datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    permission_id   int                                not null comment '权限id',
    permission_name varchar(255)                       null comment '权限名称',
    permission      varchar(255)                       null comment '权限内容'
)
    comment '权限表' charset = utf8mb3;



DROP TABLE IF EXISTS `person`;
create table `person`
(
    id               int unsigned auto_increment comment 'Primary Key'
        primary key,
    create_time      datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time      datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    person_id        char(255)                          not null comment '身份证号',
    name             varchar(255)                       not null comment '姓名',
    gender           char                               not null comment '性别',
    nation           varchar(255)                       not null comment '民族',
    birthday         date                               not null comment '出生日期',
    address          varchar(255)                       not null comment '住址',
    transaction_hash varchar(255)                       null comment '交易哈希',
    constraint person_id
        unique (person_id)
)
    comment '个人基本信息' charset = utf8mb3;


DROP TABLE IF EXISTS `person_user`;
create table `person_user`
(
    id           int unsigned auto_increment comment 'Primary Key'
        primary key,
    create_time  datetime   default CURRENT_TIMESTAMP    null comment '创建时间',
    update_time  datetime   default CURRENT_TIMESTAMP    null on update CURRENT_TIMESTAMP comment '更新时间',
    person_id    varchar(18)                             not null comment '身份证号',
    phone        varchar(11)                             not null comment '手机号码',
    email        varchar(50)                             not null comment '邮箱',
    password     varchar(255)                            not null comment '密码',
    salt         varchar(255)                            not null comment '盐值',
    sign_user_id varchar(255) collate utf8mb4_general_ci null comment '用户的身份证',
    public_key   varchar(255) collate utf8mb4_general_ci null comment '公钥',
    address      varchar(255) collate utf8mb4_general_ci null comment '地址',
    encrypt_type tinyint(1) default 0                    not null comment '密钥类型',
    type         tinyint(1) default 0                    not null comment '注册者类型：0是person，1是第三方',
    constraint person_id
        unique (person_id),
    constraint phone
        unique (phone)
)
    comment '平台个人信息' charset = utf8mb3;


DROP TABLE IF EXISTS `reproxy`;
create table `reproxy`
(
    id          int auto_increment
        primary key,
    sender_id   varchar(255)         null comment '发起人id（A用户）',
    receiver_id varchar(255)         null comment '接收人id（B用户）',
    cipher_text blob                 null comment '代理重加密后的密文，m''''',
    captcha     varchar(255)         null comment '验证码',
    capsule     text                 null comment '接收到的密文',
    new_capsule text                 null comment '加密后的X',
    public_x    text                 null comment '公开大素数X',
    create_time datetime             null on update CURRENT_TIMESTAMP comment '创建时间',
    update_time datetime             null on update CURRENT_TIMESTAMP comment '更新时间',
    state       tinyint(1) default 0 not null comment '状态：0,,,,,,,,1正在授权，2拒绝授权，3授权成功'
);

DROP TABLE IF EXISTS `role`;
create table `role`
(
    id          int unsigned auto_increment comment 'Primary Key'
        primary key,
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    role_code   int                                not null comment '角色编码',
    role_name   varchar(255)                       null comment '角色名称',
    create_user varchar(255)                       null comment '创建人'
)
    comment '角色表' charset = utf8mb3;


DROP TABLE IF EXISTS `role_permission`;
create table `role_permission`
(
    id            int unsigned auto_increment comment 'Primary Key'
        primary key,
    create_time   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    role_code     int                                not null comment '角色编码',
    permission_id int                                not null comment '权限id'
)
    comment '角色权限表' charset = utf8mb3;


DROP TABLE IF EXISTS `student`;
create table `student`
(
    id          int unsigned auto_increment comment 'Primary Key'
        primary key,
    create_time datetime         default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime         default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    stu_id      char(40)                                   not null comment '学号',
    person_id   char(18)                                   not null comment '身份证号',
    name        varchar(255)                               not null comment '姓名',
    edu_type    varchar(255)                               not null comment '教育类型',
    is_uplinked tinyint unsigned default '0'               null comment '是否已上链0/1',
    constraint stu_id
        unique (stu_id)
)
    comment '学生信息' charset = utf8mb3;


DROP TABLE IF EXISTS `user_captcha`;
create table `user_captcha`
(
    id           int auto_increment
        primary key,
    user_id      varchar(255) not null comment '用户id',
    capsule      text         null comment '第一次加密的m''',
    ciphere_text blob         null comment '加密二进制',
    captcha      varchar(255) not null comment '验证码',
    create_time  datetime     null on update CURRENT_TIMESTAMP,
    update_time  datetime     null on update CURRENT_TIMESTAMP
);


DROP TABLE IF EXISTS `user_key`;
create table `user_key`
(
    id          int auto_increment
        primary key,
    user_id     varchar(255)         not null comment '用户id，学员id为身份证',
    public_key  text                 null comment '公钥',
    type        tinyint(1) default 0 not null comment '0：学员，1：第三方',
    create_time datetime             null on update CURRENT_TIMESTAMP,
    update_time datetime             null on update CURRENT_TIMESTAMP
);
