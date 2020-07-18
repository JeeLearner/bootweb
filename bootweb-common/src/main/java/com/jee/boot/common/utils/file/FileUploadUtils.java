package com.jee.boot.common.utils.file;

import com.jee.boot.common.config.BWProp;
import com.jee.boot.common.constant.Constants;
import com.jee.boot.common.constant.SysConstants;
import com.jee.boot.common.exception.file.FileNameLengthLimitExceededException;
import com.jee.boot.common.exception.file.FileSizeLimitExceededException;
import com.jee.boot.common.exception.file.InvalidExtensionException;
import com.jee.boot.common.utils.DateUtils;
import com.jee.boot.common.utils.security.Md5Utils;
import com.jee.boot.common.utils.text.JeeStringUtils;
import org.apache.commons.io.FilenameUtils;
import org.assertj.core.util.DateUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * 文件上传工具类
 *
 * @author jeeLearner
 * @version V1.0
 */
public class FileUploadUtils {

    /*** 默认大小 50M */
    public static final long DEFAULT_MAX_SIZE = 50 * 1024 * 1024;

    /*** 默认的文件名最大长度 100 */
    public static final int DEFAULT_FILE_NAME_LENGTH = 100;

    /*** 默认上传的地址 */
    private static String defaultBaseDir = BWProp.getProfile();

    private static int counter = 0;

    /**
     * 自定义默认baseDir
     * @param defaultBaseDir
     */
    public static void setDefaultBaseDir(String defaultBaseDir){
        FileUploadUtils.defaultBaseDir = defaultBaseDir;
    }

    public static String getDefaultBaseDir(){
        return defaultBaseDir;
    }


    /**
     * 以默认配置进行文件上传
     *
     * @param file 上传的文件
     * @return 文件名称
     * @throws Exception
     */
    public static final String upload(MultipartFile file) throws IOException{
        try{
            return upload(getDefaultBaseDir(), file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    /**
     * 根据文件路径上传
     *
     * @param baseDir 相对应用的基目录
     * @param file 上传的文件
     * @return 文件名称
     * @throws IOException
     */
    public static final String upload(String baseDir, MultipartFile file) throws IOException {
        try{
            return upload(baseDir, file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
        } catch (Exception e){
            throw new IOException(e.getMessage(), e);
        }
    }

    /**
     * 文件上传
     *
     * @param baseDir 相对应用的基目录
     * @param file 上传的文件
     * @param allowedExtension 上传文件类型
     * @return 返回上传成功的文件名
     * @throws FileSizeLimitExceededException 如果超出最大大小
     * @throws FileNameLengthLimitExceededException 文件名太长
     * @throws IOException 比如读写文件出错时
     * @throws InvalidExtensionException 文件校验异常
     */
    public static final String upload(String baseDir, MultipartFile file, String[] allowedExtension)
            throws FileSizeLimitExceededException, IOException, FileNameLengthLimitExceededException,
            InvalidExtensionException {

        int filenameLength = file.getOriginalFilename().length();
        if (filenameLength > DEFAULT_FILE_NAME_LENGTH){
            throw new FileNameLengthLimitExceededException(DEFAULT_FILE_NAME_LENGTH);
        }
        //校验文件大小和类型
        assertAllowd(file, allowedExtension);
        //编码文件名   2020/06/19/b5aac38cbdf83edae9d686095590d196.png
        String fileName = extractFilename(file);
        //得到文件路径  D:\bw\avatar\2020\06\19\b5aac38cbdf83edae9d686095590d196.png
        File desc = getAbsoluteFile(baseDir, fileName);
        //上传
        file.transferTo(desc);
        //  /profile/avatar/2020/06/19/b5aac38cbdf83edae9d686095590d196.png
        String pathFileName = getPathFileName(baseDir, fileName);
        return pathFileName;
    }

    /**
     *
     * @param uploadDir
     * @param fileName
     * @return
     */
    private static final String getPathFileName(String uploadDir, String fileName) {
        //D:/bw/   7
        int dirLastIndex = BWProp.getProfile().length() + 1;
        //  avatar
        String currentDir = JeeStringUtils.substring(uploadDir, dirLastIndex);
        //  /profile/avatar/2020/06/19/b5aac38cbdf83edae9d686095590d196.png
        String pathFileName = SysConstants.RESOURCE_PREFIX + "/" + currentDir + "/" + fileName;
        return pathFileName;
    }

    /**
     * 获取绝对路径
     * @param uploadDir
     * @param fileName
     * @return
     */
    private static final File getAbsoluteFile(String uploadDir, String fileName) throws IOException {
        File desc = new File(uploadDir + File.separator + fileName);
        if (!desc.getParentFile().exists()){
            desc.getParentFile().mkdirs();
        }
        if (!desc.exists()){
            desc.createNewFile();
        }
        return desc;
    }

    /**
     * 编码文件名
     * @param file
     * @return
     */
    private static final String extractFilename(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String extension = getExtension(file);
        fileName = DateUtils.datePath() + "/" + encodingFilename(fileName) + "." + extension;
        // 2020/06/19/b5aac38cbdf83edae9d686095590d196.png
        return fileName;
    }

    /**
     * 编码文件名
     */
    private static final String encodingFilename(String fileName){
        fileName = fileName.replace("_", " ");
        fileName = Md5Utils.hash(fileName + System.nanoTime() + counter++);
        return fileName;
    }

    /**
     * 文件大小及类型校验
     *
     * @param file 上传的文件
     * @return
     * @throws FileSizeLimitExceededException 如果超出最大大小
     * @throws InvalidExtensionException
     */
    private static final void assertAllowd(MultipartFile file, String[] allowedExtension)
            throws FileSizeLimitExceededException, InvalidExtensionException{
        long size = file.getSize();
        if (DEFAULT_MAX_SIZE != -1 && size > DEFAULT_MAX_SIZE){
            throw new FileSizeLimitExceededException(DEFAULT_MAX_SIZE / 1024 / 1024);
        }

        String fileName = file.getOriginalFilename();
        String extension = getExtension(file);
        if (allowedExtension != null && !isAllowedExtension(extension, allowedExtension)){
            if (allowedExtension == MimeTypeUtils.IMAGE_EXTENSION){
                throw new InvalidExtensionException.InvalidImageExtensionException(allowedExtension, extension,fileName);
            } else if (allowedExtension == MimeTypeUtils.FLASH_EXTENSION) {
                throw new InvalidExtensionException.InvalidFlashExtensionException(allowedExtension, extension,fileName);
            } else if (allowedExtension == MimeTypeUtils.MEDIA_EXTENSION) {
                throw new InvalidExtensionException.InvalidMediaExtensionException(allowedExtension, extension,fileName);
            } else {
                throw new InvalidExtensionException(allowedExtension, extension, fileName);
            }
        }

    }

    /**
     * 判断MIME类型是否是允许的MIME类型
     *
     * @param extension
     * @param allowedExtension
     * @return
     */
    private static final boolean isAllowedExtension(String extension, String[] allowedExtension) {
        for (String str : allowedExtension){
            if (str.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取文件名的后缀
     *
     * @param file 表单文件
     * @return 后缀名
     */
    private static final String getExtension(MultipartFile file){
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (JeeStringUtils.isEmpty(extension)){
            extension = MimeTypeUtils.getExtension(file.getContentType());
        }
        return extension;
    }
}

