package ASTVisitor.Scripting;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WindowsScript implements Script {
    @Override
    public void compileCFile(String fileName) throws IOException {
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        sendCommand(createTempScript(
                "gcc " + fileName.replace("\\", "/") + " -L./Lib -leziotlib"), false
        );
    }

    @Override
    public void runProgram() throws IOException {

        sendCommand(createTempScript("a.exe"), true);
    }


    public void sendCommand(File inputFile, boolean show) {
        try {
            ProcessBuilder pb = new ProcessBuilder(inputFile.toString());
            if(!show) {
                pb.redirectOutput();
            }
            else {
                pb.inheritIO();
            }
            Process process = pb.start();
            process.waitFor();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        } finally {
            inputFile.delete();
        }
    }
}
