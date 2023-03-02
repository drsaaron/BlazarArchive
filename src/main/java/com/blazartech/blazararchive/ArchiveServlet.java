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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.xml.transform.StringResult;

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
    private Jaxb2Marshaller marshaller;

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("doPut");

        Enumeration<String> reqEnum = req.getHeaderNames();
        List<String> headers = new ArrayList<>();
        while (reqEnum.hasMoreElements()) {
            String h = reqEnum.nextElement();
            headers.add(h);
        }
        String headerString = headers.stream().collect(Collectors.joining(","));

        log.info("req query string = " + req.getQueryString());
        log.info("req path = " + req.getRequestURI());
        log.info("req header anmes = " + headerString);

        int bodySize = req.getIntHeader("content-length");
        log.info("bodySize = " + bodySize);

        byte[] content = req.getInputStream().readAllBytes();

        ArchiveFileDescriptor archiveFileDescriptor = archiveFile.getArchiveFileDescriptor(req.getRequestURI());
        String filePath = archiveFileDescriptor.getFilePath();
        File savedFile = new File(filePath);
        String directoryPath = archiveFileDescriptor.getFileDirectory();
        log.info("directory = {}", directoryPath);
        File directory = new File(directoryPath);
        if (directory.exists()) {
            log.info("directory {} exists", directoryPath);
        } else {
            log.info("creating new file " + savedFile.getAbsolutePath());
            directory.mkdirs();
        }
        try (FileOutputStream os = new FileOutputStream(savedFile)) {
            os.write(content);

            if (archiveFileDescriptor.getArtifact().endsWith((".jar"))) {
                // update meta data
                MavenMetadata metadata = new MavenMetadata();
                File metadataFile = new File(archiveFileDescriptor.getArchiveRoot(), "maven-metadata.xml");
                if (metadataFile.exists()) {
                    Source metadataFileSource = new StreamSource(metadataFile);
                    metadata = (MavenMetadata) marshaller.unmarshal(metadataFileSource);
                }
                log.info("metadata = " + metadata);

                metadata.setArtifactId(archiveFileDescriptor.getArtifact().replace(".jar", "").replace(archiveFileDescriptor.getVersion(), ""));
                metadata.setGroupId(archiveFileDescriptor.getGroup());
                metadata.getMetadataVersioning().getVersions().add(archiveFileDescriptor.getVersion());
                metadata.getMetadataVersioning().setLatest(archiveFileDescriptor.getVersion());

                StringResult stringResult = new StringResult();
                marshaller.marshal(metadata, stringResult);

                try (FileOutputStream metaOs = new FileOutputStream(metadataFile.getAbsolutePath())) {
                    metaOs.write(stringResult.toString().getBytes());
                }

                // do MD5 and SHA1
                saveDigest("MD5", archiveFileDescriptor.getArchiveRoot(), "maven-metadata.xml.md5", stringResult.toString());
                saveDigest("SHA-1", archiveFileDescriptor.getArchiveRoot(), "maven-metadata.xml.sha1", stringResult.toString());
            }
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("doPost");
        super.doPost(req, resp); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("doGet");

        Enumeration<String> reqEnum = req.getHeaderNames();
        List<String> headers = new ArrayList<>();
        while (reqEnum.hasMoreElements()) {
            String h = reqEnum.nextElement();
            headers.add(h);
        }
        String headerString = headers.stream().collect(Collectors.joining(","));

        log.info("req query string = " + req.getQueryString());
        log.info("req path = " + req.getRequestURI());
        log.info("req header anmes = " + headerString);

        String localFile = req.getRequestURI().replaceFirst(servletMapping, dataRoot);
        log.info("returning {}", localFile);
        try (FileInputStream is = new FileInputStream(localFile)) {
            byte[] content = is.readAllBytes();
            resp.getOutputStream().write(content);
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("doOptions");
        super.doOptions(req, resp); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("doDelete");
        super.doDelete(req, resp); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("doHead");
        super.doHead(req, resp); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("servlet initialized");
    }
}
