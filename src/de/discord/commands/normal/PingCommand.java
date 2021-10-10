package de.discord.commands.normal;

import de.discord.commands.types.Command;
import de.discord.commands.types.CommandAnnotation;
import de.discord.commands.types.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.temporal.ChronoUnit;

@CommandAnnotation
public class PingCommand implements Command, SlashCommand {
    @Override
    public void performCommand(Member m, TextChannel channel, Message message, String prefix) {


        channel.sendMessageEmbeds(new EmbedBuilder().setColor(Color.red).setTitle("» Calculating ping...").build()).queue(msg -> {
                long ping = message.getTimeCreated().until(msg.getTimeCreated(), ChronoUnit.MILLIS);
                msg.editMessageEmbeds(new EmbedBuilder().setTitle("» Ping").setColor(Color.red).setDescription("**» Response Time:** " + ping + "ms \n**» JDA-Ping:** " + m.getJDA().getGatewayPing() + "ms").setThumbnail("https://media.nature.com/lw800/magazine-assets/d41586-018-06610-y/d41586-018-06610-y_16109938.gif").build()).queue();
        });

    }

    @Override
    public @NotNull String getName() {
        return "ping";
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


        e.replyEmbeds(new EmbedBuilder().setColor(Color.red).setTitle("Calculating ping...").build()).queue(interactionHook -> interactionHook.retrieveOriginal().queue(interac -> {
            long ping = e.getTimeCreated().until(interac.getTimeCreated(), ChronoUnit.MILLIS);
            interactionHook.editOriginalEmbeds(new EmbedBuilder().setTitle("» Ping").setColor(Color.red).setDescription("**» Response Time:** " + ping + "ms \n**» JDA-Ping:** " + e.getJDA().getGatewayPing() + "ms").setThumbnail("https://media.nature.com/lw800/magazine-assets/d41586-018-06610-y/d41586-018-06610-y_16109938.gif").build()).queue();
        }));

    }

    @Override
    public @NotNull String getDescription() {
        return "Gives you the ping of our bot";
    }

    @Override
    public OptionData[] getOptionData() {
        return new OptionData[0];
    }
}
