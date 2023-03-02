/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.blazartech.blazararchive;

import lombok.Data;

/**
 *
 * @author aar1069
 */
@Data
public class ArchiveFileDescriptor {
    
    private String version;
    private String artifact;
    private String name;
    private String filePath;
    private String fileDirectory;
    private String archiveRoot;
    private String group;
}
