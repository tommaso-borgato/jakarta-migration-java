package org.jboss.eapqe.jakarta.migration;

import org.jboss.eapqe.jakarta.migration.process.ProcessFiles;
import picocli.CommandLine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Entry point
 */
@CommandLine.Command(name = "JavaxToJakarta", version = "JavaxToJakarta 1.0", mixinStandardHelpOptions = true)
public class JavaxToJakarta implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(JavaxToJakarta.class);

    @CommandLine.Option(names = {"-dir", "--directory"}, description = "Maven Project Root Directory", required = true)
    public static String directory;

    @CommandLine.Option(
            names = {"-dep", "--dependencies"},
            description = "Analyze dependencies YES/NO",
            arity = "0..1"
    )
    public static boolean analyzeDependencies;

    @CommandLine.Option(
            names = {"-java", "--java-sources"},
            description = "Process Java source files YES/NO",
            arity = "0..1"
    )
    public static boolean processJavaSources;

    @CommandLine.Option(
            names = {"-xmlns", "--xml-schema-namespaces"},
            description = "Process XML Schema Namespaces and properties YES/NO",
            arity = "0..1"
    )
    public static boolean processXmlSchemaNamespaces;

    /**
     * Process all files in the projects root directory according to the options specifies as command line arguments
     */
    @Override
    public void run() {
        Path startingDir = Paths.get(directory);

        if (!startingDir.toFile().exists() || !startingDir.toFile().isDirectory()) {
            LOGGER.error("Invalid input directory {}", startingDir.toFile().isDirectory());
        }

        LOGGER.info("analyzeDependencies: {}", analyzeDependencies);
        LOGGER.info("processJavaSources: {}", processJavaSources);
        LOGGER.info("processXmlSchemaNamespaces: {}", processXmlSchemaNamespaces);

        ProcessFiles fileProcessor = new ProcessFiles();
        try {
            LOGGER.info("Processing directory {}", startingDir.toFile().getAbsolutePath());
            Files.walkFileTree(startingDir, fileProcessor);
        } catch (IOException e) {
            throw new RuntimeException("Error processing files in directory " + directory, e);
        }
    }

    /**
     * Entry point
     * @param args: see CommandLine options above
     */
    public static void main(String[] args) {
        int exitCode = new CommandLine(new JavaxToJakarta()).execute(args);
        System.exit(exitCode);
    }

}
