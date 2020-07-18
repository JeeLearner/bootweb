package com.jee.boot.alone.license.server.manager;

import com.jee.boot.alone.license.server.model.LicenseHardwareCheck;
import de.schlichtherle.license.*;
import de.schlichtherle.xml.GenericCertificate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

/**
 * 自定义LicenseManager，用于自定义校验服务器信息
 *
 * @author jeeLearner
 * @version V1.0
 */
public class CustomLicenseManager extends LicenseManager {
    private static Logger logger = LoggerFactory.getLogger(CustomLicenseManager.class);

    //XML编码
    private static final String XML_CHARSET = "UTF-8";
    //默认BUFSIZE
    private static final int DEFAULT_BUFSIZE = 8 * 1024;

    private LicenseHardwareCheck licenseCheckModel;

    public CustomLicenseManager() {
    }

    public CustomLicenseManager(LicenseParam param, LicenseHardwareCheck licenseCheckModel) {
        super(param);
        this.licenseCheckModel = licenseCheckModel;
    }

    /**
     * 重写create
     * @param content
     * @param notary
     * @return
     * @throws Exception
     */
    @Override
    protected synchronized byte[] create(LicenseContent content, LicenseNotary notary) throws Exception {
        initialize(content);
        validateCreate(content);
        final GenericCertificate certificate = notary.sign(content);
        return getPrivacyGuard().cert2key(certificate);
    }

    /**
     * 重写install方法，其中validate方法调用本类中的validate方法，校验IP地址、Mac地址等其他信息
     * @param key
     * @param notary
     * @return
     * @throws Exception
     */
    @Override
    protected synchronized LicenseContent install(final byte[] key, final LicenseNotary notary) throws Exception {
        final GenericCertificate certificate = getPrivacyGuard().key2cert(key);
        notary.verify(certificate);
        final LicenseContent content = (LicenseContent) load(certificate.getEncoded());
        validate(content);
        setLicenseKey(key);
        setCertificate(certificate);
        return content;
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
        //1. 首先调用父类的validate方法
        super.validate(content);

        //2. 然后校验自定义的License参数
        //License中可被允许的参数信息
        LicenseHardwareCheck expectedCheckModel = (LicenseHardwareCheck) content.getExtra();
        //当前服务器真实的参数信息
        LicenseHardwareCheck serverCheckModel = licenseCheckModel;
        if(expectedCheckModel != null && serverCheckModel != null) {
            //校验IP地址
            if(!checkIpAndMac(expectedCheckModel.getIpAddress(),serverCheckModel.getIpAddress())){
                throw new LicenseContentException("当前服务器的IP没在授权范围内");
            }

            //校验Mac地址
            if(!checkIpAndMac(expectedCheckModel.getMacAddress(),serverCheckModel.getMacAddress())){
                throw new LicenseContentException("当前服务器的Mac地址没在授权范围内");
            }

            //校验主板序列号
            if(!checkSerial(expectedCheckModel.getMainBoardSerial(),serverCheckModel.getMainBoardSerial())){
                throw new LicenseContentException("当前服务器的主板序列号没在授权范围内");
            }

            //校验CPU序列号
            if(!checkSerial(expectedCheckModel.getCpuSerial(),serverCheckModel.getCpuSerial())){
                throw new LicenseContentException("当前服务器的CPU序列号没在授权范围内");
            }
        } else {
            throw new LicenseContentException("未获取到服务器硬件信息...");
        }
    }


    /**
     * 校验生成证书的参数信息
     *
     * @param content 证书正文
     */
    private void validateCreate(LicenseContent content) throws LicenseContentException {
        final LicenseParam param = getLicenseParam();

        final Date now = new Date();
        final Date notBefore = content.getNotBefore();
        final Date notAfter = content.getNotAfter();
        if (null != notAfter && now.after(notAfter)){
            throw new LicenseContentException("证书失效时间不能早于当前时间");
        }
        if (null != notBefore && null != notAfter && notAfter.before(notBefore)){
            throw new LicenseContentException("证书生效时间不能晚于证书失效时间");
        }
        final String consumerType = content.getConsumerType();
        if (null == consumerType){
            throw new LicenseContentException("用户类型不能为空");
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

