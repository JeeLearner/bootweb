package com.jee.boot.framework.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.jee.boot.common.utils.IpUtils;
import com.jee.boot.common.utils.text.JeeStringUtils;
import com.jee.boot.common.utils.spring.SpringUtils;
import com.jee.boot.framework.config.MyConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

/**
 * 获取地址类
 *
 * @author jeeLearner
 */
public class AddressUtils {

    private static final Logger log = LoggerFactory.getLogger(AddressUtils.class);

    // IP地址查询
    //public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp";
    public static final String IP_URL = "http://ip.taobao.com/service/getIpInfo.php";

    // 未知地址
    public static final String UNKNOWN = "XX XX";

    public static String getRealAddressByIP(String ip) {
        String address = UNKNOWN;
        if (JeeStringUtils.isEmpty(ip)){
            return UNKNOWN;
        }

        // 内网不查询
        if (IpUtils.internalIp(ip)){
            return "内网IP";
        }
        if (MyConfig.isAddressEnabled()){
            //String rspStr = HttpUtils.sendPost(IP_URL, "ip=" + ip);
            String info = getAddressInfo(IP_URL, ip);
            if (JeeStringUtils.isEmpty(info)) {
                log.error("获取地理位置异常 {}", ip);
                return address;
            }
            try {
                JSONObject obj = JSON.parseObject(info);
                JSONObject data = obj.getJSONObject("data");
                String region = data.getString("region");
                String city = data.getString("city");
                address = region + " " + city;
            } catch (Exception e){
                log.error("获取地理位置异常 {}", ip);
            }

        }
        return address;
    }

    private static String getAddressInfo(String url, String ip){
        RestTemplate restTemplate = SpringUtils.getBean(RestTemplate.class);

        //请求头设置
        url = url + "?ip=" + ip;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);
        headers.setAccept(Lists.newArrayList(MediaType.TEXT_HTML));
        headers.set("Accept-Charset", "utf-8");
        HttpEntity<String> httpEntity = new HttpEntity<>(ip, headers);
        String adressInfo = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class).getBody();
        return adressInfo;
    }


}

