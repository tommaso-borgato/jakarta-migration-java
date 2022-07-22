package org.jboss.eapqe.jakarta.migration;

import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.graph.DependencyVisitor;

public class JavaxDependencyGraphDumper implements DependencyVisitor  {

    @Override
    public boolean visitEnter(DependencyNode dependencyNode) {
        return false;
    }

    @Override
    public boolean visitLeave(DependencyNode dependencyNode) {
        return false;
    }
}
