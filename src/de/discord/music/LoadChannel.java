package de.discord.music;

import de.discord.core.RadioBot;
import de.discord.listener.LeaveListener;
import de.discord.manage.managers.RadioStation;
import de.discord.manage.sql.LiteSQL;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class LoadChannel {

    public static void start() {

        ResultSet set = LiteSQL.onQuery("SELECT * FROM rejoin");

        if(set != null) {

            try {

                while (set.next()) {

                    long channelid = set.getLong("channelid");
                    String radioName = set.getString("radio");

                    VoiceChannel channel = RadioBot.INSTANCE.shardMan.getVoiceChannelById(channelid);
                    RadioStation station = RadioBot.INSTANCE.radioMan.radios.get(radioName);


                    if (channel != null && station != null) {

                        try {

                            channel.getGuild().getAudioManager().openAudioConnection(channel);
                            MusicController controller = RadioBot.INSTANCE.playerManager.getController(channel.getGuild());

                            controller.currentStation = station;

                            boolean b = false;

                            for (Member member : channel.getMembers()) {

                                if (!member.getUser().isBot()) {
                                    b = true;
                                    RadioBot.INSTANCE.playerManager.playRadio(channel.getGuild(), station, null, null, null);
                                    break;
                                }
                            }

                            if (!b) LeaveListener.datenschoner.put(channelid, station);

                        } catch (InsufficientPermissionException ignored) {
                        }
                    }
                }
                set.close();
                LiteSQL.onUpdate("DELETE FROM rejoin");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("[main] ERROR WHILE LOADING CHANNEL TO JOIN!");
        }
    }

    public static void stop() {

        Collection<MusicController> musicControllers = RadioBot.INSTANCE.playerManager.controller.values();

        for (MusicController controller : musicControllers) {

            Guild guild = RadioBot.INSTANCE.shardMan.getGuildById(controller.getGuild().getIdLong());

            if (guild != null) {

                VoiceChannel channel = guild.getAudioManager().getConnectedChannel();

                if (channel != null) {

                    if (controller.currentStation != null) {

                        LiteSQL.preparedUpdate("INSERT INTO rejoin(channelid, radio) VALUES(?, ?)", channel.getIdLong(), controller.currentStation.name);

                    }
                }
            }
        }
    }
}