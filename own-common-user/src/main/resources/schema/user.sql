-- ----------------------------
-- 用户表 (担心user会是关键字, 就用 't_user' 吧)
-- ----------------------------
-- drop table if exists t_user;
create table if not exists t_user (
    id            varchar(50)    not null       comment '用户ID',
    username      varchar(30)    not null       comment '用户账号',
    password      varchar(255)   not null       comment '密码',
    nickname      varchar(30)    default ''     comment '用户昵称',
    email         varchar(50)    default ''     comment '用户邮箱',
    tel           varchar(15)    default ''     comment '手机号码',
    sex           tinyint(1)     default 0      comment '用户性别(1男 2女 0未知)',
    avatar        varchar(255)   default ''     comment '头像地址',
    status        tinyint(1)     default 0      comment '帐号状态(0正常 1停用)',
    create_time   datetime       default now()  comment '创建时间',
    update_time   datetime       default now()  comment '更新时间',
    deleted       tinyint(1)     default 0      comment '删除标志(0否 1是)',
    primary key (id)
) engine = innodb comment = '用户表';
create unique index t_user_username_index on t_user (username);