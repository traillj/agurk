/*
 * Agurk
 * Author: traillj
 */

package tests;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

import core.GameDriver;


public class GameDriverTest {

    @Test
    public void isValidInputTest() {
        Method method;
        try {
            method = GameDriver.class.getDeclaredMethod("isValidInput",
                    String.class, int.class, String.class);
            method.setAccessible(true);
            boolean actual;
            
            // Test invalid card
            actual = (Boolean)method.invoke(null, "10", 0, "T");
            assertEquals(actual, false);
            
            // Test valid card not in hand
            actual = (Boolean)method.invoke(null, "9", 0, "T");
            assertEquals(actual, false);
            
            // Test lowest card
            actual = (Boolean)method.invoke(null, "3", 8, "3,9");
            assertEquals(actual, true);
            
            // Test equal card
            actual = (Boolean)method.invoke(null, "8", 8, "3,8");
            assertEquals(actual, true);
            
            // Test higher card
            actual = (Boolean)method.invoke(null, "T", 8, "3,T");
            assertEquals(actual, true);
            
            // Test second lowest card
            actual = (Boolean)method.invoke(null, "4", 8, "3,4,T");
            assertEquals(actual, false);
            
        } catch (NoSuchMethodException | SecurityException e) {
            fail("Reflection Exception"); 
        } catch (IllegalAccessException e) {
            fail("Reflection Exception"); 
        } catch (IllegalArgumentException e) {
            fail("Reflection Exception"); 
        } catch (InvocationTargetException e) {
            fail("Reflection Exception"); 
        }
    }

}
