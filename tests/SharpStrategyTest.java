/*
 * Agurk
 * Author: traillj
 */

package tests;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import strategies.SharpStrategy;

public class SharpStrategyTest {

    @Test
    public void testChooseIndex() {
        List<Integer> hand = new LinkedList<Integer>();
        hand.add(2);
        hand.add(9);
        hand.add(13);
        
        SharpStrategy strategy = new SharpStrategy();
        int chosenIndex;
        
        // Play highest card
        chosenIndex = strategy.chooseIndex(hand, 8);
        assertEquals(chosenIndex, hand.size() - 1);
        
        // Play equal highest card
        chosenIndex = strategy.chooseIndex(hand, 13);
        assertEquals(chosenIndex, hand.size() - 1);
        
        // Play lowest card
        chosenIndex = strategy.chooseIndex(hand, 14);
        assertEquals(chosenIndex, 0);
    }

}
