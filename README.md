# own-commons
Some of their commonly used tool classes.

## own-core
包含了一些常用的工具类, 如: 统一的返回格式, 基础的错误码定义, 日志打印等... 
## own-db
包含了一些数据库相关的工具类, 如: 数据库连接池, 数据库操作, 数据库事务等...
## own-ext 
包含了一些扩展功能.
## own-leaf
包含了一些分布式ID生成器相关的工具类, 如: Snowflake 等...
## own-storage
包含了一些存储相关的工具类, 如: MiniO, 阿里OSS, 腾讯云OSS等...
## own-test 
测试相关
## own-own-third-party
包含了一些第三方工具类, 如: Prometheus, Kubernetes, Redis等...
> 不在配置中引入相关配置, 对应工具类, 不会加载到SpringIOC容器中, 可以根据需要引入相关配置
## own-user
包含了一些用户相关的工具类, 如: 密码加密, 密码校验等...
## own-web
包含了一些Web相关的工具类, 如: 验证码, 图片验证码等...



---
开发计划: 
 - own-task: 任务调度
 - own-redis: 分布式锁
 - own-rabbit: 消息队列
 - own-trace: SkyWalking
 - own-ext: SpEL, AOP, Cglib, 反射, 字节码增强等...