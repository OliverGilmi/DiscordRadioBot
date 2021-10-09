package de.discord.core;


import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import de.discord.listener.CommandListener;
import de.discord.listener.LeaveListener;
import de.discord.listener.ServerJoinLeaveListener;
import de.discord.manage.managers.CommandManager;
import de.discord.manage.managers.PrefixManager;
import de.discord.manage.managers.RadioManager;
import de.discord.manage.managers.Settings;
import de.discord.manage.other.RadioCommandReactionListener;
import de.discord.manage.other.Utils;
import de.discord.manage.sql.LiteSQL;
import de.discord.music.PlayerManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class RadioBot {

	public static RadioBot INSTANCE;

	public final RadioCommandReactionListener reactionMan;
	public ShardManager shardMan;
	private final CommandManager cmdMan;
	public Timer timer;
	public AudioPlayerManager audioPlayerManager;
	public PlayerManager playerManager;
	public RadioManager radioMan;
	public PrefixManager prefixMan;
	public Settings settings;
	public Logger logger;

	public String botname;
	public JSONObject botOptions;
	public final String token;
	public final String invite;
	public int i;
	public boolean stopStatusChange = false;
	private final String[] status;

	public static void main(String[] args) {
		try {
			System.out.println("[main] INFO Starting Discord Radio Bot.");
			LiteSQL.connect();
			new RadioBot();
		} catch (LoginException e) {
			System.out.println("[main] ERROR The Token provided was invalid");
			System.out.println("[main] INFO Shutting down in 5 Seconds!");

			try {
				Thread.sleep(5000L);
			} catch (InterruptedException e2) {
				e2.printStackTrace();
			}

			System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public RadioBot() throws LoginException, IllegalArgumentException, IOException {

		INSTANCE = this;

		File json = new File("settings.json");
		if (!json.exists()) {
			try {
				if (json.createNewFile()) {
					FileWriter writer = new FileWriter(json);
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("token", "");
					jsonObject.put("name", "");
					jsonObject.put("version", "");
					jsonObject.put("status", new JSONArray());
					jsonObject.put("owner", new JSONArray());
					jsonObject.put("joinloggings", "");
					jsonObject.put("prefix", "");
					writer.write(jsonObject.toString(2));
					writer.close();
					System.out.println("[main] INFO Created setting.json File! For info check https://github.com/OliverGilmi/DiscordRadioBot/blob/main/README.md");
				} else {
					System.out.println("[main] ERROR An Error occured while creating the setting.json File, which is required.");
				}

				System.out.println("[main] INFO Shutting down in 5 Seconds!");
				Thread.sleep(5000L);
				System.exit(1);
			} catch (InterruptedException | IOException var6) {
				var6.printStackTrace();
				System.exit(2);
			}
		}

		this.botOptions = new JSONObject(new String(Files.readAllBytes(json.toPath()), StandardCharsets.UTF_8));
		if (!(botOptions.has("token") && botOptions.has("name") && botOptions.has("version") && botOptions.has("owner") && botOptions.has("joinloggings") && botOptions.has("prefix") && botOptions.has("status"))) {
			System.out.println("[main] ERROR settings.json is malformed! Check https://github.com/OliverGilmi/DiscordRadioBot/blob/main/README.md for help on the settings.json");
			System.out.println("[main] ERROR You can delete the settings.json in order to create a new settings.json file at the next start");
			System.out.println("[main] INFO Shutting down in 5 Seconds!");

			try {
				Thread.sleep(5000L);
			} catch (InterruptedException var5) {
				var5.printStackTrace();
			}

			System.exit(1);
		}

		this.token = this.botOptions.getString("token");
		if (this.token == null || this.token.length() == 0) {
			System.out.println("[main] ERROR The Bot's Token wasn't defined inside the settings.json file!");
			System.out.println("[main] INFO Shutting down in 5 Seconds!");

			try {
				Thread.sleep(5000L);
			} catch (InterruptedException var4) {
				var4.printStackTrace();
			}

			System.exit(1);
		}

		this.botname = this.botOptions.getString("name");
		if (this.botname == null || this.botname.length() == 0) {
			System.out.println("[main] INFO Bot name is empty! Using \"Radio Bot\" as default");
			this.botname = "Radio Bot";
		}

		if (this.botOptions.getJSONArray("owner") == null || this.botOptions.getJSONArray("owner").length() == 0) {
			System.out.println("[main] INFO No owner id's provided! Owner commands can't be used");
		}

		String standardPrefix;
		if (this.botOptions.getString("prefix") != null && this.botOptions.getString("prefix").length() != 0) {
			standardPrefix = this.botOptions.getString("prefix");
		} else {
			standardPrefix = "rb!";
			System.out.println("[main] INFO Prefix not set! Using standard prefix \"rb!\"");
		}

		String version;
		if (this.botOptions.getString("version") != null && this.botOptions.getString("version").length() != 0) {
			version = this.botOptions.getString("version");
		} else {
			version = "1.0.0";
			System.out.println("[main] INFO Prefix not set! Using \"1.0.0\" as default");
		}
		CommandListener.v = version;


		this.status = this.botOptions.getJSONArray("status") == null ? new String[0] : Arrays.copyOf(this.botOptions.getJSONArray("status").toList().toArray(), this.botOptions.getJSONArray("status").length(), String[].class);

		this.logger = new Logger();
		DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.create(GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_EMOJIS, GatewayIntent.DIRECT_MESSAGES);
		builder.disableCache(CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS, CacheFlag.ONLINE_STATUS, CacheFlag.MEMBER_OVERRIDES, CacheFlag.ROLE_TAGS);
		builder.setToken(token);

		this.timer = new Timer(true);

		this.audioPlayerManager = new DefaultAudioPlayerManager();
		this.playerManager = new PlayerManager();
		this.radioMan = new RadioManager();
		this.prefixMan = new PrefixManager(standardPrefix);
		this.reactionMan = new RadioCommandReactionListener();

		builder.addEventListeners(new CommandListener(), new LeaveListener(), new ServerJoinLeaveListener(), reactionMan);

		shardMan = builder.build();

		this.cmdMan = new CommandManager();
		this.settings = new Settings();
		AudioSourceManagers.registerRemoteSources(audioPlayerManager);
		audioPlayerManager.getConfiguration().setFilterHotSwapEnabled(true);

		this.invite = this.shardMan.getShards().get(0).getInviteUrl(Permission.MESSAGE_MANAGE, Permission.MESSAGE_WRITE, Permission.VOICE_SPEAK, Permission.VOICE_CONNECT, Permission.VIEW_CHANNEL, Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_ADD_REACTION);
		System.out.println("[main] INFO Invite: " + this.invite);

		Utils.SetupStuff();

		new ConsoleCommands();
		runLoop();
	}


	public void runLoop() {

		i = 0;

		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				if (!stopStatusChange) {
					onMinute(i);
					i++;
					i = i == status.length ? 0 : i;
				}
			}
		};

		this.timer.scheduleAtFixedRate(task, 2000, 60000);
	}

	public void onMinute(int i) {
		if (this.status.length != 0) {

			int server = 0;

			for (JDA jda : shardMan.getShards()) {
				server += jda.getGuilds().size();
			}

			int finalServer = server;

			this.shardMan.getShards().forEach((jda) -> {
				String text = this.status[i].replaceAll("%guilds", "" + finalServer);
				jda.getPresence().setActivity(Activity.playing(text));
			});

		}
	}

	public CommandManager getCmdMan() {
		return cmdMan;

	}

	public PrefixManager getPrefixMan() {
		return prefixMan;
	}

	public String getName() {
		return this.botname;
	}
}