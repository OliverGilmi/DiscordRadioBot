package de.discord.commands.normal;

import java.awt.Color;
import java.lang.management.ManagementFactory;
import java.time.Instant;
import java.util.Objects;

import de.discord.commands.types.CommandAnnotation;
import de.discord.core.RadioBot;
import de.discord.commands.types.Command;
import de.discord.listener.CommandListener;
import de.discord.music.MusicController;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.Component;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.jetbrains.annotations.NotNull;

@CommandAnnotation
public class InfoCommand implements Command {

	@Override
	public void performCommand(Member m, TextChannel channel, Message message, String prefix) {
		
		EmbedBuilder builder = new EmbedBuilder();
		
		int count = 0;
		int connectedCount = 0;
		int activeCount = 0;

		for (MusicController controller : RadioBot.INSTANCE.playerManager.controller.values()) {
			
			Guild guild = RadioBot.INSTANCE.shardMan.getGuildById(controller.getGuild().getIdLong());

			if (guild != null) {

				VoiceChannel vc = guild.getAudioManager().getConnectedChannel();

				if (vc != null) {

					int tcount = count;

					for (Member mem : vc.getMembers()) {

						if (!mem.getUser().isBot()) {
							count += 1;
						}
					}

					if (tcount < count) activeCount++;

					connectedCount++;
				}
			}
		}

		int servercount = 0;

		for (JDA jda : RadioBot.INSTANCE.shardMan.getShards()) {
			servercount += jda.getGuilds().size();
		}


		builder.addField("» Prefix on this Discord:", prefix, false);
		
		builder.addField("» Total server count:", servercount + "", false);

		builder.addField("» Radio-Station count:", RadioBot.INSTANCE.radioMan.radios.values().size() + "", false);

		builder.addField("» Connected voice channels:", connectedCount + "", false);

		builder.addField("» Active voice channels:", activeCount + "", false);

		builder.addField("» Users listening to music right now:", count + "", false);
		
		final long duration = ManagementFactory.getRuntimeMXBean().getUptime();

		final long jahre = duration / 31104000000L;
		final long monate = duration / 2592000000L % 12;
		final long tage = duration / 86400000L % 30;
		final long stunden = duration / 3600000L % 24;
		final long minuten = duration / 60000L % 60;
		final long sekunden = duration / 1000L % 60;

		String uptime = (jahre == 0 ? "" : jahre + " years, ")
				+ (monate == 0 ? "" : monate + " months, ") 
				+ (tage == 0 ? "" : tage + " days, \n ")
				+ (stunden == 0 ? "" : stunden + " hours, ")
				+ (minuten == 0 ? "" : minuten + " minutes, ")
				+ (sekunden == 0 ? "" : sekunden + " seconds");

		builder.addField("» Version:", CommandListener.v, false);
		builder.addField("» Online since:", uptime, false);
		builder.addField("» Developers:", "lxxrxtz#0472 | <@!439868330996924417> \nＫｉｍ Ｊｏｎｇ Ａｕｔｉｓｍ#0403 | <@!315141871825321985>", false);
		builder.addField("» Librarys:", "[JDA](https://github.com/DV8FromTheWorld/JDA), [SQLite Driver](https://github.com/xerial/sqlite-jdbc), [Apache Common](https://github.com/apache/commons-lang), [Lavaplayer](https://github.com/sedmelluq/lavaplayer)", false);
		builder.addField("\u200e\n» Links:", " \u200e \u200e**» Invite:** [" + RadioBot.INSTANCE.getName() + "](" + RadioBot.INSTANCE.invite + ")\n\n\u200e \u200e**» Code:** [Github](https://github.com/OliverGilmi/DiscordRadioBot)", false);
		builder.setAuthor(RadioBot.INSTANCE.getName(), null, m.getJDA().getSelfUser().getAvatarUrl());
		builder.setColor(Color.red);
		builder.setTitle("» " + RadioBot.INSTANCE.getName() + " Information");
		builder.setTimestamp(Instant.now());
		builder.setFooter("Requested by " + m.getEffectiveName(), m.getUser().getAvatarUrl());

		Button invite = Button.link(RadioBot.INSTANCE.invite, Emoji.fromUnicode("U+1F517"));
		Button github = Button.link("https://github.com/OliverGilmi/DiscordRadioBot", Emoji.fromUnicode("U+2699"));
		ActionRow[] actionRows = new ActionRow[]{ActionRow.of(invite, github)};

		channel.sendMessageEmbeds(builder.build()).setActionRows(actionRows).queue();

	}

	@Override
	public @NotNull String getName() {
		return "info";
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
		return new String[]{"i", "botinfo", "information"};
	}

	@Override
	public @NotNull EmbedBuilder getHelp(String prefix) {
		return getDefaultHelp(prefix).appendDescription("\u200E\n**» Description:**\nThis command shows statistics and information about " + RadioBot.INSTANCE.getName() + ".");
	}
}