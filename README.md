### 核心功能
基于TestNg的Dubbo接口自动化测试简易框架

已支持：
1. 数据驱动，基于yaml文件管理和维护
2. Dubbo服务泛化调用抽象成公共处理类，服务通过json文件进行维护，在服务接口方法中，以自定义注解的方式进行获取泛化所需的服务参数

待支持：
1. 预留测试接口服务统计处理器
2. 预留测试报告邮件自动发送处理器
3. 数据驱动可扩展从db或者excel中读取
4. 请求参数随机生成处理器


### 代码结构

``````
|-- gaea
  |-- base-service             // 提供一些公共等基础类
    |-- annos                  // 自定义注解
    |-- base                   // 基础服务类
    |-- bridge                 // 服务路由处理器
    |-- config                 // 自定义配置
    |-- driver                 // TestNg数据驱动处理类
    |-- utils                  // 自定义工具类
  |-- dubbo-api-service
    |-- service                // 业务服务
    |-- resources
      |-- api-config           // Double提供的服务接口相关配置，新增接口时必配
      |-- data/yaml            // 测试数据
    |-- application.properties // Double注册地址等配置信息
      
``````

### 操作说明

1. api-config/payment-api-dubbo.json 新增接口信息

````
{
  "schema": "payment.pay", //通过schema来区分不同的应用系统
  "apis": {                //应用的接口服务集合
    "pay": {               //服务id，对应@DubboAPI中的api，必填
      "interface" : "接口全限定类名", //对应@DubboAPI中的service，可不填
      "method" : "方法名",           //对应@DubboAPI中的method，可不填
      "request": {
        "var0": "${payRequestParams}" //参数，如果存在多个，则依次var1...varN,变量名不重复即可，无一一对应关系
      }
    }
  }
}
````

2. service中实现接口方法调用

   参考com.qa.service.pay.PaymentService#pay()

3. test中实现测试方法

    参考com.qa.payment.TestPay#test_pay()
    
4. 新增测试数据

    在data/yaml中创建测试数据源，文件名为测试类名，以.yml为后缀，文件内容按照测试方法名进行依次填写