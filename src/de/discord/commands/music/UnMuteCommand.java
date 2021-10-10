package de.discord.commands.music;

import de.discord.commands.types.Command;
import de.discord.commands.types.CommandAnnotation;
import de.discord.commands.types.SlashCommand;
import de.discord.core.Error;
import de.discord.core.RadioBot;
import de.discord.music.MusicUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@CommandAnnotation
public class UnMuteCommand implements Command, SlashCommand {

    @Override
    public void performCommand(Member m, TextChannel channel, Message message, String prefix) {

        VoiceChannel vc = Objects.requireNonNull(m.getVoiceState()).getChannel();

        if (vc == null) {
            Error.set(channel.getIdLong(), Error.Type.NoVC, getClass().getSimpleName());
            return;
        }

        if (!MusicUtil.voiceHasBot(vc)) {
            Error.set(channel.getIdLong(), Error.Type.BotNoVC, getClass().getSimpleName());
            return;
        }

        String unicode;


        if (m.getGuild().getAudioManager().getSendingHandler() == null) {

            MusicUtil.increaseHandler(m.getGuild());

            unicode = "\uD83D\uDD0A";
        } else {
            unicode = "\u274C";
        }

        if (message != null)
            message.addReaction(unicode).queue();
    }

    @Override
    public @NotNull String getName() {
        return "unmute";
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
        return getDefaultHelp(prefix);
    }

    @Override
    public void executeSlashCommand(SlashCommandEvent e) {

        if (e.getChannelType() != ChannelType.TEXT) {
            e.reply("This command can only be used on Servers.").queue();
            return;
        }
        VoiceChannel vc = Objects.requireNonNull(e.getMember().getVoiceState()).getChannel();

        if (vc == null) {
            Error.set(e.getTextChannel(), Error.Type.NoVC, getClass().getSimpleName(), e);
            return;
        }

        if (!MusicUtil.voiceHasBot(vc)) {
            Error.set(e.getTextChannel(), Error.Type.BotNoVC, getClass().getSimpleName(), e);
            return;
        }

        performCommand(Objects.requireNonNull(e.getMember()), e.getTextChannel(), null, null);

        e.reply("Muted "+ RadioBot.INSTANCE.getName() +"!").queue(interactionHook -> interactionHook.deleteOriginal().queueAfter(5, TimeUnit.SECONDS));

    }

    @Override
    public @NotNull String getDescription() {
        return "This command lets you unmute the bot";
    }

    @Override
    public OptionData[] getOptionData() {
        return new OptionData[0];
    }
}