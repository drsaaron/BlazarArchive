/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.blazartech.blazararchive;

import java.io.File;

/**
 *
 * @author aar1069
 */
public interface MetadataFileReader {
    
    public MavenMetadata readMetadataFile(String directory, String file);
    public File getMetadataFile(String directory, String file);
}
