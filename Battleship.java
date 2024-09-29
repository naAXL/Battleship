import java.util.Scanner;

public class Battleship {

    public static void main(String[] args) {
        String name = requestPlayerName();
    }

    private static String requestPlayerName() {
        String name = new Scanner(System.in).nextLine();

        return name;
    }    
}