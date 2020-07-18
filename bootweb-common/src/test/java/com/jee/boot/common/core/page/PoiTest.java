package com.jee.boot.common.core.page;

import com.jee.boot.common.poi.ExcelUtil;
import com.jee.boot.common.poi.annotation.Excel;
import com.jee.boot.common.poi.annotation.Excel.ColumnType;
import com.jee.boot.common.poi.annotation.Excel.Type;
import com.jee.boot.common.poi.demo.SysDeptDemo;
import com.jee.boot.common.poi.demo.UserModelDemo;
import com.jee.boot.common.utils.DateUtils;
import org.apache.ibatis.type.LocalDateTimeTypeHandler;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author jeeLearner
 * @version V1.0
 */
public class PoiTest {

    @Test
    public void test1() {
        String downloadPath = "D:/bw/download/";

        List<UserModelDemo> list = new LinkedList<>();
        UserModelDemo demo = new UserModelDemo();
        demo.setUserId(1L);
        demo.setLoginName("admin");
        demo.setUserName("jee");
        demo.setEmail("11@qq.com");
        demo.setPhonenumber("13888888888");
        demo.setSex("0");
        demo.setStatus("0");
        demo.setLoginIp("127.0.0.1");
        demo.setLoginDate(LocalDateTime.now());
        SysDeptDemo dept = new SysDeptDemo();
        dept.setDeptName("总部");
        dept.setLeader("jeeLearner");
        demo.setDept(dept);
        list.add(demo);
        System.out.println("startTime: " + LocalDateTime.now());
        ExcelUtil<UserModelDemo> util = new ExcelUtil<UserModelDemo>(UserModelDemo.class);
        util.exportExcel(list, "用户信息", downloadPath);
        System.out.println("endTime: " + LocalDateTime.now());
    }

    @Test
    public void test2() throws Exception {
        String downloadPath = "H:/我的下载/用户数据.xlsx";

        System.out.println("startTime: " + LocalDateTime.now());
        ExcelUtil<UserModelDemo> util = new ExcelUtil<UserModelDemo>(UserModelDemo.class);
        InputStream is = new FileInputStream(new File(downloadPath));
        List<UserModelDemo> list = util.importExcel(is);
        for (UserModelDemo demo : list) {
            System.out.println(demo);
        }

        System.out.println("endTime: " + LocalDateTime.now());
    }

    @Test
    public void test3() throws Exception {
        String downloadPath = "C:\\Users\\lyd\\Desktop\\周报\\天津应用总.xlsx";
        ExcelUtil<AppDemo> util = new ExcelUtil<AppDemo>(AppDemo.class);
        InputStream is = new FileInputStream(new File(downloadPath));
        List<AppDemo> list = util.importExcel(is);
        for (int i = list.size()-1; i >= 0; i--) {
            AppDemo app = list.get(i);
            if (app.getIp().contains(",")){
                String[] split = app.getIp().split(",");
                for (String s : split) {
                    list.add(new AppDemo(app.getAppName(), s));
                }
                list.remove(i);
            }
        }
        System.out.println(list);
        //ExcelUtil<UserModelDemo> util = new ExcelUtil<UserModelDemo>(UserModelDemo.class);
        //util.exportExcel(list, "应用信息", "C:\\Users\\lyd\\Desktop\\周报\\");
        //System.out.println("endTime: " + LocalDateTime.now());
        importToDb2(list);

    }

    private static void importToDb2(List<AppDemo> dbList){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            //String url = "jdbc:mysql://10.178.46.205:3306/fos?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8&useSSL=false";
            String url = "jdbc:mysql://localhost:3306/bootweb?serverTimezone=UTC&allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8&useSSL=false";
            //Connection conn = DriverManager.getConnection(url, "fos", "Fos@2018");
            Connection conn = DriverManager.getConnection(url, "root", "root");
            for (AppDemo app : dbList) {
                String sql = "insert into thirdpart_cmc_app(delegated,ip) values (?,?)";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setString(1, app.getAppName());
                statement.setString(2, app.getIp());
                //emp_name,ip,房间号，部门，网段
                int rep = statement.executeUpdate();
                if (rep > 0){
                    System.out.println("录入成功");
                }
                statement.close();
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
