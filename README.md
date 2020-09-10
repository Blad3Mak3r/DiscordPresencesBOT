<img align="right" src="https://cdn.discordapp.com/avatars/753165290732257300/647b616a0fb48c5b45694fa8f8e46821.png?size=256" height="200" width="200">

# Discord Presences BOT
 
**Discord Presences BOT** is a bot designed for the Among Us Spanish Discord server. You are free to fork the code, build the project and use it for your own benefit.

This bot shows how many users are playing **Among Us** on the server, or whatever game you define in [environment variables](#run-with-environment-variables).

## How to?

### Prerequisites
This project requires JVM 11 and Kotlin 1.4.0

### Discord Bot Token
To be able to log in with a bot you need a Discord Bot Token, follow [our guide][DISCORD_BOT_TOKEN_WIKI] to get one and configure it correctly.

### Game Name and Application ID
|    Game Name    |   Application ID   |
|-----------------|--------------------|
| Among Us        | 746966631686733855 |
| Fall Guys       | 742897755160313986 |
| Minecraft       | 356875570916753438 |
| Overwatch       | 356875221078245376 |
| Terraria        | 356943499456937984 |
| Valorant        | 700136079562375258 |

### Compile
Run the following command in your terminal to create a jar file:
```shell
$ mvn package
```
Once the project is packaged, two **.jar** files will be created in the **target** folder, the jar with all the dependencies and manifest is **DPB-jar-with-dependencies.jar**.

### Run the bot
To run the bot you need to define two environment variables or by default include their values as execution arguments.

#### Run with Environment Variables
| ENV NAME          | VALUE                                                                 | Default Value                 |
|-------------------|-----------------------------------------------------------------------|-------------------------------|
| DISCORD_TOKEN     | A Discord bot token.                                                  | ``null``                      |
| GUILD_ID          | The ID of the Discord server for which you are going to use this bot. | ``null``                      |
| GAME_ID           | A valid Discord's Snowflake Application ID                            | 746966631686733855            |
| GAME_NAME         | The game name                                                         | Among us                      |
| PRESENCE_TEMPLATE | A string containing "{game}" and "{count}"                            | {game} with {count} members!! |

#### Run with execution arguments
```shell
$ java -jar target/DPB-jar-with-dependencies.jar [DISCORD_TOKEN] [GUILD_ID]
```
``Replace the names with the values and do not include the []``

[DISCORD_BOT_TOKEN_WIKI]: https://github.com/Blad3Mak3r/AmongUsES/wiki/Get-a-Discord-Bot-Token
