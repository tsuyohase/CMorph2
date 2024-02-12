package cmorph.utils;

import java.io.*;
import java.util.HashMap;

/**
 * Basic logger class
 */
public class BasicLogger implements Closeable {
    protected PrintWriter writer = null;

    /** Store loggers */
    private static HashMap<String, BasicLogger> namedLoggers = new HashMap<>();

    /**
     * Cannot be instanced by the constructor directly. Use BasicLogger.getLogger.
     */
    private BasicLogger() {
        // this.writer = new PrintWriter(new BufferedWriter(new
        // OutputStreamWriter(System.out)));
    }

    /** Returns logger instance by name */
    public static BasicLogger getLogger(String name) {
        BasicLogger logger = namedLoggers.get(name);
        if (logger == null) {
            logger = new BasicLogger();
            namedLoggers.put(name, logger);
        }
        return logger;
    }

    /** Change writer */
    public void setWriter(PrintWriter writer) {
        if (this.writer != null) {
            this.writer.close();
        }
        this.writer = writer;
    }

    public void setFileWriter(File outputFile) throws IOException {
        setWriter(new PrintWriter(new BufferedWriter(new FileWriter(outputFile))));
    }

    public void log(String message) {
        print(message);
    }

    public void print(String message) {
        if (this.writer != null) {
            this.writer.print(message);
            this.writer.flush();
        } else {
            System.out.print(message);
        }
    }

    public void println(String message) {
        if (this.writer != null) {
            this.writer.println(message);
        } else {
            System.out.println(message);
        }
    }

    public void close() {
        this.writer.close();
    }
}
