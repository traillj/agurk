/*
 * Agurk
 * Author: traillj
 */

package core;

import java.util.Scanner;


/**
 * A single player version of the card game Agurk.
 * In Danish rules: hand size = 7, initial lives = 2, death points = 21,
 * number of players = 2 to 7.
 * Assumes the player indexed at 0 is the non-AI player.
 */
public class GameDriver {

    /** Number of players. */
    private static final int NUM_PLAYERS = 4;
    /** Number of cards held by each player at the start of a trick. */
    private static final int HAND_SIZE = 3;
    /** Starting number of lives. */
    private static final int INITIAL_LIVES = 1;
    /** Minimum points necessary to lose a life. */
    private static final int DEATH_POINTS = 11;
    
    /** Length for each player when printing end of round information. */
    private static final int PLAYER_LENGTH = 6;
    /** Length for sub-headings when printing end of round information. */
    private static final int SUBHEADING_LENGTH = 6;
    
    /** Horizontal length of the end of round information. */
    private static int statsLength;
    
    /** The player that leads at the start of a round. */
    private static int forehand = 0;
    /** The player to lead the next trick. */
    private static int leadPlayer = forehand;
    
    
    /**
     * Loops rounds until the game is over.
     */
    public static void main(String[] args) {
        if (HAND_SIZE * NUM_PLAYERS > Deck.getDeckSize()) {
            System.out.println("Error: Hand size or number of players"
                    + " is too large.");
            System.exit(1);
        }
        
        statsLength = NUM_PLAYERS * PLAYER_LENGTH + SUBHEADING_LENGTH;
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
    
    /**
     * Plays a round of the game.
     * @param game Contains playing components and game information.
     */
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
    
    /**
     * Plays a trick in a round.
     * @param game Contains playing components and game information.
     */
    private static void playNonLastTrick(Game game) {
        TrickInfo startTrickInfo = game.startNonLastTrick(leadPlayer);
        TrickInfo middleTrickInfo = nonAITurn(game, startTrickInfo);
        TrickInfo finishTrickInfo =
                game.finishNonLastTrick(middleTrickInfo, leadPlayer);
        
        printTrickCardsPlayed(startTrickInfo, middleTrickInfo,
                finishTrickInfo);
        
        // The winner leads the next trick
        leadPlayer = finishTrickInfo.getHighestPlayPlayer();
    }
    
    /**
     * Prints all cards in the trick in the order played.
     * @param startTrickInfo Contains cards played before the non-AI player.
     * @param middleTrickInfo Contains the card played by the non-AI player.
     * @param finishTrickInfo Contains cards played after the non-AI player.
     */
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
    
    /**
     * Prints information, gets external input, verifies the input,
     * returns trick information containing the card.
     * @param game Contains playing components and game information.
     * @param startTrickInfo Contains cards played before the non-AI player.
     * @return Trick information containing the card played and the highest
     *         card and corresponding player of the trick so far.
     */
    private static TrickInfo nonAITurn(Game game, TrickInfo startTrickInfo) {
        printPreTurnInfo(game, startTrickInfo);
        Scanner reader = new Scanner(System.in);
        String in = reader.nextLine();
        
        int highestPlay = startTrickInfo.getHighestPlay();
        String nonAIHand = game.showNonAIHand();
        while (!isValidInput(in, highestPlay, nonAIHand)) {
            System.out.print("Invalid input\nChoose: ");
            in = reader.nextLine();
        }
        
        char inSymbol = in.charAt(0);
        int inValue = Deck.toValue(inSymbol);
        
        TrickInfo middleTrickInfo = new TrickInfo(1);
        middleTrickInfo.updateTrickInfo(inValue, 0);
        if (startTrickInfo.getHighestPlay() > inValue) {
            middleTrickInfo.setHighestPlay(startTrickInfo.getHighestPlay());
            middleTrickInfo.setHighestPlayPlayer(
                    startTrickInfo.getHighestPlayPlayer());
        }
        game.removeNonAICard(inValue);
        return middleTrickInfo;
    }
    
    /**
     * Prints the cards played in the trick so far and
     * the non-AI player's hand.
     * @param game Contains playing components and game information.
     * @param startTrickInfo Contains cards played before the non-AI player.
     */
    private static void printPreTurnInfo(Game game,
            TrickInfo startTrickInfo) {
        StringBuilder msg = new StringBuilder();
        msg.append("Played: ");
        msg.append(Deck.cardsToString(startTrickInfo.getCardsPlayed()));
        
        msg.append("\nHand: " + game.showNonAIHand());
        msg.append("\nChoose: ");
        System.out.print(msg);
    }
    
    /**
     * Verifies that the input represents a card symbol and
     * is valid in relation to the highest card played and
     * cards in hand.
     * @param in The external input.
     * @param highestPlay Highest card played in the trick.
     * @param hand Player's cards.
     * @return True if the input is valid.
     */
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
    
    
    /**
     * Prints the cards played and points awarded for the last trick.
     * @param game Contains playing components and game information.
     * @param gameInfo Up to date game information.
     */
    private static void printLastTrick(Game game, GameInfo gameInfo) {
        TrickInfo trickInfo = gameInfo.getTrickInfo();
        StringBuilder msg = new StringBuilder();
        
        msg.append(createHeadingLineBreak("Last Trick"));
        msg.append("\n");
        msg.append(createPlayerLine(game));
        msg.append("\n");
        msg.append(createLineBreak());
        
        msg.append("\nPlayed");
        for (Integer card : trickInfo.getCardsPlayed()) {
            msg.append("     " + Deck.toSymbol(card));
        }
        
        msg.append("\nPoints");
        for (Integer playerPoints : gameInfo.getTrickPoints()) {
            msg.append(leftPad(playerPoints, PLAYER_LENGTH));
        }
        
        msg.append("\n");
        msg.append(createLineBreak());
        System.out.println(msg);
    }
    
    /**
     * Prints the game points and lives.
     * @param game Contains playing components and game information.
     * @param gameInfo Up to date game information.
     */
    private static void printScores(Game game, GameInfo gameInfo) {
        StringBuilder msg = new StringBuilder();
        msg.append(createHeadingLineBreak("Scores"));
        msg.append("\n");
        msg.append(createPlayerLine(game));
        msg.append("\n");
        msg.append(createLineBreak());
        
        msg.append("\nPoints");
        for (Integer score : gameInfo.getGamePoints()) {
            msg.append(leftPad(score, PLAYER_LENGTH));
        }
        
        msg.append("\nLives ");
        for (Integer playerLives : gameInfo.getLives()) {
            msg.append(leftPad(playerLives, PLAYER_LENGTH));
        }
        
        msg.append("\n");
        msg.append(createLineBreak());
        System.out.println(msg);
    }
    
    /**
     * Fills the front of the item with spaces
     * to create a string of desired length.
     * If the desired length is smaller than the
     * item length, no padding is added.
     * @param item The item to be padded with spaces.
     * @param desiredLength Desired string length.
     * @return Left padded string.
     */
    private static String leftPad(Integer item, int desiredLength) {
        return leftPad(item.toString(), desiredLength);
    }
    
    /**
     * Fills the front of the item with spaces
     * to create a string of desired length.
     * If the desired length is smaller than the
     * item length, no padding is added.
     * @param item The item to be padded with spaces.
     * @param desiredLength Desired string length.
     * @return Left padded string.
     */
    private static String leftPad(String item, int desiredLength) {
        int itemLength = item.length();
        StringBuilder line = new StringBuilder();
        
        for (int i = itemLength; i < desiredLength; i++) {
            line.append(" ");
        }
        line.append(item);
        return line.toString();
    }
    
    /**
     * Creates a line of dashes with a heading in the middle.
     * @param heading String to be decorated with dashes.
     * @return The decorated heading.
     */
    private static String createHeadingLineBreak(String heading) {
        heading = " " + heading + " ";
        StringBuilder line = new StringBuilder();
        
        int halfNumHeadingDashes = (statsLength - heading.length()) / 2;
        for (int i = 0; i < halfNumHeadingDashes; i++) {
            line.append("-");
        }
        line.append(heading);
        
        for (int i = 0; i < halfNumHeadingDashes; i++) {
            line.append("-");
        }
        return line.toString();
    }
    
    /**
     * Creates a row of player names.
     * @param game Contains playing components and game information.
     * @return Formatted player names.
     */
    private static String createPlayerLine(Game game) {
        StringBuilder line = new StringBuilder();
        line.append("Player");
        
        for (String name : game.getPlayerNames()) {
            line.append(leftPad(name, PLAYER_LENGTH));

        }
        return line.toString();
    }
    
    /**
     * Creates a line of dashes.
     * @return A line break.
     */
    private static String createLineBreak() {
        StringBuilder lineBreak = new StringBuilder();
        for (int i = 0; i < statsLength; i++) {
            lineBreak.append("-");
        }
        return lineBreak.toString();
    }
}
