package com.jee.boot.alone.license.server.utils;

import java.io.File;

/**
 * @author jeeLearner
 * @version V1.0
 */
public class FileUtils {

    /**
     * 文件不存在则创建
     * @param filePath
     */
    public static void checkParentFile(String filePath){
        File desc = new File(filePath);
        if (!desc.getParentFile().exists()){
            desc.getParentFile().mkdirs();
        }
//        if (!desc.exists()){
//            desc.createNewFile();
//        }
    }

}

