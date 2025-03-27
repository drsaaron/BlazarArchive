/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.blazartech.blazararchive;

import jakarta.servlet.http.HttpServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class ServletRegistrationConfiguration {
    
    private static final Logger log = LoggerFactory.getLogger(ServletRegistrationConfiguration.class);
    
    @Value("${servlet.mapping}")
    private String servletMapping;
    
    @Autowired
    private HttpServlet archiveServlet;
    
    @Bean
    public ServletRegistrationBean exampleServletBean() {
        ServletRegistrationBean<HttpServlet> bean = new ServletRegistrationBean<>(archiveServlet, servletMapping + "/*");
        bean.setLoadOnStartup(1);
        log.info("servlet registered");
        return bean;
    }
}
