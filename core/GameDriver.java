/*
 * Agurk
 * Author: traillj
 */

package core;

import java.util.Scanner;


public class GameDriver {

    private static final int PLAYER_LENGTH = 6;
    private static final int SUBHEADING_LENGTH = 6;
    private static final int MAX_SPACES = 5;
    
    private static int statsLineLength;
    
    // temporary
    private static final int NUM_PLAYERS = 4;
    
    public static void main(String[] args) {
        statsLineLength = NUM_PLAYERS * PLAYER_LENGTH + SUBHEADING_LENGTH;
        Game game = new Game(NUM_PLAYERS);
        playTrick(game);
        
        GameInfo gameInfo = game.playLastTrick();
        System.out.println();
        printLastTrick(game, gameInfo);
        
        System.out.println();
        printScores(game, gameInfo);
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
            System.out.print("Invalid input\nChoose: ");
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
    
    
    private static void printLastTrick(Game game, GameInfo gameInfo) {
        TrickInfo trickInfo = gameInfo.getTrickInfo();
        StringBuilder msg = new StringBuilder();
        
        msg.append(createTitleLineBreak("Last Trick"));
        msg.append("\n");
        msg.append(createPlayerLine(game));
        msg.append("\n");
        msg.append(createLineBreak());
        
        msg.append("\nPlayed");
        for (Integer card : trickInfo.getCardsPlayed()) {
            msg.append("     " + Deck.toSymbol(card));
        }
        
        msg.append("\nPoints");
        int numLength;
        for (Integer playerPoints : gameInfo.getTrickPoints()) {
            
            numLength = playerPoints.toString().length();
            for (int i = numLength - 1; i < MAX_SPACES; i++) {
                msg.append(" ");
            }
            msg.append(playerPoints);
        }
        
        msg.append("\n");
        msg.append(createLineBreak());
        System.out.println(msg);
    }
    
    private static void printScores(Game game, GameInfo gameInfo) {
        StringBuilder msg = new StringBuilder();
        msg.append(createTitleLineBreak("Scores"));
        msg.append("\n");
        msg.append(createPlayerLine(game));
        msg.append("\n");
        msg.append(createLineBreak());
        
        msg.append("\nPoints");
        int numLength;
        for (Integer score : gameInfo.getGamePoints()) {
            
            numLength = score.toString().length();
            for (int i = numLength - 1; i < MAX_SPACES; i++) {
                msg.append(" ");
            }
            msg.append(score);
        }
        
        msg.append("\nLives ");
        for (Integer playerLives : gameInfo.getLives()) {
            msg.append("     " + playerLives);
        }
        
        msg.append("\n");
        msg.append(createLineBreak());
        System.out.println(msg);
    }
    
    private static String createTitleLineBreak(String title) {
        title = " " + title + " ";
        StringBuilder line = new StringBuilder();
        
        int halfNumTitleDashes = (statsLineLength - title.length()) / 2;
        for (int i = 0; i < halfNumTitleDashes; i++) {
            line.append("-");
        }
        line.append(title);
        
        for (int i = 0; i < halfNumTitleDashes; i++) {
            line.append("-");
        }
        return line.toString();
    }
    
    private static String createPlayerLine(Game game) {
        StringBuilder line = new StringBuilder();
        line.append("Player");
        for (String name : game.getPlayerNames()) {
            line.append("     " + name);
        }
        return line.toString();
    }
    
    private static String createLineBreak() {
        StringBuilder lineBreak = new StringBuilder();
        for (int i = 0; i < statsLineLength; i++) {
            lineBreak.append("-");
        }
        return lineBreak.toString();
    }
}
