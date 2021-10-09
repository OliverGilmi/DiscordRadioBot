package de.discord.listener;

import java.awt.Color;
import java.time.format.DateTimeFormatter;

import de.discord.core.RadioBot;
import de.discord.manage.sql.LiteSQL;
import de.discord.music.MusicController;
import de.discord.music.MusicUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.JSONException;

public class ServerJoinLeaveListener extends ListenerAdapter{

	public long channelid;
	public boolean isActive;

	public ServerJoinLeaveListener() {
		try {
			this.channelid = RadioBot.INSTANCE.botOptions.getLong("joinloggings");
			this.isActive = true;
		} catch (JSONException var2) {
			this.isActive = false;
		}
	}

	@Override
	public void onGuildJoin(GuildJoinEvent event) {

		EmbedBuilder builder = new EmbedBuilder();

		if (isActive) {

			event.getGuild().retrieveMetaData().queue((metaData) -> {

				int server = 0;

				for (JDA jda : RadioBot.INSTANCE.shardMan.getShards()) {
					server += jda.getGuilds().size();
				}

				builder.setAuthor("Joined " + event.getGuild().getName() + " (#" + server + ")!");
				builder.addField("Name", event.getGuild().getName(), true);
				builder.addField("Members", metaData.getApproximateMembers() + "", true);
				builder.addField("Boosts", event.getGuild().getBoostCount() + "", true);

				builder.addBlankField(false);

				builder.addField("ID", event.getGuild().getId(), true);
				builder.addField("Description", event.getGuild().getDescription() != null ? event.getGuild().getDescription() : "None", true);
				builder.addField("Created at", event.getGuild().getTimeCreated().format(DateTimeFormatter.ISO_LOCAL_DATE), true);

				builder.setFooter("Now in " + server+ " Servers!");

				builder.setThumbnail(event.getGuild().getIconUrl());

				builder.setColor(Color.green);

				TextChannel channel = event.getJDA().getTextChannelById(this.channelid);
				if (channel != null) {
					channel.sendMessageEmbeds(builder.build()).queue();
				} else {
					RadioBot.INSTANCE.logger.warning("Couldn't send Join message because the channelid is invalid");
				}


			});

		}

		if (event.getGuild().getSystemChannel() != null) {

			EmbedBuilder embed = new EmbedBuilder();
			embed.setAuthor("» I'm " + RadioBot.INSTANCE.getName() + " and " + event.getGuild().getName() + " is a nice place to be in!", null, event.getJDA().getSelfUser().getAvatarUrl());
			embed.setDescription("» Hey, I'm " + event.getJDA().getSelfUser().getAsMention() + "!\n» I can play music from many popular :radio: Radio-Stations all around the world. :earth_americas:\n\n» The default prefix is `" + RadioBot.INSTANCE.prefixMan.standard + "`, however, if you want to you can change it by typing: `" + RadioBot.INSTANCE.prefixMan.standard + "prefix <newprefix>` or " + event.getJDA().getSelfUser().getAsMention() + "` prefix <newprefix>`.\n\n» To get started, simply type `" + RadioBot.INSTANCE.prefixMan.standard + "radio` and select a country. Type the command shown there to start the playback.\n\n» Because of you, " + event.getJDA().getSelfUser().getAsMention() + " is now in " + event.getJDA().getGuilds().size() + " Servers. \n» Thanks for inviting me. Have fun!");
			embed.setColor(Color.RED);
			embed.setFooter("This Bot is developed by lxxrxtz#0472 and Ｋｉｍ Ｊｏｎｇ Ａｕｔｉｓｍ#0403. Check " + RadioBot.INSTANCE.prefixMan.standard + "credits for more info.");

			try {

				event.getGuild().getSystemChannel().sendMessageEmbeds(embed.build()).queue();

			} catch (InsufficientPermissionException ignored) {

			}
		}

	}

	@Override
	public void onGuildLeave(GuildLeaveEvent event) {

		MusicController controller = RadioBot.INSTANCE.playerManager.getController(event.getGuild());

		if (controller.player != null) {
			MusicUtil.reduceHandler(event.getGuild());
		}

		int server = 0;

		for (JDA jda : RadioBot.INSTANCE.shardMan.getShards()) {
			server += jda.getGuilds().size();
		}

		int count = server + 1;

		EmbedBuilder builder = new EmbedBuilder();

		builder.setAuthor("Left " + event.getGuild().getName() + " (#" + count + ")!");
		builder.addField("Name", event.getGuild().getName(), true);
		builder.addField("Members", "Unknown", true);
		builder.addField("Boosts", event.getGuild().getBoostCount() + "", true);

		builder.addBlankField(false);

		builder.addField("ID", event.getGuild().getId(), true);
		builder.addField("Description", event.getGuild().getDescription() != null ? event.getGuild().getDescription() : "None", true);
		builder.addField("Created at", event.getGuild().getTimeCreated().format(DateTimeFormatter.ISO_LOCAL_DATE), true);

		builder.setFooter("Now in " + server + " Servers!");

		builder.setThumbnail(event.getGuild().getIconUrl());

		builder.setColor(Color.red);

		TextChannel channel = event.getJDA().getTextChannelById(this.channelid);
		if (channel != null) {
			channel.sendMessageEmbeds(builder.build()).queue();
		} else {
			RadioBot.INSTANCE.logger.warning("Couldn't send Leave message because the channelid is invalid");
		}


		LiteSQL.preparedUpdate("DELETE FROM setting WHERE guildid = ?", event.getGuild().getIdLong());
		LiteSQL.preparedUpdate("DELETE FROM prefix WHERE guildid = ?", event.getGuild().getIdLong());

	}

}