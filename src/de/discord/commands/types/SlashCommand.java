package de.discord.commands.types;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

public interface SlashCommand extends Command {

    void executeSlashCommand(SlashCommandEvent e);

    @NotNull
    String getDescription();

    OptionData[] getOptionData();

}
