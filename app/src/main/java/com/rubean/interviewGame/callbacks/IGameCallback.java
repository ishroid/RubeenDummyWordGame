package com.rubean.interviewGame.callbacks;

public interface IGameCallback {
    void onServiceConnected();
    void onServiceDisconnected();
    void onReceiveBotReply(String botCommand);
    void onSendCommandToBot(String userCommand);
    void onGameOver(String gameOverReason);
}
