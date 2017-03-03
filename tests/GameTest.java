/*
 * Agurk
 * Author: traillj
 */

package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import players.NoStrategyException;

import core.Game;
import core.TrickInfo;

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
                TrickInfo startTrickInfo = game.startNonLastTrick();
                game.finishNonLastTrick(startTrickInfo);
            } catch (NoStrategyException e) {
                fail(e.getMessage());
            }
            
            System.out.println(game.showHands());
            
            // For 3 players the length should decrease by 4 each trick
            // as Player 0 (non-AI player) is ignored
            int handLength = game.showHands().toString().length();
            assertEquals(handLength, prevHandLength - 4);
        }
    }

}
