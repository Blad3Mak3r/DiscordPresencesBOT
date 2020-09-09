<img align="right" src="https://cdn.discordapp.com/avatars/753165290732257300/647b616a0fb48c5b45694fa8f8e46821.png?size=256" height="200" width="200">

# Among Us ES [BOT]
 
AmongUsS is a bot designed for the Among Us Spanish Discord server. You are free to compile this project and use it for your own benefit.

This bot shows how many users are playing **Among Us** on the server.

## How to?

### Prerequisites
This project requires JVM 11 and Kotlin 1.4.0

### Compile
Run the following command in your terminal to create a jar file:
```shell
$ mvn package
```
Once the project is packaged, two **.jar** files will be created in the **target** folder, the jar with all the dependencies and manifest is **AmongUsES-jar-with-dependencies.jar**.

### Run the bot
To run the bot you need to define two environment variables or by default include their values as execution arguments.

#### Run with Environment Variables
| ENV NAME      | VALUE                                                                 |
|---------------|-----------------------------------------------------------------------|
| DISCORD_TOKEN | A Discord bot token.                                                  |
| GUILD_ID      | The ID of the Discord server for which you are going to use this bot. |

#### Run with execution arguments
```shell
$ java -jar target/AmongUsES-jar-with-dependencies.jar [DISCORD_TOKEN] [GUILD_ID]
```
``Replace the names with the values and do not include the []``
