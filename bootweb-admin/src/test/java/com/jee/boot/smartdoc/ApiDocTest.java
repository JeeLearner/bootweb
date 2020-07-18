package com.jee.boot.smartdoc;

import com.power.doc.builder.AdocDocBuilder;
import com.power.doc.builder.ApiDocBuilder;
import com.power.doc.builder.HtmlApiDocBuilder;
import com.power.doc.model.ApiConfig;
import com.power.doc.model.CustomRespField;
import org.junit.Test;

/**
 * 接口文档生成测试类
 *
 * @author jeeLearner
 * @version V1.0
 */
public class ApiDocTest {

    @Test
    public void testApiDoc(){
        String outparh = "d:/md2/";

        ApiConfig config = new ApiConfig();
        config.setProjectName("API DOC");
        config.setShowAuthor(true);
        //接口地址
        config.setServerUrl("http://localhost:8080");
        //严格模式
        config.setStrict(true);
        //是否生成到一个文档中
        config.setAllInOne(true);
        //文档输出地址
        config.setOutPath(outparh);
        //覆盖文件
        config.setCoverOld(true);
        //扫描包的过滤器
        config.setPackageFilters("com.jee.boot.admin.controller.tool");
        //生成adoc文件
        AdocDocBuilder.builderApiDoc(config);
        //生成md文件
        //ApiDocBuilder.buildApiDoc(config);
        //生成html文件
        HtmlApiDocBuilder.buildApiDoc(config);
    }
}

