/*
 * Agurk
 * Author: traillj
 */

package core;

import java.util.ArrayList;
import java.util.List;


// Information for the last trick and overall scores and lives.
public class GameInfo {
    
    // Minimum score necessary to lose a life
    private static final int DEATH_SCORE = 21;
    private static final int INITIAL_LIVES = 2;
    
    private TrickInfo trickInfo;
    private List<Integer> trickPoints;
    private List<Integer> scores;
    private List<Integer> lives;
    
    // Initial game information, no tricks played.
    // Initialises scores and lives.
    public GameInfo(int numPlayers) {
        scores = new ArrayList<Integer>();
        for (int i = 0; i < numPlayers; i++) {
            scores.add(0);
        }
        
        lives = new ArrayList<Integer>();
        for (int i = 0; i < numPlayers; i++) {
            lives.add(INITIAL_LIVES);
        }
    }
    
    // Call after the trick is complete
    public GameInfo(int leadPlayer, TrickInfo trickInfo,
            List<Integer> prevScores, List<Integer> prevLives) {
        
        this.trickInfo = trickInfo;
        int numPlayers = prevLives.size();
        computeTrickPoints(leadPlayer, numPlayers);
        
        boolean lifeLost = updateScores(prevScores);
        updateLives(prevLives, lifeLost);
    }
    
    private void computeTrickPoints(int leadPlayer, int numPlayers) {
        trickPoints = new ArrayList<Integer>();
        for (int i = 0; i < numPlayers; i++) {
            trickPoints.add(0);
        }
        
        List<Integer> cardsPlayed = trickInfo.getCardsPlayed();
        for (int i = 0; i < numPlayers; i++) {
            int turn = (i + leadPlayer) % numPlayers;
            if (turn == trickInfo.highestPlayPlayer) {
                // Winner gets points equal to their card
                trickPoints.set(turn, trickInfo.highestPlay);
                
            } else if (cardsPlayed.get(turn) == trickInfo.highestPlay) {
                // deduct points from players who played same rank as winner
                trickPoints.set(turn, -trickInfo.highestPlay);
            }
        }
    }
    
    // Returns true if the trick winner lost a life
    private boolean updateScores(List<Integer> prevScores) {
        int numPlayers = prevScores.size();
        scores = new ArrayList<Integer>();
        for (int i = 0; i < numPlayers; i++) {
            scores.add(prevScores.get(i));
        }
        
        int winnerIndex = trickInfo.highestPlayPlayer;
        int winnerPrevScore = prevScores.get(winnerIndex);
        prevScores.set(winnerIndex, winnerPrevScore + trickInfo.highestPlay);
        
        boolean lifeLost = false;
        if (scores.get(winnerIndex) >= DEATH_SCORE) {
            lifeLost = true;
            // Reset to the next highest score
            scores.set(winnerIndex, 0);
            scores.set(winnerIndex, max(scores));
        }
        
        deductScores(prevScores);
        return lifeLost;
    }
    
    // Deduct points after the winner's
    // score has been completely updated
    private void deductScores(List<Integer> prevScores) {
        int numPlayers = prevScores.size();
        for (int i = 0; i < numPlayers; i++) {
            int playerTrickPoints = trickPoints.get(i);
            if (playerTrickPoints < 0) {
                int prevScore = prevScores.get(i);
                int newScore = prevScore + playerTrickPoints;
                
                if (newScore < 0) {
                    // Cannot have negative score
                    scores.set(i, 0);
                } else {
                    scores.set(i, newScore);
                }
            }
        }
    }
    
    private void updateLives(List<Integer> prevLives, boolean lifeLost) {
        int numPlayers = prevLives.size();
        lives = new ArrayList<Integer>();
        for (int i = 0; i < numPlayers; i++) {
            lives.add(prevLives.get(i));
        }
        
        if (lifeLost) {
            int winnerIndex = trickInfo.highestPlayPlayer;
            int winnerLives = lives.get(winnerIndex);
            lives.set(winnerIndex, winnerLives - 1);
        }
    }
    
    // Assumes list is not empty
    private int max(List<Integer> list) {
        int max = list.get(0);
        for (Integer item : list) {
            if (item > max) {
                max = item;
            }
        }
        return max;
    }

    public TrickInfo getTrickInfo() {
        return trickInfo;
    }

    public List<Integer> getTrickPoints() {
        return trickPoints;
    }

    public List<Integer> getScores() {
        return scores;
    }

    public List<Integer> getLives() {
        return lives;
    }
}
