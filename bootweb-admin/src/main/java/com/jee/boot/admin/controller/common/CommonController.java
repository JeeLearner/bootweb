package com.jee.boot.admin.controller.common;

import com.jee.boot.common.config.BWProp;
import com.jee.boot.common.constant.Constants;
import com.jee.boot.common.constant.SysConstants;
import com.jee.boot.common.core.result.R;
import com.jee.boot.common.utils.file.FileUploadUtils;
import com.jee.boot.common.utils.file.FileUtils;
import com.jee.boot.common.utils.spring.ServerUtils;
import com.jee.boot.common.utils.spring.SpringUtils;
import com.jee.boot.common.utils.text.JeeStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * 通用请求处理
 *
 * @author jeeLearner
 * @version V1.0
 */
@Controller
public class CommonController {

    private static final Logger log = LoggerFactory.getLogger(CommonController.class);


    /**
     * 通用下载请求
     *
     * @param fileName 文件名称
     * @param delete 是否删除
     */
    @GetMapping("common/download")
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request){
        try {
            if (JeeStringUtils.isEmpty(fileName)){
                throw new Exception("文件名不能为空");
            }
            if (!FileUtils.isValidFilename(fileName)){
                throw new Exception(JeeStringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
            }
            //fileName：   用户信息_cbe5048c-2595-4897-b78a-a09ffda600da.xlsx
            //realFileName： 用户信息_1593223687203.xlsx
            String realFileName = fileName.substring(0, fileName.indexOf("_")+1) + System.currentTimeMillis() + fileName.substring(fileName.lastIndexOf("."));
            String filePath = BWProp.getDownloadPath() + fileName;

            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition",
                    "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, realFileName));
            FileUtils.writeBytes(filePath, response.getOutputStream());
            if (delete){
                FileUtils.deleteFile(filePath);
            }
        } catch (Exception e){
            log.error("下载文件失败", e);
        }
    }

    /**
     * 通用上传请求
     */
    @PostMapping("/common/upload")
    @ResponseBody
    public R uploadFile(MultipartFile file) throws Exception {
        try {
            // 上传文件路径
            String filePath = BWProp.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = ServerUtils.getUrl() + fileName;
            return R.ok().data("fileName", fileName).data("url", url);
        } catch (Exception e){
            return R.error().msg(e.getMessage());
        }
    }

    /**
     * 本地资源通用下载
     */
    @GetMapping("/common/download/resource")
    public void resourceDownload(String resource, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // 本地资源路径
        String localPath = BWProp.getProfile();
        // 数据库资源地址
        String downloadPath = localPath + JeeStringUtils.substringAfter(resource, SysConstants.RESOURCE_PREFIX);
        // 下载名称
        String downloadName = JeeStringUtils.substringAfterLast(downloadPath, "/");
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition",
                "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, downloadName));
        FileUtils.writeBytes(downloadPath, response.getOutputStream());
    }

    public static void main(String[] args) {
        String str = "aaa.bbb.doc";
        String sp = ".";
        System.out.println(StringUtils.substringBefore(str, sp));
        System.out.println(StringUtils.substringBeforeLast(str, sp));
        System.out.println(StringUtils.substringAfter(str, sp));
        System.out.println(StringUtils.substringAfterLast(str, sp));
    }
}

