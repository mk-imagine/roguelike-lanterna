import com.googlecode.lanterna.*;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.AWTTerminalFrame;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/********************************************************************************************************
 * Game Driver
 *
 * @author Mark Kim
 ********************************************************************************************************/
public class GameDriverScreen {
    /*
     * Blocks of code that you may want to edit will be annotated with ALL CAPS in comment blocks like this one.
     * THIS IS A HASHMAP OF VALID COMMANDS. YOU CAN ADD OR REMOVE COMMANDS AS NECESSARY.
     */
    public static final HashMap<Character, String> COMMAND_OPS = new HashMap<>(Map.ofEntries(
            Map.entry('n', "move up/north  "),
            Map.entry('s', "move down/south"),
            Map.entry('w', "move left/west "),
            Map.entry('e', "move right/east")
    ));

    /*
     * THIS IS TO DRAW THE WORLD
     */
    public static void drawWorld(TextGraphics tg, World w) {
        for (int i = 0; i < w.getWorldHeight(); i++) {
            tg.putString(2, i+1, w.getRow(i));
        }
    }

    // generate the debugging strings
    public static String[] generateDebugStrings(KeyStroke ks, boolean actionTaken) {
        char command = ks.getCharacter();
        String[] outputStrings = {"Last Keystroke: ", "Command: ", "Was Valid: ", "Action Taken: "};
        String[] inputStrings = {ks.toString(),
                COMMAND_OPS.getOrDefault(command, "None           "),
                COMMAND_OPS.containsKey(command) ? "True     " : "False    ",
                actionTaken ? "Yes      " : "No       "
        };

        for (int i = 0; i < outputStrings.length; i++) {
            outputStrings[i] += inputStrings[i];
        }

        return outputStrings;
    }

    // update the debugging info on the screen
    public static void updateDebugScreen(TextGraphics tg, TerminalPosition tp, String[] dbStrings) {
        for (int i = 0; i < dbStrings.length; i++) {
            tg.putString(tp.withRelativeRow(i + 1), dbStrings[i]);
        }
    }

    /***************************************************************************************************
     * play method
     * This is the main play method where all the game logic lies.
     *
     * @param debug - flag for debugging information
     ***************************************************************************************************/
    public static void play(boolean debug) {
        DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();

        // set initial terminal size (you can change the size if you want)
        defaultTerminalFactory.setInitialTerminalSize(new TerminalSize(120, 40));

        Screen screen = null;
        try {
            // create terminal
            Terminal terminal = defaultTerminalFactory.createTerminal();
            if (terminal instanceof SwingTerminalFrame) {
                ((SwingTerminalFrame) terminal).setLocationRelativeTo(null);
            } else if (terminal instanceof AWTTerminalFrame) {
                ((AWTTerminalFrame) terminal).setLocationRelativeTo(null);
            }

            // initialize and start a new terminal screen
            screen = new TerminalScreen(terminal);
            screen.startScreen();

            // turn off the cursor
            screen.setCursorPosition(null);

            // this object is how graphics are drown to the terminal
            final TextGraphics textGraphics = screen.newTextGraphics();

            /*
             * YOUR WORLD INITIALIZATION HERE AND UPDATE DISPLAY 2D ARRAY
             */
            World world = new World(15, 40);
            world.updateWorldDisplay();
            boolean actionTaken = false;  // whether an action was taken

            /*
             * YOU CAN PLAY WITH THESE OFFSETS TO CHANGE WHERE DEBUGGING TEXT APPEARS
             */
            final int OFFSET_X = 4;
            final int OFFSET_Y = 2 + world.getWorldHeight();

            // the position of the text below the map
            TerminalPosition belowMap = new TerminalPosition(OFFSET_X, OFFSET_Y);

            // the current terminal size
            TerminalSize terminalSize = screen.getTerminalSize();

            // draw the world
            drawWorld(textGraphics, world);

            // refresh the screen
            screen.refresh();

            // declare & initialize some variables
            char command;
            KeyStroke prevKeyStroke = null;
            String[] debugStrings = {"Last Keystroke: <Pending>", "Command: <Pending>",
                    "Was Valid: <Pending>", "Action Taken: <Pending>"};

            // main loop to take in input and refresh the screen
            while(true) {
                // key listener to receive keyboard input
                KeyStroke keyStroke = screen.pollInput();

                // if you press the escape key, we will break out of this loop
                if(keyStroke != null && (keyStroke.getKeyType() == KeyType.ESCAPE)) {
                    break;
                }

                // resize terminal dynamically if you resize the window
                TerminalSize newSize = screen.doResizeIfNecessary();
                if(newSize != null) {
                    terminalSize = newSize;
                    drawWorld(textGraphics, world);
                    if (debug) {
                        if (prevKeyStroke != null) {
                            debugStrings = generateDebugStrings(prevKeyStroke, actionTaken);
                        }
                        updateDebugScreen(textGraphics, belowMap, debugStrings);
                    }
                }

                // logic for displaying debugging information
                String sizeLabel = "Terminal Size: " + terminalSize;

                // check if there is a keystroke to run logic below
                if (keyStroke != null) {
                    // retrieve the command from the keystroke
                    command = keyStroke.getCharacter();
                    prevKeyStroke = new KeyStroke(command, keyStroke.isCtrlDown(), keyStroke.isAltDown());

                    // if the command is valid, take action
                    if (COMMAND_OPS.containsKey(command)) {

                        /*
                         * THIS IS THE LOGIC TO UPDATE THE WORLD
                         */
                        actionTaken = world.updateActors(command);
                        world.updateWorldDisplay();

                        // draw world
                        drawWorld(textGraphics, world);
                    }

                    debugStrings = generateDebugStrings(keyStroke, actionTaken);
                }

                if (debug) {
                    textGraphics.putString(belowMap, sizeLabel);
                    updateDebugScreen(textGraphics, belowMap, debugStrings);
                }

                screen.refresh();
                Thread.yield();
            }
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if(screen != null) {
                try {
                    screen.close();
                    System.exit(0);
                }
                catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /****************************************************************************************************
     * Main Method
     ****************************************************************************************************/
    public static void main(String[] args) {
        final boolean debug = true;
        play(debug);
    }
}
