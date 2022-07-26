package org.jboss.eapqe.jakarta.migration.process;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
//import org.apache.maven.resolver.examples.util.Booter;
//import org.apache.maven.resolver.examples.util.ConsoleRepositoryListener;
//import org.apache.maven.resolver.examples.util.ConsoleTransferListener;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.collection.CollectResult;
import org.eclipse.aether.collection.DependencyCollectionException;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.LocalRepository;
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

    static Map<String, String> transformed;
    static {
        transformed = new HashMap<>();
        transformed.put("ironjacamar-common-impl","ironjacamar-common-impl-jakarta");
        transformed.put("wildfly-mail","wildfly-mail-jakarta");
        transformed.put("wildfly-rts","wildfly-rts-jakarta");
        transformed.put("ironjacamar-deployers-common","ironjacamar-deployers-common-jakarta");
        transformed.put("openjdk-orb","openjdk-orb-jakarta");
        transformed.put("jboss-ejb-client","jboss-ejb-client-jakarta");
        transformed.put("wildfly-datasources-agroal","wildfly-datasources-agroal-jakarta");
        transformed.put("artemis-journal","artemis-jakarta-ra");
        transformed.put("jboss-iiop-client","jboss-iiop-client-jakarta");
        transformed.put("narayana-jts-integration","narayana-jts-integration-jakarta");
        transformed.put("jbossxts","jbossxts-jakarta");
        transformed.put("hornetq-jms-client","hornetq-jakarta-client");
        transformed.put("ironjacamar-core-api","ironjacamar-core-api-jakarta");
        transformed.put("wildfly-xts","wildfly-xts-jakarta");
        transformed.put("restat-bridge","restat-bridge-jakarta");
        transformed.put("jboss-transaction-spi","jboss-transaction-spi-jakarta");
        transformed.put("wildfly-microprofile-fault-tolerance-smallrye-executor","wildfly-microprofile-fault-tolerance-smallrye-executor-jakarta");
        transformed.put("compensations","compensations-jakarta");
        transformed.put("jboss-metadata-common","jboss-metadata-common-jakarta");
        transformed.put("wildfly-undertow","wildfly-undertow-jakarta");
        transformed.put("wildfly-clustering-singleton-extension","wildfly-testsuite-shared-jakarta");
        transformed.put("jbosstxbridge","jbosstxbridge-jakarta");
        transformed.put("wildfly-testsuite-shared","wildfly-testsuite-shared-jakarta");
        transformed.put("wildfly-mod_cluster-undertow","wildfly-mod_cluster-undertow-jakarta");
        transformed.put("wildfly-client-all","wildfly-client-all-jakarta");
        transformed.put("wildfly-microprofile-openapi-smallrye","wildfly-microprofile-openapi-smallrye-jakarta");
        transformed.put("wildfly-microprofile-fault-tolerance-smallrye-extension","wildfly-microprofile-fault-tolerance-smallrye-extension-jakarta");
        transformed.put("wildfly-microprofile-opentracing-smallrye","wildfly-microprofile-opentracing-smallrye-jakarta");
        transformed.put("wildfly-transactions","wildfly-transactions-jakarta");
        transformed.put("wildfly-iiop-openjdk","wildfly-iiop-openjdk-jakarta");
        transformed.put("ironjacamar-validator","ironjacamar-validator-jakarta");
        transformed.put("wildfly-weld","wildfly-weld-bean-validation-jakarta");
        transformed.put("wildfly-microprofile-opentracing-extension","wildfly-microprofile-opentracing-extension-jakarta");
        transformed.put("ironjacamar-core-impl","ironjacamar-core-impl-jakarta");
        transformed.put("wildfly-weld-bean-validation","wildfly-weld-common-jakarta");
        transformed.put("wildfly-bean-validation","wildfly-bean-validation-jakarta");
        transformed.put("wildfly-clustering-weld-ejb","wildfly-clustering-weld-ejb-jakarta");
        transformed.put("wildfly-weld-transactions","wildfly-weld-transactions-jakarta");
        transformed.put("wildfly-ee-security","wildfly-ee-security-jakarta");
        transformed.put("jboss-jakarta-xml-ws-api_3.0_spec","jboss-jakarta-xml-ws-api_4.0_spec");
        transformed.put("wildfly-weld-common","wildfly-weld-ejb-jakarta");
        transformed.put("wildfly-weld-jpa","wildfly-weld-jpa-jakarta");
        transformed.put("wildfly-clustering-weld-core","wildfly-clustering-weld-core-jakarta");
        transformed.put("wildfly-weld-ejb","wildfly-weld-jakarta");
        transformed.put("artemis-server","artemis-jakarta-service-extensions");
        transformed.put("wildfly-ejb3","wildfly-ejb3-jakarta");
        transformed.put("artemis-jdbc-store","artemis-jakarta-client");
        transformed.put("artemis-selector","artemis-jakarta-server");
        transformed.put("wildfly-jsf","wildfly-jsf-injection-jakarta");
        transformed.put("${wildfly-testsuite-shared.artifactId}","wildfly-testsuite-shared-jakarta");
        transformed.put("wildfly-clustering-web-undertow","wildfly-clustering-web-undertow-jakarta");
        transformed.put("wildfly-microprofile-health-smallrye","wildfly-microprofile-health-smallrye-jakarta");
        transformed.put("wildfly-http-transaction-client","wildfly-http-transaction-client-jakarta");
        transformed.put("wildfly-clustering-weld-web","wildfly-clustering-weld-web-jakarta");
        transformed.put("wildfly-ee","wildfly-ee-jakarta");
        transformed.put("artemis-ra","artemis-jakarta-ra");
        transformed.put("txframework","txframework-jakarta");
        transformed.put("ironjacamar-common-spi","ironjacamar-common-spi-jakarta");
        transformed.put("wildfly-microprofile-reactive-streams-operators-cdi-provider","wildfly-microprofile-reactive-streams-operators-cdi-provider-jakarta");
        transformed.put("artemis-service-extensions","artemis-jakarta-service-extensions");
        transformed.put("wildfly-jaxrs","wildfly-jaxrs-jakarta");
        transformed.put("wildfly-messaging-activemq-injection","wildfly-messaging-activemq-injection-jakarta");
        transformed.put("wildfly-web-common","wildfly-web-common-jakarta");
        transformed.put("ironjacamar-common-api","ironjacamar-common-api-jakarta");
        transformed.put("restat-integration","restat-integration-jakarta");
        transformed.put("jipijapa-hibernate5-3","jipijapa-eclipselink-jakarta");
        transformed.put("wildfly-batch-jberet","wildfly-batch-jberet-jakarta");
        transformed.put("wildfly-microprofile-jwt-smallrye","wildfly-microprofile-jwt-smallrye-jakarta");
        transformed.put("wildfly-connector","wildfly-connector-jakarta");
        transformed.put("wildfly-weld-spi","wildfly-weld-spi-jakarta");
        transformed.put("restat-api","restat-api-jakarta");
        transformed.put("jboss-metadata-ejb","jboss-metadata-ejb-jakarta");
        transformed.put("narayana-jts-idlj","narayana-jts-idlj-jakarta");
        transformed.put("ironjacamar-jdbc","ironjacamar-jdbc-jakarta");
        transformed.put("wildfly-jpa","wildfly-jpa-jakarta");
        transformed.put("wildfly-weld-webservices","wildfly-weld-webservices-jakarta");
        transformed.put("restat-util","restat-util-jakarta");
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
