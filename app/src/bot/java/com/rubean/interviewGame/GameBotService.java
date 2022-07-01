package com.rubean.interviewGame;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import androidx.annotation.NonNull;

import com.rubean.interviewGame.utils.GameConstants;
import com.rubean.interviewGame.utils.Utilities;

public class GameBotService extends Service {
    public GameBotService() {}

    @Override
    public void onCreate() {
        super.onCreate();
        Utilities.showToast("onCreate GameBotService",this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Utilities.showToast("onStartCommand GameBotService",this);
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public IBinder onBind(Intent intent) {
        Utilities.showToast("onBind GameBotService",this);
        return gameCommandSenderMessenger.getBinder();
    }


    private Messenger gameCommandSenderMessenger = new Messenger(new GameCommandHandler());
    private class GameCommandHandler extends Handler {

        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == GameConstants.GAME_USER_ACTION) {
                String userCommand = msg.getData().getString("userCommand");
                Utilities.showToast(userCommand+ " Received",GameBotService.this);

                Message botReplyCommandMessage = Message.obtain(null,GameConstants.GAME_BOT_ACTION);
                String botCommand ="Bot Reply "+ userCommand ;
                Bundle botData = new Bundle();
                botData.putString("botCommand",botCommand);
                botReplyCommandMessage.setData(botData);

                try {
                    msg.replyTo.send(botReplyCommandMessage);
                    Utilities.showToast("Message sent successfull",GameBotService.this);
                } catch (RemoteException e) {
                    Utilities.showToast(e.toString(),GameBotService.this);
                    e.printStackTrace();
                }
            }else{
                Utilities.showToast(" msg.what "+msg.what,GameBotService.this);
            }
        }

    }

}