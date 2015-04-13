package sasc.smartcard.common;

import sasc.terminal.CardConnection;
import sasc.terminal.TerminalException;
import sasc.terminal.TerminalUtil;
import sasc.util.Log;

import java.io.*;

/**
 * Utility class used for running a pre-determined set of APDU's from a file.
 */
public class APDURunner {

    FileReader commandFile;

    public APDURunner(String fileName) throws FileNotFoundException {
        if (fileName == null || fileName.isEmpty()) {
            throw new FileNotFoundException("CommandFile cannot be blank.");
        } else {
            commandFile = new FileReader(fileName);
        }
    }

    public void start() {

        CardConnection cardConnection = null;

        try {
            cardConnection = TerminalUtil.connect(TerminalUtil.State.CARD_INSERTED);
            BufferedReader bufferedReader = new BufferedReader(commandFile);
            for (String line; (line = bufferedReader.readLine()) != null;) {
                Log.debug("APDU: " + line);
            }
        } catch (TerminalException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
