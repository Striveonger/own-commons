-- ----------------------------
-- 2. 文件表(文件存储服务)
-- ----------------------------
-- drop table if exists t_file;
create table if not exists t_file (
    id            varchar(50)    not null       comment '文件ID',
    filename      varchar(30)    not null       comment '文件名',
    filepath      varchar(255)   not null       comment '文件路径',
    filetype      varchar(30)    not null       comment '文件类型',
    hashcode      varchar(255)   not null       comment '文件Hash',
    create_time   datetime       default now()  comment '创建时间',
    update_time   datetime       default now()  comment '更新时间',
    deleted       tinyint(1)     default 0      comment '删除标志(0否 1是)',
    primary key (id)
) engine = innodb comment = '文件表';
