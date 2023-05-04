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
        System.out.println(s);
        sendCommand(createTempScript(
                "gcc --version"//"gcc -L./Lib -l Eziotlib " + fileName.replace("\\", "\\\\")
        ));
    }

    @Override
    public void runProgram() throws IOException {

        sendCommand(createTempScript("a.exe"));
    }


    public void sendCommand(File file) {
        try {
            ProcessBuilder pb = new ProcessBuilder(file.toString());
            pb.inheritIO();
            Process process = pb.start();
            process.waitFor();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        } finally {
            file.delete();
        }
    }
}
