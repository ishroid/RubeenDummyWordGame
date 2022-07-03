package com.rubean.interviewGame;

import java.util.HashSet;
import java.util.Random;

public class GameManager {
    private final HashSet<String> allGameWords = new HashSet<>();
    private final StringBuilder wordsSoFar = new StringBuilder();

    GameManager(){
    }

    /**
     * Verify user step and add to move
     * */
    public boolean verifyUserNextMove(String userReply, IGameMoveCallbacks moveCallback) {
         String[] userWords = userReply.split("\\s");
         String[] totalWordsTillNow = wordsSoFar.toString().split("\\s");
         String newAddedWord = userWords[userWords.length-1];

        if (isDuplicateWordAdded(newAddedWord,moveCallback)){
            return false;
        }else  if (isTwoOrMoreWordsTypeByUser(userWords,totalWordsTillNow,moveCallback)){
            return false;
        }else if(!isWordsAreInCorrectOrder(userWords,totalWordsTillNow,moveCallback)) {
            return false;
        }else if (isWordLengthNotValid(userWords,moveCallback)){
            return false;
        }else {
            if (wordsSoFar.length()>0){
                wordsSoFar.append(" ");
            }
            wordsSoFar.append(newAddedWord);
            allGameWords.add(newAddedWord);
            return true;
        }

    }

    /**
     * Verify bot step and add to move
     * */
    public boolean verifyBotNextMove(String newAddedWord, IGameMoveCallbacks moveCallback){
        if((Math.random() < 0.03)) {
            /*
             * As the bot can always repeat things easily and will never lose,
             *  add some probability that the bot gives back the answer
             * “TOO_MUCH_FOR_ME” and loses the game. Let the losing probability be 3%.
             * Otherwise make it respond with the correct answer.
             * You can choose new words for the bot as you’d like.
             * */
            if (moveCallback!=null)
                moveCallback.onGameOver("BOT: is unlucky and Lose the game \nTOO_MUCH_FOR_ME ");
            return false;
        }else{
            if (wordsSoFar.length()>0){
                wordsSoFar.append(" ");
            }
            wordsSoFar.append(newAddedWord);
            allGameWords.add(newAddedWord);
            return true;
        }
    }

    public String getFinalStringAfterBotMove(){
        return wordsSoFar.toString();
    }

    /**
     *  if user added more words and handle 1st entry if user add two words as 1st move
     *  If a player adds more then one word at a time, the player looses the game.
     * */
    private boolean isTwoOrMoreWordsTypeByUser(String[] userWords, String[] totalWordsTillNow, IGameMoveCallbacks moveCallback){
        boolean isWrongInitMove = (totalWordsTillNow.length==1 && userWords.length>1);
        int diffLength = (userWords.length-totalWordsTillNow.length);
        boolean isTwoOrMoreWordsTypeByUser =  isWrongInitMove || (totalWordsTillNow.length>1 && (diffLength>1));
        boolean isUserTypeLessWords =  (totalWordsTillNow.length>1 && (diffLength<1));

        String reasonString = "USER: Lose the game \n Type more then one word";
        if (isUserTypeLessWords){
            reasonString="USER: Lose the game \n user type less words";
        }
        if (moveCallback!=null && (isTwoOrMoreWordsTypeByUser || isUserTypeLessWords))
            moveCallback.onGameOver(reasonString);

        return isTwoOrMoreWordsTypeByUser;
    }

    /***
     * If the player adds a word that had already been played, the player looses the game.
     */
    private boolean isDuplicateWordAdded(String newAddedWord, IGameMoveCallbacks moveCallback){
        boolean isDuplicateWordAdded = allGameWords.contains(newAddedWord);
        if (moveCallback!=null && isDuplicateWordAdded)
            moveCallback.onGameOver("USER: Lose the game duplicate word found \n \""+newAddedWord+"\"");
        return isDuplicateWordAdded;
    }

    /**
     * repeats all the words that have been said in the game in the order they had been said.
     *  @param userWords words that user send
     * @param totalWordsTillNow all the words from the starting of the game.
     * @param moveCallback callback*/
    private boolean isWordsAreInCorrectOrder(String[] userWords, String[] totalWordsTillNow, IGameMoveCallbacks moveCallback){
        boolean isCorrectInOrder = false;
        String reasonString = "USER: Lose the game \n Words were not in correct order";
        if  (userWords.length>1){ // Checking if not 1st move
            for(int i =0; i<totalWordsTillNow.length ; i++){
                //Ignoring the newly added word in userWords we are just checking if all previous words are at same index
                String previousWordAtIndex = totalWordsTillNow[i];
                String userWordAtIndex = userWords[i];
                isCorrectInOrder= previousWordAtIndex.equals(userWordAtIndex);
                if (!isCorrectInOrder && !allGameWords.contains(userWordAtIndex)){
                    reasonString = "USER: Lose the game \n Unknown word \""+userWordAtIndex+"\" in the order";
                }
            }
        }else
            isCorrectInOrder=true;

        if (moveCallback!=null && !isCorrectInOrder)
            moveCallback.onGameOver(reasonString);

        return isCorrectInOrder;
    }

    /**
     * You can assume that nobody will be able to play more than 100 words, so after 100 words, do whatever you want.
     * */
    private boolean isWordLengthNotValid(String[] userWords, IGameMoveCallbacks moveCallback){
        boolean isWordLengthValid = userWords.length>99;
        if (moveCallback!=null && isWordLengthValid)
            moveCallback.onGameOver("USER: Lose the game \n As Limit of 100 words exceed ");
        return isWordLengthValid;
    }
    /**
     * Generate random word of max length 5 for the bot
     * */
    String generateBotNextWord(){
       String generatedWord = generateRandomWord();
       while (allGameWords.contains(generatedWord)){
           generatedWord = generateRandomWord();
       }
       return generatedWord;
    }

    private String generateRandomWord(){
        int GENERATED_WORD_LENGTH = 5;
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = new Random().nextInt(GENERATED_WORD_LENGTH) + 1;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }
}
