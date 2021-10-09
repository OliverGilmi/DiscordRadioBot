package de.discord.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import de.discord.commands.types.Command;
import de.discord.commands.types.CommandAnnotation;
import de.discord.commands.types.SlashCommand;
import de.discord.core.Error;
import de.discord.core.RadioBot;
import de.discord.manage.managers.RadioStation;
import de.discord.music.MusicController;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

@CommandAnnotation
public class NowPlayingCommand implements Command, SlashCommand {

	@Override
	public void performCommand(Member m, TextChannel channel, Message message, String prefix) {

		MusicController controller = RadioBot.INSTANCE.playerManager.getController(channel.getGuild());

		AudioPlayer player = controller.getPlayer();

		RadioStation station = controller.currentStation;

		if (player == null || station == null || player.getPlayingTrack() != null ) {
			Error.set(channel.getIdLong(), Error.Type.NoTrackPlaying, getClass().getSimpleName());
			return;
		}


		channel.sendMessageEmbeds(station.getEmbed(m).build()).queue();
	}

	@Override
	public @NotNull String getName() {
		return "nowplaying";
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
		return new String[]{"np", "cp", "currentlyplaying"};
	}

	@Override
	public @NotNull EmbedBuilder getHelp(String prefix) {
		return getDefaultHelp(prefix).appendDescription("This command shows what is currently playing");
	}

	@Override
	public void executeSlashCommand(SlashCommandEvent e) {

		if (e.getChannelType() != ChannelType.TEXT) {
			e.reply("This command can only be used on Servers.").queue();
			return;
		}

		performCommand(e.getMember(), e.getTextChannel(), null, null);

	}

	@Override
	public @NotNull String getDescription() {
		return "This command shows what is currently playing";
	}

	@Override
	public OptionData[] getOptionData() {
		return new OptionData[0];
	}
}