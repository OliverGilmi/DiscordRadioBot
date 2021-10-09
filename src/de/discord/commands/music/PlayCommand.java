package de.discord.commands.music;

import de.discord.commands.types.CommandAnnotation;
import de.discord.commands.types.DoNotAddAsNormalCommand;
import de.discord.commands.types.SlashCommand;
import de.discord.core.RadioBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

@CommandAnnotation
public class PlayCommand implements SlashCommand, DoNotAddAsNormalCommand {

    @Override
    public void performCommand(Member m, TextChannel channel, Message message, String prefix) {}

    @Override
    public @NotNull String getName() {
        return "play";
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
        return new String[0];
    }

    @Override
    public @NotNull EmbedBuilder getHelp(String prefix) {
        return getDefaultHelp(prefix).appendDescription(desc + "This is a Slash Command, which lets you directly play a Radio Station");
    }

    @Override
    public void executeSlashCommand(SlashCommandEvent e) {

        OptionMapping map = e.getOption("station");

        if (map == null) return;

        String station = map.getAsString();

        if (e.getChannelType() != ChannelType.TEXT) return;

        RadioBot.INSTANCE.radioMan.TriggerRadio(station, e.getTextChannel(), e.getMember(), e.getGuild(), e);

    }

    @Override
    public @NotNull String getDescription() {
        return "Plays the selected Radio Station";
    }

    @Override
    public OptionData[] getOptionData() {
        return new OptionData[]{new OptionData(OptionType.STRING, "station", "The station you want to play").setRequired(true)};
    }
}
