package ASTVisitor.Scripting;

import java.io.*;

public class UnixScript implements Script {

    private String absPath;

    @Override
    public void compileCFile(String fileName) throws IOException {
        String command = "gcc -L./Lib -l eziotlib_unix -lcjson -lcurl " + fileName;
        File tempScript = createTempScript(command);
        absPath = System.getProperty("user.dir");

        sendCommand(tempScript);
    }

    @Override
    public void runProgram() throws IOException {
        File tempScript = createTempScript("osascript -e 'tell app \"Terminal\"\n do script \"cd " + absPath + "&& ./a.out && rm a.out\"\n end tell'");

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
