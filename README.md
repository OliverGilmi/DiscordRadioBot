# DiscordRadioBot

## What is this?

This project contains a fully featured Discord-Radio-Bot made by Ｋｉｍ Ｊｏｎｇ Ａｕｔｉｓｍ#0403 and lxxrxtz#0472.<br> You can use it to **play online streams from radio stations** from all around the world while **categorizing them into countries**. You have the ability to **let the Bot play 24/7/365**, **control the volume**, **who can use the Bot**, **what stations are supported by the Bot** and many other things.<br><br>
It is written in Java using [JDA](https://github.com/DV8FromTheWorld/JDA) and [Lavaplayer](https://github.com/sedmelluq/lavaplayer).<br>
You can run it the way it is or modify it yourself, we will most likely not update this due to our time beeing very limited. Find out more on [what you're allowed to do](#what-can-i-do-with-this) and [how to set everything up for yourself](#getting-started-running-the-bot-yourself) below.

## What can I do with this?

If you download the files and follow the instructions below on how to setup this Bot, you can **run a Discord-Radio-Bot for you, your friends or even everyone**.<br>
You can find out more about what you're allowed to do with this code/project [here](https://github.com/OliverGilmi/DiscordRadioBot/blob/main/LICENSE).

## Getting started: Running the Bot yourself

### Requirements

- A PC running Windows, Mac or Linux and Java 8 or newer ([How to install Java?](https://www.java.com/de/download/help/download_options_de.html))<br>
*or*
- A V-Server/VPS running a Linux Distribution like Debian or Ubuntu with Java 8 or newer ([How to install Java?](https://docs.datastax.com/en/jdk-install/doc/jdk-install/installOpenJdkDeb.html))<br>[**Recommended** | Minimum Specs: 1 Core, 1 GB RAM]

### Creating a new Discord Bot-Application

1. Create a new Application/Bot in the [Discord Developer Portal](https://discord.com/developers/applications).<br>
2. Give your Application a name and click `Create`. (Don't worry, you can still change the name later!)<br>
3. Now you're in your Application's Dashboard. Here you can change it's name, profile picture and more.<br>
   You need to copy your Bots Application ID, you will need it in step 7.<br>
4. In the left sidebar, click on `Bot`, then click `Add Bot`.<br>
5. Now click `Yes, do it!`.<br>
6. Now that you've added a Bot, to your Application, decide, whether anyone should be able to add the Bot to their server or not. If not, uncheck the precheked option.<br> 
7. Keep the site open, you'll need it again later. In a new tab, open [this site](https://discordapi.com/permissions.html#8).<br>Paste the Application ID you copied in step 4 into the field `Insert client ID here`, afterwards click on the link below that.<br>
8. Now select the server, you want to add your Bot to, then click on `Continue`. Complete the other steps neccessary.<br>
 
 ### Downloading the Bot
 
 1. Go to the [Releases page](https://github.com/OliverGilmi/DiscordRadioBot/releases/tag/v1.0.0).
 2. Download the `files.zip` of the latest release.
 3. Now save the .zip-file somewhere on your computer.
 4. Then extract (unzip) the .zip-file into an *empty* folder. 
 
 ### Setting up the Bot
 
 1. You'll now have 3 files in the folder: `DiscordRadioBot.jar`, `settings.json` & `simple-windows-start.bat`.<br>
    Open `settings.json` with any text editor, like Notepad.<br>  
 2. Confirm that it looks something like this: 
    ```json
    {
      "owner": [],
      "prefix": "",
      "name": "",
      "version": "",
      "token": "",
      "status": [],
      "joinloggings": ""
     }
     ```
  3. If you haven't already, enable Discord Developer Mode by goint to `User settings > Advanced > Developer Mode` and turn that on.<br>    
  4. Go to any server now, right-click your name and then press `Copy ID`.<br>
  5. Now paste it into the `[]` of the `"owner"`-field, it should look similar to this: 
     ```json
     "owner": [439868330996924417],
     ```
  6. Think of a prefix for your Bot. This could be `!`, `rb!` or even `radio!`. Put it inbetween the `""` of the `"prefix"-field`, it then should look like this:
     ```json
     "prefix": "r!",
     ```    
  7. Now do the same for the `"name"` (Your Bot's name) & `"version"` (Your Bot's version) field.<br>
  8. Now comes the really important part: The Token. Go back to your Bot's Dashboard tab and copy the Token. Then paste it inbetween the `""` of the `"token"`-field.<br>
     Be careful not to delete, replace or add any character on accident, because it can cause your Bot to remain offline.<br>
     Also, never publish the token anywhere - ever. Otherwise, people could perform malicious actions with your application, so you would be responsible for it.<br>
  9. If you want to have custom status(es) for your Bot, fill them into the `[]` of the `"status"`. Insert mutliple ones by seperating them with a comma (`,`).
     Single status:
     ```json
     "status": ["Radio"],
     ```
     Multiple statuses:
     ```json
     "status": ["Radio", "only the best music"],
     ```  
 10. If you want to get a message everytime your Bot joins a new server, go to a channel, right-click it and hit `Copy ID`.<br>  
 11. Now paste it inbetween the `""` of the `"joinloggings"`-field, it should look similar to this: 
     ```json
     "joinloggings": "896710085521321995"
     ```
 12. Now the file should look something like this. If it does, save the file and then you're good to go.
     ```json
     {
       "owner": [439868330996924417],
       "prefix": "r!",
       "name": "Radio Bot",
       "version": "1.0",
       "token": "ODk2NTAxNjc5MzU5NDE4NDEw.CENSORED.FOR.SAFETY",
       "status": ["Radio"],
       "joinloggings": "896710085521321995"
     }
     ```
     (The Token doesn't look like this, we changed (censored) it for safety reasons explained in step 8.)

### Running the Bot

Now that you've configured the Bot, you're ready to run it. There are multiple ways to do that.<br>
First, you have to decide if you want to run the Bot on your computer or on a V-Server/VPS.<br>

**Running it on your Computer**

If you decided to run the Bot on your computer, follow the instructions below. The first way is always the recommended one.<br>

**Windows:**<br>
Method 1:

1. Hold `Shift` on your keyboard and right-click an empty spot in the folder containing your Bot's files.
2. Release both keys.
3. Click on `Open PowerShell window here`. (For Windows 11, you have to click `Show more options` first, then you'll find `Open PowerShell window here`.).
4. Type the following into there:
   ```
   java -jar DiscordRadioBot.jar
   ```
5. You'll now see some messages pop up, meaning your Bot is starting. Shortly after your Bot should appear as `Online` on Discord.
6. You can now minimize this window. *Do not close it!* That will kill the Bot instantly.
7. If you want to stop the Bot, simply type `stop`.

Method 2:

1. Double click the file. The Bot will start and should appear as `Online` on Discord. No window will be opened.
2. To stop the Bot, you need to `End Task` via Task-Manager, which is not recommended because the Bot won't be able to save and shut down afterwards properly.

**Mac OS:**<br>
Method 1:

1. Open a new Terminal Window. 
2. Using the `cd`-Command, navigate to the folder you stored your Bot-files in like this:
   ```
   cd path/to/your/botfiles
   ```
3. Then type the following into there:
   ```
   java -jar DiscordRadioBot.jar
   ```
4. You'll now see some messages pop up, meaning your Bot is starting. Shortly after your Bot should appear as `Online` on Discord.
5. You can now minimize this window. *Do not close it!* That will kill the Bot instantly.
6. If you want to stop the Bot, simply type `stop`.

**Running it on a V-Server/VPS**

1. Copy the 3 files (`DiscordRadioBot.jar`, `settings.json` & `simple-windows-start.bat`) into an *empty* folder on your server.
2. Open your Server's Terminal (SSH), e.g. using [Putty](https://www.putty.org).
3. Install Linux Screen ([What is Linux Screen](https://www.liquidweb.com/kb/how-to-use-the-screen-command-in-linux/#:~:text=What%20is%20Screen%3F,working%20on%20a%20server.)) using the following command:
   ```
   sudo apt install screen
   ```
4. Using the `cd`-Command, navigate to the folder you stored your Bot-files in like this:
   ```
   cd path/to/your/botfiles
   ```
5. Type the following command to start your Bot:
   ```
   screen -S DiscordRadioBot java -jar DiscordRadioBot.jar
   ```
6. You'll now see some messages pop up, meaning your Bot is starting. Shortly after your Bot should appear as `Online` on Discord.
7. Now hold `CTRL`, then press and release `A`, afterwards do the same with `D`. This will disconnect you from the screen, keeping the Bot online, so you can close the terminal window.
8. To get back into the Bot's console (screen), type: 
   ```
   screen -r DiscordRadioBot
   ```
9. If you want to stop the Bot, simply type `stop` *while in the Bot's console (screen)!* Otherwise you might shut down your entire server.

## Using the Bot

In the follwing guides, we will refer to the prefix for your Bot with `r!`. If you used something different, you'll have to add that instead.

### Viewing all Commands

To view all commands of the Bot, go to a Discord Text Channel the Bot can access and type `r!help` (or `/help`).<br>
The Bot will respond with a message, where all commands as well as additional information are listed. 

### Viewing & playing available Radio-Stations

To get a list of available radio stations per country, type `r!radio` (or `/radio`), then click on the Button with the flag of the county you want to have a list of.<br>Use the command corresponding to the station you want to play, to start the playback.<br>
If you've just set up your Bot, the lists will be empty and will need to be filled with stations. Read on, to learn how to do so.

### Adding new Radio-Stations

To add new stations to the Bot, your Discord-User-ID needs to be in the `"owner"`-field in the `settings.json` file.

Things you'll need:
- The Name of your station
- The Country your station operates in
- A stream url from/of the station you want to add
  You can obtain stream urls of many popular stations on a site like [fmstream.org](https://fmstream.org).
  
Adding the station to the Bot:
1. Type the following into the chat:
   ```
   r!radio -a `command|name|country|streamurl`
   ```
   Replace ... with:
   - `command` The part after your prefix (the actual command) that shall be used to start the station, e.g. `worldwide` (Then `r!worldwide` would be the command to start the station.)
   - `name` Replace with the actual name of your station. 
   - `country` Replace with one of [these countries](https://user-images.githubusercontent.com/64920118/136698260-c296cf50-c00d-4bcd-99a7-a3722fc67e0c.png).
   - `streamurl` Paste the URL of the stream here.

   Example of what your message should look like in the chat-box:
   ```
   r!radio -a `worldwide|WWR - WorldWide Radio|USA|https://example.com/stream/worldwideradio.aac`
   ```
2. Send the message.
3. The Bot will confirm that the station has been added. 
4. Check if your station appears in the `r!radio` (`/radio`) and play it using the command you chose, e.g. `r!worldwide` (or using /play worldwide).

### Removing a Radio-Station

To remove stations from the Bot, your Discord-User-ID needs to be in the `"owner"`-field in the `settings.json` file.

1. Type the following into the chat:
   ```
   r!radio -r command
   ```
   Replace `command` with your station's command. 
   
   Example of what your message should look like in the chat-box:
   ```
   r!radio -r worldwide
   ```
2. Send the message.
3. The Bot will confirm that the station has been removed.

### Getting the Bot into your channel

When you start a station, the Bot will automatically join your channel. If the Bot shall not play anything, you can also use the `r!join` (`/join`) command.

### Changing the Bot's Volume

To change the Bot's volume, you'll have to use the `r!volume` (`/volume`) command like in this example:
```
r!volume 50
```

### Stopping the Bot

To stop the Bot's playback, type `r!stop` (`/stop`).

### Letting the Bot leave your channel 

To let the Bot leave your channel, use the `r!leave` (`/leave`) command.

