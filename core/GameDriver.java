/*
 * Agurk
 * Author: traillj
 */

package core;

import java.util.Scanner;


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
        String cardsPlayedBefore;
        cardsPlayedBefore = Deck.cardsToString(startTrickInfo.getCardsPlayed());
        
        char nonAICard = nonAITurn(game, startTrickInfo);
        
        finishTrickInfo = game.finishNonLastTrick(startTrickInfo);
        String cardsPlayedAfter;
        cardsPlayedAfter = Deck.cardsToString(finishTrickInfo.getCardsPlayed());

        System.out.println("Played: " + cardsPlayedBefore + ","
                + nonAICard + "," + cardsPlayedAfter);
    }
    
    private static char nonAITurn(Game game, TrickInfo startTrickInfo) {
        printPreTurnInfo(game, startTrickInfo);
        Scanner reader = new Scanner(System.in);
        String in = reader.nextLine();
        
        String nonAIHand = game.showNonAIHand();
        while (!isValidInput(in, startTrickInfo.highestPlay, nonAIHand)) {
            System.out.println("Invalid input\nChoose: ");
            in = reader.nextLine();
        }
        
        // Remove and return the chosen card
        char inSymbol = in.charAt(0);
        int inValue = Deck.toValue(inSymbol);
        game.removeNonAICard(inValue);
        return inSymbol;
    }
    
    private static void printPreTurnInfo(Game game,
            TrickInfo startTrickInfo) {
        StringBuilder msg = new StringBuilder();
        msg.append("Played: ");
        msg.append(Deck.cardsToString(startTrickInfo.getCardsPlayed()));
        
        msg.append("\nHand: " + game.showNonAIHand());
        msg.append("\nChoose: ");
        System.out.print(msg);
    }
    
    private static boolean isValidInput(String in, int highestPlay,
            String hand) {
        in = in.trim();
        if (!in.matches("^([2-9]|T|J|Q|K|A)$")) {
            return false;
        }
        
        // Can play any card in hand if leading the trick
        // Assumes highestPlay has been initialised
        if (!Deck.isValidValue(highestPlay)) {
            return hand.contains(in);
        }
        
        char inSymbol = in.charAt(0);
        
        // Can always play lowest card
        char lowestCard = hand.charAt(0);
        if (inSymbol == lowestCard) {
            return true;
        }
        
        // Can play any card of higher or equal value
        // to the highest card so far in the trick
        int inValue = Deck.toValue(inSymbol);
        if (inValue >= highestPlay) {
            return true;
        }
        
        return false;
    }
    
}
