/*
 * Agurk
 * Author: traillj
 */

package players;


public class NoStrategyException extends Exception {

    public NoStrategyException() {
        super("Card cannot be chosen automatically.");
    }
}
