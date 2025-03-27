/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.blazartech.blazararchive;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author aar1069
 */
@Component
public class MavenMetadataXMLBuilderImpl implements MavenMetadataXMLBuilder {

    private static final Logger log = LoggerFactory.getLogger(MavenMetadataXMLBuilderImpl.class);
    
    @Override
    public String buildXML(MavenMetadata metadata) {

        log.info("building xml for {}", metadata);
        
        List<String> versions = metadata.getMetadataVersioning().getVersions();
        String versionListXML = versions.stream()
                .map(v -> "<version>" + v + "</version>")
                .collect(Collectors.joining("\n"));

        String metadataXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<metadata>\n"
                + "  <groupId>" + metadata.getGroupId() + "</groupId>\n"
                + "  <artifactId>" + metadata.getArtifactId() + "</artifactId>\n"
                + "  <versioning>\n"
                + "    <latest>" + metadata.getMetadataVersioning().getLatest() + "</latest>\n"
                + "    <release>" + metadata.getMetadataVersioning().getRelease() + "</release>\n"
                + "    <versions>" + versionListXML + "</versions>\n"
                + "    <lastUpdated>" + metadata.getMetadataVersioning().getLatest() + "</lastUpdated>\n"
                + "  </versioning>\n"
                + "</metadata>";

        return metadataXml;
    }

}
