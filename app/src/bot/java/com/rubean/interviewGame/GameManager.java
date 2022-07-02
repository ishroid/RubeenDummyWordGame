package com.rubean.interviewGame;

import java.util.LinkedHashSet;
import java.util.Random;

public class GameManager {
    private static final int GENERATED_WORD_LENGTH=5;
    private LinkedHashSet<String> allGameWords = new LinkedHashSet<>();
    private IGameMoveCallbacks moveCallback;
    private StringBuilder wordsSoFar = new StringBuilder();

    GameManager(IGameMoveCallbacks moveCallback){
        this.moveCallback=moveCallback;
    }

    public void addUserReply(String userReply) {
        String newAddedWord = userReply.replaceAll("^.*?(\\w+)\\W*$", "$1");

        if (allGameWords.contains(newAddedWord)){
            if (moveCallback!=null)
                moveCallback.onGameOver("USER: Lose the game | Duplicate word found "+newAddedWord);
        }else
            allGameWords.add(userReply);
    }

    public void addBotReply(String botReply){
        addUserReply(botReply);
    }

    String generateBotNextWord(){
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = new Random().nextInt(GENERATED_WORD_LENGTH) + 1; //TODO
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }
}
