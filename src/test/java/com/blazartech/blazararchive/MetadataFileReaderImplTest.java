/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.blazartech.blazararchive;

import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 *
 * @author aar1069
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
    JaxbMarshallerConfiguration.class,
    MetadataFileReaderImplTest.MetadataFileReaderImplTestConfiguration.class
})
@Slf4j
public class MetadataFileReaderImplTest {
    
    @Configuration
    public static class MetadataFileReaderImplTestConfiguration {
        
        @Bean
        public MetadataFileReaderImpl instance() {
            return new MetadataFileReaderImpl();
        }
    }
    
    @Autowired
    private MetadataFileReaderImpl instance;
    
    public MetadataFileReaderImplTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of readMetadataFile method, of class MetadataFileReaderImpl.
     */
    @Test
    public void testReadMetadataFile() {
        log.info("readMetadataFile");
        
        String directory = "src/test/resources";
        String file = "test-metadata.xml";

        MavenMetadata result = instance.readMetadataFile(directory, file);
        
        assertEquals("com.blazartech", result.getGroupId());
        assertEquals("2.1.67-RELEASE", result.getMetadataVersioning().getLatest());
        
        List<String> versions = result.getMetadataVersioning().getVersions();
        assertNotNull(versions);
      //  assertFalse(versions.isEmpty());
    }
    
}
