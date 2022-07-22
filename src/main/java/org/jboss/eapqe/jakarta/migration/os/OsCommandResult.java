package org.jboss.eapqe.jakarta.migration.os;

public class OsCommandResult {
    public Integer exitCode = -1;
    public String output = "";

    public OsCommandResult() {
    }

    public Integer getExitCode() {
        return exitCode;
    }

    public void setExitCode(Integer exitCode) {
        this.exitCode = exitCode;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }
}
