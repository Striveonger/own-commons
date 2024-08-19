-- ----------------------------
-- 号段分配表(ID服务)
-- ----------------------------
-- drop table if exists t_alloc;
create table if not exists t_alloc (
    tag           varchar(128)   not null default '',
    max_id        bigint(20)     not null default '1',
    step          int(11)        not null default '1',
    description   varchar(256)   default null,
    create_time   datetime       default now()  comment '创建时间',
    update_time   datetime       default now()  comment '更新时间',
    deleted       tinyint(1)     default 0      comment '删除标志(0否 1是)',
    primary key (tag)
) engine=innodb comment = '号段分配表';