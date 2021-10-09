package de.discord.commands.normal;

import de.discord.commands.types.Command;
import de.discord.commands.types.CommandAnnotation;
import de.discord.commands.types.SlashCommand;
import de.discord.core.Error;
import de.discord.core.RadioBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.Instant;

@CommandAnnotation
public class HelpCommand implements Command, SlashCommand {


	@Override
	public void performCommand(Member m, TextChannel channel, Message message, String prefix) {


		String[] args = message.getContentRaw().split(" ");
		if (args.length == 2) {

			if (args[1].equals("adv") || args[1].equals("advanced")) {
				channel.sendMessageEmbeds(getAdvancedEmbed(prefix).build()).queue();
				return;
			}

			Command com = RadioBot.INSTANCE.getCmdMan().commands.get(args[1]);

			if (com == null) {
				Error.set(channel.getIdLong(), Error.Type.NoHelp, getClass().getSimpleName());
				return;
			}

			channel.sendMessageEmbeds(com.getHelp(prefix).setFooter("Requested by " + m.getUser().getAsTag(), m.getUser().getAvatarUrl()).build()).queue();

		} else {
			channel.sendMessageEmbeds(getNormalEmbed(prefix).setFooter("Requested by " + m.getUser().getAsTag(), m.getUser().getAvatarUrl()).build()).queue();

		}
	}

	@Override
	public void executeSlashCommand(SlashCommandEvent e) {

		String prefix = RadioBot.INSTANCE.prefixMan.standard;

		if (e.getGuild() != null) {
			prefix = RadioBot.INSTANCE.prefixMan.getPrefix(e.getGuild().getIdLong());
		}

		Member m = e.getMember();

		if (m == null) return;

		OptionMapping mapping = e.getOption("command");

		if (mapping != null) {
			String command = mapping.getAsString();

			Command com = RadioBot.INSTANCE.getCmdMan().commands.get(command);

			if (com != null) {

				e.replyEmbeds(com.getHelp(prefix).setFooter("Requested by " + m.getUser().getAsTag(), m.getUser().getAvatarUrl()).build()).queue();

			} else {

				if (command.equalsIgnoreCase("advanced")) {
					e.replyEmbeds(getAdvancedEmbed(prefix).setFooter("Requested by " + m.getUser().getAsTag(), m.getUser().getAvatarUrl()).build()).queue();

				}
			}
		}
		e.replyEmbeds(getNormalEmbed(prefix).setFooter("Requested by " + m.getUser().getAsTag(), m.getUser().getAvatarUrl()).build()).queue();
	}

	private EmbedBuilder getAdvancedEmbed(String prefix) {

		EmbedBuilder builder = new EmbedBuilder();
		builder.setAuthor(RadioBot.INSTANCE.getName(), null, RadioBot.INSTANCE.shardMan.getShards().get(0).getSelfUser().getAvatarUrl());
		builder.setTitle("» Advanced Help-Menu:");
		builder.appendDescription("```\nThe current prefix is: " + prefix + "\nYou can change it by typing " + prefix + "prefix <newprefix> \nPlease note that you need to have Administrator-Permission in order to be able to do this.```");
		builder.appendDescription("\n\n:clipboard: **All Commands:** :clipboard:");
		builder.addField(" \u200e\u200f\u200f\u200e \u200e\n:bar_chart: Utility: :bar_chart:", "```\n" + prefix + "credits \n" + prefix + "invite \n" + prefix + "info \n" + prefix + "help \n" + prefix + "version \n\n\u200e```", true);
		builder.addField(" \u200e\u200f\u200f\u200e \u200e\n:radio: Radio-Commands: :radio:", "```\n" + prefix + "nowplaying \n" + prefix + "radio \n" + prefix + "join \n" + prefix + "stop \n" + prefix + "leave \n" + prefix + "mute \n" + prefix + "unmute ```", true);
		builder.addField(" \u200e\u200f\u200f\u200e \u200e\n:magic_wand: Mod-Commands: :magic_wand:", "```" + prefix + "prefix\n" + prefix + "settings\n\n\n\n\n\u200e```", true);
		builder.addField("\u200e\n» Get started:", "```To get started with playing radio, simply type " + prefix + "radio and selecting a country by using the buttons there. Then just type the command listed there that is associated to the radio-station you want to play from. More info at " + prefix + "help radio.```", false);
		builder.addField("\u200e\n» Tip:", "```To get more information about how a command is used properly, simply type " + prefix + "help <command>.```\n", false);
		builder.addField("\u200e\n» Links:", " \u200e \u200e» Invite: [" + RadioBot.INSTANCE.getName() + "](" + RadioBot.INSTANCE.invite + ")", false);
		builder.setColor(Color.red);
		builder.setTimestamp(Instant.now());
		return builder;
	}

	private EmbedBuilder getNormalEmbed(String prefix) {

		EmbedBuilder builder = new EmbedBuilder();
		builder.setAuthor(RadioBot.INSTANCE.getName(), null, RadioBot.INSTANCE.shardMan.getShards().get(0).getSelfUser().getAvatarUrl());
		builder.setTitle("» Help-Menu:");
		builder.addField(" \u200e\u200f\u200f\u200e \u200e\n:bar_chart: Utility: :bar_chart:", "`" + prefix + "credits` `" + prefix + "invite` `" + prefix + "info` `" + prefix + "help` `" + prefix + "version` `" + prefix + "website`", true);
		builder.addField(" \u200e\u200f\u200f\u200e \u200e\n:radio: Radio-Commands: :radio:", "`" + prefix + "nowplaying` `" + prefix + "radio` `" + prefix + "join` `" + prefix + "stop` `" + prefix + "leave` `" + prefix + "mute` `" + prefix + "unmute`", true);
		builder.addField(" \u200e\u200f\u200f\u200e \u200e\n:magic_wand: Mod-Commands: :magic_wand:", "`" + prefix + "prefix` `" + prefix + "settings`", true);
		builder.addField("\u200e\n» Get started:", "To get started with playing radio, simply type `" + prefix + "radio` and select a country by using the buttons there. Then just type the command listed there that is associated to the radio-station you want to play from. More info at `" + prefix + "help play`.", false);
		builder.addField("\u200e\n» Tip:", "To get more information about how a command is used properly, simply type `" + prefix + "help <command>`. If this Help-Embed is too confusing for you, please type `" + prefix + "help advanced`.", false);
		builder.addField("\u200e\n» Links:", " \u200e \u200e» Invite: [" + RadioBot.INSTANCE.getName() + "](" + RadioBot.INSTANCE.invite + ")", false);
		builder.setColor(Color.red);
		builder.setTimestamp(Instant.now());


		return builder;

	}

	@Override
	public @NotNull String getName() {
		return "help";
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
		return new String[]{"h", "h adv", "h advanced", "h <command>", "help adv", "help advanced", "help <command>"};
	}

	@Override
	public @NotNull EmbedBuilder getHelp(String prefix) {
		return getDefaultHelp(prefix).appendDescription(desc + "The help command has multiple ways of being used. Simply typing `%p%h` or `%p%help` shows you a help menu where all bases of commands are listed. `%p%h adv`, `%p%h advanced`, `%p%help adv` or `%p%help advanced` shows you all commands in a cleaner but bigger overview including all possible syntaxes for the commands. `%p%h <command>` or `%p%help <command>` finally shows you detailed infos about commands, like this one.".replace("%p%", prefix)).appendDescription(example + "`" +  prefix + "help play`");
	}

	@Override
	public @NotNull String getDescription() {
		return "This command shows you all of our commands and can show you details about a specific command";
	}

	@Override
	public OptionData[] getOptionData() {

		OptionData data = new OptionData(OptionType.STRING, "command", "Shows info about this command or use advanced to see a detailed list of all commands");

		return new OptionData[]{data};
	}
}