package com.jee.boot.admin.smartdoc;

import com.power.doc.builder.HtmlApiDocBuilder;
import com.power.doc.model.ApiConfig;

/**
 * @author jeeLearner
 * @version V1.0
 */

public class SmartDocConfig {

    public static void main(String[] args) {
        genApiDoc();
        System.out.println("gen ok ...");
    }

    /**
     * 生成api
     */
    public static void genApiDoc(){
        String outparh = "d:/md2/";

        ApiConfig config = new ApiConfig();
        config.setProjectName("API DOC");
        config.setServerUrl("localhost:8081");
        config.setStrict(false);
        config.setAllInOne(true);
        config.setShowAuthor(true);
        config.setOutPath(outparh);
        config.setPackageFilters("com.jee.boot.admin.controller.demo");
        HtmlApiDocBuilder.buildApiDoc(config);

    }
}

