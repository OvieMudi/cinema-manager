package cinema;

import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class Cinema {
    private static final int SEAT_PRICE_FRONT = 10;
    private static final int SEAT_PRICE_BACK = 8;
    private static final int SEAT_LIMIT = 60;

    private static final Scanner scanner = new Scanner(System.in);
    private static String[][] seatsArrangement;
    private static int purchasedTickets = 0;
    private static int totalSeats = 0;
    private static int currentIncome = 0;
    private static int totalIncome = 0;

    public static void main(String[] args) {
        beginProgram();
        boolean isRunning = true;
        while (isRunning) {
            isRunning = handleMenuOptions();
        }
    }

    private static void beginProgram() {
        System.out.println("Enter the number of rows:");
        int rows = scanner.nextInt();
        System.out.println("Enter the number of seats in each row:");
        int seatsPerRow = scanner.nextInt();

        createSeatArrangement(rows, seatsPerRow);
        totalSeats = rows * seatsPerRow;
        setTotalIncome();
    }

    private static boolean handleMenuOptions() {
        showMenu();
        int selection = scanner.nextInt();
        switch (selection) {
            case 0:
                scanner.close();
                return false;
            case 1:
                printSeats();
                return true;
            case 2:
                buyTicket();
                return true;
            case 3:
                showStats();
                return true;
            default:
                showMenu();
                return true;
        }
    }

    private static void createSeatArrangement(int rows, int seatsPerRow) {
        seatsArrangement = new String[rows][seatsPerRow];
        for (int i = 0; i < seatsArrangement.length; i++) {
            String[] seats = new String[seatsPerRow];
            Arrays.fill(seats, " S");
            seatsArrangement[i] = seats;
        }
    }

    private static void showMenu() {
        System.out.println();
        System.out.println("1. Show the seats");
        System.out.println("2. Buy a ticket");
        System.out.println("3. Statistics");
        System.out.println("0. Exit");
        System.out.println();
    }

    private static void buyTicket() {
        int rowNumber;
        int seatNumber;

        boolean isValidPosition;
        boolean isFreeSeat;
        do {
            System.out.println("Enter a row number:");
            rowNumber = scanner.nextInt();
            System.out.println("Enter a seat number in that row:");
            seatNumber = scanner.nextInt();

            isValidPosition = checkValidPosition(rowNumber, seatNumber);
            isFreeSeat = checkFreeSeat(rowNumber, seatNumber);
            if (!isValidPosition) {
                System.out.println("Wrong input!");
                continue;
            }
            if (!isFreeSeat) {
                System.out.println("That ticket has already been purchased!");
            }
        } while (!isValidPosition || !isFreeSeat);

        seatsArrangement[rowNumber - 1][seatNumber - 1] = " B";

        int ticketPrice = calculateTicketPrice(rowNumber);
        currentIncome += ticketPrice;
        purchasedTickets++;

        System.out.println("Ticket price: $" + ticketPrice);
    }

    private static boolean checkValidPosition(int row, int seatNumber) {
        boolean isValidInputs = row > 0 && seatNumber > 0;
        boolean notOutBounds = row <= seatsArrangement.length && seatNumber <= seatsArrangement[0].length;
        return isValidInputs && notOutBounds;
    }

    private static boolean checkFreeSeat(int row, int seatNumber) {
        boolean isValidPosition = checkValidPosition(row, seatNumber);
        if (isValidPosition) {
            return Objects.equals(seatsArrangement[row - 1][seatNumber - 1], " S");
        }
        return false;
    }

    private static int calculateTicketPrice(int rowNumber) {
        int rows = seatsArrangement.length;
        int numberOfFrontRows = rows / 2;

        int ticketPrice;
        if (totalSeats <= SEAT_LIMIT) {
            ticketPrice = SEAT_PRICE_FRONT;
        } else if (rowNumber > numberOfFrontRows) {
            ticketPrice = SEAT_PRICE_BACK;
        } else {
            ticketPrice = SEAT_PRICE_FRONT;
        }
        return ticketPrice;
    }

    private static void setTotalIncome() {
        int rows = seatsArrangement.length;
        int seatsPerRow = seatsArrangement[0].length;

        int numberOfFrontRows = rows / 2;
        int numberOfBackRows = rows - numberOfFrontRows;
        int totalFrontSeats = numberOfFrontRows * seatsPerRow;
        int totalBackSeats = numberOfBackRows * seatsPerRow;

        int totalSeats = rows * seatsPerRow;

        if (totalSeats <= SEAT_LIMIT) {
            totalIncome = totalSeats * SEAT_PRICE_FRONT;
        } else {
            totalIncome = (totalFrontSeats * SEAT_PRICE_FRONT) + (totalBackSeats * SEAT_PRICE_BACK);
        }
    }

    private static void showStats() {
        double percentagePurchased = ((double) purchasedTickets / totalSeats) * 100;
        System.out.printf("Number of purchased tickets: %d\n", purchasedTickets);
        System.out.printf("Percentage: %.2f%%\n", percentagePurchased);
        System.out.printf("Current income: $%d\n", currentIncome);
        System.out.printf("Total income: $%d\n", totalIncome);
    }

    private static void printSeats() {
        int rows = seatsArrangement.length;
        int seatsPerRow = seatsArrangement[0].length;

        printFirstColumnLabel(seatsPerRow);

        for (int i = 0; i < rows; i++) {
            System.out.print(i + 1);
            for (String item : seatsArrangement[i]) {
                System.out.print(item);
            }
            System.out.println();
        }
    }

    private static void printFirstColumnLabel(int seatsPerRow) {
        StringBuilder columnLabels = new StringBuilder(" ");
        for (int i = 0; i < seatsPerRow; i++) {
            columnLabels.append(" ");
            columnLabels.append(i + 1);
        }
        System.out.println();
        System.out.println("Cinema:");
        System.out.println(columnLabels);
    }
}