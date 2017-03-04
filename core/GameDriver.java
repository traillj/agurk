/*
 * Agurk
 * Author: traillj
 */

package core;


public class GameDriver {

    // temporary
    private static final int NUM_PLAYERS = 4;
    
    public static void main(String[] args) {
        Game game = new Game(NUM_PLAYERS);
        playTrick(game);
    }
    
    private static void playTrick(Game game) {
        TrickInfo startTrickInfo, finishTrickInfo;

        startTrickInfo = game.startNonLastTrick();
        String cardsPlayedBefore =
                Deck.cardsToString(startTrickInfo.cardsPlayed);
        System.out.println(cardsPlayedBefore);
        
        finishTrickInfo = game.finishNonLastTrick(startTrickInfo);
        String cardsPlayedAfter;
        cardsPlayedAfter = Deck.cardsToString(finishTrickInfo.cardsPlayed);

        System.out.println(cardsPlayedAfter);
    }
}
