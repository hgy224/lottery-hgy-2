# Lottery开发日志

### 配置

```shell
# mysql

# nacos 注册中心用于rpc接口服务发现
./startup.sh -m standalone  # 启动
# http://127.0.0.1:8848/nacos  进行配置管理
```

### 工具类的用法

```java
// hutool-all工具包
BeanUtil.copyProperties(src, dst)  // 将一个对象中的属性复制到另一个对象中，属性名相同的就复制过去，两个对象的类不一定相同
JSONUtil.toJsonStr(obj)      // 将对象转化为json字符串
```

### 知识点

```java
// 进行rpc远程通信传输的类一定要实现序列化接口
```





### 遇到的问题

```java
// --------------------------------------------------
// maven进行install时出现错误，提示子模块也需要main
// 这是因为使用spring-boot-maven-plugin来build，需要main
// 将所有的pom文件中的<build>去掉就行

// --------------------------------------------------
// 测试工程rpc的时候，找不到tests，这是因为在创建工程的时候，没有选用maven管理工程

```







### 模块分离搭建系统

- 使用IDEA创建springboot工程，springboot3.1.3, Java17
- 删除src文件夹，将pom文件中的<dependencies>去掉，打包方式改为pom使得该工程为父工程，进行分模块管理
- 创建6个模块
  - 在父工程pom文件中引入这6个模块
  - 将子模块的pom文件中的<parent>改为父工程

![system](/Users/hgy/IdeaProjects/lottery-hgy-2/doc/imgs/system.png)

#### 对各个模块的理解

- **lottery-rpc**: 提供外部访问的rpc接口，打包之后由外部引入pom配置可以使用
- **lottery-interfaces**: 实现rpc接口，整个程序的入口，外部调用rpc接口这里就会执行
- **lottery-infrastructure**: 在这个模块完成数据库的访问
- **lottery-domain**:  
- **lottery-common**: 定义一些通用的返回对象、常量、枚举、异常
- **lottery-application**: 

lottery-interfaces实现rpc接口对外提供服务，所以需要作为整个程序的入口，保留该模块的springboot启动类和配置文件，其他模块的都删除，为了部署需要将该模块打包为war包，其他模块只需要被引用所以打包为jar包

### rpc远程调用接口测试

#### 需求

实现外部可以调用接口的功能，使用Dubbo实现远程调用

接口为根据活动id查询活动，外部调用该接口可以通过活动id查询到该活动

#### 实现步骤

##### 在数据库中创建一个活动表用于测试，用于存放活动信息

```mysql
create database lottery1; # 创建数据库
DROP TABLE IF EXISTS `activity`;
# 创建活动表
CREATE TABLE `activity` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `activity_id` bigint(20) NOT NULL COMMENT '活动ID',
  `activity_name` varchar(64) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '活动名称',
  `activity_desc` varchar(128) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '活动描述',
  `begin_date_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_date_time` datetime DEFAULT NULL COMMENT '结束时间',
  `stock_count` int(11) DEFAULT NULL COMMENT '库存',
  `take_count` int(11) DEFAULT NULL COMMENT '每人可参与次数',
  `state` tinyint(2) DEFAULT NULL COMMENT '活动状态：1编辑、2提审、3撤审、4通过、5运行(审核通过后worker扫描状态)、6拒绝、7关闭、8开启',
  `creator` varchar(64) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_activity_id` (`activity_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='活动配置';

```

##### 依赖引入

- lottery-interfaces

```xml
<!--web程序都需要引入该模块，自带tomcat，作为整个程序的入口需要引入-->
<dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<!--用于测试-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>

<!--引入lottery-infrastructure模块，为了使用访问数据库的操作-->
<dependency>
    <groupId>com.hgy</groupId>
    <artifactId>lottery-infrastructure</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>

<!--使用Dubbo完成rpc远程调用服务-->
<dependency>
    <groupId>org.apache.dubbo</groupId>
    <artifactId>dubbo-spring-boot-starter</artifactId>
    <version>3.2.6</version>
</dependency>
<!--使用nacos注册中心进行服务发现-->
<dependency>
    <groupId>org.apache.dubbo</groupId>
    <artifactId>dubbo-registry-nacos</artifactId>
    <version>3.2.6</version>
</dependency>

<!--该模块需要实现rpc接口，所以需要引入rpc模块-->
<dependency>
    <groupId>com.hgy</groupId>
    <artifactId>lottery-rpc</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

- lottery-infrastructure

```xml
<!--提供对数据库的访问，所以需要引入对应的模块-->
<!--mybatis, 提供访问数据库的接口-->
<dependency>
      <groupId>org.mybatis.spring.boot</groupId>
      <artifactId>mybatis-spring-boot-starter</artifactId>
      <version>3.0.2</version>
</dependency>
<!--mysql驱动, springboot2.7.8及以上使用mysql-connector-j, 之前使用mysql-connector-java-->
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
<!--lombok来简化get,set操作-->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>
```

- lottery-common

```xml
<!--lombok来简化get,set操作-->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>
```



##### yml文件配置

```yaml
# 连接数据库相关配置
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password: hgy7777777
    url: jdbc:mysql://127.0.0.1:3306/lottery1
    
# mybatis相关配置
mybatis:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
    map-underscore-to-camel-case: true  # 实体类中属性名可以使用驼峰命名
    
# dubbo配置
dubbo:
  application:
    name: lottery-hgy-2  # 应用名称
  registry:
    address: nacos://127.0.0.1:8848  # 注册中心地址
  protocol:  # 通信协议和端口
    name: dubbo
    port: 20880
  scan:  # 扫描包，将该包下所有的rpc接口注册到服务中
    base-packages: com.hgy.lottery.rpc
```

##### 定义一些通用的类

```java
// com.hgy.lottery.common.Result  统一返回对象中的code码和信息描述
// 由于该类会在通信时进行传输所以需要实现序列化Serializable接口

// 定义枚举信息ResponseCode，表示code和信息描述之间的对应关系
```

##### 访问数据库相关类的编写

- 实体类和数据访问对象

```java
// com.hgy.lottery.infrastructure.po.Activity
// 数据库中的datatime类型对应java中的Date

// com.hgy.lottery.infrastructure.dao.IActivityDao   
// 接口，定义访问数据库的各种操作
// Dao接口类需要加@Mapper注解，在编译时会生成对应的实现类
// 可以使用注解或xml文件来使用mysql语句定义操作
// ---------------------使用注解---------------------
@Mapper
public interface IActivityDao {

    @Insert("")  // 里面填写对应的sql语句
    void insert(Activity req);

    @@Select("")
    Activity queryActivityById(Long activityId);
}

```

##### lottery-rpc定义rpc接口

```java
// com.hgy.lottery.rpc.IActivityBooth  用于向外部提供各种操作活动的接口

// 以下包中的类都需要实现序列化接口
// com.hgy.lottery.rpc.req    通信时发送请求包含的信息
// com.hgy.lottery.rpc.res    通信时收到回复包含的信息
// com.hgy.lottery.rpc.dto    通信时实际有用的信息
```

##### lottery-interfaces实现rpc接口

```java
// 需要在启动类上加@EnableDubbo使用Dubbo

// com.hgy.lottery.interfaces.ActivityBooth
// 该类需要加一个@DubboService代表将该类交由Dubbo管理，rpc远程调用时，可以使用该对象
```

##### 使用maven install 主工程

```java
// install的时候会报Unable to find main class错误，在其他的模块找不到main
// 这是因为使用spring-boot-maven-plugin来build，需要main
// 将所有的pom文件中的<build>去掉就行
```

##### 测试

```java
// 本地测试功能
// @SpringBootTest  测试类
// @Slf4j  使用日志 log 打印 需要lombok

// 创建测试工程，测试rpc接口
// 引入dubbo nacos相关依赖，引入lottery-rpc依赖
// yml配置dubbo
dubbo:
  application:
    name: lottery-test
  registry:
    address: nacos://127.0.0.1:8848
  protocol:
    name: dubbo
    port: 20880
// 启动类加上@EnableDubbo
@DubboReference(interfaceClass = IActivityBooth.class, url = "dubbo://127.0.0.1:20880")
// 使用rpc接口类时需要再上这个注解，进入注入
```

