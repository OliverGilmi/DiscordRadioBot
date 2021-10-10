# DiscordRadioBot

## What is this?

This project contains a fully featured Discord-Radio-Bot made by Ｋｉｍ Ｊｏｎｇ Ａｕｔｉｓｍ#0403 and lxxrxtz#0472.<br> You can use it to **play online streams from radio stations** from all around the world while **categorizing them into countries**. You have the ability to **control the volume**, **who can use the Bot**, **what stations are supported by the Bot** and many other things.<br><br>
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

### Creating a new Discord Bot

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

 4. Then extract the .zip-file into an *empty* folder. 
 
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


     
