package com.rubean.interviewGame;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.rubean.interviewGame.utils.GameConstants;

public class UserEntryActivity extends AppCompatActivity {

    private Messenger botCommandReceiver, userCommandSender;
    private boolean isServiceBounded = false;
    private TextView txtComm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_entry_activity);

//        txtComm = findViewById(R.id.txtComm);

        findViewById(R.id.btnSendCommand).setOnClickListener(v -> {
            sendUserCommand();
        });


        BotManager.bindToBotService(this,gameServiceConnection);
    }

    private void sendUserCommand(){
        if (isServiceBounded){
            Message userReplyCommandMessage = Message.obtain(null, GameConstants.GAME_USER_ACTION);
            userReplyCommandMessage.replyTo=botCommandReceiver;

            String userReplyCommand ="User Reply "+ botCommand ;
            Bundle botData = new Bundle();
            botData.putString("userCommand",userReplyCommand);
            userReplyCommandMessage.setData(botData);

            try {
                userCommandSender.send(userReplyCommandMessage);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    ServiceConnection gameServiceConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            isServiceBounded = true;
            userCommandSender = new Messenger(service);
            botCommandReceiver = new Messenger(new GameCommandReceiver());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            userCommandSender=null;
            botCommandReceiver=null;
            isServiceBounded = false;
        }
    };

    private  String botCommand="";
    private class GameCommandReceiver extends Handler {

        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == GameConstants.GAME_BOT_ACTION) {
                botCommand = msg.getData().getString("botCommand");
                txtComm.setText(botCommand);
            }
        }
    }

}