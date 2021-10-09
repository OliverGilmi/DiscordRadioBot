package de.discord.commands.music;

import de.discord.commands.types.Command;
import de.discord.commands.types.CommandAnnotation;
import de.discord.commands.types.SlashCommand;
import de.discord.core.Error;
import de.discord.core.RadioBot;
import de.discord.manage.managers.RadioManager;
import de.discord.manage.other.Utils;
import de.discord.music.AudioLoadResult;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@CommandAnnotation
public class JoinCommand implements Command, SlashCommand {

    @Override
    public void performCommand(Member m, TextChannel channel, Message message, String prefix) {

        AudioManager manager = m.getGuild().getAudioManager();

        VoiceChannel vc = Objects.requireNonNull(m.getVoiceState()).getChannel();

        if (Utils.checkUserPermission(channel, m, this.getClass())) return;

        if (vc == null) {
            Error.set(channel, Error.Type.NoVC, getClass().getSimpleName());
            return;
        }

        if (!RadioManager.botAvailable(manager, m)) {
            Error.set(channel, Error.Type.CurrentlyUsed, getClass().getSimpleName());
            return;
        }
        try {
            manager.openAudioConnection(vc);

            message.addReaction("\u23EC").queue();

        } catch (InsufficientPermissionException e) {
            Error.set(channel, Error.Type.ChannelFull, getClass().getSimpleName());
        }
    }

    @Override
    public @NotNull String getName() {
        return "join";
    }

    @Override
    public Permission[] getUserPermission() {
        return null;
    }

    @Override
    public Permission[] getBotPermission() {
        return new Permission[]{Permission.VOICE_CONNECT};
    }

    @Override
    public @NotNull String[] getAliases() {
        return new String[]{"j"};
    }

    @Override
    public @NotNull EmbedBuilder getHelp(String prefix) {
        return getDefaultHelp(prefix).appendDescription(desc + "This command lets " + RadioBot.INSTANCE.getName() + " join your channel");
    }

    @Override
    public void executeSlashCommand(SlashCommandEvent e) {

        if (e.getChannelType() != ChannelType.TEXT || e.getGuild() == null || e.getMember() == null) {
            e.reply("This command can only be used on Servers.!").queue();
            return;
        }

        AudioManager manager = e.getGuild().getAudioManager();

        VoiceChannel vc = Objects.requireNonNull(e.getMember().getVoiceState()).getChannel();

        if (Utils.checkUserPermission(e.getTextChannel(), e.getMember(), this.getClass())) return;

        if (vc == null) {
            Error.set(e.getTextChannel(), Error.Type.NoVC, getClass().getSimpleName(), e);
            return;
        }

        if (!RadioManager.botAvailable(manager, e.getMember())) {
            Error.set(e.getTextChannel(), Error.Type.CurrentlyUsed, getClass().getSimpleName(), e);
            return;
        }

        try {
            manager.openAudioConnection(vc);

            e.reply("Joined your Channel!").queue( interactionHook -> interactionHook.deleteOriginal().queueAfter(5, TimeUnit.SECONDS));

        } catch (InsufficientPermissionException error) {
            Error.set(error.getChannelId(), Error.Type.ChannelFull, getClass().getSimpleName());
        }
    }

    @Override
    public @NotNull String getDescription() {
        return "This command lets "+ RadioBot.INSTANCE.getName() +" join your channel";
    }

    @Override
    public OptionData[] getOptionData() {
        return new OptionData[0];
    }
}
