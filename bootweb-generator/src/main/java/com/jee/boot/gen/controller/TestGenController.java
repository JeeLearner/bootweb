package com.jee.boot.gen.controller;

import com.jee.boot.common.core.result.R;
import com.jee.boot.common.utils.text.Convert;
import com.jee.boot.gen.domain.TableInfo;
import com.jee.boot.gen.service.IGenService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 代码生成 操作处理
 * 
 * @author jeeLearner
 */
@Controller
@RequestMapping("/test/tool")
public class TestGenController {
    private String prefix = "tool/gen";

    @Autowired
    private IGenService genService;

    @GetMapping("/a")
    public String index() {
        return prefix + "/gen";
    }

    @GetMapping("/list")
    @ResponseBody
    public R list(){
        List<TableInfo> list = genService.selectTableList(null);
        return R.ok().data("list", list);
    }

    /**
     * 生成代码
     */
    @GetMapping("/genCode/{tableName}")
    public void genCode(HttpServletResponse response, @PathVariable("tableName") String tableName) throws IOException {
        byte[] data = genService.generatorCode(tableName);
        genCode(response, data);
    }

    /**
     * 批量生成代码
     */
    @GetMapping("/batchGenCode")
    @ResponseBody
    public void batchGenCode(HttpServletResponse response, String tables) throws IOException {
        String[] tableNames = Convert.toStrArray(tables);
        byte[] data = genService.generatorCode(tableNames);
        genCode(response, data);
    }

    /**
     * 生成zip文件
     * 
     * @param response
     * @param data
     * @throws IOException
     */
    private void genCode(HttpServletResponse response, byte[] data) throws IOException {
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"jeeGen.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(data, response.getOutputStream());
    }
}
