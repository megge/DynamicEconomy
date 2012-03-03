package me.ksafin.DynamicEconomy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

public class Utility {

    private File log;
    private static Logger logger = Logger.getLogger("Minecraft");
    private PrintStream writer;
    private DynamicEconomy plugin;
    private static Utility instance = new Utility();
    private SimpleDateFormat dateFormat = new SimpleDateFormat();

    private Utility() {

    }

    public static Utility getInstance() {
	return instance;
    }

    public boolean initialize(File logFile, DynamicEconomy plug) {
	log = logFile;
	plugin = plug;

	File folder = new File(plugin.getDataFolder().getPath());
	if (!folder.exists())
	    folder.mkdir();

	try {
	    if (!log.exists())
		log.createNewFile();
	} catch (IOException ioe) {
	    logger.info("[DynamicEconomy] Exception creating log.txt");
	}

	try {
	    writer = new PrintStream(new FileOutputStream(log, true), true);
	} catch (Exception e) {
	    logger.info("[DynamicEconomy] Error creating stream to log.txt");
	    e.printStackTrace();
	    return false;
	}
	return true;
    }

    // public static int[] decodeCoordinates(String stringCoords) {
    // String[] split = stringCoords.split(" ");
    // int[] intCoords = new int[3];
    // for (int x = 0; x < 3; x++)
    // intCoords[x] = Integer.parseInt(split[x]);
    // return intCoords;
    // }
    //
    // public static String encodeCoordinates(int[] coordsArray) {
    // String coords = "";
    // for (int x = 0; x < 3; x++)
    // if (x < 2)
    // coords += coordsArray[x] + " ";
    // else
    // coords += coordsArray[x];
    // return coords;
    // }

    public void writeToLog(String message) {
	if (DynamicEconomy.logwriting)
	    try {
		writer.println("[" + dateFormat.format(new Date()) + "] " + message);
	    } catch (Exception e) {
		logger.info("[DynamicEconomy] Exception writing to log.txt");
		e.printStackTrace();
	    }
    }

    public void disable() {
	writer.close();
    }
}
