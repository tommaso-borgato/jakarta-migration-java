package org.jboss.eapqe.jakarta.migration.process;

import org.jboss.eapqe.jakarta.migration.JavaxToJakarta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

public class XmlFile {

    static Map<String, String> javaEE8Schemas;
    static {
        javaEE8Schemas = new HashMap<>();
        javaEE8Schemas.put("http://xmlns.jcp.org/xml/ns/javaee/javaee_8.xsd","https://jakarta.ee/xml/ns/jakartaee/jakartaee_9.xsd");
        javaEE8Schemas.put("http://xmlns.jcp.org/xml/ns/javaee/javaee_web_services_metadata_handler_2_0.xsd","https://jakarta.ee/xml/ns/jakartaee/jakartaee_web_services_metadata_handler_3_0.xsd");
        javaEE8Schemas.put("http://xmlns.jcp.org/xml/ns/javaee/batchXML_1_0.xsd","https://jakarta.ee/xml/ns/jakartaee/batchXML_2_0.xsd");
        javaEE8Schemas.put("http://xmlns.jcp.org/xml/ns/javaee/beans_2_0.xsd","https://jakarta.ee/xml/ns/jakartaee/beans_3_0.xsd");
        javaEE8Schemas.put("http://xmlns.jcp.org/xml/ns/javaee/jobXML_1_0.xsd","https://jakarta.ee/xml/ns/jakartaee/jobXML_2_0.xsd");
        javaEE8Schemas.put("http://xmlns.jcp.org/xml/ns/javaee/jsp_2_3.xsd","https://jakarta.ee/xml/ns/jakartaee/jsp_3_0.xsd");
        javaEE8Schemas.put("http://xmlns.jcp.org/xml/ns/javaee/application_8.xsd","https://jakarta.ee/xml/ns/jakartaee/application_9.xsd");
        javaEE8Schemas.put("http://xmlns.jcp.org/xml/ns/javaee/application-client_8.xsd","https://jakarta.ee/xml/ns/jakartaee/application-client_9.xsd");
        javaEE8Schemas.put("http://xmlns.jcp.org/xml/ns/javaee/connector_1_7.xsd","https://jakarta.ee/xml/ns/jakartaee/connector_2_0.xsd");
        javaEE8Schemas.put("http://xmlns.jcp.org/xml/ns/javaee/ejb-jar_3_2.xsd","https://jakarta.ee/xml/ns/jakartaee/ejb-jar_4_0.xsd");
        javaEE8Schemas.put("http://xmlns.jcp.org/xml/ns/javaee/javaee_web_services_1_4.xsd","https://jakarta.ee/xml/ns/jakartaee/jakartaee_web_services_2_0.xsd");
        javaEE8Schemas.put("http://xmlns.jcp.org/xml/ns/javaee/javaee_web_services_client_1_4.xsd","https://jakarta.ee/xml/ns/jakartaee/jakartaee_web_services_client_2_0.xsd");
        javaEE8Schemas.put("http://xmlns.jcp.org/xml/ns/javaee/permissions_7.xsd","https://jakarta.ee/xml/ns/jakartaee/permissions_9.xsd");
        javaEE8Schemas.put("http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd","https://jakarta.ee/xml/ns/jakartaee/web-app_5_0.xsd");
        javaEE8Schemas.put("http://xmlns.jcp.org/xml/ns/javaee/web-fragment_4_0.xsd","https://jakarta.ee/xml/ns/jakartaee/web-fragment_5_0.xsd");
        javaEE8Schemas.put("http://xmlns.jcp.org/xml/ns/javaee/web-common_4_0.xsd","https://jakarta.ee/xml/ns/jakartaee/web-common_5_0.xsd");
        javaEE8Schemas.put("http://xmlns.jcp.org/xml/ns/javaee/web-facesconfig_2_3.xsd","https://jakarta.ee/xml/ns/jakartaee/web-facesconfig_3_0.xsd");
        javaEE8Schemas.put("http://xmlns.jcp.org/xml/ns/javaee/web-facelettaglibrary_2_3.xsd","https://jakarta.ee/xml/ns/jakartaee/web-facelettaglibrary_3_0.xsd");
        javaEE8Schemas.put("http://xmlns.jcp.org/xml/ns/javaee/web-partialresponse_2_3.xsd","https://jakarta.ee/xml/ns/jakartaee/web-partialresponse_3_0.xsd");
        javaEE8Schemas.put("http://xmlns.jcp.org/xml/ns/javaee/web-jsptaglibrary_2_1.xsd","https://jakarta.ee/xml/ns/jakartaee/web-jsptaglibrary_3_0.xsd");
        javaEE8Schemas.put("http://xmlns.jcp.org/xml/ns/javaee(?![/])","https://jakarta.ee/xml/ns/jakartaee");
        javaEE8Schemas.put("http://xmlns.jcp.org/xml/ns/javaee/(?![a-z0-9A-Z])","https://jakarta.ee/xml/ns/jakartaee");
    }

    static Map<String, String> javaEE7Schemas;
    static {
        javaEE7Schemas = new HashMap<>();
        javaEE7Schemas.put("http://xmlns.jcp.org/xml/ns/javaee/javaee_7.xsd","https://jakarta.ee/xml/ns/jakartaee/jakartaee_9.xsd");
        javaEE7Schemas.put("http://xmlns.jcp.org/xml/ns/javaee/javaee_web_services_metadata_handler_2_0.xsd","https://jakarta.ee/xml/ns/jakartaee/jakartaee_web_services_metadata_handler_3_0.xsd");
        javaEE7Schemas.put("http://xmlns.jcp.org/xml/ns/javaee/batchXML_1_0.xsd","https://jakarta.ee/xml/ns/jakartaee/batchXML_2_0.xsd");
        javaEE7Schemas.put("http://xmlns.jcp.org/xml/ns/javaee/beans_1_1.xsd","https://jakarta.ee/xml/ns/jakartaee/beans_3_0.xsd");
        javaEE7Schemas.put("http://xmlns.jcp.org/xml/ns/javaee/jobXML_1_0.xsd","https://jakarta.ee/xml/ns/jakartaee/jobXML_2_0.xsd");
        javaEE7Schemas.put("http://xmlns.jcp.org/xml/ns/javaee/jsp_2_3.xsd","https://jakarta.ee/xml/ns/jakartaee/jsp_3_0.xsd");
        javaEE7Schemas.put("http://xmlns.jcp.org/xml/ns/javaee/application_7.xsd","https://jakarta.ee/xml/ns/jakartaee/application_9.xsd");
        javaEE7Schemas.put("http://xmlns.jcp.org/xml/ns/javaee/application-client_7.xsd","https://jakarta.ee/xml/ns/jakartaee/application-client_9.xsd");
        javaEE7Schemas.put("http://xmlns.jcp.org/xml/ns/javaee/connector_1_7.xsd","https://jakarta.ee/xml/ns/jakartaee/connector_2_0.xsd");
        javaEE7Schemas.put("http://xmlns.jcp.org/xml/ns/javaee/ejb-jar_3_2.xsd","https://jakarta.ee/xml/ns/jakartaee/ejb-jar_4_0.xsd");
        javaEE7Schemas.put("http://xmlns.jcp.org/xml/ns/javaee/javaee_web_services_1_4.xsd","https://jakarta.ee/xml/ns/jakartaee/jakartaee_web_services_2_0.xsd");
        javaEE7Schemas.put("http://xmlns.jcp.org/xml/ns/javaee/javaee_web_services_client_1_4.xsd","https://jakarta.ee/xml/ns/jakartaee/jakartaee_web_services_client_2_0.xsd");
        javaEE7Schemas.put("http://xmlns.jcp.org/xml/ns/javaee/permissions_7.xsd","https://jakarta.ee/xml/ns/jakartaee/permissions_9.xsd");
        javaEE7Schemas.put("http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd","https://jakarta.ee/xml/ns/jakartaee/web-app_5_0.xsd");
        javaEE7Schemas.put("http://xmlns.jcp.org/xml/ns/javaee/web-fragment_3_1.xsd","https://jakarta.ee/xml/ns/jakartaee/web-fragment_5_0.xsd");
        javaEE7Schemas.put("http://xmlns.jcp.org/xml/ns/javaee/web-common_3_1.xsd","https://jakarta.ee/xml/ns/jakartaee/web-common_5_0.xsd");
        javaEE7Schemas.put("http://xmlns.jcp.org/xml/ns/javaee/web-facesconfig_2_2.xsd","https://jakarta.ee/xml/ns/jakartaee/web-facesconfig_3_0.xsd");
        javaEE7Schemas.put("http://xmlns.jcp.org/xml/ns/javaee/web-facelettaglibrary_2_2.xsd","https://jakarta.ee/xml/ns/jakartaee/web-facelettaglibrary_3_0.xsd");
        javaEE7Schemas.put("http://xmlns.jcp.org/xml/ns/javaee/web-partialresponse_2_2.xsd","https://jakarta.ee/xml/ns/jakartaee/web-partialresponse_3_0.xsd");
        javaEE7Schemas.put("http://xmlns.jcp.org/xml/ns/javaee/web-jsptaglibrary_2_1.xsd","https://jakarta.ee/xml/ns/jakartaee/web-jsptaglibrary_3_0.xsd");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlFile.class);
    private Path path;

    public XmlFile(Path path) {
        this.path = path;
    }

    public void process() throws IOException {
        if (!(JavaxToJakarta.processXmlSchemaNamespaces || JavaxToJakarta.processPersistence)) {
            return;
        }

        String content = Files.readString(path);

        if (content.contains("http://xmlns.jcp.org/xml/ns/javaee/web-facesuicomponent_2_0.xsd")) {
            throw new IOException("Namespace http://xmlns.jcp.org/xml/ns/javaee/web-facesuicomponent_2_0.xsd not handled!");
        }

        String finaltext = content;

        for (Map.Entry<String, String> entry : javaEE8Schemas.entrySet()) {
            finaltext = finaltext.replaceAll(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, String> entry : javaEE7Schemas.entrySet()) {
            finaltext = finaltext.replaceAll(entry.getKey(), entry.getValue());
        }

        if (!content.equalsIgnoreCase(finaltext)) {
            LOGGER.info("XML file {} was updated", path.toFile().getAbsolutePath());
            Files.writeString(path, finaltext, StandardOpenOption.TRUNCATE_EXISTING);
        }
    }
}
