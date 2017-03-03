/*
 * Agurk
 * Author: traillj
 */

package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import players.NoStrategyException;

import core.Game;

public class GameTest {

    @Test
    public void testNonLastTrick() {
        // 3 players
        Game game = new Game(3);
        System.out.println(game.showHands());
        
        // 7 cards per hand initially, last hand has different length
        for (int i = 0; i < 6; i++) {
            int prevHandLength = game.showHands().toString().length();
            
            try {
                game.playNonLastTrick();
            } catch (NoStrategyException e) {
                fail("Game initialised with non-AI player.");
            }
            
            // For 3 players the length should decrease by 6 each trick
            int handLength = game.showHands().toString().length();
            assertEquals(handLength, prevHandLength - 6);
            
            System.out.println(game.showHands());
        }
    }

}
