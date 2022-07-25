package org.jboss.eapqe.jakarta.migration.process;

import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.graph.DependencyVisitor;
import org.eclipse.aether.util.artifact.ArtifactIdUtils;
import org.eclipse.aether.util.graph.manager.DependencyManagerUtils;
import org.eclipse.aether.util.graph.transformer.ConflictResolver;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class JavaxDependencyGraphDumper implements DependencyVisitor  {

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

    private final PrintStream out;

    private final List<JavaxDependencyGraphDumper.ChildInfo> childInfos = new ArrayList<>();

    public JavaxDependencyGraphDumper()
    {
        this( null );
    }

    public JavaxDependencyGraphDumper( PrintStream out )
    {
        this.out = ( out != null ) ? out : System.out;
    }

    public boolean visitEnter( DependencyNode node )
    {
        out.println( formatIndentation() + formatNode( node ) );
        childInfos.add( new JavaxDependencyGraphDumper.ChildInfo( node.getChildren().size() ) );
        return true;
    }

    private String formatIndentation()
    {
        StringBuilder buffer = new StringBuilder( 128 );
        for (Iterator<JavaxDependencyGraphDumper.ChildInfo> it = childInfos.iterator(); it.hasNext(); )
        {
            buffer.append( it.next().formatIndentation( !it.hasNext() ) );
        }
        return buffer.toString();
    }

    private String formatNode( DependencyNode node )
    {
        StringBuilder buffer = new StringBuilder( 128 );
        Artifact a = node.getArtifact();
        Dependency d = node.getDependency();
        buffer.append( a );
        if ( d != null && d.getScope().length() > 0 )
        {
            buffer.append( " [" ).append( d.getScope() );
            if ( d.isOptional() )
            {
                buffer.append( ", optional" );
            }
            buffer.append( "]" );
        }
        String premanaged = DependencyManagerUtils.getPremanagedVersion( node );
        if ( premanaged != null && !premanaged.equals( a.getBaseVersion() ) )
        {
            buffer.append( " (version managed from " ).append( premanaged ).append( ")" );
        }

        premanaged = DependencyManagerUtils.getPremanagedScope( node );
        if ( premanaged != null && !premanaged.equals( d.getScope() ) )
        {
            buffer.append( " (scope managed from " ).append( premanaged ).append( ")" );
        }
        DependencyNode winner = (DependencyNode) node.getData().get( ConflictResolver.NODE_DATA_WINNER );
        if ( winner != null && !ArtifactIdUtils.equalsId( a, winner.getArtifact() ) )
        {
            Artifact w = winner.getArtifact();
            buffer.append( " (conflicts with " );
            if ( ArtifactIdUtils.toVersionlessId( a ).equals( ArtifactIdUtils.toVersionlessId( w ) ) )
            {
                buffer.append( w.getVersion() );
            }
            else
            {
                buffer.append( w );
            }
            buffer.append( ")" );
        }

        for (String dep : dependencies) {

            if (node.getArtifact().getArtifactId().contains(dep) || node.getArtifact().getGroupId().contains(dep)){
                return ANSI_YELLOW_BACKGROUND + ANSI_RED + buffer.toString() + ANSI_RESET + " <=================";
            }
        }

        return buffer.toString();
    }

    public boolean visitLeave( DependencyNode node )
    {
        if ( !childInfos.isEmpty() )
        {
            childInfos.remove( childInfos.size() - 1 );
        }
        if ( !childInfos.isEmpty() )
        {
            childInfos.get( childInfos.size() - 1 ).index++;
        }
        return true;
    }

    private static class ChildInfo
    {

        final int count;

        int index;

        ChildInfo( int count )
        {
            this.count = count;
        }

        public String formatIndentation( boolean end )
        {
            boolean last = index + 1 >= count;
            if ( end )
            {
                return last ? "\\- " : "+- ";
            }
            return last ? "   " : "|  ";
        }

    }

}
