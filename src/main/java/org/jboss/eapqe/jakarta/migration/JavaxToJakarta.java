package org.jboss.eapqe.jakarta.migration;

import picocli.CommandLine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CommandLine.Command(name = "JavaxToJakarta", version = "JavaxToJakarta 1.0", mixinStandardHelpOptions = true)
public class JavaxToJakarta implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(JavaxToJakarta.class);

    @CommandLine.Option(names = {"-dir", "--directory"}, description = "Project Root Directory", required = true)
    String directory;

    @Override
    public void run() {
        Path startingDir = Paths.get(directory);

        if (!startingDir.toFile().exists() || !startingDir.toFile().isDirectory()) {
            LOGGER.error("Invalid input directory {}", startingDir.toFile().isDirectory());
        }

        ProcessFiles pf = new ProcessFiles();
        try {
            LOGGER.info("Processing directory {}", startingDir.toFile().getAbsolutePath());
            Files.walkFileTree(startingDir, pf);
        } catch (IOException e) {
            throw new RuntimeException("Error processing files in directory " + directory, e);
        }

    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new JavaxToJakarta()).execute(args);
        System.exit(exitCode);
    }

}
