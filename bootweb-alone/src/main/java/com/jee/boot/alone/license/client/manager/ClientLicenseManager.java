package com.jee.boot.alone.license.client.manager;

import com.alibaba.fastjson.JSON;
import com.jee.boot.alone.license.client.config.YmlConfig;
import com.jee.boot.alone.license.client.model.LicenseClientHardwareCheck;
import com.jee.boot.alone.license.client.utils.ServerInfoUtils;
import com.jee.boot.alone.license.client.utils.YmlUtil;
import com.jee.boot.common.exception.CustomException;
import de.schlichtherle.license.*;
import de.schlichtherle.xml.GenericCertificate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 自定义LicenseManager，用于自定义校验服务器信息
 *
 * @author jeeLearner
 * @version V1.0
 */
public class ClientLicenseManager extends LicenseManager {
    private static Logger logger = LoggerFactory.getLogger(ClientLicenseManager.class);

    //XML编码
    private static final String XML_CHARSET = "UTF-8";
    //默认BUFSIZE
    private static final int DEFAULT_BUFSIZE = 8 * 1024;

    private LicenseClientHardwareCheck clientHardwareCheck = ServerInfoUtils.getHardwareInfo(null);

    public ClientLicenseManager() {
    }

    public ClientLicenseManager(LicenseParam param) {
        super(param);
    }

    /**
     * 重写verify方法，调用本类中的validate方法，校验IP地址、Mac地址等其他信息
     * @param notary
     * @return
     * @throws Exception
     */
    @Override
    protected synchronized LicenseContent verify(final LicenseNotary notary) throws Exception {
        GenericCertificate certificate = getCertificate();

        // Load license key from preferences,
        final byte[] key = getLicenseKey();
        if (null == key){
            throw new NoLicenseInstalledException(getLicenseParam().getSubject());
        }
        certificate = getPrivacyGuard().key2cert(key);
        notary.verify(certificate);
        final LicenseContent content = (LicenseContent)load(certificate.getEncoded());
        validate(content);
        setCertificate(certificate);

        return content;
    }

    /**
     * 重写validate方法，增加IP地址、Mac地址等其他信息校验
     *
     * @param content
     * @throws LicenseContentException
     */
    @Override
    protected synchronized void validate(final LicenseContent content) throws LicenseContentException {
        String checkType = YmlUtil.getStrYmlVal("license.client.checkType");

        //1. 首先调用父类的validate方法
        super.validate(content);

        //2. 然后校验自定义的License参数
        //License中可被允许的参数信息
        String jsonStr = JSON.toJSONString(content.getExtra());
        LicenseClientHardwareCheck expectedCheckModel = JSON.parseObject(jsonStr, LicenseClientHardwareCheck.class);
        //当前服务器真实的参数信息
        LicenseClientHardwareCheck serverCheckModel = clientHardwareCheck;
        if(expectedCheckModel != null && serverCheckModel != null) { //证书携带可用信息，则检验
            //校验IP地址
            if (checkType != null && checkType.contains("ip")){
                if(!checkIpAndMac(expectedCheckModel.getIpAddress(),serverCheckModel.getIpAddress())){
                    throw new LicenseContentException("当前服务器的IP没在授权范围内");
                }
            }

            //校验Mac地址
            if (checkType != null && checkType.contains("mac")){
                if(!checkIpAndMac(expectedCheckModel.getMacAddress(),serverCheckModel.getMacAddress())){
                    throw new LicenseContentException("当前服务器的Mac地址没在授权范围内");
                }
            }

            //校验主板序列号
            if (checkType != null && checkType.contains("board")){
                if(!checkSerial(expectedCheckModel.getMainBoardSerial(),serverCheckModel.getMainBoardSerial())){
                    throw new LicenseContentException("当前服务器的主板序列号没在授权范围内");
                }
            }

            //校验CPU序列号
            if (checkType != null && checkType.contains("cpu")){
                if(!checkSerial(expectedCheckModel.getCpuSerial(),serverCheckModel.getCpuSerial())){
                    throw new LicenseContentException("当前服务器的CPU序列号没在授权范围内");
                }
            }
        } else {
            throw new LicenseContentException("未获取到服务器硬件信息...");
        }
    }

    /**
     * 重写XMLDecoder解析XML
     * @param encoded
     * @return
     */
    private Object load(String encoded) {
        BufferedInputStream inputStream = null;
        XMLDecoder decoder = null;
        try {
            inputStream = new BufferedInputStream(new ByteArrayInputStream(encoded.getBytes(XML_CHARSET)));
            decoder = new XMLDecoder(new BufferedInputStream(inputStream, DEFAULT_BUFSIZE), null, null);
            return decoder.readObject();
        } catch (UnsupportedEncodingException e) {
            logger.error("CustomLienseManager.load()执行错误...");
            e.printStackTrace();
        } finally {
            try {
                if(decoder != null){
                    decoder.close();
                }
                if(inputStream != null){
                    inputStream.close();
                }
            } catch (Exception e) {
                logger.error("XMLDecoder解析XML失败",e);
            }
        }
        return null;
    }

    /**
     * 校验当前服务器的IP/Mac地址是否在可被允许的IP范围内
     * @param expectedList
     * @param serverList
     * @return
     */
    private boolean checkIpAndMac(List<String> expectedList, List<String> serverList){
        if (expectedList != null && expectedList.size() > 0){
            if(serverList != null && serverList.size() > 0){
                for (String expected : expectedList) {
                    if (serverList.contains(expected.trim())){
                        return true;
                    }
                }
            }
            return false;
        } else {
            return true;
        }
    }

    /**
     * 校验当前服务器硬件（主板、CPU等）序列号是否在可允许范围内
     * @param expectedSerial
     * @param serverSerial
     * @return
     */
    private boolean checkSerial(String expectedSerial,String serverSerial){
        if(StringUtils.isNotBlank(expectedSerial)){
            if(StringUtils.isNotBlank(serverSerial)){
                if (expectedSerial.equalsIgnoreCase(serverSerial)){
                    return true;
                }
            }
            return false;
        } else {
            return true;
        }
    }
}

