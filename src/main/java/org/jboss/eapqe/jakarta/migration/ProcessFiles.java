package org.jboss.eapqe.jakarta.migration;

import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.eclipse.aether.collection.DependencyCollectionException;
import org.eclipse.aether.resolution.DependencyResolutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import static java.nio.file.FileVisitResult.CONTINUE;

public class ProcessFiles extends SimpleFileVisitor<Path> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessFiles.class);

    // Print information about
    // each type of file.
    @Override
    public FileVisitResult visitFile(Path file,
                                     BasicFileAttributes attr) throws IOException {
        if (attr.isSymbolicLink()) {
            //System.out.format("Symbolic link: %s ", file);
        } else if (attr.isRegularFile()) {
            //System.out.format("Regular file: %s ", file);
            if (file.toFile().getName().endsWith(".java")) {
                //LOGGER.info("Processing java file {}", file.toFile().getAbsolutePath());
                JavaSourceFile javaSourceFile = new JavaSourceFile(file);
                javaSourceFile.process();
            } if (file.toFile().getName().equalsIgnoreCase("pom.xml")) {
                LOGGER.info("Processing pom.xml file {}", file.toFile().getAbsolutePath());
                PomFile pomFile = new PomFile(file);
                try {
                    pomFile.process();
                } catch (XmlPullParserException e) {
                    throw new RuntimeException(e);
                } catch (DependencyCollectionException e) {
                    throw new RuntimeException(e);
                } catch (DependencyResolutionException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            //System.out.format("Other: %s ", file);
        }
        //System.out.println("(" + attr.size() + "bytes)");
        return CONTINUE;
    }

    // Print each directory visited.
    @Override
    public FileVisitResult postVisitDirectory(Path dir,
                                              IOException exc) {
        //System.out.format("Directory: %s%n", dir);
        return CONTINUE;
    }

    // If there is some error accessing
    // the file, let the user know.
    // If you don't override this method
    // and an error occurs, an IOException
    // is thrown.
    @Override
    public FileVisitResult visitFileFailed(Path file,
                                           IOException exc) {
        System.err.println(exc);
        return CONTINUE;
    }
}
