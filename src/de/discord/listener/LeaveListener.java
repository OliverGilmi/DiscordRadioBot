package de.discord.listener;

import de.discord.core.RadioBot;
import de.discord.manage.managers.RadioStation;
import de.discord.manage.managers.Settings;
import de.discord.music.AudioPlayerSendHandler;
import de.discord.music.MusicController;
import de.discord.music.MusicUtil;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.StageChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LeaveListener extends ListenerAdapter {

	public static final Map<Long, RadioStation> datenschoner = new ConcurrentHashMap<>();

	@Override
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {

		if (event.getMember().equals(event.getGuild().getSelfMember())) {


			datenschoner.remove(event.getChannelLeft().getIdLong());

			MusicController controller = RadioBot.INSTANCE.playerManager.getController(event.getGuild());

			controller.currentStation = null;


			if (event.getGuild().getAudioManager().getSendingHandler() != null) {
				MusicUtil.reduceHandler(event.getGuild());
				event.getGuild().getAudioManager().setSendingHandler(null);
			}

			return;
		}



		if (event.getChannelLeft().getMembers().contains(event.getGuild().getMember(event.getJDA().getSelfUser()))) {

			int i = 0;
			for (Member m : event.getChannelLeft().getMembers()) {

				if (!m.getUser().isBot()) {
					i = i + 1;
				}
			}

			if (i == 0) {

				MusicController controller = RadioBot.INSTANCE.playerManager.getController(event.getGuild());
				
				if (event.getGuild().getAudioManager().getSendingHandler() == null) {
				event.getGuild().getAudioManager().closeAudioConnection();
				} else {
					((AudioPlayerSendHandler)event.getGuild().getAudioManager().getSendingHandler()).max--;
					event.getGuild().getAudioManager().setSendingHandler(null);
					datenschoner.put(event.getChannelLeft().getIdLong(), controller.currentStation);
				}
			}
		}
	}
	
	@Override
	public void onGuildVoiceMove(GuildVoiceMoveEvent event) {

		if (event.getMember().equals(event.getGuild().getSelfMember()) && (Boolean) RadioBot.INSTANCE.settings.getValue(event.getGuild().getIdLong(), Settings.Setting.StageSpeaker)) {

			if (event.getChannelJoined() instanceof StageChannel) {

				StageChannel stageChannel = (StageChannel) event.getChannelJoined();

				if (stageChannel.getStageInstance() != null) {

					if (!stageChannel.getStageInstance().getSpeakers().contains(event.getMember())) {

						stageChannel.getStageInstance().requestToSpeak().queue();

					}
				}
			}
		}

		if (event.getMember().getUser().isBot()) return;

		if (!event.getChannelJoined().getMembers().contains(event.getGuild().getSelfMember())) return;

		MusicController controller = RadioBot.INSTANCE.playerManager.getController(event.getGuild());

		if (event.getChannelLeft().getMembers().contains(event.getGuild().getMember(event.getJDA().getSelfUser()))) {

			int i = 0;
			for (Member m : event.getChannelLeft().getMembers()) {

				if (!m.getUser().isBot()) {
					i = i + 1;
				}

			}

			if (i == 0) {


				if (event.getGuild().getAudioManager().getSendingHandler() == null) {
					event.getGuild().getAudioManager().closeAudioConnection();
				} else {
					datenschoner.put(event.getChannelLeft().getIdLong(), controller.currentStation);
				}
			}
		} else if (event.getChannelJoined().getMembers().contains(event.getGuild().getSelfMember())) {

			int i = 0;
			for (Member m : event.getChannelJoined().getMembers()) {

				if (!m.getUser().isBot()) {
					i = i + 1;
				}
			}

			if (i == 1) {
				RadioStation t = datenschoner.remove(event.getChannelJoined().getIdLong());
				if (t != null) {
					RadioBot.INSTANCE.playerManager.playRadio(event.getGuild(), t, null, null, null);
				}
			}
		}
	}

	@Override
	public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {

		if (event.getMember().equals(event.getGuild().getSelfMember()) && (Boolean) RadioBot.INSTANCE.settings.getValue(event.getGuild().getIdLong(), Settings.Setting.StageSpeaker)) {

			if (event.getChannelJoined() instanceof StageChannel) {

				StageChannel stageChannel = (StageChannel) event.getChannelJoined();

				if (stageChannel.getStageInstance() != null) {

					if (!stageChannel.getStageInstance().getSpeakers().contains(event.getMember())) {

						stageChannel.getStageInstance().requestToSpeak().queue();

					}
				}
			}
		}

		if (event.getMember().getUser().isBot()) return;

		if (!event.getChannelJoined().getMembers().contains(event.getGuild().getSelfMember())) return;

		MusicController controller = RadioBot.INSTANCE.playerManager.getController(event.getGuild());

		int i = 0;
		for (Member m : event.getChannelJoined().getMembers()) {

			if (!m.getUser().isBot()) {
				i = i + 1;
			}
		}

		if (i == 1) {
			RadioStation station = datenschoner.remove(event.getChannelJoined().getIdLong());
			if (station != null)
				RadioBot.INSTANCE.playerManager.playRadio(controller.getGuild(), station, null, null, null);
		}
	}
}