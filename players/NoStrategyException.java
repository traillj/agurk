/*
 * Agurk
 * Author: traillj
 */

package players;

public class NoStrategyException extends Exception {

    public NoStrategyException() {
        super("Error: Player has no strategy.");
    }
}
