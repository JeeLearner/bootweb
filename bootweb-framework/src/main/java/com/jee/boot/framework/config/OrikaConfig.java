//package com.jee.boot.framework.config;
//
//import ma.glasnost.orika.MapperFacade;
//import ma.glasnost.orika.MapperFactory;
//import ma.glasnost.orika.impl.DefaultMapperFactory;
//import ma.glasnost.orika.metadata.ClassMapBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.LinkedList;
//import java.util.List;
//
//@Configuration
//public class OrikaConfig {
//
//    @Bean
//    MapperFactory mapperFactory() {
//        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
//        List<ClassMapBuilder> builders = new LinkedList<>();
//
//        for (ClassMapBuilder builder : builders) {
//            builder.byDefault().register();
//        }
//        return mapperFactory;
//    }
//
//    @Bean
//    MapperFacade mapperFacade() {
//        MapperFacade mapper = mapperFactory().getMapperFacade();
//        return mapper;
//    }
//
//
//}