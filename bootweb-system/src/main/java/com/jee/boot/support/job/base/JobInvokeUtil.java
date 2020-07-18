package com.jee.boot.support.job.base;

import com.jee.boot.common.utils.spring.SpringUtils;
import com.jee.boot.common.utils.text.JeeStringUtils;
import com.jee.boot.monitor.dto.SysJobDTO;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * 任务执行工具
 *
 * @author jeeLearner
 * @version V1.0
 */
public class JobInvokeUtil {

    /**
     * 执行方法
     *
     * @param sysJob 系统任务
     */
    public static void invokeMethod(SysJobDTO sysJob) throws Exception {
        String invokeTarget = sysJob.getInvokeTarget();
        String beanName = getBeanName(invokeTarget);
        String methodName = getMethodName(invokeTarget);
        List<Object[]> methodParams = getMethodParams(invokeTarget);
        if (!isValidClassName(beanName)) {
            //bean名
            Object bean = SpringUtils.getBean(beanName);
            invokeMethod(bean, methodName, methodParams);
        } else {
            //包含包名
            Object bean = Class.forName(beanName).newInstance();
            invokeMethod(bean, methodName, methodParams);
        }
    }

    /**
     * 调用任务方法
     *
     * @param bean 目标对象
     * @param methodName 方法名称
     * @param methodParams 方法参数
     */
    private static void invokeMethod(Object bean, String methodName, List<Object[]> methodParams)
            throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        if (JeeStringUtils.isEmpty(methodParams) || methodParams.size() == 0){
            Method method = bean.getClass().getDeclaredMethod(methodName);
            method.invoke(bean);
        } else {
            Method method = bean.getClass().getDeclaredMethod(methodName, getMethodParamsType(methodParams));
            method.invoke(bean, getMethodParamsValue(methodParams));
        }
    }

    /**
     * 获取参数类型
     *
     * @param methodParams 参数相关列表
     * @return 参数类型列表
     */
    public static Class<?>[] getMethodParamsType(List<Object[]> methodParams) {
        Class<?>[] classs = new Class<?>[methodParams.size()];
        int index = 0;
        for (Object[] os : methodParams) {
            classs[index] = (Class<?>) os[1];
            index++;
        }
        return classs;
    }

    /**
     * 获取参数值
     *
     * @param methodParams 参数相关列表
     * @return 参数值列表
     */
    public static Object[] getMethodParamsValue(List<Object[]> methodParams) {
        Object[] classs = new Object[methodParams.size()];
        int index = 0;
        for (Object[] os : methodParams) {
            classs[index] = os[0];
            index++;
        }
        return classs;
    }


    /**
     * 校验是否为为class包名
     *
     * @param invokeTarget 名称
     * @return true是 false否
     */
    public static boolean isValidClassName(String invokeTarget) {
        return JeeStringUtils.countMatches(invokeTarget, ".") > 1;
    }

    /**
     * 获取bean名称
     *      a.b.c() ==>  a.b
     *
     * @param invokeTarget 目标字符串
     * @return bean名称
     */
    private static String getBeanName(String invokeTarget) {
        String beanName = JeeStringUtils.substringBefore(invokeTarget, "(");
        return JeeStringUtils.substringBeforeLast(beanName, ".");
    }

    /**
     * 获取bean方法
     *     a.b.c() ==>  c
     * @param invokeTarget 目标字符串
     * @return method方法
     */
    public static String getMethodName(String invokeTarget) {
        String methodName = JeeStringUtils.substringBefore(invokeTarget, "(");
        return JeeStringUtils.substringAfterLast(methodName, ".");
    }

    /**
     * 获取method方法参数相关列表
     *
     * @param invokeTarget 目标字符串
     * @return method方法相关参数列表
     */
    private static List<Object[]> getMethodParams(String invokeTarget) {
        String methodStr = JeeStringUtils.substringBetween(invokeTarget, "(", ")");
        if (JeeStringUtils.isEmpty(methodStr)){
            return null;
        }
        String[] methodParams = methodStr.split(",");
        List<Object[]> classs = new LinkedList<>();
        for (int i = 0; i < methodParams.length; i++) {
            String str = JeeStringUtils.trimToEmpty(methodParams[i]);
            // String字符串类型，包含'
            if (JeeStringUtils.contains(str, "'")) {
                classs.add(new Object[] { JeeStringUtils.replace(str, "'", ""), String.class });
            }
            // boolean布尔类型，等于true或者false
            else if (JeeStringUtils.equals(str, "true") || JeeStringUtils.equalsIgnoreCase(str, "false")) {
                classs.add(new Object[] { Boolean.valueOf(str), Boolean.class });
            }
            // long长整形，包含L
            else if (JeeStringUtils.containsIgnoreCase(str, "L")) {
                classs.add(new Object[] { Long.valueOf(JeeStringUtils.replaceIgnoreCase(str, "L", "")), Long.class });
            }
            // double浮点类型，包含D
            else if (JeeStringUtils.containsIgnoreCase(str, "D")) {
                classs.add(new Object[] { Double.valueOf(JeeStringUtils.replaceIgnoreCase(str, "D", "")), Double.class });
            }
            // 其他类型归类为整形
            else {
                classs.add(new Object[] { Integer.valueOf(str), Integer.class });
            }
        }
        return classs;
    }

}

