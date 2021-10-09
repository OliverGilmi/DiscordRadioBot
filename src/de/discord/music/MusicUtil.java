package de.discord.music;

import de.discord.core.RadioBot;
import de.discord.manage.managers.RadioStation;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MusicUtil {

	private static final Map<Long, TextChannel> channelMap = new ConcurrentHashMap<>();

	public static void UpdateChannel(TextChannel channel) {
		channelMap.put(channel.getGuild().getIdLong(), channel);
	}

	public static boolean voiceHasBot(VoiceChannel channel) {
		return channel.getMembers().contains(channel.getGuild().getMember(channel.getJDA().getSelfUser()));
	}

	public static void sendEmbed(long guildid, EmbedBuilder builder) {

		TextChannel channel = channelMap.get(guildid);

		if (channel != null) {
			channel.sendMessageEmbeds(builder.build()).queue();
		}
	}

	public static void reduceHandler(Guild guild) {
		MusicController controller = RadioBot.INSTANCE.playerManager.getController(guild);
		if (guild.getAudioManager().getSendingHandler() != null) {
			AudioPlayerSendHandler handler = (AudioPlayerSendHandler)guild.getAudioManager().getSendingHandler();
			--handler.max;
			if (handler.max < 1) {
				if (controller.currentStation != null) {
					RadioBot.INSTANCE.playerManager.players.remove(controller.currentStation);
				}

				if (controller.player != null) {
					RadioBot.INSTANCE.playerManager.sendHandlers.remove(controller.player);
					controller.player.destroy();
				}
			}
		}
	}

	public static void increaseHandler(Guild guild) {
		AudioPlayerSendHandler handler = (AudioPlayerSendHandler)guild.getAudioManager().getSendingHandler();
		if (handler == null) {
			MusicController controller = RadioBot.INSTANCE.playerManager.getController(guild);
			RadioStation station = controller.currentStation;
			if (station != null) {
				RadioBot.INSTANCE.playerManager.playRadio(guild, station, null, null, null);
			}
		} else {
			++handler.max;
		}
	}
}
