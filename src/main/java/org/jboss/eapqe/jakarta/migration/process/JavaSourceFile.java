package org.jboss.eapqe.jakarta.migration.process;

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

    static Map<String, String> javaxPackages;
    static {
        javaxPackages = new HashMap<>();
        javaxPackages.put("javax.activation.", "jakarta.activation.");
        javaxPackages.put("javax.annotation.", "jakarta.annotation.");
        javaxPackages.put("javax.annotation.security.", "jakarta.annotation.security.");
        javaxPackages.put("javax.annotation.sql.", "jakarta.annotation.sql.");
        javaxPackages.put("javax.batch.api.", "jakarta.batch.api.");
        javaxPackages.put("javax.batch.api.chunk.", "jakarta.batch.api.chunk.");
        javaxPackages.put("javax.batch.api.chunk.listener.", "jakarta.batch.api.chunk.listener.");
        javaxPackages.put("javax.batch.api.listener.", "jakarta.batch.api.listener.");
        javaxPackages.put("javax.batch.api.partition.", "jakarta.batch.api.partition.");
        javaxPackages.put("javax.batch.operations.", "jakarta.batch.operations.");
        javaxPackages.put("javax.batch.runtime.", "jakarta.batch.runtime.");
        javaxPackages.put("javax.batch.runtime.context.", "jakarta.batch.runtime.context.");
        javaxPackages.put("javax.decorator.", "jakarta.decorator.");
        javaxPackages.put("javax.ejb.", "jakarta.ejb.");
        javaxPackages.put("javax.ejb.embeddable.", "jakarta.ejb.embeddable.");
        javaxPackages.put("javax.ejb.spi.", "jakarta.ejb.spi.");
        javaxPackages.put("javax.el.", "jakarta.el.");
        javaxPackages.put("javax.enterprise.concurrent.", "jakarta.enterprise.concurrent.");
        javaxPackages.put("javax.enterprise.context.", "jakarta.enterprise.context.");
        javaxPackages.put("javax.enterprise.context.control.", "jakarta.enterprise.context.control.");
        javaxPackages.put("javax.enterprise.context.spi.", "jakarta.enterprise.context.spi.");
        javaxPackages.put("javax.enterprise.event.", "jakarta.enterprise.event.");
        javaxPackages.put("javax.enterprise.inject.", "jakarta.enterprise.inject.");
        javaxPackages.put("javax.enterprise.inject.literal.", "jakarta.enterprise.inject.literal.");
        javaxPackages.put("javax.enterprise.inject.se.", "jakarta.enterprise.inject.se.");
        javaxPackages.put("javax.enterprise.inject.spi.", "jakarta.enterprise.inject.spi.");
        javaxPackages.put("javax.enterprise.inject.spi.configurator.", "jakarta.enterprise.inject.spi.configurator.");
        javaxPackages.put("javax.enterprise.util.", "jakarta.enterprise.util.");
        javaxPackages.put("javax.faces.", "jakarta.faces.");
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
        javaxPackages.put("javax.inject.", "jakarta.inject.");
        javaxPackages.put("javax.interceptor.", "jakarta.interceptor.");
        javaxPackages.put("javax.jms.", "jakarta.jms.");
        javaxPackages.put("javax.json.", "jakarta.json.");
        javaxPackages.put("javax.json.bind.", "jakarta.json.bind.");
        javaxPackages.put("javax.json.bind.adapter.", "jakarta.json.bind.adapter.");
        javaxPackages.put("javax.json.bind.annotation.", "jakarta.json.bind.annotation.");
        javaxPackages.put("javax.json.bind.config.", "jakarta.json.bind.config.");
        javaxPackages.put("javax.json.bind.serializer.", "jakarta.json.bind.serializer.");
        javaxPackages.put("javax.json.bind.spi.", "jakarta.json.bind.spi.");
        javaxPackages.put("javax.json.spi.", "jakarta.json.spi.");
        javaxPackages.put("javax.json.stream.", "jakarta.json.stream.");
        javaxPackages.put("javax.jws.", "jakarta.jws.");
        javaxPackages.put("javax.jws.soap.", "jakarta.jws.soap.");
        javaxPackages.put("javax.mail.", "jakarta.mail.");
        javaxPackages.put("javax.mail.event.", "jakarta.mail.event.");
        javaxPackages.put("javax.mail.internet.", "jakarta.mail.internet.");
        javaxPackages.put("javax.mail.search.", "jakarta.mail.search.");
        javaxPackages.put("javax.mail.util.", "jakarta.mail.util.");
        javaxPackages.put("javax.persistence.", "jakarta.persistence.");
        javaxPackages.put("javax.persistence.criteria.", "jakarta.persistence.criteria.");
        javaxPackages.put("javax.persistence.metamodel.", "jakarta.persistence.metamodel.");
        javaxPackages.put("javax.persistence.spi.", "jakarta.persistence.spi.");
        javaxPackages.put("javax.resource.", "jakarta.resource.");
        javaxPackages.put("javax.resource.cci.", "jakarta.resource.cci.");
        javaxPackages.put("javax.resource.spi.", "jakarta.resource.spi.");
        javaxPackages.put("javax.resource.spi.endpoint.", "jakarta.resource.spi.endpoint.");
        javaxPackages.put("javax.resource.spi.security.", "jakarta.resource.spi.security.");
        javaxPackages.put("javax.resource.spi.work.", "jakarta.resource.spi.work.");
        javaxPackages.put("javax.security.auth.message.", "jakarta.security.auth.message.");
        javaxPackages.put("javax.security.auth.message.callback.", "jakarta.security.auth.message.callback.");
        javaxPackages.put("javax.security.auth.message.config.", "jakarta.security.auth.message.config.");
        javaxPackages.put("javax.security.auth.message.module.", "jakarta.security.auth.message.module.");
        javaxPackages.put("javax.security.enterprise.", "jakarta.security.enterprise.");
        javaxPackages.put("javax.security.enterprise.authentication.mechanism.http.", "jakarta.security.enterprise.authentication.mechanism.http.");
        javaxPackages.put("javax.security.enterprise.credential.", "jakarta.security.enterprise.credential.");
        javaxPackages.put("javax.security.enterprise.identitystore.", "jakarta.security.enterprise.identitystore.");
        javaxPackages.put("javax.security.jacc.", "jakarta.security.jacc.");
        javaxPackages.put("javax.servlet.", "jakarta.servlet.");
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
        javaxPackages.put("javax.transaction.", "jakarta.transaction.");
        javaxPackages.put("javax.validation.", "jakarta.validation.");
        javaxPackages.put("javax.validation.bootstrap.", "jakarta.validation.bootstrap.");
        javaxPackages.put("javax.validation.constraints.", "jakarta.validation.constraints.");
        javaxPackages.put("javax.validation.constraintvalidation.", "jakarta.validation.constraintvalidation.");
        javaxPackages.put("javax.validation.executable.", "jakarta.validation.executable.");
        javaxPackages.put("javax.validation.groups.", "jakarta.validation.groups.");
        javaxPackages.put("javax.validation.metadata.", "jakarta.validation.metadata.");
        javaxPackages.put("javax.validation.spi.", "jakarta.validation.spi.");
        javaxPackages.put("javax.validation.valueextraction.", "jakarta.validation.valueextraction.");
        javaxPackages.put("javax.websocket.", "jakarta.websocket.");
        javaxPackages.put("javax.websocket.server.", "jakarta.websocket.server.");
        javaxPackages.put("javax.ws.rs.", "jakarta.ws.rs.");
        javaxPackages.put("javax.ws.rs.client.", "jakarta.ws.rs.client.");
        javaxPackages.put("javax.ws.rs.container.", "jakarta.ws.rs.container.");
        javaxPackages.put("javax.ws.rs.core.", "jakarta.ws.rs.core.");
        javaxPackages.put("javax.ws.rs.ext.", "jakarta.ws.rs.ext.");
        javaxPackages.put("javax.ws.rs.sse.", "jakarta.ws.rs.sse.");
        javaxPackages.put("javax.xml.bind.", "jakarta.xml.bind.");
        javaxPackages.put("javax.xml.bind.annotation.", "jakarta.xml.bind.annotation.");
        javaxPackages.put("javax.xml.bind.annotation.adapters.", "jakarta.xml.bind.annotation.adapters.");
        javaxPackages.put("javax.xml.bind.attachment.", "jakarta.xml.bind.attachment.");
        javaxPackages.put("javax.xml.bind.helpers.", "jakarta.xml.bind.helpers.");
        javaxPackages.put("javax.xml.bind.util.", "jakarta.xml.bind.util.");
        javaxPackages.put("javax.xml.soap.", "jakarta.xml.soap.");
        javaxPackages.put("javax.xml.ws.", "jakarta.xml.ws.");
        javaxPackages.put("javax.xml.ws.handler.", "jakarta.xml.ws.handler.");
        javaxPackages.put("javax.xml.ws.handler.soap.", "jakarta.xml.ws.handler.soap.");
        javaxPackages.put("javax.xml.ws.http.", "jakarta.xml.ws.http.");
        javaxPackages.put("javax.xml.ws.soap.", "jakarta.xml.ws.soap.");
        javaxPackages.put("javax.xml.ws.spi.", "jakarta.xml.ws.spi.");
        javaxPackages.put("javax.xml.ws.spi.http.", "jakarta.xml.ws.spi.http.");
        javaxPackages.put("javax.xml.ws.wsaddressing.", "jakarta.xml.ws.wsaddressing.");
    }

    public JavaSourceFile(Path path) {
        this.path = path;
    }

    public void process() throws IOException {
        String content = Files.readString(path);

        String finaltext = content.replaceAll("import javax.activation", "import jakarta.activation");
        finaltext = finaltext.replaceAll("import javax.annotation", "import jakarta.annotation");
        finaltext = finaltext.replaceAll("import javax.annotation.security", "import jakarta.annotation.security");
        finaltext = finaltext.replaceAll("import javax.annotation.sql", "import jakarta.annotation.sql");
        finaltext = finaltext.replaceAll("import javax.batch.api", "import jakarta.batch.api");
	/*finaltext.replaceAll( "import javax.batch.api.chunk", "import jakarta.batch.api.chunk");
	finaltext.replaceAll( "import javax.batch.api.chunk.listener", "import jakarta.batch.api.chunk.listener");
	finaltext.replaceAll( "import javax.batch.api.listener", "import jakarta.batch.api.listener");
	finaltext.replaceAll( "import javax.batch.api.partition", "import jakarta.batch.api.partition")*/
        finaltext = finaltext.replaceAll("import javax.batch.operations", "import jakarta.batch.operations");
        finaltext = finaltext.replaceAll("import javax.batch.runtime", "import jakarta.batch.runtime");
        finaltext = finaltext.replaceAll("import javax.batch.runtime.context", "import jakarta.batch.runtime.context");
        finaltext = finaltext.replaceAll("import javax.decorator", "import jakarta.decorator");
        finaltext = finaltext.replaceAll("import javax.ejb", "import jakarta.ejb");
        //finaltext.replaceAll( "import javax.ejb.embeddable", "import jakarta.ejb.embeddable");
        //finaltext.replaceAll( "import javax.ejb.spi", "import jakarta.ejb.spi");
        finaltext = finaltext.replaceAll("import javax.el", "import jakarta.el");
        finaltext = finaltext.replaceAll("import javax.enterprise.concurrent", "import jakarta.enterprise.concurrent");
        finaltext = finaltext.replaceAll("import javax.enterprise.context", "import jakarta.enterprise.context");
        finaltext = finaltext.replaceAll("import javax.enterprise.context.control", "import jakarta.enterprise.context.control");
        finaltext = finaltext.replaceAll("import javax.enterprise.context.spi", "import jakarta.enterprise.context.spi");
        finaltext = finaltext.replaceAll("import javax.enterprise.event", "import jakarta.enterprise.event");
        finaltext = finaltext.replaceAll("import javax.enterprise.inject", "import jakarta.enterprise.inject");
        finaltext = finaltext.replaceAll("import javax.enterprise.inject.literal", "import jakarta.enterprise.inject.literal");
        finaltext = finaltext.replaceAll("import javax.enterprise.inject.se", "import jakarta.enterprise.inject.se");
        finaltext = finaltext.replaceAll("import javax.enterprise.inject.spi", "import jakarta.enterprise.inject.spi");
        finaltext = finaltext.replaceAll("import javax.enterprise.inject.spi.configurator", "import jakarta.enterprise.inject.spi.configurator");
        finaltext = finaltext.replaceAll("import javax.enterprise.util", "import jakarta.enterprise.util");
        finaltext = finaltext.replaceAll("import javax.faces", "import jakarta.faces");
	/*finaltext.replaceAll( "import javax.faces.annotation", "import jakarta.faces.annotation");
	finaltext.replaceAll( "import javax.faces.application", "import jakarta.faces.application");
	finaltext.replaceAll( "import javax.faces.bean", "import jakarta.faces.bean");
	finaltext.replaceAll( "import javax.faces.component", "import jakarta.faces.component");
	finaltext.replaceAll( "import javax.faces.component.behavior", "import jakarta.faces.component.behavior");
	finaltext.replaceAll( "import javax.faces.component.html", "import jakarta.faces.component.html");
	finaltext.replaceAll( "import javax.faces.component.search", "import jakarta.faces.component.search");
	finaltext.replaceAll( "import javax.faces.component.visit", "import jakarta.faces.component.visit");
	finaltext.replaceAll( "import javax.faces.context", "import jakarta.faces.context");
	finaltext.replaceAll( "import javax.faces.convert", "import jakarta.faces.convert");
	finaltext.replaceAll( "import javax.faces.el", "import jakarta.faces.el");
	finaltext.replaceAll( "import javax.faces.event", "import jakarta.faces.event");
	finaltext.replaceAll( "import javax.faces.flow", "import jakarta.faces.flow");
	finaltext.replaceAll( "import javax.faces.flow.builder", "import jakarta.faces.flow.builder");
	finaltext.replaceAll( "import javax.faces.lifecycle", "import jakarta.faces.lifecycle");
	finaltext.replaceAll( "import javax.faces.model", "import jakarta.faces.model");
	finaltext.replaceAll( "import javax.faces.push", "import jakarta.faces.push");
	finaltext.replaceAll( "import javax.faces.render", "import jakarta.faces.render");
	finaltext.replaceAll( "import javax.faces.validator", "import jakarta.faces.validator");
	finaltext.replaceAll( "import javax.faces.view", "import jakarta.faces.view");
	finaltext.replaceAll( "import javax.faces.view.facelets", "import jakarta.faces.view.facelets");
	finaltext.replaceAll( "import javax.faces.webapp", "import jakarta.faces.webapp")*/
        finaltext = finaltext.replaceAll("import javax.inject", "import jakarta.inject");
        finaltext = finaltext.replaceAll("import javax.interceptor", "import jakarta.interceptor");
        finaltext = finaltext.replaceAll("import javax.jms", "import jakarta.jms");
        finaltext = finaltext.replaceAll("import javax.json", "import jakarta.json");
	/*finaltext.replaceAll( "import javax.json.bind", "import jakarta.json.bind");
	finaltext.replaceAll( "import javax.json.bind.adapter", "import jakarta.json.bind.adapter");
	finaltext.replaceAll( "import javax.json.bind.annotation", "import jakarta.json.bind.annotation");
	finaltext.replaceAll( "import javax.json.bind.config", "import jakarta.json.bind.config");
	finaltext.replaceAll( "import javax.json.bind.serializer", "import jakarta.json.bind.serializer");
	finaltext.replaceAll( "import javax.json.bind.spi", "import jakarta.json.bind.spi");
	finaltext.replaceAll( "import javax.json.spi", "import jakarta.json.spi");
	finaltext.replaceAll( "import javax.json.stream", "import jakarta.json.stream")*/
        finaltext = finaltext.replaceAll("import javax.jws", "import jakarta.jws");
        //finaltext.replaceAll( "import javax.jws.soap", "import jakarta.jws.soap");
        finaltext = finaltext.replaceAll("import javax.mail", "import jakarta.mail");
	/*finaltext.replaceAll( "import javax.mail.event", "import jakarta.mail.event");
	finaltext.replaceAll( "import javax.mail.internet", "import jakarta.mail.internet");
	finaltext.replaceAll( "import javax.mail.search", "import jakarta.mail.search");
	finaltext.replaceAll( "import javax.mail.util", "import jakarta.mail.util")*/
        finaltext = finaltext.replaceAll("import javax.persistence", "import jakarta.persistence");
	/*finaltext.replaceAll( "import javax.persistence.criteria", "import jakarta.persistence.criteria");
	finaltext.replaceAll( "import javax.persistence.metamodel", "import jakarta.persistence.metamodel");
	finaltext.replaceAll( "import javax.persistence.spi", "import jakarta.persistence.spi")*/
        finaltext = finaltext.replaceAll("import javax.resource", "import jakarta.resource");
        //finaltext.replaceAll( "import javax.resource.cci", "import jakarta.resource.cci");
        finaltext = finaltext.replaceAll("import javax.resource.spi", "import jakarta.resource.spi");
	/*finaltext.replaceAll( "import javax.resource.spi.endpoint", "import jakarta.resource.spi.endpoint");
	finaltext.replaceAll( "import javax.resource.spi.security", "import jakarta.resource.spi.security");
	finaltext.replaceAll( "import javax.resource.spi.work", "import jakarta.resource.spi.work")*/
        finaltext = finaltext.replaceAll("import javax.security.auth.message", "import jakarta.security.auth.message");
	/*finaltext.replaceAll( "import javax.security.auth.message.callback", "import jakarta.security.auth.message.callback");
	finaltext.replaceAll( "import javax.security.auth.message.config", "import jakarta.security.auth.message.config");
	finaltext.replaceAll( "import javax.security.auth.message.module", "import jakarta.security.auth.message.module")*/
        finaltext = finaltext.replaceAll("import javax.security.enterprise", "import jakarta.security.enterprise");
        finaltext = finaltext.replaceAll("import javax.security.enterprise.authentication.mechanism.http", "import jakarta.security.enterprise.authentication.mechanism.http");
        finaltext = finaltext.replaceAll("import javax.security.enterprise.credential", "import jakarta.security.enterprise.credential");
        finaltext = finaltext.replaceAll("import javax.security.enterprise.identitystore", "import jakarta.security.enterprise.identitystore");
        finaltext = finaltext.replaceAll("import javax.security.jacc", "import jakarta.security.jacc");
        finaltext = finaltext.replaceAll("import javax.servlet", "import jakarta.servlet");
	/*finaltext.replaceAll( "import javax.servlet.annotation", "import jakarta.servlet.annotation");
	finaltext.replaceAll( "import javax.servlet.descriptor", "import jakarta.servlet.descriptor");
	finaltext.replaceAll( "import javax.servlet.http", "import jakarta.servlet.http");
	finaltext.replaceAll( "import javax.servlet.jsp", "import jakarta.servlet.jsp");
	finaltext.replaceAll( "import javax.servlet.jsp.el", "import jakarta.servlet.jsp.el");
	finaltext.replaceAll( "import javax.servlet.jsp.jstl.core", "import jakarta.servlet.jsp.jstl.core");
	finaltext.replaceAll( "import javax.servlet.jsp.jstl.fmt", "import jakarta.servlet.jsp.jstl.fmt");
	finaltext.replaceAll( "import javax.servlet.jsp.jstl.sql", "import jakarta.servlet.jsp.jstl.sql");
	finaltext.replaceAll( "import javax.servlet.jsp.jstl.tlv", "import jakarta.servlet.jsp.jstl.tlv");
	finaltext.replaceAll( "import javax.servlet.jsp.tagext", "import jakarta.servlet.jsp.tagext")*/
        finaltext = finaltext.replaceAll("import javax.transaction", "import jakarta.transaction");
        finaltext = finaltext.replaceAll("import javax.validation", "import jakarta.validation");
	/*finaltext.replaceAll( "import javax.validation.bootstrap", "import jakarta.validation.bootstrap");
	finaltext.replaceAll( "import javax.validation.constraints", "import jakarta.validation.constraints");
	finaltext.replaceAll( "import javax.validation.constraintvalidation", "import jakarta.validation.constraintvalidation");
	finaltext.replaceAll( "import javax.validation.executable", "import jakarta.validation.executable");
	finaltext.replaceAll( "import javax.validation.groups", "import jakarta.validation.groups");
	finaltext.replaceAll( "import javax.validation.metadata", "import jakarta.validation.metadata");
	finaltext.replaceAll( "import javax.validation.spi", "import jakarta.validation.spi");
	finaltext.replaceAll( "import javax.validation.valueextraction", "import jakarta.validation.valueextraction")*/
        finaltext = finaltext.replaceAll("import javax.websocket", "import jakarta.websocket");
        //finaltext.replaceAll( "import javax.websocket.server", "import jakarta.websocket.server");
        finaltext = finaltext.replaceAll("import javax.ws.rs", "import jakarta.ws.rs");
	/*finaltext.replaceAll( "import javax.ws.rs.client", "import jakarta.ws.rs.client");
	finaltext.replaceAll( "import javax.ws.rs.container", "import jakarta.ws.rs.container");
	finaltext.replaceAll( "import javax.ws.rs.core", "import jakarta.ws.rs.core");
	finaltext.replaceAll( "import javax.ws.rs.ext", "import jakarta.ws.rs.ext");
	finaltext.replaceAll( "import javax.ws.rs.sse", "import jakarta.ws.rs.sse")*/
        finaltext = finaltext.replaceAll("import javax.xml.bind", "import jakarta.xml.bind");
	/*finaltext.replaceAll( "import javax.xml.bind.annotation", "import jakarta.xml.bind.annotation");
	finaltext.replaceAll( "import javax.xml.bind.annotation.adapters", "import jakarta.xml.bind.annotation.adapters");
	finaltext.replaceAll( "import javax.xml.bind.attachment", "import jakarta.xml.bind.attachment");
	finaltext.replaceAll( "import javax.xml.bind.helpers", "import jakarta.xml.bind.helpers");
	finaltext.replaceAll( "import javax.xml.bind.util", "import jakarta.xml.bind.util")*/
        finaltext = finaltext.replaceAll("import javax.xml.soap", "import jakarta.xml.soap");
        finaltext = finaltext.replaceAll("import javax.xml.ws", "import jakarta.xml.ws");
	/*finaltext.replaceAll( "import javax.xml.ws.handler", "import jakarta.xml.ws.handler");
	finaltext.replaceAll( "import javax.xml.ws.handler.soap", "import jakarta.xml.ws.handler.soap");
	finaltext.replaceAll( "import javax.xml.ws.http", "import jakarta.xml.ws.http");
	finaltext.replaceAll( "import javax.xml.ws.soap", "import jakarta.xml.ws.soap");
	finaltext.replaceAll( "import javax.xml.ws.spi", "import jakarta.xml.ws.spi");
	finaltext.replaceAll( "import javax.xml.ws.spi.http", "import jakarta.xml.ws.spi.http");
	finaltext.replaceAll( "import javax.xml.ws.wsaddressing", "import jakarta.xml.ws.wsaddressing")*/

        for (Map.Entry<String, String> entry : javaxPackages.entrySet()) {
            finaltext = finaltext.replace(entry.getKey(), entry.getValue());
        }

        if (!content.equalsIgnoreCase(finaltext)) {
            LOGGER.info("Java file {} was updated", path.toFile().getAbsolutePath());
            Files.writeString(path, finaltext, StandardOpenOption.TRUNCATE_EXISTING);
        }
    }
}
