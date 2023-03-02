/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.blazartech.blazararchive;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 *
 * @author aar1069
 */
@XmlRootElement(name = "versioning")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class MavenMetadataVersioning {
    
    @XmlElement(name = "latest")
    private String latest = "";
    
    @XmlElement(name = "release")
    private String release = "";
    
    @XmlElementWrapper(name = "versions")
    private List<String> versions = new ArrayList<>();
}
