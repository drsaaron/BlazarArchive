/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.blazartech.blazararchive;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author aar1069
 */
@Configuration
@Slf4j
public class ServletRegistrationConfiguration {
    
    @Value("${servlet.mapping}")
    private String servletMapping;
    
    @Autowired
    private ArchiveServlet archiveServlet;
    
    @Bean
    public ServletRegistrationBean exampleServletBean() {
        ServletRegistrationBean bean = new ServletRegistrationBean(archiveServlet, servletMapping + "/*");
        bean.setLoadOnStartup(1);
        log.info("servlet registered");
        return bean;
    }
}
