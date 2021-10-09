package de.discord.manage.other;

import net.dv8tion.jda.api.EmbedBuilder;
import javax.annotation.Nullable;

public class HelpBuilder {

	public String syntaxes;

	public String description;

	public String example;

	public String footer;

	public HelpBuilder(String syntaxes, String description, @Nullable String example, @Nullable String footer) {

		this.footer = footer;

		this.example = example;

		this.syntaxes = syntaxes;

		this.description = description;

	}

	public EmbedBuilder getEmbed(String prefix) {

		EmbedBuilder builder = new EmbedBuilder();

		builder.appendDescription("**» Alias(es) & Syntax(es):** \n`" + prefix + syntaxes.replace(", ", "`, `" + prefix) + "`\n\u200E");

		builder.appendDescription("\u200E\n**» Description:**\n" + description.replace("%p%", prefix));

		if (example != null) {
			builder.appendDescription("\n\u200E\n**» Example(s):**\n`" + example.replace("%p%", prefix).replace(", ", "`, `") + "`");
		}

		if (footer != null)
		builder.setFooter(footer.replace("%p%", prefix));

		builder.setColor(0xff0000);

		return builder;

	}

}