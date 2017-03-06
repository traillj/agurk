/*
 * Agurk
 * Author: traillj
 */

package core;

import java.util.ArrayList;
import java.util.List;


// Information for the last trick, and for overall game points and lives.
public class GameInfo {
    
    private TrickInfo trickInfo;
    private List<Integer> trickPoints;
    private List<Integer> gamePoints;
    private List<Integer> lives;
    
    private int numAlive;
    private int deathPoints;
    
    // Initial game information, no tricks played.
    // Initialises game points and lives.
    public GameInfo(int numAlive, int initialLives, int deathPoints) {
        this.numAlive = numAlive;
        this.deathPoints = deathPoints;
        
        gamePoints = new ArrayList<Integer>();
        for (int i = 0; i < numAlive; i++) {
            gamePoints.add(0);
        }
        
        lives = new ArrayList<Integer>();
        for (int i = 0; i < numAlive; i++) {
            lives.add(initialLives);
        }
    }
    
    // Call after the trick is complete
    public GameInfo(int leadPlayer, TrickInfo trickInfo,
            GameInfo prevGameInfo) {
        this.trickInfo = trickInfo;
        numAlive = prevGameInfo.getNumAlive();
        deathPoints = prevGameInfo.getDeathPoints();
        List<Integer> prevGamePoints = prevGameInfo.getGamePoints();
        List<Integer> prevLives = prevGameInfo.getLives();
        
        computeTrickPoints(leadPlayer, numAlive);
        
        boolean lifeLost = updateGamePoints(prevGamePoints, prevLives);
        updateLives(prevLives, lifeLost);
    }
    
    private void computeTrickPoints(int leadPlayer, int numAlive) {
        trickPoints = new ArrayList<Integer>();
        for (int i = 0; i < numAlive; i++) {
            trickPoints.add(0);
        }
        
        List<Integer> cardsPlayed = trickInfo.getCardsPlayed();
        for (int i = 0; i < numAlive; i++) {
            int turn = (i + leadPlayer) % numAlive;
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
    private boolean updateGamePoints(List<Integer> prevGamePoints,
            List<Integer> prevLives) {
        gamePoints = new ArrayList<Integer>();
        for (int i = 0; i < numAlive; i++) {
            gamePoints.add(prevGamePoints.get(i));
        }
        
        int winnerIndex = trickInfo.highestPlayPlayer;
        int winnerPrevPoints = prevGamePoints.get(winnerIndex);
        gamePoints.set(winnerIndex, winnerPrevPoints + trickInfo.highestPlay);
        
        boolean lifeLost = false;
        if (gamePoints.get(winnerIndex) >= deathPoints) {
            lifeLost = true;
            
            if (prevLives.get(winnerIndex) > 1) {
                // Player not dead, reset to the next highest points
                gamePoints.set(winnerIndex, 0);
                gamePoints.set(winnerIndex, max(gamePoints));
            }
        }
        
        deductGamePoints(prevGamePoints);
        return lifeLost;
    }
    
    // Deduct points after the winner's points has been completely updated
    private void deductGamePoints(List<Integer> prevGamePoints) {
        for (int i = 0; i < numAlive; i++) {
            int playerTrickPoints = trickPoints.get(i);
            if (playerTrickPoints < 0) {
                int prevPoints = prevGamePoints.get(i);
                int newPoints = prevPoints + playerTrickPoints;
                
                if (newPoints < 0) {
                    // Cannot have negative points
                    gamePoints.set(i, 0);
                } else {
                    gamePoints.set(i, newPoints);
                }
            }
        }
    }
    
    private void updateLives(List<Integer> prevLives, boolean lifeLost) {
        lives = new ArrayList<Integer>();
        for (int i = 0; i < numAlive; i++) {
            lives.add(prevLives.get(i));
        }
        
        if (lifeLost) {
            int winnerIndex = trickInfo.highestPlayPlayer;
            int winnerLives = lives.get(winnerIndex);
            lives.set(winnerIndex, winnerLives - 1);
            numAlive--;
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
    
    public void removePlayer(int index) {
        trickPoints.remove(index);
        gamePoints.remove(index);
        lives.remove(index);
    }

    public TrickInfo getTrickInfo() {
        return trickInfo;
    }

    public List<Integer> getTrickPoints() {
        return trickPoints;
    }

    public List<Integer> getGamePoints() {
        return gamePoints;
    }

    public List<Integer> getLives() {
        return lives;
    }
    
    public int getNumAlive() {
        return numAlive;
    }
    
    public int getDeathPoints() {
        return deathPoints;
    }
}
