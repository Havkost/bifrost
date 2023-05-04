package ASTVisitor.Scripting;

import java.io.*;

public interface Script {
    public void compileCFile(String fileName) throws IOException;
    public void runProgram() throws IOException;
    public void sendCommand(File tempScript);

    public default File createTempScript(String command) throws IOException {
        File tempScript = File.createTempFile("script", null);

        Writer streamWriter = new OutputStreamWriter(new FileOutputStream(
                tempScript));
        PrintWriter printWriter = new PrintWriter(streamWriter);

        printWriter.println(command);

        printWriter.close();

        return tempScript;
    }
}
