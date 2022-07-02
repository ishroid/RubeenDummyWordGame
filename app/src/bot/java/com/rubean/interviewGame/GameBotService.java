package com.rubean.interviewGame;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import androidx.annotation.NonNull;

import com.rubean.interviewGame.utils.GameConstants;
import com.rubean.interviewGame.utils.Utilities;

public class GameBotService extends Service implements IGameMoveCallbacks {
    GameManager gameManager;
    private final Messenger gameCommandSenderMessenger;

    public GameBotService() {
        gameManager = new GameManager(this);
        gameCommandSenderMessenger = new Messenger(new GameCommandHandler(gameManager));
    }

    @Override
    public void onCreate() {super.onCreate();}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return gameCommandSenderMessenger.getBinder();
    }

    @Override
    public void onGameOver(String reason) {
        Message botReplyCommandMessage = Message.obtain(null,GameConstants.GAME_OVER);
        Bundle botData = new Bundle();
        botData.putString(GameConstants.KEY_OVER_REASON,reason);
        botReplyCommandMessage.setData(botData);
        try {
            gameCommandSenderMessenger.send(botReplyCommandMessage);
        }catch (Exception er){
            er.printStackTrace();
        }
    }

    private static class GameCommandHandler extends Handler {
        GameManager gameManager;

        GameCommandHandler(GameManager gameManager){
            super(Looper.getMainLooper());
            this.gameManager=gameManager;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == GameConstants.GAME_USER_ACTION) {
                String userCommand = msg.getData().getString(GameConstants.KEY_USER_COMMAND);
                gameManager.addUserReply(userCommand);

                Message botReplyCommandMessage = Message.obtain(null,GameConstants.GAME_BOT_ACTION);
                String botCommand = gameManager.generateBotNextWord();
                Bundle botData = new Bundle();
                botData.putString(GameConstants.KEY_BOT_COMMAND,botCommand);
                botReplyCommandMessage.setData(botData);

                try {
                    msg.replyTo.send(botReplyCommandMessage);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}