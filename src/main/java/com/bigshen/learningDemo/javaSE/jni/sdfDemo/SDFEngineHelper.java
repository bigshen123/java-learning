package com.bigshen.learningDemo.javaSE.jni.sdfDemo;

import com.bigshen.learningDemo.javaSE.jni.sdfDemo.exception.SdfException;
import com.sun.jna.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 本接口按照0018-2012_密码设备应用接口规范_试用的C接口改造而来
 * 约定：
 *      [IN]     表示输入参数
 *      [OUT]    表示输出参数
 *      [IN|OUT] 表示输入输出参数
 *      返回值ResultObj中ResultObj.ret_code表示接口调用的返回值，0：成功，非0：表示错误码
 *                      其他字段的含义根据具体接口而定
 */
public class SDFEngineHelper{
    /**
     * logger
     **/
    private static final Logger logger = LoggerFactory.getLogger(SDFEngineHelper.class);
    public static final String LINUX_X64 = "/com/koal/sdf_engine/linux/x64/libsdf_engine_helper.so";
    public static final String LINUX_ARM = "/com/koal/sdf_engine/linux/arm/libsdf_engine_helper.so";
    public static final String WINDOWS_X64 = "/com/koal/sdf_engine/windows/x64/sdf_engine_helper.dll";
    public static final String OSX_X64 = "/com/koal/sdf_engine/osx/x64/libsdf_engine_helper.dylib";
    static {
        String libName = "sdf_engine_helper";

        // 优先尝试手动加载情况
        try {
            // 尝试加载，用于支持手动放置动态库的情况
            System.loadLibrary(libName);
        } catch (Throwable e1) {
            logger.debug("无法获取java.library.path下的sdf_engine_helper动态库！");
            // 如果加载不到，从Jar包释放
            // 还是沿用之前旧方法，使用操作系统的临时目录存放so库文件
            // 因为如果把so文件放到到项目目录中，就会导致在X86机器上部署好的环境放到ARM上就无法直接运行，反之亦然
            try {
                if (Platform.isLinux() && Platform.isIntel()) {
                    NativeUtils.loadLibraryFromJar(LINUX_X64);
                } else if (Platform.isLinux() && Platform.isARM()){
                    NativeUtils.loadLibraryFromJar(LINUX_ARM);
                } else if (Platform.isWindows() && Platform.isIntel()) {
                    NativeUtils.loadLibraryFromJar(WINDOWS_X64);
                } else if (Platform.isMac() && Platform.isIntel()) {
                    NativeUtils.loadLibraryFromJar(OSX_X64);
                }
            } catch (Exception e2) {
                // 如果加载不到，则抛出异常
                throw new SdfException("加载SDF动态库异常", e2);
            }
        }

    }

    /**
     * 加载指定厂商的so
     * @param[IN] so_name so的名称
     *                    so查找优先级: 1: 当前工作目录 > 2: 系统目录
     *                    linux下 /lib > /lib64 > /usr/lib
     * @return result_obj 返回值对象，返回值非0时，err_msg表示错误信息
     */
    public native ResultObj loadSDF(String so_name);

    /**
     * 启用日志
     * @param[IN] enable  true: 启用，false: 关闭，默认启用日志
     */
    public native void enableLog(boolean enable);
}
