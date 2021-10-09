package de.discord.core;

import de.discord.manage.managers.RadioManager;
import de.discord.manage.managers.RadioStation;
import de.discord.manage.sql.LiteSQL;
import de.discord.music.LoadChannel;
import net.dv8tion.jda.api.OnlineStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleCommands {

    private boolean shutdown = false;

    public ConsoleCommands() {

        new Thread(() -> {

            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                while(!shutdown) {
                    line = reader.readLine();
                    if (line.equalsIgnoreCase("stop") || line.equalsIgnoreCase("kill")) {
                        shutdown = true;
                        if (RadioBot.INSTANCE.shardMan != null) {
                            //StatschannelCommand.onShutdown();
                            System.out.println("[main] INFO Saving Channel Data.");
                            LoadChannel.stop();
                            System.out.println("[main] INFO Completed saving.");

                            RadioBot.INSTANCE.shardMan.setStatus(OnlineStatus.OFFLINE);
                            RadioBot.INSTANCE.shardMan.shutdown();
                            LiteSQL.disconnect();
                            System.out.println("[main] INFO "+ RadioBot.INSTANCE.getName() +" offline.");
                        }
                        RadioBot.INSTANCE.timer.cancel();
                        reader.close();


                    } else if (line.equalsIgnoreCase("radio")) {

                        System.out.print("[RADIO] INFO Remove/Add/speedadd/getspeed: ");

                        String option = reader.readLine();

                        if (option.equalsIgnoreCase("add")) {

                            boolean success = false;

                            System.out.print("[RADIO] INFO command name: ");

                            String name = reader.readLine();

                            if (isStop(name)) {

                                System.out.print("[RADIO] INFO audio url: ");

                                String url = reader.readLine();

                                if (isStop(url)) {

                                    System.out.print("[RADIO] INFO title: ");

                                    String title = reader.readLine();

                                    if (isStop(title)) {

                                        System.out.println("[RADIO] INFO Germany, Britain, USA, Spain, Other");

                                        System.out.print("[RADIO] INFO country: ");

                                        String country = reader.readLine();

                                        if (RadioManager.countryMap.containsKey(country.toLowerCase())) {

                                            RadioStation.Country c = RadioManager.countryMap.get(country.toLowerCase());

                                            RadioBot.INSTANCE.radioMan.addRadio(name, url, title, c);

                                            System.out.println("[RADIO] INFO Added " + name);

                                            success = true;
                                        }
                                    }
                                }
                            }

                            if (!success) {
                                System.out.println("[RADIO] WARN Es wurden Fehler begangen.");
                            }

                        } else if (option.equalsIgnoreCase("remove")) {

                            System.out.print("[RADIO] INFO command name: ");

                            String name = reader.readLine();

                            if (RadioBot.INSTANCE.radioMan.radios.containsKey(name)) {

                                System.out.print("[RADIO] INFO You sure?: ");

                                String s = reader.readLine();

                                if (s.equalsIgnoreCase("y") || s.equalsIgnoreCase("yes") || s.equalsIgnoreCase("j") || s.equalsIgnoreCase("ja")) {

                                    RadioBot.INSTANCE.radioMan.removeRadio(name);

                                    System.out.println("[RADIO] INFO Deleted " + name);

                                }

                            } else {
                                System.out.println("[main] INFO Command doesn't exist");
                            }

                        } else if (option.equalsIgnoreCase("speedadd")) {

                            System.out.println("[RADIO] INFO Use: name|audiourl|title|Country");

                            String s = reader.readLine();

                            String[] args = s.split("\\|");

                            if (args.length == 4) {


                                RadioStation.Country country = RadioManager.countryMap.get(args[3].toLowerCase());

                                if (country != null) {

                                    RadioBot.INSTANCE.radioMan.addRadio(args[0], args[1], args[2], country);
                                    System.out.println("[RADIO] INFO Added " + args[0]);

                                } else {
                                    System.out.println("[RADIO] WARN Invalid country.");
                                }
                            } else {
                                System.out.println("[RADIO] WARN Invalid Syntax.");
                            }


                        } else if (option.equalsIgnoreCase("getspeed")) {

                            System.out.print("[RADIO] INFO Which radio station: ");

                            String s = reader.readLine();

                            RadioStation station = RadioBot.INSTANCE.radioMan.radios.get(s);

                            if (station != null) {
                                System.out.println(station.name + "|" + station.url + "|" + station.title + "|" + station.country);
                            } else {
                                System.out.println("[RADIO] ERROR Radio not found.");
                            }

                        } else {
                            System.out.println("[RADIO] WARN Wrong argument.");
                        }

                    } else {
                        System.out.println("[main] INFO Commands:");
                        System.out.println("\t-stop");
                        System.out.println("\t-radio");
                    }
                }
            }catch (IOException e) {
                e.printStackTrace();
            }

        }).start();

    }

    private boolean isStop(String s) {
        return !s.equalsIgnoreCase("stop");
    }

}
