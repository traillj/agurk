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
        GameInfo gameInfo = new GameInfo(3);
        
        // First trick
        TrickInfo trickInfo = new TrickInfo();
        trickInfo.updateTrickInfo(8, 0);
        trickInfo.updateTrickInfo(10, 1);
        trickInfo.updateTrickInfo(2, 2);
        
        gameInfo = new GameInfo(0, trickInfo, gameInfo.getScores(),
                gameInfo.getLives());
        
        // Test trick points
        List<Integer> expectedTrickPoints = new ArrayList<Integer>();
        expectedTrickPoints.add(0);
        expectedTrickPoints.add(10);
        expectedTrickPoints.add(0);
        assertEquals(
                expectedTrickPoints.containsAll(gameInfo.getTrickPoints()),
                true);
        
        // Test scores
        List<Integer> expectedScores = new ArrayList<Integer>();
        expectedScores.add(0);
        expectedScores.add(10);
        expectedScores.add(0);
        assertEquals(expectedScores.containsAll(gameInfo.getScores()), true);
        
        // Test Lives
        List<Integer> expectedLives = new ArrayList<Integer>();
        expectedLives.add(2);
        expectedLives.add(2);
        expectedLives.add(2);
        assertEquals(expectedLives.containsAll(gameInfo.getLives()), true);
        
        
        // Second trick
        trickInfo = new TrickInfo();
        trickInfo.updateTrickInfo(11, 0);
        trickInfo.updateTrickInfo(11, 1);
        trickInfo.updateTrickInfo(2, 2);
        
        gameInfo = new GameInfo(0, trickInfo, gameInfo.getScores(),
                gameInfo.getLives());
        
        // Test trick points
        expectedTrickPoints = new ArrayList<Integer>();
        expectedTrickPoints.add(-11);
        expectedTrickPoints.add(11);
        expectedTrickPoints.add(0);
        assertEquals(
                expectedTrickPoints.containsAll(gameInfo.getTrickPoints()),
                true);
        
        // Test scores
        expectedScores = new ArrayList<Integer>();
        expectedScores.add(0);
        expectedScores.add(11);
        expectedScores.add(0);
        assertEquals(expectedScores.containsAll(gameInfo.getScores()), true);
        
        // Test Lives
        expectedLives = new ArrayList<Integer>();
        expectedLives.add(2);
        expectedLives.add(1);
        expectedLives.add(2);
        assertEquals(expectedLives.containsAll(gameInfo.getLives()), true);
    }

}
