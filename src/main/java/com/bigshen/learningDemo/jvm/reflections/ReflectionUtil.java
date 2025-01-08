package com.bigshen.learningDemo.jvm.reflections;


import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * @author byj
 * @date 2022/7/13
 * 反射 util
 */
public class ReflectionUtil {

    /**
     * 获取spring 代理对象的真实实例
     *
     * @param proxy 代理对象
     * @return
     * @throws Exception
     */
    public static Object getTarget(Object proxy) throws Exception {
        if (!AopUtils.isAopProxy(proxy)) {
            //不是代理对象
            return proxy;
        }

        if (AopUtils.isJdkDynamicProxy(proxy)) {
            return getJdkDynamicProxyTargetObject(proxy);
        } else { //cglib
            return getCglibProxyTargetObject(proxy);
        }
    }

    private static Object getCglibProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
        h.setAccessible(true);
        Object dynamicAdvisedInterceptor = h.get(proxy);
        Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        Object target = ((AdvisedSupport) advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();
        return target;
    }

    private static Object getJdkDynamicProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
        h.setAccessible(true);
        AopProxy aopProxy = (AopProxy) h.get(proxy);
        Field advised = aopProxy.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        Object target = ((AdvisedSupport) advised.get(aopProxy)).getTargetSource().getTarget();
        return target;
    }

    public static Method findMethod(Method[] methods, String name) {
        if (methods != null) {
            for (Method m : methods) {
                if (m.getName().equals(name)) {
                    return m;
                }
            }
        }
        return null;
    }

    public static boolean hasMethod(Method[] methods, String name) {
        return null != findMethod(methods, name);
    }

    public static Class<?> getClass(final String className) {
        if (null == className) {
            return null;
        }

        return AccessController.doPrivileged((PrivilegedAction<Class<?>>) () -> {
            try {
                ClassLoader classLoader = ReflectionUtil.class.getClassLoader();
                Class<?> clazz = (null == classLoader)
                        ? Class.forName(className)
                        : classLoader.loadClass(className);
                return clazz;
            } catch (Exception e) {
            }

            return null;
        });
    }

    public static <T> Constructor<T> getDeclaredConstructor(final String className, final Class<?>... parameterTypes) {
        if (null == className) {
            return null;
        }

        return AccessController.doPrivileged((PrivilegedAction<Constructor<T>>) () -> {
            try {
                ClassLoader classLoader = ReflectionUtil.class.getClassLoader();
                @SuppressWarnings("unchecked")
                Class<T> clazz = (Class<T>) ((null == classLoader)
                        ? java.lang.Class.forName(className)
                        : classLoader.loadClass(className));
                if (null != clazz) {
                    return clazz.getDeclaredConstructor(parameterTypes);
                }
            } catch (Exception e) {
            }

            return null;
        });
    }

    public static Method getMethod(final String className, final String methodName, final Class<?>... parameterTypes) {
        if (null == className || null == methodName) {
            return null;
        }

        return AccessController.doPrivileged((PrivilegedAction<Method>) () -> {
            try {
                ClassLoader classLoader = ReflectionUtil.class.getClassLoader();
                Class<?> clazz = (null == classLoader)
                        ? java.lang.Class.forName(className)
                        : classLoader.loadClass(className);

                if (null != clazz) {
                    return clazz.getMethod(methodName, parameterTypes);
                }
            } catch (Exception e) {
            }

            return null;
        });
    }

    public static Method[] getMethods(final String className) {
        if (null == className) {
            return null;
        }

        return AccessController.doPrivileged((PrivilegedAction<Method[]>) () -> {
            try {
                ClassLoader classLoader = ReflectionUtil.class.getClassLoader();
                Class<?> clazz = (null == classLoader)
                        ? java.lang.Class.forName(className)
                        : classLoader.loadClass(className);

                if (null != clazz) {
                    return clazz.getMethods();
                }
            } catch (Exception ignored) {
            }

            return null;
        });
    }

    public static Integer getStaticInt(final String className, final String fieldName) {
        return AccessController.doPrivileged((PrivilegedAction<Integer>) () -> {
            try {
                ClassLoader classLoader = ReflectionUtil.class.getClassLoader();
                Class<?> clazz = (null == classLoader)
                        ? java.lang.Class.forName(className)
                        : classLoader.loadClass(className);

                if (null != clazz) {
                    Field field = clazz.getField(fieldName);
                    Class<?> fieldType = field.getType();
                    if (int.class == fieldType) {
                        return field.getInt(null);
                    }
                }
            } catch (Exception ignored) {
            }

            return null;
        });
    }

    public static Integer getStaticIntOrDefault(final String className, final String fieldName, int defaultValue) {
        Integer value = getStaticInt(className, fieldName);
        return null == value ? defaultValue : value;
    }

    public static Object invokeGetter(final Object obj, final Method method) {
        return invokeMethod(obj, method);
    }

    public static Object invokeMethod(final Object obj, final Method method, final Object... args) {
        return AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
            try {
                return method.invoke(obj, args);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void invokeSetter(final Object obj, final Method method, final Object arg) {
        invokeMethod(obj, method, arg);
    }

    /**
     * 用于单测调用private方法
     */
    public static void main(String[] args) throws Exception {
        Class<?> clazz = java.lang.Class.forName("TestServiceImpl");
        Method method = clazz.getDeclaredMethod("test2");
        method.setAccessible(true);
        Object target = ReflectionUtil.getTarget(ReflectionUtil.class);
        // 注意，这里不能直接用serviceImpl，因为它已经被spring管理，
        // 变成serviceImpl真实实例的代理类，而代理类中并没有私有方法，所以需要先获取它的真实实例
        method.invoke(target);
    }
}
