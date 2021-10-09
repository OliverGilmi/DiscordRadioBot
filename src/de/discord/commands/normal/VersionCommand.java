package de.discord.commands.normal;

import de.discord.commands.types.Command;
import de.discord.commands.types.CommandAnnotation;
import de.discord.listener.CommandListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

@CommandAnnotation
public class VersionCommand implements Command {
    @Override
    public void performCommand(Member m, TextChannel channel, Message message, String prefix) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setDescription("Current Version: \n :green_circle: " + CommandListener.v);
        builder.setColor(Color.red);
        channel.sendMessageEmbeds(builder.build()).queue();
    }

    @Override
    public @NotNull String getName() {
        return "version";
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
        return new String[]{"v"};
    }

    @Override
    public @NotNull EmbedBuilder getHelp(String prefix) {
        return getDefaultHelp(prefix).appendDescription(desc + "Shows you the Bots version number.");
    }
}
