package com.rubean.interviewGame.ui;

import android.app.Activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rubean.interviewGame.BotManager;
import com.rubean.interviewGame.callbacks.IGameCallback;
import com.rubean.interviewGame.models.MoveActionModel;

import java.util.ArrayList;
import java.util.List;

public class UserEntryViewModel extends ViewModel implements IGameCallback {
    private final BotManager botManager = new BotManager(this);

    private final ArrayList<MoveActionModel> actionModelArrayList = new ArrayList<>();
    private final MutableLiveData<List<MoveActionModel>> actionLiveData = new MutableLiveData<List<MoveActionModel>>();
    private final MutableLiveData<Boolean> serviceConnectionLiveData = new MutableLiveData<Boolean>();
    private final MutableLiveData<String> gameOverLiveData = new MutableLiveData<String>();

    public LiveData<List<MoveActionModel>> getActionLiveData(){
        return actionLiveData;
    }

    public LiveData<Boolean> getServiceConnectionLiveData(){
        return serviceConnectionLiveData;
    }

    public LiveData<String> getGameOverLiveData(){
        return gameOverLiveData;
    }

    private void addBotActionItem(String botCommand){
        actionModelArrayList.add(new MoveActionModel("BOT:",botCommand));
        actionLiveData.setValue(actionModelArrayList);
    }

    private void addUserActionItem(String userCommand){
        actionModelArrayList.add(new MoveActionModel("USER:",userCommand));
        actionLiveData.setValue(actionModelArrayList);
    }

    public void sendUserCommand(String userReplyCommand) {
        botManager.sendUserReplyCommand(userReplyCommand);
    }

    public void bindToGameBotService(Activity activity) {
        botManager.bindToBotService(activity);
    }

    public void unbindGameBotService(Activity activity){
        botManager.unbindBotService(activity);
    }

    @Override
    public void onServiceConnected() {
        serviceConnectionLiveData.setValue(true);
    }

    @Override
    public void onServiceDisconnected() {
        serviceConnectionLiveData.setValue(false);
    }

    @Override
    public void onReceiveBotReply(String botCommand) {
        addBotActionItem(botCommand);
    }

    @Override
    public void onSendCommandToBot(String userCommand) {
        addUserActionItem(userCommand);
    }

    @Override
    public void onGameOver(String gameOverReason) {
        gameOverLiveData.setValue(gameOverReason);
    }

    public void clearUI() {
        actionModelArrayList.clear();
        actionLiveData.setValue(actionModelArrayList);
    }
}
