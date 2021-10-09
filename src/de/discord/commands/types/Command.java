package de.discord.commands.types;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Collection;

public interface Command {

	String desc = "\n\u200E\n**» Description:**\n";
	String example = "\n\u200E\n**» Example(s):**\n";

	default EmbedBuilder getDefaultHelp(String prefix) {

		StringBuilder builder = new StringBuilder();

		builder.append("`").append(prefix).append(getName()).append("`");

		for (String alias : getAliases()) {
			builder.append(", `").append(prefix).append(alias).append("`");
		}

		return new EmbedBuilder().setColor(Color.red).setTitle("» Help for " + prefix + getName()).appendDescription("**» Alias(es) & Syntax(es):** \n" + builder);

	}

	void performCommand(Member m, TextChannel channel, Message message, String prefix);

	@NotNull
	String getName();

	@NotNull
	Permission[] getUserPermission();

	@NotNull
	Permission[] getBotPermission();

	@NotNull
	String[] getAliases();

	@NotNull
	EmbedBuilder getHelp(String prefix);

}
	