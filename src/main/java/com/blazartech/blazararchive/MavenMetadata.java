/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.blazartech.blazararchive;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

/**
 *
 * @author aar1069
 */
@XmlRootElement(name = "metadata")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class MavenMetadata {
    
    @XmlElement(name = "groupId")
    private String groupId = "";
    
    @XmlElement(name = "artifactId")
    private String artifactId = "";
    
    @XmlElement(name = "versioning")
    private MavenMetadataVersioning metadataVersioning = new MavenMetadataVersioning();
}
