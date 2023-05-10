package ASTVisitor.Scripting;

import java.io.*;

public class UnixScript implements Script {

    @Override
    public void compileCFile(String fileName) throws IOException {
        String command = "gcc -L./Lib -l eziotlib_unix -lcjson -lcurl " + fileName;
        File tempScript = createTempScript(command);

        sendCommand(tempScript);
    }

    @Override
    public void runProgram() throws IOException {
        File tempScript = createTempScript("./a.out && rm a.out");

        sendCommand(tempScript);
    }

    //@Override
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
