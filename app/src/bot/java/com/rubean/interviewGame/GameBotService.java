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

public class GameBotService extends Service {
    GameManager gameManager;
    private final Messenger gameCommandSenderMessenger;

    public GameBotService() {
        gameManager = new GameManager();
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

                boolean isUserCommandSuccess = gameManager.verifyUserNextMove(userCommand, reason -> {
                    sendData(reason,GameConstants.GAME_OVER,msg);
                });

                if (isUserCommandSuccess){
                    String botCommand = gameManager.generateBotNextWord();
                    boolean isBotCommandSuccess = gameManager.verifyBotNextMove(botCommand, reason -> {
                        sendData(reason,GameConstants.GAME_OVER,msg);
                    });

                    if (isBotCommandSuccess) {
                        sendData(gameManager.getFinalStringAfterBotMove(), GameConstants.GAME_BOT_ACTION, msg);
                    }
                }
            }
        }

        private void sendData(String data,int actionWhat,Message msg){
            Message botReplyCommandMessage = Message.obtain(null,actionWhat);
            Bundle botData = new Bundle();
            botData.putString(GameConstants.KEY_BOT_COMMAND,data);
            botReplyCommandMessage.setData(botData);

            try {
                msg.replyTo.send(botReplyCommandMessage);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

}