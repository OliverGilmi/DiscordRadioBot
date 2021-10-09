package de.discord.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import de.discord.commands.types.Command;
import de.discord.commands.types.CommandAnnotation;
import de.discord.commands.types.SlashCommand;
import de.discord.core.Error;
import de.discord.core.RadioBot;
import de.discord.manage.other.Utils;
import de.discord.music.AudioPlayerSendHandler;
import de.discord.music.MusicController;
import de.discord.music.MusicUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@CommandAnnotation
public class StopCommand implements Command, SlashCommand {

	@Override
	public void performCommand(Member m, TextChannel channel, Message message, String prefix) {

		MusicController controller = RadioBot.INSTANCE.playerManager.getController(m.getGuild());
		AudioPlayer player = controller.getPlayer();

		//Error checking
		if (Utils.checkUserPermission(channel, m, this.getClass())) return;

		VoiceChannel vc;
		if ((vc = Objects.requireNonNull(m.getVoiceState()).getChannel()) == null) {
			Error.set(channel.getIdLong(), Error.Type.NoVC, getClass().getSimpleName());
			return;
		}

		if (!vc.getMembers().contains(m.getGuild().getMember(m.getJDA().getSelfUser()))) {
			Error.set(channel.getIdLong(), Error.Type.DifferentVC, getClass().getSimpleName());
			return;
		}

		if (player == null || player.getPlayingTrack() == null) {
			Error.set(channel.getIdLong(), Error.Type.NoTrackPlaying, getClass().getSimpleName());
			return;
		}

		//Executing



		MusicUtil.reduceHandler(m.getGuild());
		m.getGuild().getAudioManager().setSendingHandler(null);

		controller.currentStation = null;
		controller.player = null;

		MusicUtil.UpdateChannel(channel);

		message.addReaction("U+23F9").queue();
	}

	@Override
	public @NotNull String getName() {
		return "stop";
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
		return new String[]{"x"};
	}

	@Override
	public @NotNull EmbedBuilder getHelp(String prefix) {
		return getDefaultHelp(prefix);
	}

	@Override
	public void executeSlashCommand(SlashCommandEvent e) {

		if (e.getChannelType() != ChannelType.TEXT) {
			e.reply("This command can only be used on Servers.").queue();
			return;
		}

		MusicController controller = RadioBot.INSTANCE.playerManager.getController(e.getGuild());
		AudioPlayer player = controller.getPlayer();

		VoiceChannel vc;
		if ((vc = Objects.requireNonNull(e.getMember().getVoiceState()).getChannel()) == null) {
			Error.set(e.getTextChannel(), Error.Type.NoVC, getClass().getSimpleName(), e);
			return;
		}

		if (!vc.getMembers().contains(e.getMember().getGuild().getSelfMember())) {
			Error.set(e.getTextChannel(), Error.Type.DifferentVC, getClass().getSimpleName(), e);
			return;
		}

		if (player.getPlayingTrack() == null) {
			Error.set(e.getTextChannel(), Error.Type.NoTrackPlaying, getClass().getSimpleName(), e);
			return;
		}

		MusicUtil.reduceHandler(e.getGuild());

		controller.currentStation = null;
		controller.player = null;

		e.getGuild().getAudioManager().setSendingHandler(null);
		MusicUtil.UpdateChannel(e.getTextChannel());

		e.reply("Successfully stopped "+ RadioBot.INSTANCE.getName() +"!").queue(interactionHook -> interactionHook.deleteOriginal().queueAfter(5, TimeUnit.SECONDS));

	}

	@Override
	public @NotNull String getDescription() {
		return "This command stops the current radio station, that is playing";
	}

	@Override
	public OptionData[] getOptionData() {
		return new OptionData[0];
	}
}
