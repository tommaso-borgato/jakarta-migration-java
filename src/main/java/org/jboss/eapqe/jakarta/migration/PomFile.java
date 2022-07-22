package org.jboss.eapqe.jakarta.migration;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.apache.maven.resolver.examples.util.Booter;
import org.apache.maven.resolver.examples.util.ConsoleRepositoryListener;
import org.apache.maven.resolver.examples.util.ConsoleTransferListener;
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
import org.jboss.eapqe.jakarta.migration.os.OsCommandResult;
import org.jboss.eapqe.jakarta.migration.os.OsCommandRunner;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.apache.maven.resolver.examples.util.Booter.SERVICE_LOCATOR;

/**
 * https://github.com/apache/maven-resolver/blob/master/maven-resolver-demos/maven-resolver-demo-snippets/src/main/java/org/apache/maven/resolver/examples/GetDependencyTree.java
 */
public class PomFile {

    private final static List<String> dependencies = Arrays.asList(
            "javax.ejb",
            "javax.activation",
            "javax.annotation",
            "javax.annotation.security",
            "javax.annotation.sql",
            "javax.batch.api",
            "javax.batch.operations",
            "javax.batch.runtime",
            "javax.decorator",
            "javax.el",
            "javax.enterprise", // added by me, not in javadoc
            "javax.enterprise.concurrent",
            "javax.enterprise.context",
            "javax.enterprise.context.control",
            "javax.enterprise.context.spi",
            "javax.enterprise.event",
            "javax.enterprise.inject",
            "javax.enterprise.inject.literal",
            "javax.enterprise.inject.se",
            "javax.enterprise.inject.spi",
            "javax.enterprise.inject.spi.configurator",
            "javax.enterprise.util",
            "javax.faces",
            "javax.inject",
            "javax.interceptor",
            "javax.jms",
            "javax.json",
            "javax.jws",
            "javax.mail",
            "javax.persistence",
            "javax.resource",
            "javax.security.auth.message",
            "javax.security.enterprise",
            "javax.security.jacc",
            "javax.servlet",
            "javax.transaction",
            "javax.validation",
            "javax.websocket",
            "javax.ws.rs",
            "javax.xml.bind",
            "javax.xml.soap",
            "javax.xml.ws"
    );

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
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = reader.read(new FileReader(path.toFile()));
        System.out.println(model.getId());
        System.out.println(model.getGroupId());
        System.out.println(model.getArtifactId());
        System.out.println(model.getVersion());
        //getDependencyTree(model, null);

        printDepTree();

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
        System.out.println(depTreeStr);
        for (String dep : dependencies) {
            if (depTreeStr.contains(dep)) {
                System.out.println("dependency found: " + dep);
            }
        }
    }
    private OsCommandRunner getOsCommandRunner() {
        if(this.osCommandRunner == null){
            this.osCommandRunner = new OsCommandRunner();
        }
        return osCommandRunner;
    }

    private void getDependencyTree(Model model, org.apache.maven.model.Dependency dependency) throws DependencyResolutionException, DependencyCollectionException {

        System.out.println( "------------------------------------------------------------" );

        RepositorySystem system = Booter.newRepositorySystem( Booter.selectFactory( new String[]{SERVICE_LOCATOR} ) );

        RepositorySystemSession session = newRepositorySystemSession( system );

        Artifact artifact = new DefaultArtifact(
                String.format("%s:%s:%s", model.getGroupId(), model.getArtifactId(), model.getVersion())
        );

        CollectRequest collectRequest = new CollectRequest();
        collectRequest.setRoot( new Dependency( artifact, "" ) );
        collectRequest.setRepositories( Booter.newRepositories( system, session ) );

        CollectResult collectResult = system.collectDependencies( session, collectRequest );

        collectResult.getRoot().accept( new JavaxDependencyGraphDumper() );

    }

    private DefaultRepositorySystemSession newRepositorySystemSession( RepositorySystem system )
    {
        DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();

        //LocalRepository localRepo = new LocalRepository( "target/local-repo" );
        LocalRepository localRepo = new LocalRepository(System.getProperty("user.home") + "/.m2/repository");
        session.setLocalRepositoryManager( system.newLocalRepositoryManager( session, localRepo ) );

        session.setTransferListener( new ConsoleTransferListener() );
        session.setRepositoryListener( new ConsoleRepositoryListener() );

        // uncomment to generate dirty trees
        // session.setDependencyGraphTransformer( null );

        return session;
    }
}
