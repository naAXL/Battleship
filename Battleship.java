import java.util.Scanner;
import java.util.Arrays;

public class Battleship {

    public static void main(String[] args) {
        String name = requestPlayerName();
        char water = '-';
        char miss = 'O';
        char hit = 'X';
        char ship = 'S';
        int gameBoardLength = 7;
        char[][] gameBoard = createGameBoard(gameBoardLength, water);

    }

    private static char[][] createGameBoard(int gameBoardLength, char water) {
        char[][] gameBoard = new char[gameBoardLength][gameBoardLength];

        for (char[] i: gameBoard) {
            Arrays.fill(i, water);
        }

        return gameBoard;
    }

    private static String requestPlayerName() {
        String name = new Scanner(System.in).nextLine();

        return name;
    }    
}