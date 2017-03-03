/*
 * Agurk
 * Author: traillj
 */

package core;

import players.NoStrategyException;


public class GameDriver {

    // temporary
    private static final int NUM_PLAYERS = 3;
    
    public static void main(String[] args) {
        Game game = new Game(NUM_PLAYERS);
        
        //System.out.print(game.showHands());
        
        /*try {
            game.playNonLastTrick();
        } catch (NoStrategyException e) {
            System.out.print("Non-AI Player's turn");
        }*/
    }

}
