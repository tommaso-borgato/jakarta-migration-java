package org.jboss.eapqe.jakarta.migration.os;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.exec.InvalidExitValueException;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.ProcessResult;
import org.zeroturnaround.exec.stream.LogOutputStream;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * Utility for executing Operative System commands
 */
public class OsCommandRunner {
    private static final Logger LOG = LoggerFactory.getLogger(OsCommandRunner.class.getName());

    public OsCommandRunner() {
    }

    public OsCommandResult execOsCommand (String... command) throws IOException, InterruptedException {
        if (command==null || command.length==0){
            throw new IllegalArgumentException("No command to execute!");
        }
        return execOsCommand(180, command);
    }

    /**
     * Used to run synchronous commands
     * @param timeout
     * @param command
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public OsCommandResult execOsCommand (Integer timeout, String... command) throws IOException, InterruptedException {
        OsCommandResult commandResult = new OsCommandResult();
        LOG.trace("\n\nrunning command = {}\n", Arrays.stream(command).collect(Collectors.joining( " " )));
        final StringBuilder output = new StringBuilder();
        try {
            ProcessResult processResult = new ProcessExecutor().command(command)
                    .readOutput(true)
                    .redirectOutput(new LogOutputStream() {
                        @Override
                        protected void processLine(String line) {
                            output.append(line).append("\n");
                            LOG.debug(line);
                        }
                    })
                    .redirectError(new LogOutputStream() {
                        @Override
                        protected void processLine(String line) {
                            output.append(line).append("\n");
                            LOG.error(line);
                        }
                    })
                    .timeout(timeout, TimeUnit.SECONDS)
                    .execute();
            commandResult.exitCode = processResult.getExitValue();
            commandResult.output = output.toString().replaceFirst("\n$","");
        }
        catch (InvalidExitValueException e) {
            LOG.error("Process exited with {}", e.getExitValue());
            commandResult.exitCode = e.getResult().getExitValue();
            commandResult.output = e.getResult().outputUTF8();
        } catch (TimeoutException e) {
            LOG.error("Process {} SECONDS time out expired", timeout);
            commandResult.exitCode = -1;
            commandResult.output = output.toString().replaceFirst("\n$","");
        }

        LOG.debug("\n\ncommand = {}\nexitCode = {}\noutput = {}\n",
                Arrays.stream(command).collect(Collectors.joining( " " )),
                commandResult.exitCode,
                commandResult.output);

        return commandResult;
    }

    /**
     * Used to start WildFly
     * @param command
     * @return
     * @throws IOException
     */
    public Future<ProcessResult> execBackgroundOsCommand(String... command) throws IOException {
        LOG.debug("\n\nrunning command = {}\n", Arrays.stream(command).collect(Collectors.joining( " " )));
        Future<ProcessResult> future = new ProcessExecutor()
                .command(command)
                .readOutput(true)
                .redirectOutput(new LogOutputStream() {
                    @Override
                    protected void processLine(String line) {
                        LOG.debug(line);
                    }
                })
                .redirectError(new LogOutputStream() {
                    @Override
                    protected void processLine(String line) {
                        LOG.error(line);
                    }
                })
                .start()
                .getFuture();
        return future;
    }
}
