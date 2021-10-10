package de.discord.commands.normal;

import de.discord.commands.types.ButtonCommand;
import de.discord.commands.types.Command;
import de.discord.commands.types.CommandAnnotation;
import de.discord.core.Error;
import de.discord.core.RadioBot;
import de.discord.manage.managers.Settings;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.interactions.components.Button;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;
import java.util.*;

@CommandAnnotation
public class SettingsCommand implements Command, ButtonCommand {

    public static void setup() {
        descriptionMap.put(Settings.Setting.AdminOnly, "Disallows users without ADMINISTRATOR Permission to control the music (e.g. change station, volume, etc.).");
        descriptionMap.put(Settings.Setting.MusicRole, "Using this setting you can set a role so only users with that role can control the music (e.g. change station, volume, etc.).");
        descriptionMap.put(Settings.Setting.StageSpeaker, RadioBot.INSTANCE.getName() + " will automatically be the Speaker of the stage channel, but if " + RadioBot.INSTANCE.getName() + " doesn't have Permissions it will just request to speak.");
    }

    private static final Map<Settings.Setting, String> descriptionMap = new HashMap<>();

    @Override
    public void performCommand(Member m, TextChannel channel, Message message, String prefix) {

        Guild guild = m.getGuild();

        String[] args = message.getContentRaw().split(" ");

        if (args.length == 3) {

            if (RadioBot.INSTANCE.settings.settingMap.containsKey(args[1].toLowerCase())) {
                Settings.Setting setting = RadioBot.INSTANCE.settings.settingMap.get(args[1].toLowerCase());

                if (Settings.boolSettings.contains(setting)) {

                    if (args[2].equalsIgnoreCase("on") || args[2].equalsIgnoreCase("true")) {

                        RadioBot.INSTANCE.settings.insertValue(guild.getIdLong(), setting, true);
                        channel.sendMessageEmbeds(new EmbedBuilder().setColor(Color.green).setDescription(":white_check_mark: Successfully turned on `" + setting.name() + "`.").build()).queue();

                    } else if (args[2].equalsIgnoreCase("off") || args[2].equalsIgnoreCase("false")) {

                        RadioBot.INSTANCE.settings.insertValue(guild.getIdLong(), setting, false);
                        channel.sendMessageEmbeds(new EmbedBuilder().setColor(Color.green).setDescription(":white_check_mark: Successfully turned off `" + setting.name() + "`.").build()).queue();

                    } else {

                        Error.set(channel.getIdLong(), Error.Type.Syntax, getClass().getSimpleName());
                    }
                } else if (Settings.roleSettings.contains(setting)) {

                    String mention = args[2];

                    mention = mention.replace("<@&", "").replace(">", "");

                    Role role = null;

                    try {

                        long id = Long.parseLong(mention);

                        role = guild.getRoleById(id);

                    } catch (NumberFormatException ignored) {
                    }

                    if (role != null) {

                        RadioBot.INSTANCE.settings.insertValue(guild.getIdLong(), setting, role);
                        channel.sendMessageEmbeds(new EmbedBuilder().setColor(Color.green).setDescription(":white_check_mark: Successfully turned on `" + setting.name() + "` with the role " + role.getAsMention() + ".").build()).queue();

                    } else if (args[2].equalsIgnoreCase("off") || args[2].equalsIgnoreCase("false")) {

                        RadioBot.INSTANCE.settings.insertValue(guild.getIdLong(), setting, null);
                        channel.sendMessageEmbeds(new EmbedBuilder().setColor(Color.green).setDescription(":white_check_mark: Successfully turned off `" + setting.name() + "`.").build()).queue();

                    } else {
                        Error.set(channel, Error.Type.Syntax, getClass().getSimpleName());
                    }


                } else {
                    Error.set(channel, Error.Type.Unknown, getClass().getSimpleName());
                }

            } else {

                Error.set(channel.getIdLong(), Error.Type.NoSetting, getClass().getSimpleName(), args[1]);
            }

        } else {

            channel.sendMessageEmbeds(getEmbed(1, guild).build()).setActionRow(Button.secondary("settings_back_1", Emoji.fromUnicode("U+2b05U+fe0f")), Button.secondary("settings_forward_1", Emoji.fromUnicode("U+27a1U+fe0f"))).queue();
        }
    }



    @Override
    public void executeButton(ButtonClickEvent event) {

        if (event.getButton() == null || event.getButton().getId() == null || event.getChannelType() != ChannelType.TEXT) return;

        String[] args = event.getButton().getId().split("_");
        int page;
        try {
            page = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            return;
        }

        List<Settings.Setting> list = Arrays.asList(Settings.settingsArray.clone());

        switch (args[1]) {

            case "back":

                if (page == 1) {
                    page = (list.size() / 5) + 1;
                } else {
                    page = (page - 1) % ((list.size() / 5) + 1);
                }

                event.editMessageEmbeds(getEmbed(page, event.getGuild()).build()).queue();

                break;

            case "forward":

                page = (page - 1) % ((list.size() / 5) + 1);

                if (page == 0) {
                    page = (list.size() / 5) + 1;
                }

                event.editMessageEmbeds(getEmbed(page, event.getGuild()).build()).queue();
                break;
        }

    }

    @Override
    public @NotNull String getName() {
        return "settings";
    }

    @Override
    public @NotNull Permission[] getUserPermission() {
        return new Permission[]{Permission.ADMINISTRATOR};
    }

    @Override
    public @NotNull Permission[] getBotPermission() {
        return new Permission[0];
    }

    @Override
    public @NotNull String[] getAliases() {
        return new String[]{"setting"};
    }

    @Override
    public @NotNull EmbedBuilder getHelp(String prefix) {
        return getDefaultHelp(prefix);
    }


    private EmbedBuilder getEmbed(int page, Guild guild) {
        EmbedBuilder builder = new EmbedBuilder();

        Settings settings = RadioBot.INSTANCE.settings;

        builder.setColor(Color.red);

        builder.setTitle("» Settings | Page " + page);

        Settings.Setting[] settingsArray = Settings.settingsArray;

        for (int i = (page - 1) * 5; i < page * 5; i++) {

            if (settingsArray.length <= i)
                break;

            Settings.Setting setting = settingsArray[i];

            String text = descriptionMap.get(setting);

            Object obj = settings.getValue(guild.getIdLong(), setting);

            String b;

            if (Settings.boolSettings.contains(setting)) {
                b = ((obj != null) ? "" + (boolean) obj : "false");
            } else if (Settings.roleSettings.contains(setting)) {
                b = (obj != null) ? ((Role) obj).getName() : "none";
            } else {
                b = "unknown";
            }

            builder.addField("» " + setting.name() + " | " + b, text, false);
        }

        builder.setFooter("Settings-Size: " + settingsArray.length + " | Pages: " + ((settingsArray.length / 5) + 1));

        //builder.addBlankField(false);
        String prefix = RadioBot.INSTANCE.prefixMan.getPrefix(guild.getIdLong());
        builder.addField("", "You can change settings with `" + prefix + "settings <setting> <option>`.\n\nExamples: \n`" + prefix + "settings NoVote on`\n`" + prefix + "settings MusicRole @Musician`", false);
        return builder;
    }
}