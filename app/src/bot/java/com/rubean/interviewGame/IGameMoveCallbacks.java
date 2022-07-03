package com.rubean.interviewGame;

/**
 * A interface that is being used only in [bot] to get callbacks on the service only if it's GameOver step.
 * */
public interface IGameMoveCallbacks {
    void onGameOver(String reason);
}
