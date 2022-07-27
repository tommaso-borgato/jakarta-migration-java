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

    static Map<String, String> javaEESchemas;
    static {
        javaEESchemas = new HashMap<>();
        javaEESchemas.put("http\\://xmlns.jcp.org/xml/ns/javaee/javaee_[0-9_]+.xsd","https://jakarta.ee/xml/ns/jakartaee/jakartaee_9.xsd");
        javaEESchemas.put("http\\://xmlns.jcp.org/xml/ns/javaee/javaee_web_services_metadata_handler_[0-9_]+.xsd","https://jakarta.ee/xml/ns/jakartaee/jakartaee_web_services_metadata_handler_3_0.xsd");
        javaEESchemas.put("http\\://xmlns.jcp.org/xml/ns/javaee/batchXML_[0-9_]+.xsd","https://jakarta.ee/xml/ns/jakartaee/batchXML_2_0.xsd");
        javaEESchemas.put("http\\://xmlns.jcp.org/xml/ns/javaee/beans_[0-9_]+.xsd","https://jakarta.ee/xml/ns/jakartaee/beans_3_0.xsd");
        javaEESchemas.put("http\\://xmlns.jcp.org/xml/ns/javaee/jobXML_[0-9_]+.xsd","https://jakarta.ee/xml/ns/jakartaee/jobXML_2_0.xsd");
        javaEESchemas.put("http\\://xmlns.jcp.org/xml/ns/javaee/jsp_[0-9_]+.xsd","https://jakarta.ee/xml/ns/jakartaee/jsp_3_0.xsd");
        javaEESchemas.put("http\\://xmlns.jcp.org/xml/ns/javaee/application_[0-9_]+.xsd","https://jakarta.ee/xml/ns/jakartaee/application_9.xsd");
        javaEESchemas.put("http\\://xmlns.jcp.org/xml/ns/javaee/application-client_[0-9_]+.xsd","https://jakarta.ee/xml/ns/jakartaee/application-client_9.xsd");
        javaEESchemas.put("http\\://xmlns.jcp.org/xml/ns/javaee/connector_[0-9_]+.xsd","https://jakarta.ee/xml/ns/jakartaee/connector_2_0.xsd");
        javaEESchemas.put("http\\://xmlns.jcp.org/xml/ns/javaee/ejb-jar_[0-9_]+.xsd","https://jakarta.ee/xml/ns/jakartaee/ejb-jar_4_0.xsd");
        javaEESchemas.put("http\\://xmlns.jcp.org/xml/ns/javaee/javaee_web_services_[0-9_]+.xsd","https://jakarta.ee/xml/ns/jakartaee/jakartaee_web_services_2_0.xsd");
        javaEESchemas.put("http\\://xmlns.jcp.org/xml/ns/javaee/javaee_web_services_client_[0-9_]+.xsd","https://jakarta.ee/xml/ns/jakartaee/jakartaee_web_services_client_2_0.xsd");
        javaEESchemas.put("http\\://xmlns.jcp.org/xml/ns/javaee/permissions_[0-9_]+.xsd","https://jakarta.ee/xml/ns/jakartaee/permissions_9.xsd");
        javaEESchemas.put("http\\://xmlns.jcp.org/xml/ns/javaee/web-app_[0-9_]+.xsd","https://jakarta.ee/xml/ns/jakartaee/web-app_5_0.xsd");
        javaEESchemas.put("http\\://xmlns.jcp.org/xml/ns/javaee/web-fragment_[0-9_]+.xsd","https://jakarta.ee/xml/ns/jakartaee/web-fragment_5_0.xsd");
        javaEESchemas.put("http\\://xmlns.jcp.org/xml/ns/javaee/web-common_[0-9_]+.xsd","https://jakarta.ee/xml/ns/jakartaee/web-common_5_0.xsd");
        javaEESchemas.put("http\\://xmlns.jcp.org/xml/ns/javaee/web-facesconfig_[0-9_]+.xsd","https://jakarta.ee/xml/ns/jakartaee/web-facesconfig_3_0.xsd");
        javaEESchemas.put("http\\://xmlns.jcp.org/xml/ns/javaee/web-facelettaglibrary_[0-9_]+.xsd","https://jakarta.ee/xml/ns/jakartaee/web-facelettaglibrary_3_0.xsd");
        javaEESchemas.put("http\\://xmlns.jcp.org/xml/ns/javaee/web-partialresponse_[0-9_]+.xsd","https://jakarta.ee/xml/ns/jakartaee/web-partialresponse_3_0.xsd");
        javaEESchemas.put("http\\://xmlns.jcp.org/xml/ns/javaee/web-jsptaglibrary_[0-9_]+.xsd","https://jakarta.ee/xml/ns/jakartaee/web-jsptaglibrary_3_0.xsd");
        javaEESchemas.put("http\\://xmlns.jcp.org/xml/ns/javaee(?![/])","https://jakarta.ee/xml/ns/jakartaee");
        javaEESchemas.put("http\\://xmlns.jcp.org/xml/ns/javaee/(?![a-z0-9A-Z])","https://jakarta.ee/xml/ns/jakartaee");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlFile.class);
    private Path path;

    public XmlFile(Path path) {
        this.path = path;
    }

    public void process() throws IOException {
        if (!JavaxToJakarta.processXmlSchemaNamespaces) {
            return;
        }

        String content = Files.readString(path);
        if (content.contains("http://xmlns.jcp.org/xml/ns/javaee/web-facesuicomponent_2_0.xsd")) {
            throw new IOException("Namespace http://xmlns.jcp.org/xml/ns/javaee/web-facesuicomponent_2_0.xsd not handled!");
        }

        String finalContent = new String(content.getBytes());

        // replace namespaces
        for (Map.Entry<String, String> entry : javaEESchemas.entrySet()) {
            finalContent = finalContent.replaceAll(entry.getKey(), entry.getValue());
        }

        // replace properties
        finalContent = replaceProperties(finalContent);

        if (finalContent.contains("<persistence ") && finalContent.contains("</persistence>")) {
            finalContent = replaceVersion(finalContent,"3.0");
        }

        if (finalContent.contains("<permissions ") && finalContent.contains("</permissions>")) {
            System.out.println("\n\n\n permissions \n\n");
            finalContent = replaceVersion(finalContent,"9");
        }

        if (!content.equalsIgnoreCase(finalContent)) {
            LOGGER.info("XML file {} was updated", path.toFile().getAbsolutePath());
            Files.writeString(path, finalContent, StandardOpenOption.TRUNCATE_EXISTING);
        }
    }

    private String replaceProperties(String str) {
        return str.replaceAll("(<property.*) name=\"javax\\.","$1 name=\"jakarta.");
    }

    private String replaceVersion(String str, String version) {
        return str.replaceFirst("(version=\")[^\"]+(\")", "$1" + version + "$2");
    }

}
