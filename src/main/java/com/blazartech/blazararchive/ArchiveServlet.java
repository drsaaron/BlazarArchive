/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.blazartech.blazararchive;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 *
 * @author aar1069
 */
@Component
@Slf4j
public class ArchiveServlet extends HttpServlet implements InitializingBean {

    @Value("${data.root}")
    private String dataRoot;

    @Value("${servlet.mapping}")
    private String servletMapping;

    @Autowired
    private ArchiveFile archiveFile;

    @Autowired
    private MavenMetadataXMLBuilder xmlBuilder;

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("doPut");

        byte[] content = req.getInputStream().readAllBytes();

        ArchiveFileDescriptor archiveFileDescriptor = archiveFile.getArchiveFileDescriptor(req.getRequestURI());
        String filePath = archiveFileDescriptor.getFilePath();
        File savedFile = new File(filePath);
        String directoryPath = archiveFileDescriptor.getFileDirectory();
        File directory = new File(directoryPath);
        
        // does the directory exist?  If yes, ensure we have no duplicates.  Else create
        // the directory for storage and initialize the metadata files.
        if (directory.exists()) {
            // ensure no duplicates
            if (savedFile.exists() && archiveFileDescriptor.getArtifact().endsWith(".jar")) {
                log.info("saved file {} already exists", filePath);
                resp.sendError(HttpStatus.BAD_REQUEST.value(), "object " + archiveFileDescriptor.getArtifact() + " already archived");
                return;
            }
        } else {
            log.info("creating new file " + savedFile.getAbsolutePath());
            directory.mkdirs();

            // initialize the meta data from scratch
            MavenMetadata metadata = new MavenMetadata();
            File metadataFile = new File(archiveFileDescriptor.getArchiveRoot(), "maven-metadata.xml");

            metadata.setArtifactId(archiveFileDescriptor.getArtifact().replace(".jar", "").replace(archiveFileDescriptor.getVersion(), ""));
            metadata.setGroupId(archiveFileDescriptor.getGroup());
            metadata.getMetadataVersioning().setLatest(archiveFileDescriptor.getVersion());

            File metadataFileDirectory = new File(archiveFileDescriptor.getArchiveRoot());
            List<File> directoryContents = Arrays.asList(metadataFileDirectory.listFiles());
            List<String> versions = directoryContents.stream()
                    .map(f -> f.getAbsoluteFile())
                    .filter(f -> f.isDirectory())
                    .map(d -> d.getAbsolutePath().replace(archiveFileDescriptor.getArchiveRoot() + "/", ""))
                    .collect(Collectors.toList());
            log.info("found version list " + versions);
            metadata.getMetadataVersioning().setVersions(versions);
            log.info("metadata = {}", metadata);

            // build the meta data file manually as jaxb doesn't seem to the array of versions
            String metadataXml = xmlBuilder.buildXML(metadata);

            try (FileOutputStream metaOs = new FileOutputStream(metadataFile)) {
                metaOs.write(metadataXml.getBytes());
            }

            // do MD5 and SHA1
            saveDigest("MD5", archiveFileDescriptor.getArchiveRoot(), "maven-metadata.xml.md5", metadataXml);
            saveDigest("SHA-1", archiveFileDescriptor.getArchiveRoot(), "maven-metadata.xml.sha1", metadataXml);

        }
        
        // save the file
        try (FileOutputStream os = new FileOutputStream(savedFile)) {
            os.write(content);
        }

        resp.getWriter().print("putted");
    }

    private void saveDigest(String digestType, String root, String fileName, String metadata) {
        try {
            MessageDigest digest = MessageDigest.getInstance(digestType);
            digest.update(metadata.getBytes());
            String digestHash = DatatypeConverter.printHexBinary(digest.digest()).toUpperCase();
            String digestString = digestHash + " " + fileName;
            try (FileOutputStream md5os = new FileOutputStream(new File(root, fileName))) {
                md5os.write(digestString.getBytes());
            }
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException("error saving digest: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("doGet");

        String localFile = req.getRequestURI().replaceFirst(servletMapping, dataRoot);
        
        // ensure the file exists
        File lf = new File(localFile);
        if (!lf.exists()) {
            resp.sendError(HttpStatus.NOT_FOUND.value(), "file not found");
            return;
        }
        
        log.info("returning {}", localFile);
        try (FileInputStream is = new FileInputStream(localFile)) {
            byte[] content = is.readAllBytes();
            resp.getOutputStream().write(content);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("servlet initialized");
    }
}
