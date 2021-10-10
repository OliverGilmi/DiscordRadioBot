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
 ([Show as image](https://user-images.githubusercontent.com/64920118/136673461-d9fa377e-374f-4f1d-b8a5-7b7f0fcf2fd7.png))
 
2. Give your Application a name and click `Create`. (Don't worry, you can still change the name later!)<br>
 ([Show as image](https://user-images.githubusercontent.com/64920118/136673523-b5b35e1e-3ebd-431c-84d3-77017ab984c7.png))

3. Now you're in your Application's Dashboard. Here you can change it's name, profile picture and more.<br>
   You need to copy your Bots Application ID, you will need it in step 7.<br>
 ([Show as image](https://user-images.githubusercontent.com/64920118/136673656-1337e9de-1aa8-4e37-a61c-776916a4584a.png))

4. In the left sidebar, click on `Bot`, then click `Add Bot`.<br>
 ([Show as image](https://user-images.githubusercontent.com/64920118/136673675-89175c9e-1c60-45f3-8ec6-9fce9093b5b6.png))

5. Now click `Yes, do it!`.<br>
 ([Show as image](https://user-images.githubusercontent.com/64920118/136673722-fce18150-6e38-40a5-98fa-127d705907f9.png))

6. Now that you've added a Bot, to your Application, decide, whether anyone should be able to add the Bot to their server or not. If not, uncheck the precheked option.<br>
 ([Show as image](https://user-images.githubusercontent.com/64920118/136673811-06359d07-f690-411f-a89e-a956e82db6d0.png))
 
7. Keep the site open, you'll need it again later. In a new tab, open [this site](https://discordapi.com/permissions.html#8).<br>Paste the Application ID you copied in step 4 into the field `Insert client ID here`, afterwards click on the link below that.<br>
 ([Show as image](https://user-images.githubusercontent.com/64920118/136673996-2be73e39-de9b-4b8e-b599-a4252f5e4233.png))

8. Now select the server, you want to add your Bot to, then click on `Continue`. Complete the other steps neccessary.<br>
 ([Show as image](https://user-images.githubusercontent.com/64920118/136674048-ef3b4980-5ae9-41c6-8293-aeed289a5126.png))
 
 ### Downloading the Bot
 
 ### Setting up the Bot
 
 1. You'll now have 3 files in the folder: `DiscordRadioBot.jar`, `settings.json` & `simple-windows-start.bat`.<br>
    Open `settings.json` with any text editor, like Notepad.<br>
    ([Show as image](https://user-images.githubusercontent.com/64920118/136689745-72c374de-8903-4e01-9f60-05e74b0b2cae.png))
    
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
     ([Show as image](https://user-images.githubusercontent.com/64920118/136690769-f89b71a0-1849-41bf-8fd2-5452689dbc9c.png))
  
  3. If you haven't already, enable Discord Developer Mode by goint to `User settings > Advanced > Developer Mode` and turn that on.<br>
     ([Show as image](https://user-images.githubusercontent.com/64920118/136690816-e33e9a76-4621-4cde-a5ea-59d7c1cab317.png))
     
  4. Go to any server now, right-click your name and then press `Copy ID`.<br>
     ([Show as image](https://user-images.githubusercontent.com/64920118/136690975-edfdf00e-b2a4-4c2f-8473-0a5d31d9c4ea.png))

  5. Now paste it into the `[]` of the `"owner"`-field, it should look similar to this: 
  ```
  "owner": [439868330996924417],
  ```
  ([Show as image](https://user-images.githubusercontent.com/64920118/136691022-a9ab8893-103e-4688-b7c6-551723511b40.png))


