package de.discord.commands.normal;

import de.discord.commands.types.Command;
import de.discord.commands.types.CommandAnnotation;
import de.discord.core.RadioBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

@CommandAnnotation
public class CreditCommand implements Command {
    @Override
    public void performCommand(Member m, TextChannel channel, Message message, String prefix) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("» Credits");
        builder.setDescription("**» Bot developed By:** \nlxxrxtz#0472 (<@!439868330996924417>) \nＫｉｍ Ｊｏｎｇ Ａｕｔｉｓｍ#0403 (<@!315141871825321985>)\nGet the Bot yourself: [Github](https://github.com/OliverGilmi/DiscordRadioBot)");
        builder.setFooter("Requested by " + m.getUser().getAsTag(), m.getUser().getAvatarUrl());
        builder.setTimestamp(Instant.now());
        builder.setColor(0xff0000);
        channel.sendMessageEmbeds(builder.build()).queue();
    }

    @Override
    public @NotNull String getName() {
        return "credit";
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
        return new String[]{"credits", "cr"};
    }

    @Override
    public @NotNull EmbedBuilder getHelp(String prefix) {
        return getDefaultHelp(prefix).appendDescription("Shows you who developed "+ RadioBot.INSTANCE.getName() +" and who is in our Team");
    }
}
