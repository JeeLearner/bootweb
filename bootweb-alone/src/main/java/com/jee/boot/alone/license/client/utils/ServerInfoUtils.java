package com.jee.boot.alone.license.client.utils;

import com.jee.boot.alone.license.client.model.LicenseClientHardwareCheck;
import com.jee.boot.alone.license.client.support.AbstractServerInfos;
import com.jee.boot.alone.license.client.support.LinuxServerInfos;
import com.jee.boot.alone.license.client.support.WindowsServerInfos;
import org.apache.commons.lang3.StringUtils;

/**
 * @author jeeLearner
 * @version V1.0
 */
public class ServerInfoUtils {
    /**
     * 获取服务器硬件信息
     *
     * @param osName
     * @return
     */
    public static LicenseClientHardwareCheck getHardwareInfo(String osName){
        //操作系统类型
        if(StringUtils.isBlank(osName)){
            osName = System.getProperty("os.name");
        }
        osName = osName.toLowerCase();
        AbstractServerInfos infos = null;
        //根据不同操作系统类型选择不同的数据获取方法
        if (osName.startsWith("windows")) {
            infos = new WindowsServerInfos();
        } else if (osName.startsWith("linux")) {
            infos = new LinuxServerInfos();
        }else{//其他服务器类型
            infos = new LinuxServerInfos();
        }
        if (infos != null){
            return infos.getServerInfos();
        } else {
            return null;
        }
    }
}

