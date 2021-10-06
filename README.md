# lowin

### 介绍

```
基于springboot的简单易用认证授权框架，相对于其他认证授权框架，lowin更为轻量级，对工程的倾入程度也更低。从设计理念上抛弃了shiro等框架对复杂程序环境的支持，仅支持前后端分离的Web环境，减少了不必要的性能开支，同时又借鉴并吸取了其他框架对于认证和授权的解决方案，使用方式相近，学习成本低。
运行环境要求: java 8+、spring boot2.x
```

### 入门

#### 核心功能（认证、授权）

lowin的认证和授权需要实现框架提供的接口，并装配为一条规则（最后与路径匹配的为规则名），一条规则必须有且仅有一个认证处理器，授权处理器可以为若干个也可为空。为了更形象展示，下文以一个token单点登录为案例介绍如何使用。

##### 认证对象

在介绍认证处理器之前先了解一下认证对象，认证对象又称身份校验对象，其只在认证和授权的过程中传播并接受处理，有两个需要重写的抽象方法。校验证书即从请求中获取出待检验的信息，在校验之前赋值，校验逻辑中可以对其进行逻辑判断是否满足身份条件；许可证即后续授权中是否授权以及授予何等权限的凭证。

```java
/**
 * 返回从请求中解析出的待校验信息
 *
 * @return 待校验信息（校验证书）
 */
Object getVerifyKey();

/**
 * 返回校验通过后得到的身份信息
 * tips: 可能与待校验信息一致也可能有更多信息被查询得到
 *
 * @return 校验成功后完善的身份信息（许可证）
 */
Object getAuthLicence();
```

以下为案例代码

```java
@Data
public class MyVerifyObject implements VerifyObject {
    private String token;

    private String userName;

    @Override
    public Object getVerifyKey() {
        return token;
    }

    @Override
    public Object getAuthLicence() {
        return userName;
    }
}
```

##### 认证处理器

认证处理器是规则的必要组成成分，工作流程为从请求中解析出认证对象（称为校验证书更合理，此时许可证可以为空），根据校验证书执行自定义校验逻辑。

```java
@Component
public class MyVerifyHandler implements VerifyHandler {

    @Override
    public boolean isAllowExecution(HttpServletRequest request, HttpServletResponse response) {
        return request.getHeader("token") != null;
    }

    @Override
    public VerifyObject createVerifyObject(HttpServletRequest request) {
        String token = request.getHeader("token");
        MyVerifyObject myVerifyObject = new MyVerifyObject();
        myVerifyObject.setToken(token);
        return myVerifyObject;
    }

    @Override
    public boolean verify(Object verifyKey) throws VerifyFailedException {
        String token = (String) verifyKey;
        if (token.length() >= 6) {
            throw new VerifyFailedException("长度异常");
        }
        if (token.contains("root")) {
            throw new VerifyFailedException("格式错误");
        }
        return true;
    }

    @Override
    public void verifySuccessCallBack(VerifyObject verifyObject) {
        MyVerifyObject myVerifyObject = (MyVerifyObject) verifyObject;
        // 查数据库将用户名赋值给许可证
    }
}
```

isAllowExecution、verifySuccessCallBack两个方法可以不重写，前者是认证流程前判断是否满足认证的先决条件；后者是认证成功后的回调方法，此处允许对许可证进行修改。

##### 授权处理器

授权的逻辑比较轻松，即通过许可证赋予当前请求主体应有的权限。SimpleAuthInfo为内置数据类型，当前版本中是否扩展意义不大。

```java
@Component
public class AuthHandlerTwo implements AuthHandler {
    @Override
    public AuthInfo getAuthInfo(Object authLicence) {
        String userName = (String) authLicence;
        SimpleAuthInfo simpleAuthInfo = new SimpleAuthInfo();
        // 此时应当查数据库并授权，下面的代码仅做测试用
        int randomInt = RandomUtil.randomInt(3);
        switch (randomInt % 3) {
            case 0:
                simpleAuthInfo.addRole("add");
                break;
            case 1:
                simpleAuthInfo.addRole("case");
                break;
            case 2:
                simpleAuthInfo.addRole("qqq");
        }
        return simpleAuthInfo;
    }
}
```

##### 规则装配管理

以上说明的所有组件无论是否被springboot的容器管理，都尚未加入受lowin管理的规则列表。

使用自定义规则之前必须先完成配置管理。

```
@Configuration
@RequiredArgsConstructor
public class LowInConfiguration {

    private final AuthHandlerOne authHandlerOne;

    private final AuthHandlerTwo authHandlerTwo;

    private final MyVerifyHandler myVerifyHandler;

    @Bean
    public RuleFactoryBean ruleFactoryBean() {
        RuleFactoryBean ruleFactoryBean = new RuleFactoryBean();
        ruleFactoryBean.addRule(Rule.create().ruleName("myRule").bindVerifyHandler(myVerifyHandler).addAuthHandler(authHandlerOne).addAuthHandler(authHandlerTwo));
        return ruleFactoryBean;
    }
}
```

如上所见必须要在spring容器中注入RuleFactoryBean，可以调用Rule类提供的api将之前所有自定义的组件装配为一个规则。

规则的命名不能与关键字重合，当前关键字有以下内容：

| lowin | r404 | r500 | blackList |
| :---: | :--: | :--: | :-------: |

##### 路由匹配

有了规则列表后，还不能与对应的接口或者说对应的请求关联上，想要真正完成核心功能还有最后一步，定义路由规则匹配逻辑。路由匹配遵循ant格式。

lowin提倡在配置文件中管理所有的路由规则匹配（匹配优先级仅次于黑白名单）

假设存在application-lowin.yml，填入以下配置

```yml
lowin:
  route-rules:
    - routePath: /boott/hello/in
      ruleName: r500
    - routePath: /boott/hello/**
      ruleName: myRule
    - routePath: /**
      ruleName: lowin
```

虽然最后匹配的是所有路径，但是lowin会根据编写顺序匹配优先级较高的逻辑。

#### 鉴权

鉴权是认证授权框架应当提供的最基础的功能，lowin有多种形式可以辅助开发者完成鉴权。

##### 注解鉴权

```java
@Service
@EnableAuthModel
public class HelloService {

    @EnableAuthModel
    public void say(AuthModel authModel) {
        System.out.println("HelloService.say");
        System.out.println("authModel = " + authModel);
    }
}
```

@EnableAuthModel可以注解在类上也可以注解在方法上，在类上时表示该类下的所有方法参数列表中如果存在AuthModel请求主体信息类则会自动对其注入当前请求主体信息，在方法上时即针对该方法处理。

必须注意的是不可在Controller层使用，除非是目标方法不接受前端任何参数，否则会出现不可预知的错误。

```java
@RequiredRoles(value = "root",logical = Logical.AND)
public String roles() {
}

@RequiredPermissions(value = "add",logical = Logical.OR)
public String permissions() {
}
```

@RequiredRoles、@RequiredPermissions这两个注解只能针对方法使用，value代表对应的角色名或者权限名，类型为字符串数组，logical代表并存关系或独立存在关系。

这一组注解可以加在任意方法上但是要求必须满足条件，否则抛出相关异常，且角色的优先级更高，若角色不能满足条件必然优先于权限不足的异常。

##### 工具类鉴权

lowin内置了AuthUtil工具类，涵盖了鉴权功能，与注解鉴权相比，更为安全。

首先提供了 AuthUtil.getAuthModel() 方法，可以获取请求主体信息。

关于鉴权方面提供的api较多，但使用方法几乎一致以下举两个实用例子

```java
// 鉴权后执行逻辑，接受两个参数，前者为鉴定条件，后者为执行逻辑，无返回值
AuthUtil.checkPermissions(
                condition -> condition
                        .findOne("case")
                        .or(conditionLink -> conditionLink.notFindOne("qqq"))
                        .and(conditionLink -> conditionLink.findOne("add")),
                () -> System.out.println("kkk"));

// 鉴权，返回鉴定结果,参数为鉴定条件
Boolean allowAdd = AuthUtil.queryPermissions(
                condition -> condition
                        .findOne("case")
                        .or(conditionLink -> conditionLink.notFindOne("qqq"))
                        .and(conditionLink -> conditionLink.findOne("add")));
```

