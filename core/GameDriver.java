/*
 * Agurk
 * Author: traillj
 */

package core;

import java.util.Scanner;


// Assumes Player indexed at 0 is the non-AI player
public class GameDriver {

    private static final int HAND_SIZE = 3;
    // Starting number of lives
    private static final int INITIAL_LIVES = 1;
    // Minimum points necessary to lose a life
    private static final int DEATH_POINTS = 16;
    
    private static final int PLAYER_LENGTH = 6;
    private static final int SUBHEADING_LENGTH = 6;
    private static final int MAX_SPACES = 5;
    
    private static int statsLineLength;
    
    // The player that leads the first trick
    private static int forehand = 0;
    private static int leadPlayer = forehand;
    
    // temporary
    private static final int NUM_PLAYERS = 4;
    
    public static void main(String[] args) {
        statsLineLength = NUM_PLAYERS * PLAYER_LENGTH + SUBHEADING_LENGTH;
        Game game = new Game(NUM_PLAYERS, HAND_SIZE, INITIAL_LIVES,
                DEATH_POINTS);
        
        while (!game.isGameOver()) {
            playRound(game);
        }
        
        if (game.hasLost()) {
            System.out.println("LOSE");
        } else {
            System.out.println("WIN");
        }
    }
    
    private static void playRound(Game game) {
        for (int i = 0; i < HAND_SIZE - 1; i++) {
            System.out.println(createLineBreak());
            playNonLastTrick(game);
        }
        
        GameInfo gameInfo = game.playLastTrick(leadPlayer);
        System.out.println();
        printLastTrick(game, gameInfo);
        
        System.out.println();
        printScores(game, gameInfo);
        
        int numRemainingPlayers = game.removeDeadPlayers();
        forehand = (forehand + 1) % numRemainingPlayers;
        leadPlayer = forehand;
    }
    
    private static void playNonLastTrick(Game game) {
        TrickInfo startTrickInfo = game.startNonLastTrick(leadPlayer);
        TrickInfo middleTrickInfo = nonAITurn(game, startTrickInfo);
        TrickInfo finishTrickInfo =
                game.finishNonLastTrick(middleTrickInfo, leadPlayer);
        
        printTrickCardsPlayed(startTrickInfo, middleTrickInfo,
                finishTrickInfo);
        leadPlayer = finishTrickInfo.highestPlayPlayer;
    }
    
    private static void printTrickCardsPlayed(TrickInfo startTrickInfo,
            TrickInfo middleTrickInfo, TrickInfo finishTrickInfo) {
        String cardsPlayedBefore =
                Deck.cardsToString(startTrickInfo.getCardsPlayed());
        String nonAICard =
                Deck.cardsToString(middleTrickInfo.getCardsPlayed());
        String cardsPlayedAfter =
                Deck.cardsToString(finishTrickInfo.getCardsPlayed());
        String allCardsPlayed;
        
        if (leadPlayer == 0) {
            allCardsPlayed = nonAICard + "," + cardsPlayedAfter;
        } else if (leadPlayer == 1) {
            allCardsPlayed = cardsPlayedBefore + "," + nonAICard;
        } else {
            allCardsPlayed = cardsPlayedBefore + "," + nonAICard
                    + "," + cardsPlayedAfter;
        }
        
        System.out.println("Played: " + allCardsPlayed);
    }
    
    private static TrickInfo nonAITurn(Game game, TrickInfo startTrickInfo) {
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
        
        TrickInfo middleTrickInfo = new TrickInfo(1);
        middleTrickInfo.updateTrickInfo(inValue, 0);
        if (startTrickInfo.highestPlay > inValue) {
            middleTrickInfo.highestPlay = startTrickInfo.highestPlay;
            middleTrickInfo.highestPlayPlayer =
                    startTrickInfo.highestPlayPlayer;
        }
        game.removeNonAICard(inValue);
        return middleTrickInfo;
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
        // Assumes highestPlay has been initialised to an invalid value
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
