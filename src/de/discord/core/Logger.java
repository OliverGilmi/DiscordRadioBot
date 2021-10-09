package de.discord.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.text.DateFormat;
import java.time.Instant;
import java.util.Date;

public class Logger {

    private PrintStream stream;

    public Logger() {

        try {

            DateFormat format = DateFormat.getDateTimeInstance();

            File errorTxt = new File("errors.txt");

            if (!errorTxt.exists()) {
                System.out.println(errorTxt.createNewFile() ? "[main] INFO errors.txt created." : "[main] WARN errors.txt wasn't created.");
            }

            File debugTxt = new File("debug.txt");

            if (!debugTxt.exists()) {
                System.out.println(debugTxt.createNewFile() ? "[main] INFO debug.txt created." : "[main] WARN debug.txt wasn't created.");
            }

            StringBuilder builder = new StringBuilder();

            Files.lines(errorTxt.toPath()).forEach(line -> builder.append(line).append("\n"));

            builder.append("\n\n-------------------------\n\n").append(format.format(new Date())).append("\n");


            PrintStream out = new PrintStream(new FileOutputStream(errorTxt));

            out.write(builder.toString().getBytes(), 0, builder.toString().getBytes().length);

            System.setErr(out);

            StringBuilder b = new StringBuilder();

            Files.lines(debugTxt.toPath()).forEach(line -> b.append(line).append("\n"));

            b.append("\n\n-------------------------\n\n").append(format.format(new Date())).append("\n");

            stream = new PrintStream(new FileOutputStream(debugTxt));

            stream.write(b.toString().getBytes(), 0, b.toString().getBytes().length);

            System.out.println("[main] INFO Logger setuped.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final DateFormat format = DateFormat.getDateTimeInstance();

    public void info(String text) {

        text = format.format(Date.from(Instant.now())) + " | Info: " + text + "\n";

        stream.write(text.getBytes(), 0, text.getBytes().length);
    }

    public void warning(String text) {

        text = format.format(Date.from(Instant.now())) + " Warning: " + text + "\n";

        stream.write(text.getBytes(), 0, text.getBytes().length);
    }
}