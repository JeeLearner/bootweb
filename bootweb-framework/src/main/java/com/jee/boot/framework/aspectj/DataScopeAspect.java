package com.jee.boot.framework.aspectj;

import com.jee.boot.common.annotation.DataScope;
import com.jee.boot.common.core.domain.BaseEntity;
import com.jee.boot.common.utils.text.JeeStringUtils;
import com.jee.boot.framework.service.ISysService;
import com.jee.boot.system.dto.SysRoleDTO;
import com.jee.boot.system.dto.SysUserDTO;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 数据过滤处理
 *
 * @author jeeLearner
 * @version V1.0
 */

/**
 * 数据过滤处理
 *
 * 文档说明：=======================================================================================================
 *
 * 全部权限1:
 *
 * 自定数据权限2:
 * 	   OR u.dept_id IN ( SELECT dept_id FROM sys_role_dept WHERE role_id = 2 )
 * 	  AND (u.dept_id IN ( SELECT dept_id FROM sys_role_dept WHERE role_id = 2 ) )
 *
 * 部门数据权限3:
 * 	   OR u.dept_id = 103
 * 	  AND (u.dept_id = 103 )
 *
 * 部门及以下数据权限4：
 * 	   OR u.dept_id IN ( SELECT dept_id FROM sys_dept WHERE dept_id = 103 OR FIND_IN_SET( 103 , ancestors ) )
 * 	  AND (u.dept_id IN ( SELECT dept_id FROM sys_dept WHERE dept_id = 103 OR FIND_IN_SET( 103 , ancestors ) ))
 *
 * 仅本人数据权限5:
 * 	userAlias不为空
 * 	   OR u.user_id = 2
 *    AND u.user_id = 2
 * 	数据权限为仅本人且没有userAlias别名不查询任何数据
 * 	   OR 1=0
 * 	  AND (1=0 )
 * ================================================================================================================
 *
 数据权限：
     角色：这里有个bug，后测
         USER->role->dept->users->role
         102->2->100/101/104/107->4/101->2
     用户：
         USER->role->dept->users
         102->2->100/101/104/107->相关联的users
     部门：
         USER->role->dept
         102->2->100/101/104/107
 *
 * =============================================================================
 * 注解方法：
     SysRoleServiceImpl
        listRole
     SysUserServiceImpl
         listUser
         listAllocatedRole
         listUnallocatedRole
     SysDeptServiceImpl
         listDept
         listDeptTree
         listDeptTreeExcludeChild
 *
 *  * @author jeeLearner
 *  * @version V1.0
 */
@Aspect
@Component
public class DataScopeAspect {

    /**
     * 数据权限过滤关键字
     */
    public static final String DATA_SCOPE = "dataScope";

    @Autowired
    private ISysService sysService;

    @Pointcut("@annotation(com.jee.boot.common.annotation.DataScope)")
    public void dataScopePointCunt(){

    }


    @Before("dataScopePointCunt()")
    public void doBefore(JoinPoint point) throws Throwable{
        handleDataScope(point);
    }


    private void handleDataScope(final JoinPoint point) {
        //获得注解
        DataScope annDataScope = getAnnotationLog(point);
        if (annDataScope == null){
            return;
        }
        // 获取当前的用户
        SysUserDTO currentUser = sysService.getCurrentUser();
        if (currentUser != null){
            // 如果是超级管理员，则不过滤数据
            if (!currentUser.isAdmin()){
                dataScopeFilter(point, currentUser, annDataScope.deptAlias(), annDataScope.userAlias());
            }
        }


    }

    /**
     * 数据范围过滤
     *
     * @param joinPoint 切点
     * @param user 用户
     * @param deptAlias 部门别名
     * @param userAlias 用户别名
     */
    private void dataScopeFilter(JoinPoint joinPoint, SysUserDTO user, String deptAlias, String userAlias){
        StringBuffer sqlString = new StringBuffer();

        for (SysRoleDTO role : user.getRoles()) {
            if (DataScopeType.ALL.getValue().equals(role.getDataScope())){
                sqlString.setLength(0);
                break;
            } else if (DataScopeType.CUSTOM.getValue().equals(role.getDataScope())){
                sqlString.append(JeeStringUtils.format(
                        " OR {}.dept_id IN ( SELECT dept_id FROM sys_role_dept WHERE role_id = {} ) ", deptAlias,
                        role.getRoleId()));
            } else if (DataScopeType.DEPT.getValue().equals(role.getDataScope())){
                sqlString.append(JeeStringUtils.format(" OR {}.dept_id = {} ", deptAlias, user.getDeptId()));
            } else if (DataScopeType.DEPT_AND_CHILD.getValue().equals(role.getDataScope())){
                sqlString.append(JeeStringUtils.format(
                        " OR {}.dept_id IN ( SELECT dept_id FROM sys_dept WHERE dept_id = {} or find_in_set( {} , ancestors ) )",
                        deptAlias, user.getDeptId(), user.getDeptId()));
            } else if (DataScopeType.SELF.getValue().equals(role.getDataScope())){
                if (JeeStringUtils.isNotBlank(userAlias)){
                    sqlString.append(JeeStringUtils.format(" OR {}.user_id = {} ", userAlias, user.getUserId()));
                } else {
                    // 数据权限为仅本人且没有userAlias别名不查询任何数据
                    sqlString.append(" OR 1=0 ");
                }
            }
        }
        System.out.println(sqlString.toString());
        if (JeeStringUtils.isNotEmpty(sqlString.toString())){
            //joinPoint.getArgs()是DataScope注解的方法里的参数 SysRoleDTO/SysDeptDTO...
            BaseEntity baseEntity = (BaseEntity) joinPoint.getArgs()[0];
            baseEntity.getParams().put(DATA_SCOPE, " AND (" + sqlString.substring(4) + ")");
            System.out.println(baseEntity.getParams().get(DATA_SCOPE));
        }
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private DataScope getAnnotationLog(JoinPoint joinPoint){
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null){
            return method.getAnnotation(DataScope.class);
        }
        return null;
    }
}

enum DataScopeType {
    /*** 全部数据权限*/
    ALL("1"),
    /*** 自定数据权限*/
    CUSTOM("2"),
    /*** 部门数据权限*/
    DEPT("3"),
    /*** 部门及以下数据权限*/
    DEPT_AND_CHILD("4"),
    /*** 仅本人数据权限*/
    SELF("5");

    private final String value;

    DataScopeType(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}

