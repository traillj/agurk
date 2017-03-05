/*
 * Agurk
 * Author: traillj
 */

package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import core.GameInfo;
import core.TrickInfo;


public class GameInfoTest {

    @Test
    public void gameInfoTest() {
        int numPlayers = 3;
        GameInfo gameInfo = new GameInfo(numPlayers);
        
        // First trick
        TrickInfo trickInfo = new TrickInfo(numPlayers);
        trickInfo.updateTrickInfo(8, 0);
        trickInfo.updateTrickInfo(10, 1);
        trickInfo.updateTrickInfo(2, 2);
        
        gameInfo = new GameInfo(0, trickInfo, gameInfo.getGamePoints(),
                gameInfo.getLives());
        
        // Test trick points
        List<Integer> expectedTrickPoints = new ArrayList<Integer>();
        expectedTrickPoints.add(0);
        expectedTrickPoints.add(10);
        expectedTrickPoints.add(0);
        assertEquals(
                gameInfo.getTrickPoints().containsAll(expectedTrickPoints),
                true);
        
        // Test game points
        List<Integer> expectedGamePoints = new ArrayList<Integer>();
        expectedGamePoints.add(0);
        expectedGamePoints.add(10);
        expectedGamePoints.add(0);
        assertEquals(gameInfo.getGamePoints().containsAll(expectedGamePoints),
                true);
        
        // Test Lives
        List<Integer> expectedLives = new ArrayList<Integer>();
        expectedLives.add(2);
        expectedLives.add(2);
        expectedLives.add(2);
        assertEquals(gameInfo.getLives().containsAll(expectedLives), true);
        
        
        // Second trick
        trickInfo = new TrickInfo(numPlayers);
        trickInfo.updateTrickInfo(11, 0);
        trickInfo.updateTrickInfo(11, 1);
        trickInfo.updateTrickInfo(2, 2);
        
        gameInfo = new GameInfo(0, trickInfo, gameInfo.getGamePoints(),
                gameInfo.getLives());
        
        // Test trick points
        expectedTrickPoints = new ArrayList<Integer>();
        expectedTrickPoints.add(-11);
        expectedTrickPoints.add(11);
        expectedTrickPoints.add(0);
        assertEquals(
                gameInfo.getTrickPoints().containsAll(expectedTrickPoints),
                true);
        
        // Test game points
        expectedGamePoints = new ArrayList<Integer>();
        expectedGamePoints.add(0);
        expectedGamePoints.add(0);
        expectedGamePoints.add(0);
        assertEquals(gameInfo.getGamePoints().containsAll(expectedGamePoints),
                true);
        
        // Test Lives
        expectedLives = new ArrayList<Integer>();
        expectedLives.add(2);
        expectedLives.add(1);
        expectedLives.add(2);
        assertEquals(gameInfo.getLives().containsAll(expectedLives), true);
    }

}
