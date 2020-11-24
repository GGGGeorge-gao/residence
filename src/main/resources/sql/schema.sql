create table user
(
    id          int primary key auto_increment comment '自增id',
    username    varchar(255) not null comment '用户昵称',
    password    varchar(255) not null comment '密码',
    gender      tinyint comment '性别,男-0 女-1',
    phone       varchar(255) comment '电话号码',
    email       varchar(255) comment '邮箱',
    address     varchar(255) comment '住址',
    create_time timestamp    not null default CURRENT_TIMESTAMP comment '创建时间',
    update_time timestamp comment '修改时间',
    status      tinyint               default 1 comment '账号当前状态 0-禁用 1-启用'
) ENGINE = InnoDB
  CHARSET = utf8mb4;

create table role
(
    id   int primary key auto_increment comment '权限自增id',
    name varchar(255) not null comment '权限名称'
) ENGINE = InnoDB
  CHARSET = utf8mb4;

create table user_roles
(
    user_id int not null comment '用户id',
    role_id int not null comment '权限id',

    foreign key (user_id) references user (id),
    foreign key (role_id) references role (id)
) CHARSET = utf8mb4;

-- 用户登录日志表
create table login_info
(
    id         int primary key auto_increment,
    user_id    int       not null,
    login_time timestamp not null default CURRENT_TIMESTAMP comment '登录时间',
    ip         varchar(255) comment '登录ip',

    foreign key (user_id) references user (id)
) CHARSET = utf8mb4;

-- 场景表
create table scene
(
    id          int primary key auto_increment,
    parent_id   int comment '父id',
    user_id     int comment '所属用户id',
    name        varchar(255) not null comment '场景名称',
    description varchar(255) comment '描述',
    create_time timestamp    not null default CURRENT_TIMESTAMP comment '创建时间',
    update_time timestamp comment '修改时间',

    foreign key (user_id) references user (id)
) CHARSET = utf8mb4;

-- 插座表
create table receptacle
(
    id             int primary key auto_increment,
    user_id        int comment '所属用户id',
    scene_id       int comment '场景id',
    name           varchar(255) comment '插座名称',
    status         tinyint comment '当前状态',
    last_used_time timestamp comment '上一次使用时间',
    create_time    timestamp not null default CURRENT_TIMESTAMP comment '创建时间',
    update_time    timestamp comment '修改时间',

    foreign key (user_id) references user (id),
    foreign key (scene_id) references scene (id)
) CHARSET = utf8mb4;

-- 插孔表
create table jack
(
    id            int primary key auto_increment,
    user_id       int comment '所属用户id',
    receptacle_id int comment '插座id',
    type          tinyint comment '插孔类型',
    status        tinyint comment '插孔状态',

    foreign key (user_id) references user (id),
    foreign key (receptacle_id) references receptacle (id)
) CHARSET = utf8mb4;

-- 设备表
create table device
(
    id          int primary key auto_increment,
    user_id     int comment '所属用户id',
    jack_id     int comment '插孔id',
    name        varchar(255) comment '设备名称',
    status      tinyint comment '当前状态',
    create_time timestamp not null default CURRENT_TIMESTAMP comment '创建时间',
    update_time timestamp comment '修改时间',

    foreign key (user_id) references user (id),
    foreign key (jack_id) references receptacle (id)
) CHARSET = utf8mb4;

-- 预警信息表
create table alert
(
    user_id    int,
    email_auth varchar(255) comment '邮箱授权码',
    ding_token varchar(255) comment '钉钉token',

    foreign key (user_id) references user (id)
) CHARSET = utf8mb4;