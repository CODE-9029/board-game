package marupeke;

import java.util.Scanner;


public class MarupekeRUN
{
    static Scanner reader = new Scanner(System.in);
    static Marupeke game = null;
    
    public static void main(String[] args)
    {
        boolean running = true;

        System.out.println("Welcome to play Marupeke");
        System.out.println("-------------------------------------------");
     
        while(running)
        {
            String command = readInput("Please enter a command(quit, new, setx, seto, unmark or help):");

            switch(command)
            {
                case "quit" :
                {
                    running = false;
                    break;
                }
                case "new" :
                {
                    int size = Integer.parseInt(readInput("Game Size (3-10): "));

                    if(size < 3 || size > 10)
                    {
                        System.out.println("That is not a valid game size");
                        break;
                    }

                    String difficultyString = readInput("Game Difficulty (easy, medium, hard): ");
                    Difficulty difficulty = null;

                    try
                    {
                         difficulty = Difficulty.valueOf(difficultyString.substring(0,1).toUpperCase() + difficultyString.substring(1));
                    }
                    catch (IllegalArgumentException e)
                    {
                        System.out.println("That is not a valid difficulty");
                        break;
                    }

                    game = Marupeke.randomPuzzle(size, difficulty);
                    System.out.print(game.toString());
                    break;
                }
                case "setx" :
                {
                    if(game == null) break;

                    int[] coords = getUserCoordinates();
                    if(coords == null) break;

                    boolean canEdit = game.setX(coords[0], coords[1], true);

                    if(canEdit == false)
                    {
                        System.out.println("This tile is not editable");
                        break;
                    }

                    System.out.print(game.toString());
                    break;
                }
                case "seto" :
                {
                    if(game == null) break;

                    int[] coords = getUserCoordinates();
                    if(coords == null) break;

                    boolean canEdit = game.setO(coords[0], coords[1], true);

                    if(canEdit == false)
                    {
                        System.out.println("This tile is not editable");
                        break;
                    }

                    System.out.print(game.toString());
                    break;
                }
                case "unmark" :
                {
                    if(game == null) break;

                    int[] coords = getUserCoordinates();
                    if(coords == null) break;

                    boolean canEdit = game.unmark(coords[0], coords[1]);

                    if(canEdit == false)
                    {
                        System.out.println("This tile is not editable");
                        break;
                    }

                    System.out.print(game.toString());
                    break;
                }
                case "help" :
                {
                    System.out.println("Commands ----------------------------------");
                    System.out.println("'new' - Create a new game");
                    System.out.println("'quit' - Quit the game");
                    System.out.println("'x' - Place an 'X' mark on the grid");
                    System.out.println("'o' - Place an 'O' mark on the grid");
                    System.out.println("'unmark' - Un-mark a tile on the grid");
                    System.out.println("-------------------------------------------");
                    break;
                }
            }
        }

        System.out.println("Thanks for playing!");
    }
    
    private static String readInput(String prompt)
    {
        System.out.print(prompt);
        return reader.next().toLowerCase();
    }

    private static int[] getUserCoordinates()
    {
        int x = Integer.parseInt(readInput("Row Position " + "(0-" + (game.getGrid().length - 1) + "): "));
        int y = Integer.parseInt(readInput("Column Position " + "(0-" + (game.getGrid().length - 1) + "): "));

        if(x < 0 || y < 0 || x > game.getGrid().length-1 || y > game.getGrid().length-1)
        {
            System.out.println("Please enter values that are within the game bounds: " + "(0-" + (game.getGrid().length - 1) + ")");
            return null;
        }

        return new int[]{x,y};
    }


}
