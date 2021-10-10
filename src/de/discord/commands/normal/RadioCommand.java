package de.discord.commands.normal;

import de.discord.commands.types.ButtonCommand;
import de.discord.commands.types.Command;
import de.discord.commands.types.CommandAnnotation;
import de.discord.commands.types.SlashCommand;
import de.discord.core.Error;
import de.discord.core.RadioBot;
import de.discord.manage.managers.RadioManager;
import de.discord.manage.managers.RadioStation;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.Instant;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@CommandAnnotation
public class RadioCommand implements Command, SlashCommand, ButtonCommand {

    private static Map<RadioStation.Country, String> emojiFlagMap;
    private static Map<Long, Data> dataMap;

    public static void setup() {
        dataMap = new ConcurrentHashMap<>();
        emojiFlagMap = new ConcurrentHashMap<>();

        emojiFlagMap.put(RadioStation.Country.Germany, ":flag_de:");
        emojiFlagMap.put(RadioStation.Country.USA, ":flag_us:");
        emojiFlagMap.put(RadioStation.Country.Britain, ":flag_gb:");
        emojiFlagMap.put(RadioStation.Country.Spain, ":flag_es:");
        emojiFlagMap.put(RadioStation.Country.France, ":flag_fr:");
        emojiFlagMap.put(RadioStation.Country.Italy, ":flag_it:");
        emojiFlagMap.put(RadioStation.Country.Netherlands, ":flag_nl:");
        emojiFlagMap.put(RadioStation.Country.Other, ":flag_white:");

        RadioBot.INSTANCE.timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (Data data : dataMap.values()) {
                    if (data.lastinteraction < System.currentTimeMillis() - 120 * 1000) {
                        dataMap.remove(data.id);
                    }
                }
            }
        }, 120 * 1000, 120 * 1000);

    }

    /*
        :flag_de: U+1f1e9U+1f1ea
        :flag_us: U+1f1faU+1f1f8
        :flag_gb: U+1f1ecU+1f1e7
        :flag_es: U+1f1eaU+1f1f8
        :flag_white: U+1f3f3

        :left_arrow: U+2b05U+fe0f
        :right_arrow: U+27a1U+fe0f
        :leftwards_arrow_with_hook: U+21a9U+fe0f
     */
    @Override
    public void performCommand(Member m, TextChannel channel, Message message, String prefix) {

        String[] args = message.getContentRaw().split(" ");

        if ((args.length > 2 && (RadioBot.INSTANCE.botOptions.getJSONArray("owner").toList().contains(m.getIdLong()) || RadioBot.INSTANCE.botOptions.getJSONArray("owner").toList().contains(m.getId())))) {
            if (args[1].equals("-a")) {
                if (args.length != 3) {
                    Error.set(channel, Error.Type.Syntax, getClass().getSimpleName());
                    return;
                }
                String[] station = args[2].replace("`", "").split("\\|");
                if (station.length != 4) {
                    channel.sendMessageEmbeds((new EmbedBuilder()).setColor(Color.red).setTitle("» An Error occurred!").setDescription(":information_source: Error **101**\n:x: **Syntax incorrect!**").setFooter("Error-ID: 300 | Module: RADIOCOMMAND\nGuild-ID: " + m.getGuild().getId() + " | Channel-ID: " + channel.getId()).build(), new MessageEmbed[0]).queue();
                    return;
                }

                RadioStation.Country country = RadioManager.countryMap.get(station[2].toLowerCase());
                if (country == null) {
                    channel.sendMessageEmbeds((new EmbedBuilder()).setColor(Color.red).setTitle("» An Error occurred!").setDescription(":information_source: Error **400**\n:x: **Invalid country!**\nThere is no country with that name. Please try again.").setFooter("Error-ID: 300 | Module: RADIOCOMMAND\nGuild-ID: " + m.getGuild().getId() + " | Channel-ID: " + channel.getId()).build(), new MessageEmbed[0]).queue();
                    return;
                }

                RadioBot.INSTANCE.radioMan.addRadioV2(station[0], station[1].replace("_", " "), country, station[3]);
                channel.sendMessageEmbeds((new EmbedBuilder()).setColor(Color.green).setAuthor("Added Radio-Station!").setTitle("» " + station[1]).setFooter("Added by " + m.getUser().getAsTag(), m.getUser().getAvatarUrl()).setTimestamp(Instant.now()).build(), new MessageEmbed[0]).queue();
            } else if (args[1].equals("-r")) {

                if (args.length == 3 && args[2] != null) {
                    RadioStation station = RadioBot.INSTANCE.radioMan.radios.get(args[2]);
                    if (station == null) {
                        channel.sendMessageEmbeds((new EmbedBuilder()).setColor(Color.red).setTitle("» An Error occurred!").setDescription(":information_source: Error **400**\n:x: **Invalid station!**\nThere is no station with that name. Please try again.").setFooter("Error-ID: 300 | Module: RADIOCOMMAND\nGuild-ID: " + m.getGuild().getId() + " | Channel-ID: " + channel.getId()).build(), new MessageEmbed[0]).queue();
                        return;
                    }

                    channel.sendMessageEmbeds((new EmbedBuilder()).setColor(Color.red).setAuthor("Removed Radio-Station!").setTitle("» " + station.title.replace(":radio: Now playing: ", "").replace(":radio: Now playing ", "")).setFooter("Removed by " + m.getUser().getAsTag(), m.getUser().getAvatarUrl()).setTimestamp(Instant.now()).build(), new MessageEmbed[0]).queue();
                    RadioBot.INSTANCE.radioMan.removeRadio(args[2]);


                } else {
                    Error.set(channel, Error.Type.Syntax, getClass().getSimpleName());
                }
            } else if (args[1].equals("-g")) {


                if (args.length == 3 && args[2] != null) {

                    RadioStation station = RadioBot.INSTANCE.radioMan.radios.get(args[2]);
                    if (station == null) {
                        channel.sendMessageEmbeds((new EmbedBuilder()).setColor(Color.red).setTitle("» An Error occurred!").setDescription(":information_source: Error **400**\n:x: **Invalid station!**\nThere is no station with that name. Please try again.").setFooter("Error-ID: 300 | Module: RADIOCOMMAND\nGuild-ID: " + m.getGuild().getId() + " | Channel-ID: " + channel.getId()).build(), new MessageEmbed[0]).queue();
                        return;
                    }

                    channel.sendMessageEmbeds((new EmbedBuilder()).setColor(Color.yellow).setAuthor("Details for Radio-Station!").setTitle("» " + station.title.replace(":radio: Now playing: ", "").replace(":radio: Now playing ", "")).setDescription("```fix\n`" + station.name + "|" + station.title.replace(":radio: Now playing: ", "").replace(":radio: Now playing ", "").replace(" ", "_") + "|" + station.country + "|" + station.url  + "`\n```").setFooter("Requested by " + m.getUser().getAsTag(), m.getUser().getAvatarUrl()).setTimestamp(Instant.now()).build()).queue();
                } else {
                    Error.set(channel, Error.Type.Syntax, getClass().getSimpleName());
                }
            } else {
                Error.set(channel, Error.Type.Syntax, getClass().getSimpleName());
            }
        } else {
            sendEmbed(null, channel, null);
        }
    }

    private void sendEmbed(Data data, TextChannel channel, ButtonClickEvent event) {

        if (event == null) {

            long time = System.currentTimeMillis();

            List<Button> list = new ArrayList<>();
            list.add(Button.secondary("radio_germany_" + time, Emoji.fromUnicode("U+1F1E9 U+1F1EA")));
            list.add(Button.secondary("radio_usa_" + time, Emoji.fromUnicode("U+1f1fa U+1f1f8")));
            list.add(Button.secondary("radio_britain_" + time, Emoji.fromUnicode("U+1F1EC U+1F1E7")));
            list.add(Button.secondary("radio_spain_" + time, Emoji.fromUnicode("U+1F1EA U+1F1F8")));

            ActionRow row = ActionRow.of(list);

            list.clear();

            list.add(Button.secondary("radio_france_" + time, Emoji.fromUnicode("U+1f1eb U+1f1f7")));
            list.add(Button.secondary("radio_italy_" + time, Emoji.fromUnicode("U+1f1ee U+1f1f9")));
            list.add(Button.secondary("radio_netherlands_" + time, Emoji.fromUnicode("U+1f1f3 U+1F1F1")));
            list.add(Button.secondary("radio_other_" + time, Emoji.fromUnicode("U+1F3F3")));

            data = new Data(time);

            channel.sendMessageEmbeds(getSelectionEmbed().build()).setActionRows(row, ActionRow.of(list)).queue();

            dataMap.put(time, data);

            return;
        }

        String[] args = event.getButton().getId().split("_");

        if (data.state == 0) {

            RadioStation.Country country = RadioManager.countryMap.get(args[1]);


            if (country != null) {

                data.state = 1;
                data.page = 1;
                data.currentcountry = country;
                data.lastinteraction = System.currentTimeMillis();

                ActionRow row = ActionRow.of(Button.secondary("radio_back_" + data.id, Emoji.fromUnicode("U+2b05U+fe0f")), Button.secondary("radio_forward_" + data.id, Emoji.fromUnicode("U+27a1U+fe0f")), Button.secondary("radio_allback_" + data.id, Emoji.fromUnicode("U+21a9U+fe0f")));

                event.editComponents(row).setEmbeds(getCountryEmbed(country, channel, 1).build()).queue();


            }

        } else {

            List<RadioStation> list = RadioBot.INSTANCE.radioMan.countryStationList.get(data.currentcountry);

            switch (args[1]) {

                case "back":

                    if (data.page == 1) {

                        int size = (list.size() / 10);

                        if (list.size() % 10 != 0)
                            size++;

                        data.page = size;
                    } else {
                        data.page--;
                    }

                    data.lastinteraction = System.currentTimeMillis();

                    event.editMessageEmbeds(getCountryEmbed(data.currentcountry, channel, data.page).build()).queue();

                    break;
                case "forward":

                    data.page = data.page + 1;

                    int size = (list.size() / 10);

                    if (list.size() % 10 != 0)
                        size++;

                    if (data.page > size) {

                        data.page = 1;

                    }

                    data.lastinteraction = System.currentTimeMillis();

                    event.editMessageEmbeds(getCountryEmbed(data.currentcountry, channel, data.page).build()).queue();

                    break;
                case "allback":

                    data.state = 0;
                    data.currentcountry = null;
                    data.lastinteraction = System.currentTimeMillis();

                    List<Button> buttons = new ArrayList<>();
                    buttons.add(Button.secondary("radio_germany_" + data.id, Emoji.fromUnicode("U+1F1E9 U+1F1EA")));
                    buttons.add(Button.secondary("radio_usa_" + data.id, Emoji.fromUnicode("U+1f1fa U+1f1f8")));
                    buttons.add(Button.secondary("radio_britain_" + data.id, Emoji.fromUnicode("U+1F1EC U+1F1E7")));
                    buttons.add(Button.secondary("radio_spain_" + data.id, Emoji.fromUnicode("U+1F1EA U+1F1F8")));

                    ActionRow row = ActionRow.of(buttons);

                    buttons.clear();

                    buttons.add(Button.secondary("radio_france_" + data.id, Emoji.fromUnicode("U+1f1eb U+1f1f7")));
                    buttons.add(Button.secondary("radio_italy_" + data.id, Emoji.fromUnicode("U+1f1ee U+1f1f9")));
                    buttons.add(Button.secondary("radio_netherlands_" + data.id, Emoji.fromUnicode("U+1f1f3 U+1F1F1")));
                    buttons.add(Button.secondary("radio_other_" + data.id, Emoji.fromUnicode("U+1F3F3")));

                    event.editComponents(row, ActionRow.of(buttons)).setEmbeds(getSelectionEmbed().build()).queue();

                    break;
            }
        }
    }

    @Override
    public void executeButton(ButtonClickEvent event) {

        if (event.getChannelType() != ChannelType.TEXT) return;

        if (event.getButton() == null || event.getButton().getId() == null) return;
        try {
            Data data = dataMap.get(Long.parseLong(event.getButton().getId().split("_")[2]));

            if (data != null) {
                sendEmbed(data, event.getTextChannel(), event);
            }

        } catch (NumberFormatException ignored) {

        }
    }

    @Override
    public void executeSlashCommand(SlashCommandEvent e) {

        if (e.getChannelType() != ChannelType.TEXT) {
            e.reply("This command can only be used on Servers.").queue();
            return;
        }

        sendEmbed(null, e.getTextChannel(), null);
    }

    private EmbedBuilder getSelectionEmbed() {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(Color.red);
        builder.setTitle("» Select a country");
        builder.setFooter("You have 30 seconds to click the matching flag below.");

        for (RadioStation.Country country : RadioStation.Country.values()) {
            builder.addField("» " + country.name() + " " + emojiFlagMap.get(country), RadioBot.INSTANCE.radioMan.countryStationList.get(country).size() + " Radio-Station(s) available.", false);
        }

        return builder;
    }

    private EmbedBuilder getCountryEmbed(RadioStation.Country country, TextChannel channel, int page) {
        EmbedBuilder builder = new EmbedBuilder();

        List<RadioStation> list = RadioBot.INSTANCE.radioMan.countryStationList.get(country);

        builder.setTitle("» Radio-Stations from " + country.name() + " | Page " + page);

        builder.setColor(Color.red);

        if (list.isEmpty()) {
            builder.setDescription(":spider_web: *kinda empty here* :spider_web:");
        }

        for (int i = ((page - 1) * 10); i < 10 + ((page - 1) * 10); i++) {

            if (list.size() <= i)
                break;

            if (i % 2 == 0 && i != ((page - 1) * 10))
                builder.addBlankField(false);

            RadioStation station = list.get(i);

            builder.addField(RadioBot.INSTANCE.prefixMan.getPrefix(channel.getGuild().getIdLong()) + station.name, "» [" + station.title.replace(":radio: Now playing: ", "").replace(":radio: Now playing ", "") + "](" + station.url + ")", true);

        }

        if (!channel.getGuild().getSelfMember().hasPermission(channel, Permission.MESSAGE_MANAGE))
            builder.setFooter("I can't remove the reactions, because I lack the Permission 'Manage Messages'!");

        return builder;
    }

    @Override
    public @NotNull EmbedBuilder getHelp(String prefix) {
        return getDefaultHelp(prefix);
    }

    @Override
    public @NotNull String getName() {
        return "radio";
    }

    @Override
    public @NotNull Permission[] getUserPermission() {
        return new Permission[0];
    }

    @Override
    public @NotNull Permission[] getBotPermission() {
        return new Permission[0];
    }

    @Override
    public @NotNull String[] getAliases() {
        return new String[]{"r"};
    }


    @Override
    public @NotNull String getDescription() {
        return "Shows you all Radio-Stations.";
    }

    @Override
    public OptionData[] getOptionData() {
        return new OptionData[0];
    }

    private static class Data {

        Data(Long time) {
            this.id = time;
            this.lastinteraction = time;
        }

        public int state = 0;
        public int page = 1;
        public long id;

        public long lastinteraction;

        public RadioStation.Country currentcountry;
    }
}