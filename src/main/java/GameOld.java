//import GameObject.Glyph;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Scanner;
//
//public class Game {
//    public static HashMap<Character, String> commandDescriptions = new HashMap<>(Map.ofEntries(
//        Map.entry('n', "move up/north"),
//        Map.entry('s', "move down/south"),
//        Map.entry('w', "move left/west"),
//        Map.entry('e', "move right/east"),
//        Map.entry('q', "quit game")
//    ));
//
//    public static boolean isValidCommand(char command) {
//        return commandDescriptions.keySet().contains(command);
//        // for (char c : commandDescriptions.keySet()) {
//        //     if (command == c) {
//        //         return true;
//        //     }
//        // }
//        // return false;
//    }
//
//    public static void printCommandStatus(char command, boolean actionTaken) {
//        System.out.printf("Command provided: %c\tValid Command: %b\tAction taken: %s\n",
//                          command, isValidCommand(command), actionTaken ? commandDescriptions.get(command) : "None");
//    }
//
//    public static void test() {
//        System.out.println(Glyph.FLOOR_LIGHT.get());
//        // World world = new World();
//        // Player player = world.getPlayer();
//
//        // System.out.println(player);
//        // player.move('n');
//        // System.out.println(player);
//        // player.move('w');
//        // System.out.println(player);
//        // player.move('s');
//        // System.out.println(player);
//        // player.move('e');
//        // System.out.println(player);
//    }
//
//    public static void play() {
//        Scanner input = new Scanner(System.in);
//        World world = new World(15, 40);
//        char command = '0';
//        boolean actionTaken = false;
//
//        world.drawWorld();
//        printCommandStatus(command, actionTaken);
//
//        while (command != 'q') {
//            System.out.print("Enter command: ");
//            command = input.next().charAt(0);
//            if (isValidCommand(command) && command != 'q') {
//                actionTaken = world.updateActors(command);
//            } else if (command == 'q') {
//                actionTaken = true;
//            } else {
//                actionTaken = false;
//            }
//            world.drawWorld();
//            printCommandStatus(command, actionTaken);
//        }
//
//        System.out.println("Exited Game...");
//
//        input.close();
//    }
//
//    public static void main(String[] args) {
//        play();
//        // test();
//    }
//}
