package org.jboss.eapqe.jakarta.migration.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransformedModules {
    public static void main (String[] args) throws IOException {
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

