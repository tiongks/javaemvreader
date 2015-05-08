package sasc.smartcard.common;

import sasc.emv.EMVUtil;
import sasc.terminal.CardConnection;
import sasc.terminal.CardResponse;
import sasc.terminal.TerminalException;
import sasc.terminal.TerminalUtil;
import sasc.util.Log;
import sasc.util.Util;

import java.io.*;

import org.bouncycastle.util.Strings;

/**
 * Utility class used for running a pre-determined set of APDU's from a file.
 */
public class APDURunner {

    InputStreamReader commandFile;

    public APDURunner(String fileName) throws FileNotFoundException {
        if (fileName == null || fileName.isEmpty()) {
            throw new FileNotFoundException("CommandFile cannot be blank.");
        } else {
        	ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        	InputStream inputStream = classLoader.getResourceAsStream(fileName);
        	if (inputStream == null) {
        		throw new FileNotFoundException("Unable to load file: " + fileName);
        	}
            commandFile = new InputStreamReader(inputStream);
        }
    }

    public void start() {

        CardConnection cardConnection = null;

        try {
            cardConnection = TerminalUtil.connect(TerminalUtil.State.CARD_PRESENT);
            BufferedReader bufferedReader = new BufferedReader(commandFile);
            long startTime = System.currentTimeMillis();
            for (String line; (line = bufferedReader.readLine()) != null;) {
                byte[] apdu = Util.fromHexString(line);
                CardResponse response = EMVUtil.sendCmd(cardConnection, apdu);
            }
            long endTime = System.currentTimeMillis();
            Log.debug("Total Execution time: " + (endTime - startTime) + "ms");
            cardConnection.disconnect(true);
        } catch (TerminalException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
