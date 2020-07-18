package com.jee.boot.alone.license.client.support;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * 获取客户Windows服务器的基本信息
 *
 * @author jeeLearner
 * @version V1.0
 */
public class WindowsServerInfos extends AbstractServerInfos {
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
     *      格式：BFEBFBFF000306A9
     * @return
     * @throws Exception
     */
    @Override
    protected String getCPUSerial() throws Exception {
        //使用WMIC获取CPU序列号
        String command = "wmic cpu get processorid";
        return getInfoByCmd(command);
    }

    /**
     * 获取主板信息
     *      格式：WB13252230
     *
     * @return
     * @throws Exception
     */
    @Override
    protected String getMainBoardSerial() throws Exception {
        //使用WMIC获取CPU序列号
        //String command = "wmic baseboard get serialnumber";
        String command = "wmic bios get serialnumber";
        return getInfoByCmd(command);
    }

    /**
     * 根据shell命令获取单条信息
     *
     * @param cmd
     * @return
     * @throws IOException
     */
    private String getInfoByCmd(String cmd) throws IOException {
        String info = "";

        Process process = Runtime.getRuntime().exec(cmd);
        process.getOutputStream().close();

        Scanner scanner = new Scanner(process.getInputStream());

        while (scanner.hasNext()){
            info = scanner.next().trim();
        }

        scanner.close();
        return info;
    }
}

