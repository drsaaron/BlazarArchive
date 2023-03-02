/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.blazartech.blazararchive;

import java.io.File;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;

/**
 *
 * @author aar1069
 */
@Component
@Slf4j
public class MetadataFileReaderImpl implements MetadataFileReader {

    @Autowired
    private Jaxb2Marshaller marshaller;

    @Override
    public MavenMetadata readMetadataFile(String directory, String file) {
        MavenMetadata metadata = new MavenMetadata();
        File metadataFile = getMetadataFile(directory, file);
        if (metadataFile.exists()) {
            Source metadataFileSource = new StreamSource(metadataFile);
            metadata = (MavenMetadata) marshaller.unmarshal(metadataFileSource);
        }
        log.info("metadata = " + metadata);
        return metadata;
    }

    @Override
    public File getMetadataFile(String directory, String file) {
        File metadataFile = new File(directory, file);
        return metadataFile;
    }

}
