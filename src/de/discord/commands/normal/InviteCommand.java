package de.discord.commands.normal;

import de.discord.commands.types.Command;
import de.discord.commands.types.CommandAnnotation;
import de.discord.core.RadioBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.Component;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.Instant;
import java.util.Objects;

@CommandAnnotation
public class InviteCommand implements Command {
    @Override
    public void performCommand(Member m, TextChannel channel, Message message, String prefix) {

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("» Invites");
        builder.setDescription("**» ** [" + RadioBot.INSTANCE.getName() + "](" + RadioBot.INSTANCE.invite + ")");
        builder.setFooter("Requested by " + m.getUser().getAsTag(), m.getUser().getAvatarUrl());
        builder.setTimestamp(Instant.now());
        builder.setColor(Color.red);

        Button vote = Button.link(RadioBot.INSTANCE.invite, Emoji.fromUnicode("U+1F517"));
        channel.sendMessageEmbeds(builder.build()).setActionRows(new ActionRow[]{ActionRow.of(vote)}).queue();
    }

    @Override
    public @NotNull String getName() {
        return "invite";
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
        return new String[]{"inv"};
    }

    @Override
    public @NotNull EmbedBuilder getHelp(String prefix) {
        return getDefaultHelp(prefix).appendDescription(desc+"This command gives you invites to our discord and our bots");
    }
}
