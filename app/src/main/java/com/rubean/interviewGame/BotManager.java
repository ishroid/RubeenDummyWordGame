package com.rubean.interviewGame;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import androidx.annotation.NonNull;

import com.rubean.interviewGame.callbacks.IGameCallback;
import com.rubean.interviewGame.utils.GameConstants;

public class BotManager {

    private static final String BOAT_ACTION_TRIGGERED  = "com.rubean.interviewGame.GameBotService.STARTORBIND";
    private Messenger botCommandReceiver, userCommandSender;
    private boolean isServiceBounded = false;
    private IGameCallback gameCallback;

    public BotManager(IGameCallback gameCallback){
        this.gameCallback=gameCallback;
    }

    public void bindToBotService(Activity activity){
        Intent gameRemoteServiceIntent = new Intent(BOAT_ACTION_TRIGGERED);
        gameRemoteServiceIntent.setComponent(new ComponentName("com.rubean.interviewGame.bot","com.rubean.interviewGame.GameBotService"));
        activity.bindService(gameRemoteServiceIntent,gameServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void unbindBotService(Activity activity){
        activity.unbindService(gameServiceConnection);
    }

    ServiceConnection gameServiceConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            isServiceBounded = true;
            userCommandSender = new Messenger(service);
            botCommandReceiver = new Messenger(new GameCommandReceiver(gameCallback));

            if (gameCallback!=null)
                gameCallback.onServiceConnected();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            userCommandSender=null;
            botCommandReceiver=null;
            isServiceBounded = false;

            if (gameCallback!=null)
                gameCallback.onServiceDisconnected();
        }
    };

   public void sendUserReplyCommand(String userReplyCommand){
        if (isServiceBounded){
            Message userReplyCommandMessage = Message.obtain(null, GameConstants.GAME_USER_ACTION);
            userReplyCommandMessage.replyTo=botCommandReceiver;

            Bundle botData = new Bundle();
            botData.putString(GameConstants.KEY_USER_COMMAND,userReplyCommand);
            userReplyCommandMessage.setData(botData);

            try {
                userCommandSender.send(userReplyCommandMessage);
                if (gameCallback!=null)
                    gameCallback.onSendCommandToBot(userReplyCommand);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private static class GameCommandReceiver extends Handler {
        IGameCallback gameCallback;
        GameCommandReceiver(IGameCallback gameCallback){
            super(Looper.getMainLooper());
            this.gameCallback=gameCallback;
        }
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == GameConstants.GAME_BOT_ACTION) {
                String botReplyCommand = msg.getData().getString(GameConstants.KEY_BOT_COMMAND);
                if (gameCallback!=null)
                    gameCallback.onReceiveBotReply(botReplyCommand);
            }else  if (msg.what == GameConstants.GAME_OVER) {
                String reason = msg.getData().getString(GameConstants.KEY_OVER_REASON);
                if (gameCallback!=null)
                    gameCallback.onGameOver(reason);
            }
        }
    }

}
