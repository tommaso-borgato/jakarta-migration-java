package org.jboss.eapqe.jakarta.migration.process;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
//import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
//import org.apache.maven.resolver.examples.util.Booter;
//import org.apache.maven.resolver.examples.util.ConsoleRepositoryListener;
//import org.apache.maven.resolver.examples.util.ConsoleTransferListener;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
//import org.eclipse.aether.RepositorySystemSession;
//import org.eclipse.aether.artifact.Artifact;
//import org.eclipse.aether.artifact.DefaultArtifact;
//import org.eclipse.aether.collection.CollectRequest;
//import org.eclipse.aether.collection.CollectResult;
import org.eclipse.aether.collection.DependencyCollectionException;
//import org.eclipse.aether.graph.Dependency;
//import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.resolution.DependencyResolutionException;
import org.jboss.eapqe.jakarta.migration.JavaxToJakarta;
import org.jboss.eapqe.jakarta.migration.os.OsCommandResult;
import org.jboss.eapqe.jakarta.migration.os.OsCommandRunner;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import static org.apache.maven.resolver.examples.util.Booter.SERVICE_LOCATOR;

/**
 * https://github.com/apache/maven-resolver/blob/master/maven-resolver-demos/maven-resolver-demo-snippets/src/main/java/org/apache/maven/resolver/examples/GetDependencyTree.java
 */
public class PomFile {

    // TODO: enrich the list
    private final static List<String> javaEE8dependencies = Arrays.asList(
            "javaee-api",
            "activation",
            "activation-api",
            "cdi-api",
            "javaee-api",
            "javaee-web-api",
            "annotation-api",
            "annotations-api",
            "batch-api",
            "ejb-api",
            "el-api",
            "enterprise.concurrent-api",
            "concurrent-api",
            "enterprise.deploy-api",
            "deploy-api",
            "faces-api",
            "faces",
            "inject",
            "interceptor-api",
            "jms-api",
            "json-api",
            "json.bind-api",
            "xml.bind-api",
            "xml.ws-api",
            "ws-api",
            "mail",
            "management.j2ee-api",
            "j2ee-api",
            "persistence-api",
            "persistence",
            "resource-api",
            "security.auth.message-api",
            "message-api",
            "security.enterprise-api",
            "enterprise-api",
            "security.jacc-api",
            "jacc-api",
            "servlet-api",
            "servlet.jsp-api",
            "jsp-api",
            "servlet.jsp.jstl-api",
            "jstl-api",
            "transaction-api",
            "websocket-api",
            "websocket-client-api",
            "ws.rs-api",
            "rs-api",
            "xml.registry-api",
            "registry-api",
            "xml.rpc-api",
            "rpc-api",
            "jsf-api",
            "jsf-impl",
            "servlet-api",
            "soap-api",
            "validation-api",
            "authentication-api",
            "authorization-api",
            "inject-api",
            "jws-api",
            "mail-api",
            "javax.mvc-api",
            "mvc-api"
    );

    private final static List<String> javaEE8Apis = Arrays.asList(
            "javax.annotation",
            "javax.activation",
            "javax.annotation.security",
            "javax.annotation.sql",
            "javax.batch.api",
            "javax.batch.api.chunk",
            "javax.batch.api.chunk.listener",
            "javax.batch.api.listener",
            "javax.batch.api.partition",
            "javax.batch.operations",
            "javax.batch.runtime",
            "javax.batch.runtime.context",
            "javax.decorator",
            "javax.ejb",
            "javax.ejb.embeddable",
            "javax.ejb.spi",
            "javax.el",
            "javax.enterprise.concurrent",
            "javax.enterprise.context",
            "javax.enterprise.context.control",
            "javax.enterprise.context.spi",
            "javax.enterprise",
            "javax.enterprise.deploy.model",
            "javax.enterprise.deploy.model.exceptions",
            "javax.enterprise.deploy.shared",
            "javax.enterprise.deploy.shared.factories",
            "javax.enterprise.deploy.spi",
            "javax.enterprise.deploy.spi.exceptions",
            "javax.enterprise.deploy.spi.factories",
            "javax.enterprise.deploy.spi.status",
            "javax.enterprise.event",
            "javax.enterprise.inject",
            "javax.enterprise.inject.literal",
            "javax.enterprise.inject.se",
            "javax.enterprise.inject.spi",
            "javax.enterprise.inject.spi.configurator",
            "javax.enterprise.util",
            "javax.faces",
            "javax.faces.annotation",
            "javax.faces.application",
            "javax.faces.bean",
            "javax.faces.component",
            "javax.faces.component.behavior",
            "javax.faces.component.html",
            "javax.faces.component.search",
            "javax.faces.component.visit",
            "javax.faces.context",
            "javax.faces.convert",
            "javax.faces.el",
            "javax.faces.event",
            "javax.faces.flow",
            "javax.faces.flow.builder",
            "javax.faces.lifecycle",
            "javax.faces.model",
            "javax.faces.push",
            "javax.faces.render",
            "javax.faces.validator",
            "javax.faces.view",
            "javax.faces.view.facelets",
            "javax.faces.webapp",
            "javax.inject",
            "javax.interceptor",
            "javax.jms",
            "javax.json",
            "javax.jws",
            "javax.mail",
            "javax.mail.event",
            "javax.mail.internet",
            "javax.mail.search",
            "javax.mail.util",
            "javax.management.j2ee",
            "javax.management.j2ee.statistics",
            "javax.persistence",
            "javax.persistence.criteria",
            "javax.persistence.metamodel",
            "javax.persistence.spi",
            "javax.resource",
            "javax.resource.cci",
            "javax.resource.spi",
            "javax.resource.spi.endpoint",
            "javax.resource.spi.security",
            "javax.resource.spi.work",
            "javax.security.auth.message",
            "javax.security.auth.message.callback",
            "javax.security.auth.message.config",
            "javax.security.auth.message.module",
            "javax.security.enterprise",
            "javax.security.enterprise.authentication.mechanism.http",
            "javax.security.enterprise.credential",
            "javax.security.enterprise.identitystore",
            "javax.security.jacc",
            "javax.servlet",
            "javax.servlet.annotation",
            "javax.servlet.descriptor",
            "javax.servlet.http",
            "javax.servlet.jsp",
            "javax.servlet.jsp.el",
            "javax.servlet.jsp.jstl.core",
            "javax.servlet.jsp.jstl.fmt",
            "javax.servlet.jsp.jstl.sql",
            "javax.servlet.jsp.jstl.tlv",
            "javax.servlet.jsp.tagext",
            "javax.transaction",
            "javax.transaction.xa",
            "javax.validation",
            "javax.validation.bootstrap",
            "javax.validation.constraints",
            "javax.validation.constraintvalidation",
            "javax.validation.executable",
            "javax.validation.groups",
            "javax.validation.metadata",
            "javax.validation.spi",
            "javax.validation.valueextraction",
            "javax.websocket",
            "javax.websocket.server",
            "javax.ws.rs",
            "javax.xml.bind",
            "javax.xml.soap",
            "javax.xml.ws",
            "org.jboss.spec.javax"
    );

    /**
     * Pull request
     * <a href="https://github.com/wildfly/wildfly/pull/15833">[WFLY-16652] Move standard WildFly build to EE 10</a>
     * changes the wildfly-ee and wildfly feature packs (called eap and eap-xp in product) to use the binaries from the
     * source-transformed modules: the list of transformed modules has been extracted form this PR;
     */
    static Map<String, String> transformed;
    static {
        transformed = new HashMap<>();
        transformed.put("ironjacamar-common-impl","org.jboss.ironjacamar:ironjacamar-common-impl-jakarta");
        transformed.put("wildfly-mail","org.wildfly:wildfly-mail-jakarta");
        transformed.put("wildfly-rts","org.wildfly:wildfly-rts-jakarta");
        transformed.put("ironjacamar-deployers-common","org.jboss.ironjacamar:ironjacamar-deployers-common-jakarta");
        transformed.put("openjdk-orb","org.jboss.openjdk-orb:openjdk-orb-jakarta");
        transformed.put("jboss-ejb-client","org.jboss:jboss-ejb-client-jakarta");
        transformed.put("wildfly-datasources-agroal","org.wildfly:wildfly-datasources-agroal-jakarta");
        transformed.put("artemis-journal","artemis-jakarta-ra");
        transformed.put("jboss-iiop-client","org.jboss:jboss-iiop-client-jakarta");
        transformed.put("narayana-jts-integration","org.jboss.narayana.jts:narayana-jts-integration-jakarta");
        transformed.put("jbossxts","org.jboss.narayana.xts:jbossxts-jakarta");
        transformed.put("hornetq-jms-client","hornetq-jakarta-client");
        transformed.put("ironjacamar-core-api","org.jboss.ironjacamar:ironjacamar-core-api-jakarta");
        transformed.put("wildfly-xts","org.wildfly:wildfly-xts-jakarta");
        transformed.put("restat-bridge","org.jboss.narayana.rts:restat-bridge-jakarta");
        transformed.put("jboss-transaction-spi","org.jboss:jboss-transaction-spi-jakarta");
        transformed.put("wildfly-microprofile-fault-tolerance-smallrye-executor","org.wildfly:wildfly-microprofile-fault-tolerance-smallrye-executor-jakarta");
        transformed.put("compensations","org.jboss.narayana.compensations:compensations-jakarta");
        transformed.put("jboss-metadata-common","org.jboss.metadata:jboss-metadata-common-jakarta");
        transformed.put("wildfly-undertow","org.wildfly:wildfly-undertow-jakarta");
        transformed.put("wildfly-clustering-singleton-extension","org.wildfly:wildfly-testsuite-shared-jakarta");
        transformed.put("jbosstxbridge","org.jboss.narayana:jbosstxbridge-jakarta");
        transformed.put("wildfly-testsuite-shared","org.wildfly:wildfly-testsuite-shared-jakarta");
        transformed.put("wildfly-mod_cluster-undertow","org.wildfly:wildfly-mod_cluster-undertow-jakarta");
        transformed.put("wildfly-client-all","org.wildfly:wildfly-client-all-jakarta");
        transformed.put("wildfly-microprofile-openapi-smallrye","org.wildfly:wildfly-microprofile-openapi-smallrye-jakarta");
        transformed.put("wildfly-microprofile-fault-tolerance-smallrye-extension","org.wildfly:wildfly-microprofile-fault-tolerance-smallrye-extension-jakarta");
        transformed.put("wildfly-microprofile-opentracing-smallrye","org.wildfly:wildfly-microprofile-opentracing-smallrye-jakarta");
        transformed.put("wildfly-transactions","org.wildfly:wildfly-transactions-jakarta");
        transformed.put("wildfly-iiop-openjdk","org.wildfly:wildfly-iiop-openjdk-jakarta");
        transformed.put("ironjacamar-validator","org.jboss.ironjacamar:ironjacamar-validator-jakarta");
        transformed.put("wildfly-weld","org.wildfly:wildfly-weld-bean-validation-jakarta");
        transformed.put("wildfly-microprofile-opentracing-extension","org.wildfly:wildfly-microprofile-opentracing-extension-jakarta");
        transformed.put("ironjacamar-core-impl","org.jboss.ironjacamar:ironjacamar-core-impl-jakarta");
        transformed.put("wildfly-weld-bean-validation","org.wildfly:wildfly-weld-common-jakarta");
        transformed.put("wildfly-bean-validation","org.wildfly:wildfly-bean-validation-jakarta");
        transformed.put("wildfly-clustering-weld-ejb","org.wildfly:wildfly-clustering-weld-ejb-jakarta");
        transformed.put("wildfly-weld-transactions","org.wildfly:wildfly-weld-transactions-jakarta");
        transformed.put("wildfly-ee-security","org.wildfly:wildfly-ee-security-jakarta");
        transformed.put("jboss-jakarta-xml-ws-api_3.0_spec","jboss-jakarta-xml-ws-api_4.0_spec");
        transformed.put("wildfly-weld-common","org.wildfly:wildfly-weld-ejb-jakarta");
        transformed.put("wildfly-weld-jpa","org.wildfly:wildfly-weld-jpa-jakarta");
        transformed.put("wildfly-clustering-weld-core","org.wildfly:wildfly-clustering-weld-core-jakarta");
        transformed.put("wildfly-weld-ejb","org.wildfly:wildfly-weld-jakarta");
        transformed.put("artemis-server","artemis-jakarta-service-extensions");
        transformed.put("wildfly-ejb3","org.wildfly:wildfly-ejb3-jakarta");
        transformed.put("artemis-jdbc-store","artemis-jakarta-client");
        transformed.put("artemis-selector","artemis-jakarta-server");
        transformed.put("wildfly-jsf","org.wildfly:wildfly-jsf-injection-jakarta");
        transformed.put("${wildfly-testsuite-shared.artifactId}","org.wildfly:wildfly-testsuite-shared-jakarta");
        transformed.put("wildfly-clustering-web-undertow","org.wildfly:wildfly-clustering-web-undertow-jakarta");
        transformed.put("wildfly-microprofile-health-smallrye","org.wildfly:wildfly-microprofile-health-smallrye-jakarta");
        transformed.put("wildfly-http-transaction-client","org.wildfly.wildfly-http-client:wildfly-http-transaction-client-jakarta");
        transformed.put("wildfly-clustering-weld-web","org.wildfly:wildfly-clustering-weld-web-jakarta");
        transformed.put("wildfly-ee","org.wildfly:wildfly-ee-jakarta");
        transformed.put("artemis-ra","artemis-jakarta-ra");
        transformed.put("txframework","org.jboss.narayana.txframework:txframework-jakarta");
        transformed.put("ironjacamar-common-spi","org.jboss.ironjacamar:ironjacamar-common-spi-jakarta");
        transformed.put("wildfly-microprofile-reactive-streams-operators-cdi-provider","org.wildfly:wildfly-microprofile-reactive-streams-operators-cdi-provider-jakarta");
        transformed.put("artemis-service-extensions","artemis-jakarta-service-extensions");
        transformed.put("wildfly-jaxrs","org.wildfly:wildfly-jaxrs-jakarta");
        transformed.put("wildfly-messaging-activemq-injection","org.wildfly:wildfly-messaging-activemq-injection-jakarta");
        transformed.put("wildfly-web-common","org.wildfly:wildfly-web-common-jakarta");
        transformed.put("ironjacamar-common-api","org.jboss.ironjacamar:ironjacamar-common-api-jakarta");
        transformed.put("restat-integration","org.jboss.narayana.rts:restat-integration-jakarta");
        transformed.put("jipijapa-hibernate5-3","org.wildfly:jipijapa-eclipselink-jakarta");
        transformed.put("wildfly-batch-jberet","org.wildfly:wildfly-batch-jberet-jakarta");
        transformed.put("wildfly-microprofile-jwt-smallrye","org.wildfly:wildfly-microprofile-jwt-smallrye-jakarta");
        transformed.put("wildfly-connector","org.wildfly:wildfly-connector-jakarta");
        transformed.put("wildfly-weld-spi","org.wildfly:wildfly-weld-spi-jakarta");
        transformed.put("restat-api","org.jboss.narayana.rts:restat-api-jakarta");
        transformed.put("jboss-metadata-ejb","org.jboss.metadata:jboss-metadata-ejb-jakarta");
        transformed.put("narayana-jts-idlj","org.jboss.narayana.jts:narayana-jts-idlj-jakarta");
        transformed.put("ironjacamar-jdbc","org.jboss.ironjacamar:ironjacamar-jdbc-jakarta");
        transformed.put("wildfly-jpa","org.wildfly:wildfly-jpa-jakarta");
        transformed.put("wildfly-weld-webservices","org.wildfly:wildfly-weld-webservices-jakarta");
        transformed.put("restat-util","org.jboss.narayana.rts:restat-util-jakarta");
    }

    // TODO: remove this list
    private final static List<String> complete = Arrays.asList(
            "org.jboss.invocation:jboss-invocation-jakarta",
            "org.jboss.ironjacamar:ironjacamar-common-api-jakarta",
            "org.jboss.ironjacamar:ironjacamar-common-impl-jakarta",
            "org.jboss.ironjacamar:ironjacamar-common-spi-jakarta",
            "org.jboss.ironjacamar:ironjacamar-core-api-jakarta",
            "org.jboss.ironjacamar:ironjacamar-core-impl-jakarta",
            "org.jboss.ironjacamar:ironjacamar-deployers-common-jakarta",
            "org.jboss.ironjacamar:ironjacamar-jdbc-jakarta",
            "org.jboss.ironjacamar:ironjacamar-validator-jakarta",
            "org.jboss:jboss-ejb-client-jakarta",
            "org.jboss:jboss-iiop-client-jakarta",
            "org.jboss:jboss-transaction-spi-jakarta",
            "org.jboss:jboss-transaction-spi-jakarta",
            "org.jboss.metadata:jboss-metadata-common-jakarta",
            "org.jboss.metadata:jboss-metadata-ejb-jakarta",
            "org.jboss.narayana.compensations:compensations-jakarta",
            "org.jboss.narayana:jbosstxbridge-jakarta",
            "org.jboss.narayana.jts:narayana-jts-idlj-jakarta",
            "org.jboss.narayana.jts:narayana-jts-integration-jakarta",
            "org.jboss.narayana.rts:restat-api-jakarta",
            "org.jboss.narayana.rts:restat-bridge-jakarta",
            "org.jboss.narayana.rts:restat-integration-jakarta",
            "org.jboss.narayana.rts:restat-util-jakarta",
            "org.jboss.narayana.txframework:txframework-jakarta",
            "org.jboss.narayana.xts:jbossxts-jakarta",
            "org.jboss.openjdk-orb:openjdk-orb-jakarta",
            "org.wildfly.core:wildfly-elytron-integration-jakarta",
            "org.wildfly:jipijapa-eclipselink-jakarta",
            "org.wildfly:jipijapa-spi-jakarta",
            "org.wildfly.transaction:wildfly-transaction-client-jakarta",
            "org.wildfly:wildfly-batch-jberet-jakarta",
            "org.wildfly:wildfly-bean-validation-jakarta",
            "org.wildfly:wildfly-client-all-jakarta",
            "org.wildfly:wildfly-clustering-faces-api-jakarta",
            "org.wildfly:wildfly-clustering-faces-jakarta",
            "org.wildfly:wildfly-clustering-faces-mojarra-jakarta",
            "org.wildfly:wildfly-clustering-jakarta",
            "org.wildfly:wildfly-clustering-web-jakarta",
            "org.wildfly:wildfly-clustering-web-undertow-jakarta",
            "org.wildfly:wildfly-clustering-weld-core-jakarta",
            "org.wildfly:wildfly-clustering-weld-ejb-jakarta",
            "org.wildfly:wildfly-clustering-weld-jakarta",
            "org.wildfly:wildfly-clustering-weld-web-jakarta",
            "org.wildfly:wildfly-connector-jakarta",
            "org.wildfly:wildfly-datasources-agroal-jakarta",
            "org.wildfly:wildfly-ee-jakarta",
            "org.wildfly:wildfly-ee-security-jakarta",
            "org.wildfly:wildfly-ejb3-jakarta",
            "org.wildfly.wildfly-http-client:wildfly-http-ejb-client-jakarta",
            "org.wildfly.wildfly-http-client:wildfly-http-transaction-client-jakarta",
            "org.wildfly:wildfly-iiop-openjdk-jakarta",
            "org.wildfly:wildfly-jaxrs-jakarta",
            "org.wildfly:wildfly-jpa-jakarta",
            "org.wildfly:wildfly-jsf-injection-jakarta",
            "org.wildfly:wildfly-jsf-jakarta",
            "org.wildfly:wildfly-jsf-parent-jakarta",
            "org.wildfly:wildfly-mail-jakarta",
            "org.wildfly:wildfly-messaging-activemq-injection-jakarta",
            "org.wildfly:wildfly-messaging-activemq-subsystem-jakarta",
            "org.wildfly:wildfly-microprofile-fault-tolerance-smallrye-executor-jakarta",
            "org.wildfly:wildfly-microprofile-fault-tolerance-smallrye-extension-jakarta",
            "org.wildfly:wildfly-microprofile-fault-tolerance-smallrye-jakarta",
            "org.wildfly:wildfly-microprofile-health-smallrye-jakarta",
            "org.wildfly:wildfly-microprofile-jakarta",
            "org.wildfly:wildfly-microprofile-jwt-smallrye-jakarta",
            "org.wildfly:wildfly-microprofile-openapi-smallrye-jakarta",
            "org.wildfly:wildfly-microprofile-opentracing-extension-jakarta",
            "org.wildfly:wildfly-microprofile-opentracing-smallrye-jakarta",
            "org.wildfly:wildfly-microprofile-reactive-streams-operators-cdi-provider-jakarta",
            "org.wildfly:wildfly-mod_cluster-undertow-jakarta",
            "org.wildfly:wildfly-opentelemetry-api-jakarta",
            "org.wildfly:wildfly-opentelemetry-jakarta",
            "org.wildfly:wildfly-rts-jakarta",
            "org.wildfly:wildfly-testsuite-shared-jakarta",
            "org.wildfly:wildfly-transactions-jakarta",
            "org.wildfly:wildfly-undertow-jakarta",
            "org.wildfly:wildfly-web-common-jakarta",
            "org.wildfly:wildfly-webservices-jakarta",
            "org.wildfly:wildfly-webservices-server-integration-jakarta",
            "org.wildfly:wildfly-weld-bean-validation-jakarta",
            "org.wildfly:wildfly-weld-common-jakarta",
            "org.wildfly:wildfly-weld-ejb-jakarta",
            "org.wildfly:wildfly-weld-jakarta",
            "org.wildfly:wildfly-weld-jpa-jakarta",
            "org.wildfly:wildfly-weld-spi-jakarta",
            "org.wildfly:wildfly-weld-transactions-jakarta",
            "org.wildfly:wildfly-weld-webservices-jakarta",
            "org.wildfly:wildfly-xts-jakarta"
    );


    public static void main(String[] a) {
        Map<String,String> rs = new HashMap<>();
        for (Map.Entry<String, String> entry : transformed.entrySet()) {
            boolean f = false;
            // transformed.put("wildfly-web-common","wildfly-web-common-jakarta");
            // "org.wildfly:wildfly-web-common-jakarta"
            for (String s: complete) {
                if (s.matches("^.*:" + entry.getValue() + "$")) {
                    rs.put(entry.getKey(), s);
                    f = true;
                }
            }
            if (!f) {
                rs.put(entry.getKey(), entry.getValue());
            }
        }
        for (Map.Entry<String, String> entry : rs.entrySet()) {
            System.out.println("transformed.put(\"" + entry.getKey() + "\",\"" + entry.getValue() + "\");");
        }
    }

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    private OsCommandRunner osCommandRunner;

    private Path path;

    public PomFile(Path path) {
        this.path = path;
    }

    public void process() throws IOException, XmlPullParserException, DependencyCollectionException, DependencyResolutionException, InterruptedException {
        if (!JavaxToJakarta.analyzeDependencies) {
            return;
        }

        if (false) {
            MavenXpp3Reader reader = new MavenXpp3Reader();
            Model model = reader.read(new FileReader(path.toFile()));
            System.out.println(model.getId());
            System.out.println(model.getGroupId());
            System.out.println(model.getArtifactId());
            System.out.println(model.getVersion());
            getDependencyTree(model, null);
        } else {
            printDepTree();
        }

        /*for (org.apache.maven.model.Dependency dependency : model.getDependencies()){
            getDependencyTree(model, dependency);
        }*/

        // https://github.com/apache/maven-resolver/blob/master/maven-resolver-demos/maven-resolver-demo-snippets/src/main/java/org/apache/maven/resolver/examples/GetDependencyTree.java
    }

    private void printDepTree() throws IOException, InterruptedException {
        OsCommandResult result = getOsCommandRunner().execOsCommand("mvn", "dependency:tree", "-f", path.toFile().getAbsolutePath());
        if (result.getExitCode() != 0) {
            throw new IOException(String.format("error when in \"mvn dependency:tree -f %s\": %s", path.toFile().getAbsolutePath(), result.getOutput()));
        }
        String depTreeStr = result.getOutput();

        // poolish the output
        depTreeStr = depTreeStr.replaceFirst("(?s).*\\[INFO\\] --- maven-dependency-plugin[^\\n]*", "");

        // Java EE 8 APIs names
        for (String dep : javaEE8dependencies) {
            depTreeStr = depTreeStr.replace(dep, ANSI_YELLOW_BACKGROUND + ANSI_PURPLE + dep + ANSI_RESET);
        }

        // Java EE 8 APIs packages names
        for (String dep : javaEE8Apis) {
            depTreeStr = depTreeStr.replace(dep, ANSI_YELLOW_BACKGROUND + ANSI_CYAN + dep + ANSI_RESET);
        }

        // WildFly transformed modules
        for (Map.Entry<String, String> entry : transformed.entrySet()) {
            depTreeStr = depTreeStr.replace(entry.getKey(), ANSI_YELLOW_BACKGROUND + ANSI_BLUE + entry.getKey() + " --> " + entry.getValue() + ANSI_RESET);
        }

        System.out.println("\n\n\n\ndepTreeStr:\n\n" + depTreeStr);
    }
    private OsCommandRunner getOsCommandRunner() {
        if(this.osCommandRunner == null){
            this.osCommandRunner = new OsCommandRunner();
        }
        return osCommandRunner;
    }

    private void getDependencyTree(Model model, org.apache.maven.model.Dependency dependency) throws DependencyResolutionException, DependencyCollectionException {

        System.out.println( "------------------------------------------------------------" );

        /*RepositorySystem system = Booter.newRepositorySystem( Booter.selectFactory( new String[]{SERVICE_LOCATOR} ) );

        RepositorySystemSession session = newRepositorySystemSession( system );

        Artifact artifact = new DefaultArtifact(
                String.format("%s:%s:%s", model.getGroupId(), model.getArtifactId(), model.getVersion())
        );

        CollectRequest collectRequest = new CollectRequest();
        collectRequest.setRoot( new Dependency( artifact, "" ) );
        collectRequest.setRepositories( Booter.newRepositories( system, session ) );

        CollectResult collectResult = system.collectDependencies( session, collectRequest );

        collectResult.getRoot().accept( new JavaxDependencyGraphDumper() );*/

    }

    private DefaultRepositorySystemSession newRepositorySystemSession( RepositorySystem system )
    {
        /*
        DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();

        //LocalRepository localRepo = new LocalRepository( "target/local-repo" );
        LocalRepository localRepo = new LocalRepository(System.getProperty("user.home") + "/.m2/repository");
        session.setLocalRepositoryManager( system.newLocalRepositoryManager( session, localRepo ) );

        session.setTransferListener( new ConsoleTransferListener() );
        session.setRepositoryListener( new ConsoleRepositoryListener() );

        // uncomment to generate dirty trees
        // session.setDependencyGraphTransformer( null );

        return session;*/
        return null;
    }
}
