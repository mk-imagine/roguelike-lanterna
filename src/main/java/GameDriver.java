import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalResizeListener;
import java.nio.charset.Charset;
import java.util.SortedMap;

public class GameDriver {
    public static final HashMap<Character, String> COMMAND_OPS = new HashMap<>(Map.ofEntries(
            Map.entry('n', "move up/north  "),
            Map.entry('s', "move down/south"),
            Map.entry('w', "move left/west "),
            Map.entry('e', "move right/east")
    ));

    public static void includeDebugInfo(TextGraphics textGraphics, TerminalSize ts, int offsetX, int offsetY) {
            String[] infoStrings = {"Terminal Size: ", "Last Keystroke: ", "Command: ", "Was Valid: ", "Action Taken: "};

            textGraphics.putString(offsetX, offsetY, infoStrings[0], SGR.BOLD);
            textGraphics.putString(offsetX + infoStrings[0].length(), offsetY, ts.toString());

            for (int i = 1; i < infoStrings.length; i++) {
                textGraphics.putString(offsetX, offsetY + i, infoStrings[i], SGR.BOLD);
                textGraphics.putString(offsetX + infoStrings[i].length(), offsetY + i, "<Pending>");
            }
    }

    public static void includeDebugInfo(TextGraphics textGraphics, TerminalSize ts,
                                        KeyStroke keyStroke, int offsetX, int offsetY,
                                        boolean actionTaken) {
        includeDebugInfo(textGraphics, ts, offsetX, offsetY);

        if (ts != null) {
            textGraphics.putString(offsetX + "Terminal Size: ".length(), offsetY, ts.toString());
        }

        char command;
        if ((command = keyStroke.getCharacter()) != '~') {
            textGraphics.putString(offsetX + "Last Keystroke: ".length(), offsetY + 1, keyStroke.toString());
            textGraphics.putString(offsetX + "Command: ".length(), offsetY + 2, COMMAND_OPS.getOrDefault(command, "None           "));
            textGraphics.putString(offsetX + "Was Valid: ".length(), offsetY + 3, COMMAND_OPS.containsKey(command) ? "True     " : "False    ");
            textGraphics.putString(offsetX + "Action Taken: ".length(), offsetY + 4, actionTaken ? "Yes      " : "No       ");
        }

    }

    public static void test() {
        SortedMap<String,Charset> charsets = Charset.availableCharsets();
        System.out.println(charsets.keySet());
    }

    public static void play(boolean debug) {
        DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
        defaultTerminalFactory.setInitialTerminalSize(new TerminalSize(120, 40));
        Terminal terminal = null;
        try {
            terminal = defaultTerminalFactory.createTerminal();
            terminal.enterPrivateMode();

            terminal.clearScreen();

            terminal.setCursorVisible(false);

            final TextGraphics textGraphics = terminal.newTextGraphics();

            World world = new World(15, 40);
            boolean actionTaken = false;

            world.updateWorldDisplay();  // update display array

            for (int i = 0; i < world.getWorldHeight(); i++) {
                textGraphics.putString(2, i+1, world.getRow(i));
            }

            int offsetX = 4;
            int offsetY = 4 + world.getWorldHeight();

            if (debug) {
                includeDebugInfo(textGraphics, terminal.getTerminalSize(), offsetX, offsetY);
            }

            terminal.addResizeListener(new TerminalResizeListener() {
                @Override
                public void onResized(Terminal terminal, TerminalSize newSize) {
                    // Be careful here though, this is likely running on a separate thread. Lanterna is threadsafe in
                    // a best-effort way so while it shouldn't blow up if you call terminal methods on multiple threads,
                    // it might have unexpected behavior if you don't do any external synchronization
                    world.updateWorldDisplay();
                    textGraphics.fill(' ');
                    for (int i = 0; i < world.getWorldHeight(); i++) {
                        textGraphics.putString(2, i+1, world.getRow(i));
//                    System.out.println(world.getRow(i));
                    }
                    if (debug) {
                        includeDebugInfo(textGraphics, newSize, new KeyStroke('~', false, false), offsetX, offsetY, false);
//                        textGraphics.putString(offsetX + "Terminal Size: ".length(), offsetY, newSize.toString());
                    }

                    try {
                        terminal.flush();
                    }
                    catch(IOException e) {
                        // Not much we can do here
                        throw new RuntimeException(e);
                    }
                }
            });

            terminal.flush();

            KeyStroke keyStroke = terminal.readInput();

            while(keyStroke.getKeyType() != KeyType.ESCAPE) {
                char command = keyStroke.getCharacter();
                if (COMMAND_OPS.containsKey(command)) {
                    actionTaken = world.updateActors(command);
                    world.updateWorldDisplay();
                    textGraphics.fill(' ');
                    for (int i = 0; i < world.getWorldHeight(); i++) {
                        textGraphics.putString(2, i+1, world.getRow(i));
//                    System.out.println(world.getRow(i));
                    }
                }

                if (debug) {
                    includeDebugInfo(textGraphics, terminal.getTerminalSize(), keyStroke, offsetX, offsetY, actionTaken);
                }

                terminal.flush();
                keyStroke = terminal.readInput();
            }

        }
        catch(IOException e) {
            e.printStackTrace();
        }
        finally {
            if(terminal != null) {
                try {
                    terminal.close();
                }
                catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        boolean debug = true;
        play(debug);
//        test();
    }
}
