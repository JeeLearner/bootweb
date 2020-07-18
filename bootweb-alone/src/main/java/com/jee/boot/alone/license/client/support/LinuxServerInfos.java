package com.jee.boot.alone.license.client.support;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 获取客户Linux服务器的基本信息
 *
 * @author jeeLearner
 * @version V1.0
 */
public class LinuxServerInfos extends AbstractServerInfos {

    /**
     * 获取ip地址
     *
     * @return
     * @throws Exception
     */
    @Override
    protected List<String> getIpAddress() throws Exception {
        List<String> result = new ArrayList<>();
        List<InetAddress> inetAddress = getLocalAllInetAddress();
        if (inetAddress != null && inetAddress.size() > 0){
            result = inetAddress.stream()
                    .map(InetAddress::getHostAddress)
                    .distinct()
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());
        }
        return result;
    }

    /**
     * 获取MAC地址
     *
     * @return
     * @throws Exception
     */
    @Override
    protected List<String> getMacAddress() throws Exception {
        List<String> result = new ArrayList<>();
        List<InetAddress> inetAddress = getLocalAllInetAddress();
        if (inetAddress != null && inetAddress.size() > 0){
            result = inetAddress.stream()
                    .map(this::getMacByInetAddress)
                    .distinct()
                    .collect(Collectors.toList());
        }
        return result;
    }

    /**
     * 获取CPU序列号
     *      格式：09234D56-D4CE-A65A-1188-7FF032BE5D31
     * @return
     * @throws Exception
     */
    @Override
    protected String getCPUSerial() throws Exception {
        //使用dmidecode命令获取CPU序列号
        String[] shell = {"/bin/bash","-c","dmidecode -t processor | grep 'ID' | awk -F ':' '{print $2}' | head -n 1"};
        return getInfoByShell(shell);
    }

    /**
     * 获取主板信息
     *      格式：VMware-56 4d 23 09 ce d4 5a a6-11 88 7f f0 32 be 5d 31
     *
     * @return
     * @throws Exception
     */
    @Override
    protected String getMainBoardSerial() throws Exception {
        //使用dmidecode命令获取主板序列号
        String[] shell = {"/bin/bash","-c","dmidecode | grep 'Serial Number' | awk -F ':' '{print $2}' | head -n 1"};
        return getInfoByShell(shell);
    }

    /**
     * 根据shell命令获取单条信息
     *
     * @param shell
     * @return
     * @throws IOException
     */
    private String getInfoByShell(String[] shell) throws IOException {
        String info = "";

        Process process = Runtime.getRuntime().exec(shell);
        process.getOutputStream().close();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = reader.readLine().trim();
        if(StringUtils.isNotBlank(line)){
            info = line;
        }
        reader.close();
        return info;
    }
}

