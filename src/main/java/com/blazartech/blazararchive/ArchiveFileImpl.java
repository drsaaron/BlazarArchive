/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.blazartech.blazararchive;

import java.io.File;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author aar1069
 */
@Component
@Slf4j
public class ArchiveFileImpl implements ArchiveFile {

    @Value("${servlet.mapping}")
    private String servletMapping;
    
    @Value("${data.root}")
    private String dataRoot;
    
    @Override
    public ArchiveFileDescriptor getArchiveFileDescriptor(String requestURI) {
        String resourcePath = requestURI.replace(servletMapping, "");
        String[] pieces = resourcePath.split("/");
        int n = pieces.length;
        
        ArchiveFileDescriptor descriptor = new ArchiveFileDescriptor();
        descriptor.setArtifact(pieces[n-1]);
        descriptor.setName(pieces[n-3]);
        descriptor.setVersion(pieces[n-2]);
        descriptor.setFilePath(dataRoot + "/" + resourcePath);
        
        File savedFile = new File(descriptor.getFilePath());
        descriptor.setFileDirectory(savedFile.toPath().getParent().toString());        
        descriptor.setArchiveRoot(savedFile.toPath().getParent().getParent().toString());

        log.info("descriptor = " + descriptor);
        
        return descriptor;
    }
    
}
