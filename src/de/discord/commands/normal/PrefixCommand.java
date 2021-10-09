package de.discord.commands.normal;

import de.discord.commands.types.Command;
import de.discord.commands.types.CommandAnnotation;
import de.discord.commands.types.SlashCommand;
import de.discord.core.Error;
import de.discord.core.RadioBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

@CommandAnnotation
public class PrefixCommand implements Command, SlashCommand {

	@Override
	public void performCommand(Member m, TextChannel channel, Message message, String prefix) {
		String[] args = message.getContentDisplay().split(" ");

		if (args.length == 2) {
			String string = args[1].replace("\n", "");
			if (string.length() <= 5) {

				RadioBot.INSTANCE.prefixMan.newPrefix(m.getGuild().getIdLong(), string);

				EmbedBuilder builder = new EmbedBuilder();
				builder.setDescription("Set the new prefix from `" + prefix + "` to `" + string + "` !");
				builder.setColor(Color.green);
				channel.sendMessageEmbeds(builder.build()).queue();

			} else {
				Error.set(channel.getIdLong(), Error.Type.Prefix, getClass().getSimpleName());
			}
		} else {
			Error.set(channel.getIdLong(), Error.Type.Syntax, getClass().getSimpleName());
		}

	}

	@Override
	public @NotNull String getName() {
		return "prefix";
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
		return new String[]{"setprefix"};
	}

	@Override
	public @NotNull EmbedBuilder getHelp(String prefix) {
		return getDefaultHelp(prefix).appendDescription(desc + "This command lets you change the prefix for this server.").appendDescription(example + "`" + prefix + "prefix radio!" + "`");
	}

	@Override
	public void executeSlashCommand(SlashCommandEvent e) {

		if (e.getChannelType() != ChannelType.TEXT) {
			e.reply("This command must be executed in a Server!").queue();
			return;
		}

		Member member = e.getMember();
		Guild guild = e.getGuild();

		if ( guild == null || member == null) {
			e.reply("An unknown Error was caused!").queue();
			return;
		}

		if (!member.hasPermission(Permission.ADMINISTRATOR)) {
			Error.set(e.getTextChannel(), Error.Type.UserPermLack, getClass().getSimpleName(), e, "ADMINISTRATOR");
			return;
		}

		OptionMapping mapping = e.getOption("prefix");

		if (mapping == null) {
			Error.set(e.getTextChannel(), Error.Type.Syntax, getClass().getSimpleName(), e);
			return;
		}

		String newprefix = mapping.getAsString();

		String prefix = RadioBot.INSTANCE.prefixMan.getPrefix(guild.getIdLong());

		RadioBot.INSTANCE.prefixMan.newPrefix(guild.getIdLong(), newprefix);

		EmbedBuilder builder = new EmbedBuilder();
		builder.setDescription("Set the new prefix from `" + prefix + "` to `" + newprefix + "` !");
		builder.setColor(Color.green);

		e.replyEmbeds(builder.build()).queue();

	}

	@Override
	public @NotNull String getDescription() {
		return "Lets you change the Bots prefix";
	}

	@Override
	public OptionData[] getOptionData() {
		return new OptionData[]{new OptionData(OptionType.STRING, "prefix", "The new prefix").setRequired(true)};
	}
}