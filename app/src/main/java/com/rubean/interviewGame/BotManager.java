package com.rubean.interviewGame;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

public class BotManager {

    private static final String BOAT_ACTION_TRIGGERED  = "com.rubean.interviewGame.GameBotService.STARTORBIND";

    public static void bindToBotService(Activity activity, ServiceConnection userGameServiceConnection){
        Intent gameRemoteServiceIntent = new Intent(BOAT_ACTION_TRIGGERED);
        gameRemoteServiceIntent.setComponent(new ComponentName("com.rubean.interviewGame.bot","com.rubean.interviewGame.GameBotService"));
        activity.bindService(gameRemoteServiceIntent,userGameServiceConnection, Context.BIND_AUTO_CREATE);
    }

}
