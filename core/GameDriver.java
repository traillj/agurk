/*
 * Agurk
 * Author: traillj
 */

package core;

import players.NoStrategyException;


public class GameDriver {

    // temporary
    private static final int NUM_PLAYERS = 4;
    
    public static void main(String[] args) {
        Game game = new Game(NUM_PLAYERS);
        playTrick(game);
    }
    
    public static void playTrick(Game game) {
        TrickInfo startTrickInfo, finishTrickInfo;
        try {
            startTrickInfo = game.startNonLastTrick();
            StringBuilder cardsPlayedBefore =
                    Deck.cardsToString(startTrickInfo.cardsPlayed);
            System.out.println(cardsPlayedBefore);
            
            finishTrickInfo = game.finishNonLastTrick(startTrickInfo);
            StringBuilder cardsPlayedAfter =
                    Deck.cardsToString(finishTrickInfo.cardsPlayed);
            System.out.println(cardsPlayedAfter);
            
        } catch (NoStrategyException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

}
