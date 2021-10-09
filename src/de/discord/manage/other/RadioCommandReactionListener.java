package de.discord.manage.other;

import de.discord.core.RadioBot;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class RadioCommandReactionListener extends ListenerAdapter {

    private final Map<Long, Run> messageMap;

    private final Map<Long, Long> timerMap;

    public RadioCommandReactionListener() {
        messageMap = new ConcurrentHashMap<>();
        timerMap = new ConcurrentHashMap<>();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                onSecond();
            }
        };

        RadioBot.INSTANCE.timer.scheduleAtFixedRate(task, 1000, 1000);
    }

    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {
        if (messageMap.containsKey(event.getMessageIdLong())) {
            Run run = messageMap.get(event.getMessageIdLong());
            boolean correct = run.isCorrect(event);
            if (correct && run.deleteAfterSuccess()) {
                messageMap.remove(event.getMessageIdLong()).run(event);
                timerMap.remove(event.getMessageIdLong());
            } else if (correct){
                run.run(event);
            }
        }
    }

    public void removeEvent(long messageid) {
        messageMap.remove(messageid);
        timerMap.remove(messageid);
    }

    public void insertEvents(long messageid, long durrationMillis, Run run) {
        timerMap.put(messageid, durrationMillis + System.currentTimeMillis());
        messageMap.put(messageid, run);
    }

    public void updateTime(long messageid, long durration) {
        timerMap.put(messageid, durration + System.currentTimeMillis());
    }

    private void onSecond() {

        for (Long id : messageMap.keySet()) {
            if (timerMap.get(id) < System.currentTimeMillis()) {
                timerMap.remove(id);
                messageMap.remove(id).remove();
            }
        }
    }


    public interface Run {
        void run (GuildMessageReactionAddEvent event);

        boolean isCorrect (GuildMessageReactionAddEvent event);

        boolean deleteAfterSuccess();

        void remove ();
    }
}