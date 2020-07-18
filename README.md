# 模块
* bootweb-generator  代码生成器
* bootweb-system  系统模块
    * domain
    * dto
    * vo
    * service
    * mapper
* bootweb-common  公共模块【工具类/xss】
    * annotation
    * constant
    * core
        * domain    
        * page
        * result
        * web
    * enums
    * exception
    * utils
    * xss
* bootweb-framework  框架模块
    * aspectj
    * config
    * datasource
    * manager
    * cacheservice
    * restlocal
    * utils
    * verifycode
* bootweb-admin  web模块
    * shiro
    * admin
* bootweb-api  接口模块
    * api
        * controller
        * redis
        * cache
        * utils 
    * security
        * 
    
admin
auth
framework
system	...
common

# 功能
* 功能模块
    * 用户管理
    * 菜单管理
    * 角色管理

# 规范
* StringUtils/CollectionUtils自己实现，方便后期统一修改判断方式
* @SuppressWarnings({ "rawtypes", "unchecked" }) 禁止显示警告
* @ConfigurationProperties("spring.datasource.druid.master")注解在方法上需要配合@Bean使用


# 问题
* com.jee.boot.common.utils.spring.ServerUtils.java 测试获取
* com.jee.boot.common.utils.text.Convert.java  测试toIntArray：一个数字是否正常
* com.jee.boot.common.utils.text.Convert.java  测试str：byte[]死循环吗？
* com.jee.boot.common.utils.thread.ThreadUtils  里面方法测试、重试方法调整
* com.jee.boot.shiro.session.OnlineSessionDAO.syncToDb() 逻辑？


# 待完成
* RepeatSubmit
* MyBatisConfig
* ShiroConfig





用户N-1角色
用户1-N岗位
角色1-N菜单
角色1-N部门????



thymeleaf
    * 1.$符号取上下文中的变量：
      <input type="text" name="userName"  th:value="${user.name}">

      2.#符号取thymeleaf工具中的方法、文字消息表达式：
      
      <p th:utext="#{home.welcome}">Welcome to our grocery store!</p>
      
      3.　*{...}选择表达式一般跟在th:object后，直接选择object中的属性
      
      <div th:object="${session.user}">
      
      <p th:text="*{name}"/><div>
      
校验名字是否存在时，无需判断其状态是否可用

1.<!-- 数据范围过滤 -->
${params.dataScope}    
2.userMapper的VO
3.deptMapper.updateDeptChildren未实现

超管角色、用户id=1，配置yum实现



修改role后，ShiroUtils.clearCachedAuthorizationInfo();

修改role数据权限dept后，
//本用户的数据权限重置
ShiroUtils.setSysUser(userService.selectUserById(ShiroUtils.getSysUser().getUserId()));

// 表示通过aop框架暴露该代理对象,AopContext能够访问
@EnableAspectJAutoProxy(exposeProxy = true)
AopContext.currentProxy()  


层级包名：
    controller
        list/add/addSave/edit/editSave/remove/clear/check/detail/import
    service
        list/get/insert/update/delete/clear/count/check/import
    dao
        select/insert/update/delete/count