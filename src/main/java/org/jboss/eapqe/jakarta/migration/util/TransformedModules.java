package org.jboss.eapqe.jakarta.migration.util;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.nio.file.FileVisitResult.CONTINUE;

/**
 * class for one-off tasks
 */
public class TransformedModules {

    public static class ProcessPoms extends SimpleFileVisitor<Path> {
        @Override
        public FileVisitResult visitFile(Path file,
                                         BasicFileAttributes attr) throws IOException {
            if (attr.isRegularFile()) {
                if (file.toFile().getName().equalsIgnoreCase("pom.xml")) {
                    //LOGGER.info("Processing pom.xml file {}", file.toFile().getAbsolutePath());
                    MavenXpp3Reader reader = new MavenXpp3Reader();
                    Model model = null;
                    try {
                        model = reader.read(new FileReader(file.toFile()));
                        //System.out.println(model.getId());
                        /*System.out.println(
                                (model.getGroupId() == null ? model.getParent().getGroupId() : model.getGroupId() )
                                        + ":" + model.getArtifactId()
                                        + ":" + (model.getVersion() == null ? model.getParent().getVersion() : model.getVersion()));*/
                        /*System.out.println( "transformed.put(\"" + model.getArtifactId().replaceFirst("-jakarta$","") + "\",\"" +
                                (model.getGroupId() == null ? model.getParent().getGroupId() : model.getGroupId() )
                                        + ":" + model.getArtifactId()
                                        + "\");");*/
                        System.out.println( model.getArtifactId().replaceFirst("-jakarta$","") + " --> " +
                                (model.getGroupId() == null ? model.getParent().getGroupId() : model.getGroupId() )
                                + ":" + model.getArtifactId());
                    } catch (XmlPullParserException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
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

    private static final Logger LOGGER = LoggerFactory.getLogger(TransformedModules.class);

    public static void main (String[] args) throws IOException {
        Path startingDir = Paths.get("/home/tborgato/projects/wildfly/ee-9/source-transform");

        ProcessPoms fileProcessor = new ProcessPoms();
        LOGGER.info("Processing directory {}", startingDir.toFile().getAbsolutePath());
        Files.walkFileTree(startingDir, fileProcessor);
    }

    public static void main2 (String[] args) throws IOException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classloader.getResourceAsStream("transformed-modules/transformed-modules.txt");

        InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamReader);

        String replaced = null;
        String replacedRegexp = "^\\-.*<artifactId>([^<]+)</artifactId>";
        Pattern replacedPattern = Pattern.compile(replacedRegexp);

        String replacer = null;
        String replacerRegexp = "^\\+.*<artifactId>([^<]+)</artifactId>";
        Pattern replacerPattern = Pattern.compile(replacerRegexp);

        Map<String,String> transformed = new HashMap<>();

        long lines = 0;
        long lineReplaced = 0;
        long lineReplacer = 0;
        for (String line; (line = reader.readLine()) != null;) {
            // -            <artifactId>wildfly-ee</artifactId>
            //+            <artifactId>wildfly-ee-jakarta</artifactId>
            Matcher m = replacedPattern.matcher(line);
            if (m.find()) {
                replaced = m.group(1);
                lineReplaced = lines;
            }
            m = replacerPattern.matcher(line);
            if (m.find()) {
                replacer = m.group(1);
                lineReplacer = lines;
            }
            if (lineReplacer == (lineReplaced+1) && replacer.contains("jakarta")) {
                //System.out.println(replaced + " --> " + replacer);
                transformed.put(replaced, replacer);
            }

            lines++;
        }

        for (Map.Entry<String, String> entry : transformed.entrySet()) {
            //System.out.println("\"" + entry.getKey() + "\",\"" + entry.getValue()+ "\", ");
            System.out.println("transformed.put(\"" + entry.getKey() + "\",\"" + entry.getValue() + "\");");
        }

        System.out.println(lines);
    }
}

