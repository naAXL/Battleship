import java.util.Random;
import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class Battleship {

    public static void main(String[] args) {
        String name = requestPlayerName();
        char water = '-';
        char miss = 'O';
        char hit = 'X';
        char sunk = 'S';
        char smallShip = 's';
        char mediumShip = 'm';
        char bigShip = 'b';
        int gameBoardLength = 7;
        int bigShipsNumber = 1;
        int mediumShipsNumber = 2;
        int smallShipsNumber = 4;

        ArrayList<Integer> takenSpaces = new ArrayList<Integer>();
        

        char[][] gameBoard = createGameBoard(gameBoardLength, water);

        gameBoard = placeShips(gameBoardLength, gameBoard, bigShip, smallShip, mediumShip, water, bigShipsNumber, mediumShipsNumber, smallShipsNumber, takenSpaces);
        displayGameBoardToUser(gameBoard, gameBoardLength, water, hit, miss, sunk);
        gameBoard = updateGameBoardAfterUserShot(gameBoard, water, miss, hit, sunk, bigShip, mediumShip, smallShip);
        displayGameBoardToUser(gameBoard, gameBoardLength, water, hit, miss, sunk);
   
    }

    private static char[][] placeShips(int gameBoardLength, char[][] gameBoard, char bigShip, char smallShip, char mediumShip, char water, int bigShipsNumber, int mediumShipsNumber, int smallShipsNumber, ArrayList<Integer> takenSpaces) {
        int[] bigShipPosition = new int[6];
        int[] mediumShipPosition = new int[4];
        int[] smallShipPosition = new int[2];

        while (bigShipsNumber > 0) {
            bigShipPosition = generateShipCoordinates(bigShipPosition.length/2, gameBoardLength, gameBoard, 'a', water, takenSpaces);
            for (int i = 0; i < 6; i += 2) {
                gameBoard[bigShipPosition[i]][bigShipPosition[i+1]] = bigShip;
            }
            bigShipsNumber--;
        }

        while (mediumShipsNumber > 0) {
            mediumShipPosition = generateShipCoordinates(mediumShipPosition.length/2, gameBoardLength, gameBoard, 'a', water, takenSpaces);
            for (int i = 0; i < 4; i += 2) {
                gameBoard[mediumShipPosition[i]][mediumShipPosition[i+1]] = mediumShip;
            }
            mediumShipsNumber--;
        }

        while (smallShipsNumber > 0) {
            smallShipPosition = generateShipCoordinates(smallShipPosition.length/2, gameBoardLength, gameBoard, 'a', water, takenSpaces);
            for (int i = 0; i < 2; i += 2) {
                gameBoard[smallShipPosition[i]][smallShipPosition[i+1]] = smallShip;
            }
            smallShipsNumber--;
        }

        return gameBoard;
    }

    private static boolean validatePosition(int[] shipPosition, char[][] gameBoard, int gameBoardLength, char water, char ship, ArrayList<Integer> takenSpaces) {
        int shipLength = shipPosition.length/2;
        boolean spaceValid = true;

        for (int i = 0; i < shipLength*2; i+=2) {
            for (int row = -1; row<2;row++){
                if (shipPosition[i] + row >= 0 && shipPosition[i] + row < 7){
                    for (int col = -1; col<2;col++) {
                        if (shipPosition[i + 1] + col >= 0 && shipPosition[i + 1] + col < 7){
                            if (gameBoard[shipPosition[i]+row][shipPosition[i+1]+col] == water){
                                continue;
                            }
                            else {
                                spaceValid = false;
                                return spaceValid;
                            }
                        }
                    }
                }
            }
        }
        
        return spaceValid;
    }


    private static int[] generateShipCoordinates(int shipLength, int gameBoardLength, char[][] gameBoard, char ship, char water, ArrayList<Integer> takenSpaces) {
        boolean shipPlaced = false;
        int[] coordinates = new int[shipLength*2];

        do {
            int direction = new Random().nextInt(2);
            boolean buildRight = direction == 0;
            // direction == 1 is buildDown
            if (buildRight) {
                coordinates[0] = new Random().nextInt(gameBoardLength);
                coordinates[1] = new Random().nextInt(gameBoardLength - shipLength + 1);
            }
            else {
                coordinates[0] = new Random().nextInt(gameBoardLength - shipLength + 1);
                coordinates[1] = new Random().nextInt(gameBoardLength);
            }
            
            if (buildRight) {
                for (int i = 2; i < shipLength*2;i++){
                    if (i%2==0) {
                        coordinates[i] = coordinates[0];
                    }
                    else {
                        coordinates[i] = coordinates[i-2] + 1;
                    }
                }
                if (validatePosition(coordinates, gameBoard, gameBoardLength, water, ship, takenSpaces)) {
                    shipPlaced = true;
                }
            }
            else {           
                for (int i = 2; i < shipLength*2; i++){
                    if (i%2==0) {
                        coordinates[i] = coordinates[i-2] + 1;
                    }
                    else {
                        coordinates[i] = coordinates[1];
                    }
                }
                if (validatePosition(coordinates, gameBoard, gameBoardLength, water, ship, takenSpaces)) {
                    shipPlaced = true;
                }
            }
        } while(!shipPlaced);
    
        return coordinates;
    }

    private static void displayGameBoardToUser(char[][] gameBoard, int gameBoardLength, char water, char hit, char miss, char sunk) {

        System.out.println("   1  2  3  4  5  6  7");
        for (int i = 0; i < gameBoardLength; i++) { 
            System.out.print((char)('A' + i) + "  ");
            for (int j = 0; j < gameBoardLength; j++) {
                if (gameBoard[i][j] != hit && gameBoard[i][j] != miss && gameBoard[i][j] != sunk) {
                    System.out.print(water + "  ");
                }
                else if (gameBoard[i][j] == hit) {
                    System.out.print(hit + "  ");
                }
                else if (gameBoard[i][j] == miss) {
                    System.out.print(miss + "  ");
                }
                else {
                    System.out.print(sunk + "  ");
                }
            }
            System.out.println();
        }

    }

    private static char[][] updateGameBoardAfterUserShot(char[][] gameBoard, char water, char miss, char hit, char sunk, char bigShip, char mediumShip, char smallShip) {
        int[] userShotCoordinates = getUserShotCoordinates();
        boolean shotIsValid = false;

        while (!shotIsValid) {
            char shotTile = gameBoard[userShotCoordinates[0]][userShotCoordinates[1]];

            if (shotTile == miss || shotTile == hit || shotTile == sunk) {
                System.out.println("You've have shot this tile before try again!");
                userShotCoordinates = getUserShotCoordinates();
            }
            else if(shotTile == water) {
                gameBoard[userShotCoordinates[0]][userShotCoordinates[1]] = miss;
                shotIsValid = true;
            }
            else {
                gameBoard[userShotCoordinates[0]][userShotCoordinates[1]] = hit;
                shotIsValid = true;
            }
        }
        
        return gameBoard;
    }

    private static int[] getUserShotCoordinates() {
        int[] shotCoordinates = new int[2];
        List<Character> possibleShotCharacters = Arrays.asList('A', 'B', 'C', 'D', 'E', 'F', 'G', '1', '2', '3', '4', '5', '6', '7');
        boolean shotIsValid = false;
        System.out.println("Take your shot!");

        do {
            String userGuess = new Scanner(System.in).nextLine();
            char verticalCharacter = Character.toUpperCase(userGuess.charAt(0));
            char horizontalCharacter = userGuess.charAt(1);
            boolean verticalCharacterIsValid = possibleShotCharacters.contains(verticalCharacter);
            boolean horizontalCharacterIsValid = possibleShotCharacters.contains(horizontalCharacter);
            if (verticalCharacterIsValid && horizontalCharacterIsValid){
                shotCoordinates[0] = (int)verticalCharacter - 65;
                shotCoordinates[1] = (int)horizontalCharacter - 49;
                shotIsValid = true;
            }
            else {
                System.out.println("Out of bound! Try again");
            }
        } while (!shotIsValid);

        return shotCoordinates;
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