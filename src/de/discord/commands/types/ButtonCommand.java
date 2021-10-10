package de.discord.commands.types;

import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;

public interface ButtonCommand extends Command{

    void executeButton(ButtonClickEvent event);

}
