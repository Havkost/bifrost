package ASTVisitor.Scripting;

import java.io.File;
import java.io.IOException;

public class WindowsScript implements Script {
    @Override
    public void compileCFile(String fileName) throws IOException {
        sendCommand(createTempScript("gcc -L./Lib -l eziotlib " + fileName));
    }

    @Override
    public void runProgram() throws IOException {

        sendCommand(createTempScript("a.exe"));
    }

    @Override
    public void sendCommand(File tempScript) {
        try {
            ProcessBuilder pb = new ProcessBuilder("cmd", tempScript.toString());
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
