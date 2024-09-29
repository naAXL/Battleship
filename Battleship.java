import java.util.Random;
import java.util.Scanner;
import java.util.Arrays;

public class Battleship {

    public static void main(String[] args) {
        String name = requestPlayerName();
        char water = '-';
        char miss = 'O';
        char hit = 'X';
        char smallShip = 's';
        char mediumShip = 'm';
        char bigShip = 'b';
        int gameBoardLength = 7;
        int bigShipsNumber = 1;
        int mediumShipsNumber = 2;
        int smallShipsNumber = 4;

        char[][] gameBoard = createGameBoard(gameBoardLength, water);

        gameBoard = placeShips(gameBoardLength, gameBoard, bigShip, smallShip, mediumShip, water, bigShipsNumber, mediumShipsNumber, smallShipsNumber);

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                System.out.print(gameBoard[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static char[][] placeShips(int gameBoardLength, char[][] gameBoard, char bigShip, char smallShip, char mediumShip, char water, int bigShipsNumber, int mediumShipsNumber, int smallShipsNumber) {
        int[] bigShipPosition = new int[6];
        int[] mediumShipPosition = new int[4];
        int[] smallShipPosition = new int[2];

        while (bigShipsNumber > 0) {
            bigShipPosition = generateShipCoordinates(bigShipPosition.length/2, gameBoardLength, gameBoard, bigShip, water);
            for (int i = 0; i < 6; i += 2) {
                gameBoard[bigShipPosition[i]][bigShipPosition[i+1]] = bigShip;
            }
            bigShipsNumber--;
        }

        while (mediumShipsNumber > 0) {
            mediumShipPosition = generateShipCoordinates(mediumShipPosition.length/2, gameBoardLength, gameBoard, mediumShip, water);
            for (int i = 0; i < 4; i += 2) {
                gameBoard[mediumShipPosition[i]][mediumShipPosition[i+1]] = mediumShip;
            }
            mediumShipsNumber--;
        }

        while (smallShipsNumber > 0) {
            smallShipPosition = generateShipCoordinates(smallShipPosition.length/2, gameBoardLength, gameBoard, smallShip, water);
            for (int i = 0; i < 2; i += 2) {
                gameBoard[smallShipPosition[i]][smallShipPosition[i+1]] = smallShip;
            }
            smallShipsNumber--;
        }

        return gameBoard;
    }

    private static boolean validatePosition(int[] shipPosition, char[][] gameBoard, int gameBoardLength, char water, char ship) {
        int shipLength = shipPosition.length/2;
        boolean spaceValid = true;

        for (int i = 0; i < shipLength*2; i+=2) {
            for (int row = -1; row<2;row++){
                if (shipPosition[i] + row >= 0 && shipPosition[i] + row < 7){
                    for (int col = -1; col<2;col++) {
                        if (shipPosition[i + 1] + col >= 0 && shipPosition[i + 1] + col < 7){
                            if (gameBoard[shipPosition[i]+row][shipPosition[i+1]+col] == ship || gameBoard[shipPosition[i]+row][shipPosition[i+1]+col] == water){
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


    private static int[] generateShipCoordinates(int shipLength, int gameBoardLength, char[][] gameBoard, char ship, char water) {
        boolean shipPlaced = false;
        int[] coordinates = new int[shipLength*2];

        do {
            for (int i = 0; i < 2; i++) {
                coordinates[i] = new Random().nextInt(gameBoardLength);
            }
            boolean spaceAvailableLeft = coordinates[1] >= shipLength - 1;
            boolean spaceAvailableRight = coordinates[1] <= gameBoardLength - shipLength;
            boolean spaceAvailableUp = coordinates[0] >= shipLength - 1;
            boolean spaceAvailableDown = coordinates[0] <= gameBoardLength - shipLength;
            
            int direction = new Random().nextInt(4);
            if (direction == 0 && spaceAvailableLeft) {
                for (int i = 2; i < shipLength*2; i++){
                    if (i%2==0) {
                        coordinates[i] = coordinates[0];
                    }
                    else {
                        coordinates[i] = coordinates[i-2] - 1;
                    }
                }
                if (validatePosition(coordinates, gameBoard, gameBoardLength, water, ship)) {
                    shipPlaced = true;
                }
            }
            else if (direction == 1 && spaceAvailableUp) {
                for (int i = 2; i < shipLength*2; i++){
                    if (i%2==0) {
                        coordinates[i] = coordinates[i-2] - 1;
                    }
                    else {
                        coordinates[i] = coordinates[1];
                    }
                }
                if (validatePosition(coordinates, gameBoard, gameBoardLength, water, ship)) {
                    shipPlaced = true;
                }
            }
            else if (direction == 2 && spaceAvailableRight) {
                for (int i = 2; i < shipLength*2;i++){
                    if (i%2==0) {
                        coordinates[i] = coordinates[0];
                    }
                    else {
                        coordinates[i] = coordinates[i-2] + 1;
                    }
                }
                if (validatePosition(coordinates, gameBoard, gameBoardLength, water, ship)) {
                    shipPlaced = true;
                }
            }
            else if (direction == 3 && spaceAvailableDown) {           
                for (int i = 2; i < shipLength*2; i++){
                    if (i%2==0) {
                        coordinates[i] = coordinates[i-2] + 1;
                    }
                    else {
                        coordinates[i] = coordinates[1];
                    }
                }
                if (validatePosition(coordinates, gameBoard, gameBoardLength, water, ship)) {
                    shipPlaced = true;
                }
            }
        } while(!shipPlaced);
    
        return coordinates;
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