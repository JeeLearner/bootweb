//package com.jee.boot.alone.license.client.listener;
//
//import com.jee.boot.alone.license.client.service.LicenseClientService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.stereotype.Component;
//
///**
// * License监听器
// *      项目启动时安装license
// *
// * @author jeeLearner
// * @version V1.0
// */
//@Component
//public class LicenseCheckListener implements ApplicationListener<ContextRefreshedEvent> {
//
//    @Autowired
//    LicenseClientService clientService;
//
//    @Override
//    public void onApplicationEvent(ContextRefreshedEvent event) {
//        //root application context 没有parent
//        ApplicationContext context = event.getApplicationContext().getParent();
//        if (context == null){
//            clientService.install();
//        }
//    }
//}
//
