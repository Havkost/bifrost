package ASTVisitor.Scripting;

import java.io.File;
import java.io.IOException;

public class WindowsScript implements Script {
    @Override
    public void compileCFile(String fileName) {

    }

    @Override
    public void runProgram() {

    }

    @Override
    public void sendCommand(File tempScript) {
        try {
            ProcessBuilder pb = new ProcessBuilder("bash", tempScript.toString());
            pb.inheritIO();
            Process process = pb.start();
            process.waitFor();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        } finally {
            tempScript.delete();
        }
    }
}
