/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.blazartech.blazararchive;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

/**
 *
 * @author aar1069
 */
@Configuration
public class JaxbMarshallerConfiguration {
    
    @Bean
    public Jaxb2Marshaller getJaxb2Marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(MavenMetadata.class);
        marshaller.setSupportDtd(true);
        return marshaller;
    }
}
