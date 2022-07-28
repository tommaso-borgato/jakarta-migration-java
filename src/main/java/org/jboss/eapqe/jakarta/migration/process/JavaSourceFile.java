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

public class JavaSourceFile {

    private static final Logger LOGGER = LoggerFactory.getLogger(JavaSourceFile.class);
    private Path path;

    /**
     * Replace import statements and fully qualified class names
     */
    static Map<String, String> javaxPackages;
    static {
        javaxPackages = new HashMap<>();
        javaxPackages.put("javax.activation.", "jakarta.activation.");
        javaxPackages.put("javax.annotation.security.", "jakarta.annotation.security.");
        javaxPackages.put("javax.annotation.", "jakarta.annotation.");
        javaxPackages.put("javax.annotation.sql.", "jakarta.annotation.sql.");
        javaxPackages.put("javax.batch.api.chunk.", "jakarta.batch.api.chunk.");
        javaxPackages.put("javax.batch.api.chunk.listener.", "jakarta.batch.api.chunk.listener.");
        javaxPackages.put("javax.batch.api.listener.", "jakarta.batch.api.listener.");
        javaxPackages.put("javax.batch.api.partition.", "jakarta.batch.api.partition.");
        javaxPackages.put("javax.batch.api.", "jakarta.batch.api.");
        javaxPackages.put("javax.batch.operations.", "jakarta.batch.operations.");
        javaxPackages.put("javax.batch.runtime.", "jakarta.batch.runtime.");
        javaxPackages.put("javax.batch.runtime.context.", "jakarta.batch.runtime.context.");
        javaxPackages.put("javax.decorator.", "jakarta.decorator.");
        javaxPackages.put("javax.ejb.embeddable.", "jakarta.ejb.embeddable.");
        javaxPackages.put("javax.ejb.spi.", "jakarta.ejb.spi.");
        javaxPackages.put("javax.ejb.", "jakarta.ejb.");
        javaxPackages.put("javax.el.", "jakarta.el.");
        javaxPackages.put("javax.enterprise.concurrent.", "jakarta.enterprise.concurrent.");
        javaxPackages.put("javax.enterprise.context.control.", "jakarta.enterprise.context.control.");
        javaxPackages.put("javax.enterprise.context.spi.", "jakarta.enterprise.context.spi.");
        javaxPackages.put("javax.enterprise.context.", "jakarta.enterprise.context.");
        javaxPackages.put("javax.enterprise.event.", "jakarta.enterprise.event.");
        javaxPackages.put("javax.enterprise.inject.literal.", "jakarta.enterprise.inject.literal.");
        javaxPackages.put("javax.enterprise.inject.se.", "jakarta.enterprise.inject.se.");
        javaxPackages.put("javax.enterprise.inject.spi.", "jakarta.enterprise.inject.spi.");
        javaxPackages.put("javax.enterprise.inject.spi.configurator.", "jakarta.enterprise.inject.spi.configurator.");
        javaxPackages.put("javax.enterprise.inject.", "jakarta.enterprise.inject.");
        javaxPackages.put("javax.enterprise.util.", "jakarta.enterprise.util.");
        javaxPackages.put("javax.faces.annotation.", "jakarta.faces.annotation.");
        javaxPackages.put("javax.faces.application.", "jakarta.faces.application.");
        javaxPackages.put("javax.faces.bean.", "jakarta.faces.bean.");
        javaxPackages.put("javax.faces.component.", "jakarta.faces.component.");
        javaxPackages.put("javax.faces.component.behavior.", "jakarta.faces.component.behavior.");
        javaxPackages.put("javax.faces.component.html.", "jakarta.faces.component.html.");
        javaxPackages.put("javax.faces.component.search.", "jakarta.faces.component.search.");
        javaxPackages.put("javax.faces.component.visit.", "jakarta.faces.component.visit.");
        javaxPackages.put("javax.faces.context.", "jakarta.faces.context.");
        javaxPackages.put("javax.faces.convert.", "jakarta.faces.convert.");
        javaxPackages.put("javax.faces.el.", "jakarta.faces.el.");
        javaxPackages.put("javax.faces.event.", "jakarta.faces.event.");
        javaxPackages.put("javax.faces.flow.", "jakarta.faces.flow.");
        javaxPackages.put("javax.faces.flow.builder.", "jakarta.faces.flow.builder.");
        javaxPackages.put("javax.faces.lifecycle.", "jakarta.faces.lifecycle.");
        javaxPackages.put("javax.faces.model.", "jakarta.faces.model.");
        javaxPackages.put("javax.faces.push.", "jakarta.faces.push.");
        javaxPackages.put("javax.faces.render.", "jakarta.faces.render.");
        javaxPackages.put("javax.faces.validator.", "jakarta.faces.validator.");
        javaxPackages.put("javax.faces.view.", "jakarta.faces.view.");
        javaxPackages.put("javax.faces.view.facelets.", "jakarta.faces.view.facelets.");
        javaxPackages.put("javax.faces.webapp.", "jakarta.faces.webapp.");
        javaxPackages.put("javax.faces.", "jakarta.faces.");
        javaxPackages.put("javax.inject.", "jakarta.inject.");
        javaxPackages.put("javax.interceptor.", "jakarta.interceptor.");
        javaxPackages.put("javax.jms.", "jakarta.jms.");
        javaxPackages.put("javax.json.bind.", "jakarta.json.bind.");
        javaxPackages.put("javax.json.bind.adapter.", "jakarta.json.bind.adapter.");
        javaxPackages.put("javax.json.bind.annotation.", "jakarta.json.bind.annotation.");
        javaxPackages.put("javax.json.bind.config.", "jakarta.json.bind.config.");
        javaxPackages.put("javax.json.bind.serializer.", "jakarta.json.bind.serializer.");
        javaxPackages.put("javax.json.bind.spi.", "jakarta.json.bind.spi.");
        javaxPackages.put("javax.json.spi.", "jakarta.json.spi.");
        javaxPackages.put("javax.json.stream.", "jakarta.json.stream.");
        javaxPackages.put("javax.json.", "jakarta.json.");
        javaxPackages.put("javax.jws.soap.", "jakarta.jws.soap.");
        javaxPackages.put("javax.jws.", "jakarta.jws.");
        javaxPackages.put("javax.mail.event.", "jakarta.mail.event.");
        javaxPackages.put("javax.mail.internet.", "jakarta.mail.internet.");
        javaxPackages.put("javax.mail.search.", "jakarta.mail.search.");
        javaxPackages.put("javax.mail.util.", "jakarta.mail.util.");
        javaxPackages.put("javax.mail.", "jakarta.mail.");
        javaxPackages.put("javax.persistence.criteria.", "jakarta.persistence.criteria.");
        javaxPackages.put("javax.persistence.metamodel.", "jakarta.persistence.metamodel.");
        javaxPackages.put("javax.persistence.spi.", "jakarta.persistence.spi.");
        javaxPackages.put("javax.persistence.", "jakarta.persistence.");
        javaxPackages.put("javax.resource.cci.", "jakarta.resource.cci.");
        javaxPackages.put("javax.resource.spi.", "jakarta.resource.spi.");
        javaxPackages.put("javax.resource.spi.endpoint.", "jakarta.resource.spi.endpoint.");
        javaxPackages.put("javax.resource.spi.security.", "jakarta.resource.spi.security.");
        javaxPackages.put("javax.resource.spi.work.", "jakarta.resource.spi.work.");
        javaxPackages.put("javax.resource.", "jakarta.resource.");
        javaxPackages.put("javax.security.auth.message.callback.", "jakarta.security.auth.message.callback.");
        javaxPackages.put("javax.security.auth.message.config.", "jakarta.security.auth.message.config.");
        javaxPackages.put("javax.security.auth.message.module.", "jakarta.security.auth.message.module.");
        javaxPackages.put("javax.security.auth.message.", "jakarta.security.auth.message.");
        javaxPackages.put("javax.security.enterprise.authentication.mechanism.http.", "jakarta.security.enterprise.authentication.mechanism.http.");
        javaxPackages.put("javax.security.enterprise.credential.", "jakarta.security.enterprise.credential.");
        javaxPackages.put("javax.security.enterprise.identitystore.", "jakarta.security.enterprise.identitystore.");
        javaxPackages.put("javax.security.enterprise.", "jakarta.security.enterprise.");
        javaxPackages.put("javax.security.jacc.", "jakarta.security.jacc.");
        javaxPackages.put("javax.servlet.annotation.", "jakarta.servlet.annotation.");
        javaxPackages.put("javax.servlet.descriptor.", "jakarta.servlet.descriptor.");
        javaxPackages.put("javax.servlet.http.", "jakarta.servlet.http.");
        javaxPackages.put("javax.servlet.jsp.", "jakarta.servlet.jsp.");
        javaxPackages.put("javax.servlet.jsp.el.", "jakarta.servlet.jsp.el.");
        javaxPackages.put("javax.servlet.jsp.jstl.core.", "jakarta.servlet.jsp.jstl.core.");
        javaxPackages.put("javax.servlet.jsp.jstl.fmt.", "jakarta.servlet.jsp.jstl.fmt.");
        javaxPackages.put("javax.servlet.jsp.jstl.sql.", "jakarta.servlet.jsp.jstl.sql.");
        javaxPackages.put("javax.servlet.jsp.jstl.tlv.", "jakarta.servlet.jsp.jstl.tlv.");
        javaxPackages.put("javax.servlet.jsp.tagext.", "jakarta.servlet.jsp.tagext.");
        javaxPackages.put("javax.servlet.", "jakarta.servlet.");
        javaxPackages.put("javax.transaction.xa.", "jakarta.transaction.xa.");
        javaxPackages.put("javax.transaction.", "jakarta.transaction.");
        javaxPackages.put("javax.validation.bootstrap.", "jakarta.validation.bootstrap.");
        javaxPackages.put("javax.validation.constraints.", "jakarta.validation.constraints.");
        javaxPackages.put("javax.validation.constraintvalidation.", "jakarta.validation.constraintvalidation.");
        javaxPackages.put("javax.validation.executable.", "jakarta.validation.executable.");
        javaxPackages.put("javax.validation.groups.", "jakarta.validation.groups.");
        javaxPackages.put("javax.validation.metadata.", "jakarta.validation.metadata.");
        javaxPackages.put("javax.validation.spi.", "jakarta.validation.spi.");
        javaxPackages.put("javax.validation.valueextraction.", "jakarta.validation.valueextraction.");
        javaxPackages.put("javax.validation.", "jakarta.validation.");
        javaxPackages.put("javax.websocket.server.", "jakarta.websocket.server.");
        javaxPackages.put("javax.websocket.", "jakarta.websocket.");
        javaxPackages.put("javax.ws.rs.client.", "jakarta.ws.rs.client.");
        javaxPackages.put("javax.ws.rs.container.", "jakarta.ws.rs.container.");
        javaxPackages.put("javax.ws.rs.core.", "jakarta.ws.rs.core.");
        javaxPackages.put("javax.ws.rs.ext.", "jakarta.ws.rs.ext.");
        javaxPackages.put("javax.ws.rs.sse.", "jakarta.ws.rs.sse.");
        javaxPackages.put("javax.ws.rs.", "jakarta.ws.rs.");
        javaxPackages.put("javax.xml.bind.annotation.", "jakarta.xml.bind.annotation.");
        javaxPackages.put("javax.xml.bind.annotation.adapters.", "jakarta.xml.bind.annotation.adapters.");
        javaxPackages.put("javax.xml.bind.attachment.", "jakarta.xml.bind.attachment.");
        javaxPackages.put("javax.xml.bind.helpers.", "jakarta.xml.bind.helpers.");
        javaxPackages.put("javax.xml.bind.util.", "jakarta.xml.bind.util.");
        javaxPackages.put("javax.xml.bind.", "jakarta.xml.bind.");
        javaxPackages.put("javax.xml.soap.", "jakarta.xml.soap.");
        javaxPackages.put("javax.xml.ws.handler.", "jakarta.xml.ws.handler.");
        javaxPackages.put("javax.xml.ws.handler.soap.", "jakarta.xml.ws.handler.soap.");
        javaxPackages.put("javax.xml.ws.http.", "jakarta.xml.ws.http.");
        javaxPackages.put("javax.xml.ws.soap.", "jakarta.xml.ws.soap.");
        javaxPackages.put("javax.xml.ws.spi.", "jakarta.xml.ws.spi.");
        javaxPackages.put("javax.xml.ws.spi.http.", "jakarta.xml.ws.spi.http.");
        javaxPackages.put("javax.xml.ws.wsaddressing.", "jakarta.xml.ws.wsaddressing.");
        javaxPackages.put("javax.xml.ws.", "jakarta.xml.ws.");
    }

    public JavaSourceFile(Path path) {
        this.path = path;
    }

    public void process() throws IOException {
        if (!JavaxToJakarta.processJavaSources) {
            return;
        }
        LOGGER.info("Processing java file {}", path.toFile().getAbsolutePath());

        String initialContent = Files.readString(path);
        String finalContent = new String(initialContent.getBytes());

        // Replace import statements and fully qualified class names
        for (Map.Entry<String, String> entry : javaxPackages.entrySet()) {
            finalContent = finalContent.replace(entry.getKey(), entry.getValue());
        }

        if (!initialContent.equalsIgnoreCase(finalContent)) {
            LOGGER.info("Java file {} was updated", path.toFile().getAbsolutePath());
            Files.writeString(path, finalContent, StandardOpenOption.TRUNCATE_EXISTING);
        }
    }
}
