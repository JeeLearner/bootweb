package com.jee.boot.alone.license.client.support;

import com.jee.boot.alone.license.client.model.LicenseClientHardwareCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 用于获取客户服务器的基本信息，如：IP、Mac地址、CPU序列号、主板序列号等
 *
 * @author jeeLearner
 * @version V1.0
 */
public abstract class AbstractServerInfos {
    private static final Logger logger = LoggerFactory.getLogger(AbstractServerInfos.class);

    /**
     * 组装需要额外校验的License参数
     * @return
     */
    public LicenseClientHardwareCheck getServerInfos(){
        LicenseClientHardwareCheck result = new LicenseClientHardwareCheck();

        try {
            result.setIpAddress(this.getIpAddress());
            result.setMacAddress(this.getMacAddress());
            result.setCpuSerial(this.getCPUSerial());
            result.setMainBoardSerial(this.getMainBoardSerial());
        }catch (Exception e){
            logger.error("获取服务器硬件信息失败",e);
        }

        return result;
    }

    /**
     * 获取IP地址
     * @return java.util.List
     */
    protected abstract List<String> getIpAddress() throws Exception;

    /**
     * 获取Mac地址
     * @return java.util.List
     */
    protected abstract List<String> getMacAddress() throws Exception;

    /**
     * 获取CPU序列号
     * @return java.lang.String
     */
    protected abstract String getCPUSerial() throws Exception;

    /**
     * 获取主板序列号
     * @return java.lang.String
     */
    protected abstract String getMainBoardSerial() throws Exception;

    /**
     * 获取当前服务器所有符合条件的InetAddress
     *      LoopbackAddress：本机的IP地址==>127开头的ip
     *      SiteLocalAddress：地区本地地址==>10.0.0.0 ~ 10.255.255.255、172.16.0.0 ~ 172.31.255.255、192.168.0.0 ~ 192.168.255.255
     *      LinkLocalAddress：本地连接地址==>169.254.0.0 ~ 169.254.255.255
     *      MulticastAddress：广播地址==>224.0.0.0 ~ 239.255.255.255
     *
     *      格式：[/192.168.33.2, /172.17.129.37]
     * @return java.util.List
     */
    protected List<InetAddress> getLocalAllInetAddress() throws Exception {
        List<InetAddress> result = new ArrayList();

        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        // 遍历所有的网络接口
        while (interfaces.hasMoreElements()){
            NetworkInterface iface = (NetworkInterface) interfaces.nextElement();
            // 在所有的接口下再遍历IP
            for (Enumeration inetAddresses = iface.getInetAddresses(); inetAddresses.hasMoreElements(); ) {
                InetAddress inetAddr = (InetAddress) inetAddresses.nextElement();
                //排除LoopbackAddress、SiteLocalAddress、LinkLocalAddress、MulticastAddress类型的IP地址
                if (!inetAddr.isLoopbackAddress() /*&& !inetAddr.isSiteLocalAddress()*/
                        && !inetAddr.isLinkLocalAddress() && !inetAddr.isMulticastAddress()) {
                    result.add(inetAddr);
                }
            }
        }
        return result;
    }

    /**
     * 获取某个网络接口的Mac地址
     *      格式：00-50-56-C0-00-08
     * @param inetAddr 网络接口
     * @return String  MAC
     */
    protected String getMacByInetAddress(InetAddress inetAddr) {
        try {
            byte[] mac = NetworkInterface.getByInetAddress(inetAddr).getHardwareAddress();
            StringBuffer stringBuffer = new StringBuffer();

            for (int i = 0; i < mac.length; i++) {
                if (i != 0) {
                    stringBuffer.append("-");
                }

                //将十六进制byte转化为字符串
                String temp = Integer.toHexString(mac[i] & 0xff);
                if (temp.length() == 1) {
                    stringBuffer.append("0" + temp);
                } else {
                    stringBuffer.append(temp);
                }
            }
            return stringBuffer.toString().toUpperCase();
        } catch (SocketException e) {
            logger.error("获取MAC地址错误！");
            e.printStackTrace();
        }
        return null;
    }
}

