package org.jboss.eapqe.jakarta.migration;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.apache.maven.resolver.examples.util.Booter;
import org.apache.maven.resolver.examples.util.ConsoleDependencyGraphDumper;
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
import org.eclipse.aether.graph.DependencyFilter;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.resolution.DependencyRequest;
import org.eclipse.aether.resolution.DependencyResolutionException;
import org.eclipse.aether.util.artifact.JavaScopes;
import org.eclipse.aether.util.filter.DependencyFilterUtils;

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



    private Path path;

    public PomFile(Path path) {
        this.path = path;
    }

    public void process() throws IOException, XmlPullParserException, DependencyCollectionException, DependencyResolutionException {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = reader.read(new FileReader(path.toFile()));
        System.out.println(model.getId());
        System.out.println(model.getGroupId());
        System.out.println(model.getArtifactId());
        System.out.println(model.getVersion());
        getDependencyTree(model, null);

        /*for (org.apache.maven.model.Dependency dependency : model.getDependencies()){
            getDependencyTree(model, dependency);
        }*/

        // https://github.com/apache/maven-resolver/blob/master/maven-resolver-demos/maven-resolver-demo-snippets/src/main/java/org/apache/maven/resolver/examples/GetDependencyTree.java
    }

    private void getDependencyTree(Model model, org.apache.maven.model.Dependency dependency) throws DependencyResolutionException, DependencyCollectionException {

        System.out.println( "------------------------------------------------------------" );

        RepositorySystem system = Booter.newRepositorySystem( Booter.selectFactory( new String[]{SERVICE_LOCATOR} ) );

        RepositorySystemSession session = newRepositorySystemSession( system );

        Artifact artifact = new DefaultArtifact(
                String.format("%s:%s:%s", model.getGroupId(), model.getArtifactId(), model.getVersion())
        );

        DependencyFilter classpathFlter = DependencyFilterUtils.classpathFilter( JavaScopes.COMPILE );

        CollectRequest collectRequest = new CollectRequest();
        collectRequest.setRoot( new Dependency( artifact, JavaScopes.COMPILE ) );
        collectRequest.setRepositories( Booter.newRepositories( system, session ) );

        DependencyRequest dependencyRequest = new DependencyRequest( collectRequest, classpathFlter );

        CollectResult collectResult = system.collectDependencies( session, collectRequest );

        collectResult.getRoot().accept( new JavaxDependencyGraphDumper() );


        /*List<ArtifactResult> artifactResults =
                system.resolveDependencies( session, dependencyRequest ).getArtifactResults();

        for ( ArtifactResult artifactResult : artifactResults )
        {
            System.out.println( "\n\n" + artifactResult.getArtifact() + " resolved to "
                    + artifactResult.getArtifact().getFile() );
        }*/

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
