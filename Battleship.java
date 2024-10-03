import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Battleship {

    static int bigShipHP;
    static int mediumShip1HP;
    static int mediumShip2HP;
    static int shipsTotalNumber;
    static int shotsOnePlayerNumber;

    public static void main(String[] args) {
        boolean continueGame = true;
        Map<String, Integer> playersRanking = new HashMap<>();


        do {
            clearScreen();
            String name = requestPlayerName();
            continueGame = true;
            char water = '-';
            char miss = 'O';
            char hit = '+';
            char sunk = 'X';
            char smallShip = 's';
            char mediumShip1 = 'm';
            char mediumShip2 = 'M';
            char bigShip = 'b';
            int gameBoardLength = 7;
            int smallShipsNumber = 4;
            bigShipHP = 3;
            mediumShip1HP = 2;
            mediumShip2HP = 2;
            shipsTotalNumber = 7;
            shotsOnePlayerNumber = 0;

            ArrayList<Integer> takenSpaces = new ArrayList<Integer>();

            char[][] gameBoard = createGameBoard(gameBoardLength, water);

            gameBoard = placeShips(gameBoardLength, gameBoard, bigShip, smallShip, mediumShip1, mediumShip2, water,
                    smallShipsNumber, takenSpaces);

            while (shipsTotalNumber > 0) {
                displayGameBoardToUser(gameBoard, gameBoardLength, water, hit, miss, sunk);
                gameBoard = updateGameBoardAfterUserShot(gameBoard, water, miss, hit, sunk, bigShip, mediumShip1, mediumShip2, smallShip);
                clearScreen();
                shotsOnePlayerNumber++;
            }

            displayGameBoardToUser(gameBoard, gameBoardLength, water, hit, miss, sunk);

            playersRanking.put(name, shotsOnePlayerNumber);
            displayGameWinScreen(name);
            continueGame = requestGameContinuation();
        } while (continueGame);

        clearScreen();
        playersRanking = sortRanking(playersRanking, true);
        displayPlayers(playersRanking);

    }

    private static char[][] placeShips(int gameBoardLength, char[][] gameBoard, char bigShip, char smallShip,
            char mediumShip1, char mediumShip2, char water,
            int smallShipsNumber,
            ArrayList<Integer> takenSpaces) {
        int[] bigShipPosition = new int[6];
        int[] mediumShipPosition = new int[4];
        int[] smallShipPosition = new int[2];

        bigShipPosition = generateShipCoordinates(bigShipPosition.length / 2, gameBoardLength, gameBoard, 'a',
                water, takenSpaces);
        for (int i = 0; i < 6; i += 2) {
            gameBoard[bigShipPosition[i]][bigShipPosition[i + 1]] = bigShip;
        }

        mediumShipPosition = generateShipCoordinates(mediumShipPosition.length / 2, gameBoardLength, gameBoard, 'a',
                water, takenSpaces);
        for (int i = 0; i < 4; i += 2) {
            gameBoard[mediumShipPosition[i]][mediumShipPosition[i + 1]] = mediumShip1;
        }

        mediumShipPosition = generateShipCoordinates(mediumShipPosition.length / 2, gameBoardLength, gameBoard, 'a',
                water, takenSpaces);
        for (int i = 0; i < 4; i += 2) {
            gameBoard[mediumShipPosition[i]][mediumShipPosition[i + 1]] = mediumShip2;
        }

        while (smallShipsNumber > 0) {
            smallShipPosition = generateShipCoordinates(smallShipPosition.length / 2, gameBoardLength, gameBoard, 'a',
                    water, takenSpaces);
            for (int i = 0; i < 2; i += 2) {
                gameBoard[smallShipPosition[i]][smallShipPosition[i + 1]] = smallShip;
            }
            smallShipsNumber--;
        }

        return gameBoard;
    }

    private static boolean validatePosition(int[] shipPosition, char[][] gameBoard, int gameBoardLength, char water,
            char ship, ArrayList<Integer> takenSpaces) {
        int shipLength = shipPosition.length / 2;
        boolean spaceValid = true;

        for (int i = 0; i < shipLength * 2; i += 2) {
            for (int row = -1; row < 2; row++) {
                if (shipPosition[i] + row >= 0 && shipPosition[i] + row < 7) {
                    for (int col = -1; col < 2; col++) {
                        if (shipPosition[i + 1] + col >= 0 && shipPosition[i + 1] + col < 7) {
                            if (gameBoard[shipPosition[i] + row][shipPosition[i + 1] + col] == water) {
                                continue;
                            } else {
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

    private static int[] generateShipCoordinates(int shipLength, int gameBoardLength, char[][] gameBoard, char ship,
            char water, ArrayList<Integer> takenSpaces) {
        boolean shipPlaced = false;
        int[] coordinates = new int[shipLength * 2];

        do {
            int direction = new Random().nextInt(2);
            boolean buildRight = direction == 0;
            // direction == 1 is buildDown
            if (buildRight) {
                coordinates[0] = new Random().nextInt(gameBoardLength);
                coordinates[1] = new Random().nextInt(gameBoardLength - shipLength + 1);
            } else {
                coordinates[0] = new Random().nextInt(gameBoardLength - shipLength + 1);
                coordinates[1] = new Random().nextInt(gameBoardLength);
            }

            if (buildRight) {
                for (int i = 2; i < shipLength * 2; i++) {
                    if (i % 2 == 0) {
                        coordinates[i] = coordinates[0];
                    } else {
                        coordinates[i] = coordinates[i - 2] + 1;
                    }
                }
                if (validatePosition(coordinates, gameBoard, gameBoardLength, water, ship, takenSpaces)) {
                    shipPlaced = true;
                }
            } else {
                for (int i = 2; i < shipLength * 2; i++) {
                    if (i % 2 == 0) {
                        coordinates[i] = coordinates[i - 2] + 1;
                    } else {
                        coordinates[i] = coordinates[1];
                    }
                }
                if (validatePosition(coordinates, gameBoard, gameBoardLength, water, ship, takenSpaces)) {
                    shipPlaced = true;
                }
            }
        } while (!shipPlaced);

        return coordinates;
    }

    private static void displayGameBoardToUser(char[][] gameBoard, int gameBoardLength, char water, char hit, char miss,
            char sunk) {

        System.out.println("   1  2  3  4  5  6  7");
        for (int i = 0; i < gameBoardLength; i++) {
            System.out.print((char) ('A' + i) + "  ");
            for (int j = 0; j < gameBoardLength; j++) {
                if (gameBoard[i][j] != hit && gameBoard[i][j] != miss && gameBoard[i][j] != sunk) {
                    System.out.print(water + "  ");
                } else if (gameBoard[i][j] == hit) {
                    System.out.print(hit + "  ");
                } else if (gameBoard[i][j] == miss) {
                    System.out.print(miss + "  ");
                } else {
                    System.out.print(sunk + "  ");
                }
            }
            System.out.println();
        }

    }

    private static char[][] updateGameBoardAfterUserShot(char[][] gameBoard, char water, char miss, char hit, char sunk,
            char bigShip, char mediumShip1, char mediumShip2, char smallShip) {
        int[] userShotCoordinates = getUserShotCoordinates();
        boolean shotIsValid = false;

        while (!shotIsValid) {
            char shotTile = gameBoard[userShotCoordinates[0]][userShotCoordinates[1]];

            if (shotTile == miss || shotTile == hit || shotTile == sunk) {
                System.out.println("You've have shot this tile before try again!");
                userShotCoordinates = getUserShotCoordinates();
            } else if (shotTile == water) {
                gameBoard[userShotCoordinates[0]][userShotCoordinates[1]] = miss;
                shotIsValid = true;
            } else if (shotTile == smallShip) {
                gameBoard[userShotCoordinates[0]][userShotCoordinates[1]] = sunk;
                shotIsValid = true;
                shipsTotalNumber--;
            } else if (shotTile == bigShip) {
                gameBoard[userShotCoordinates[0]][userShotCoordinates[1]] = hit;
                bigShipHP--;
                if (bigShipHP == 0) {
                    gameBoard = sinkShip(userShotCoordinates, gameBoard, 3, sunk, hit);
                }
                shotIsValid = true;
            } else if (shotTile == mediumShip1) {
                gameBoard[userShotCoordinates[0]][userShotCoordinates[1]] = hit;
                mediumShip1HP--;
                if (mediumShip1HP == 0) {
                    gameBoard = sinkShip(userShotCoordinates, gameBoard, 2, sunk, hit);
                }
                shotIsValid = true;
            } else if (shotTile == mediumShip2) {
                gameBoard[userShotCoordinates[0]][userShotCoordinates[1]] = hit;
                mediumShip2HP--;
                if (mediumShip2HP == 0) {
                    gameBoard = sinkShip(userShotCoordinates, gameBoard, 2, sunk, hit);
                }
                shotIsValid = true;
            }
        }

        return gameBoard;
    }

    private static int[] getUserShotCoordinates() {
        int[] shotCoordinates = new int[2];
        List<Character> possibleHorizontalShotCharacters = Arrays.asList('1', '2', '3', '4', '5', '6', '7');
        List<Character> possibleVerticalShotCharacters = Arrays.asList('A', 'B', 'C', 'D', 'E', 'F', 'G' );
        boolean shotIsValid = false;
        System.out.println("Take your shot!");

        do {
            String userGuess = new Scanner(System.in).nextLine();
            if (userGuess.length() != 2) {
                System.out.println("coordinates format should letter and number (e.g. a1, B2, f7...)\nTry one more time!");
                continue;
            }
            char verticalCharacter = Character.toUpperCase(userGuess.charAt(0));
            char horizontalCharacter = userGuess.charAt(1);
            boolean verticalCharacterIsValid = possibleVerticalShotCharacters.contains(verticalCharacter);
            boolean horizontalCharacterIsValid = possibleHorizontalShotCharacters.contains(horizontalCharacter);
            if (verticalCharacterIsValid && horizontalCharacterIsValid) {
                shotCoordinates[0] = (int) verticalCharacter - 65;
                shotCoordinates[1] = (int) horizontalCharacter - 49;
                shotIsValid = true;
            } else {
                System.out.println("Out of bound! Try again");
            }
        } while (!shotIsValid);

        return shotCoordinates;
    }

    private static char[][] sinkShip(int[] coordinates, char[][] gameBoard, int tilesToSink, char sunk, char hit) {
        gameBoard[coordinates[0]][coordinates[1]] = sunk;
        tilesToSink--;

        while (tilesToSink > 0) {
            for (int row = -1; row < 2; row++) {
                if (coordinates[0] + row > -1 && coordinates[0] + row < 7){
                    for (int col = -1; col < 2; col++) {
                        if (coordinates[1] + col > -1 && coordinates[1] + col < 7){
                            if (gameBoard[coordinates[0]+row][coordinates[1]+col] == hit) {
                                gameBoard[coordinates[0]+row][coordinates[1]+col] = sunk;
                                tilesToSink--;
                                coordinates[0] += row;
                                coordinates[1] += col;
                                break;
                            }
                        }
                    }
                }
            }
        }

        shipsTotalNumber--;
        return gameBoard;
    }

    private static char[][] createGameBoard(int gameBoardLength, char water) {
        char[][] gameBoard = new char[gameBoardLength][gameBoardLength];

        for (char[] i : gameBoard) {
            Arrays.fill(i, water);
        }

        return gameBoard;
    }

    private static Map<String, Integer> sortRanking(Map<String, Integer> playerRanking, boolean order)
    {
        List<Entry<String, Integer>> list = new LinkedList<> (playerRanking.entrySet());

        list.sort((o1, o2) -> order ? o1.getValue().compareTo(o2.getValue()) == 0
                ? o1.getKey().compareTo(o2.getKey())
                : o1.getValue().compareTo(o2.getValue()) : o2.getValue().compareTo(o1.getValue()) == 0
                ? o2.getKey().compareTo(o1.getKey())
                : o2.getValue().compareTo(o1.getValue()));
        return list.stream().collect(Collectors.toMap(Entry::getKey, Entry::getValue, (a, b) -> b, LinkedHashMap::new));

    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void displayGameWinScreen(String name) {
        System.out.println("Congratulations, " + name + ", you have won!");
        System.out.println("You've made " + shotsOnePlayerNumber + " shots!");
    }

    private static String requestPlayerName() {
        System.out.println("What is your name?");
        String name = new Scanner(System.in).nextLine();

        return name;
    }

    private static boolean requestGameContinuation() {
        System.out.println("Do you wish to continue playing? Yes or No");
        String userWish = new Scanner(System.in).nextLine();

        if (userWish.equalsIgnoreCase("yes")) {
            return true;
        }

        return false;


    }

    private static void displayPlayers(Map<String, Integer> playerRanking)
    {
        playerRanking.forEach((key, value) -> System.out.println("Player : " + key + " Number of shots : " + value));
    }
}