/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.blazartech.blazararchive;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
        
        List<String> groupPieces = new ArrayList<>();
        for (int i = 0; i < n - 3; i++) {
            groupPieces.add(pieces[i]);
        }
        String group = groupPieces.stream()
                .filter(s -> !s.isBlank())
                .collect(Collectors.joining("."));
        descriptor.setGroup(group);
        
        File savedFile = new File(descriptor.getFilePath());
        descriptor.setFileDirectory(savedFile.toPath().getParent().toString());        
        descriptor.setArchiveRoot(savedFile.toPath().getParent().getParent().toString());

        log.info("descriptor = " + descriptor);
        
        return descriptor;
    }
    
}
