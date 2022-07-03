# Rubeen Dummy Word Game

This open-source project contain two variant [bot, user] with default main app.

1. bot [package= "com.rubean.interviewGame.bot"]
2. main [package= "com.rubean.interviewGame"]
3. user [package= "com.rubean.interviewGame.user"]

## Product Flavors [bot]
- Just contain remote game service without any UI
- This will generate apk with package [com.rubean.interviewGame.bot]
- Handling all the logic of the game.
- [All bot related code is available here][PlDb]

## Base App [main]
- The main module that help us to distribute different variant of app.
- Base of project with package [com.rubean.interviewGame]
- This will work like a bridge between Game BOT and User.
- Through this module we are exposing our Game SDK as BotManager class,
- It contain all the logic related to remote bounded service connection and handler so our game user don't need to handle any android and lifecycle related stuff they will just get callbacks on the submitted actions.
- [All the related code is available here][fMain]

## Product Flavors [user]
- This will act as second player
- Have nice UI
- This will generate apk with package [com.rubean.interviewGame.user]
- To keep the code clean and following mvvm , i use exposed BotManager class to handle all the game related moves and actions etc.
- [All related code is available here][fUser]

## Installation & downloads
- You can directly download exported release APKs of both variant.
- [Download BOT APK from here][botAPK]
- [Download USER APK from here][userAPK]
- Install the bot app , it will just install as there is no UI so you will see nothing.
- Install the user app it have UI open the app and start the game.

## Development & Building
- Clone the project and open it in the Android Studio.
- Please give some time to Android Studio to build the project.
- Now you need to click on left bottom window "Build Variants", you will see list of available variants,choose one you wanted to run.

### Project classes
> I will use [bot] for the code which is under bot variant and [user] for the code under user vriant.
As i am following MVVM & clean architecture , the purpose of the classes being explained here.

- [bot] [GameBotService](/app/src/bot/java/com/rubean/interviewGame/GameBotService.java)
  This is remote service any one wanted to play game , must make service connection to communicate with bot,it contain GameCommandHandler that is being used to make two communication.
- [bot] [IGameMoveCallbacks](/app/src/bot/java/com/rubean/interviewGame/IGameMoveCallbacks.java)
  A interface that is being used only in [bot] to get callbacks on the service only if it's GameOver step.
- [bot] [GameManager](/app/src/bot/java/com/rubean/interviewGame/GameManager.java)
  The only class which have all the logic and possible conditions to handle bot, user and all the game move and actions.
- [main] [BotManager](/app/src/main/java/com/rubean/interviewGame/BotManager.java)
  I wrote this class to handle all the Android related stuff which is really not required to be handled by user app e.g. Service connection, Handler and other communication with remote service.
- [main] [IGameCallback](/app/src/main/java/com/rubean/interviewGame/callbacks/IGameCallback.java)
  main module expose IGameCallback interface for the user app to get all the callbacks from the remote service e.g. onServiceConnected,onGameOver etc.
- [user] [UserEntryActivity](/app/src/user/java/com/rubean/interviewGame/ui/UserEntryActivity.java)
  user variant is just about the Android UI no business logic is being handled here, ViewModel is communicating with BotManager and sending results using LiveData to user UI.

### My learning from this project
> While working on this project i learn how to deal with remote service in Android 11 or higher versions.
> While testing i found user app is not able to bind with game remote service on Android 11
> So i learn that, Android 11 introduces changes related to package visibility. These changes affect apps only if they target Android 11 or higher.

***Special Thanks to the creator of the "Interview test project".***
for creating such interesting task.

## License
**Free Software, Hell Yeah!**

[PlDb]: <https://github.com/ishroid/RubeenDummyWordGame/tree/main/app/src/bot>
[fMain]: <https://github.com/ishroid/RubeenDummyWordGame/tree/main/app/src/main>
[fUser]: <https://github.com/ishroid/RubeenDummyWordGame/tree/main/app/src/user>
[botAPK]: <https://github.com/ishroid/RubeenDummyWordGame/tree/main/release/app-bot-release.apk>
[userAPK]: <https://github.com/ishroid/RubeenDummyWordGame/tree/main/release/app-user-release.apk>
