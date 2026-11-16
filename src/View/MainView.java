package View;

import Controller.*;
import Model.*;
import Model.enums.*;
import Model.factory.*;

import java.time.*;
import java.time.format.*;
import java.util.*;


public class MainView {
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Scanner sc = new Scanner(System.in);
    private FlightController flightController = new FlightController();
    private BookingController bookingController = new BookingController();
    private FlightView flightView = new FlightView();
    private BookingView bookingView = new BookingView();

    public MainView() {
        flightController.seedSampleData();
    }

    private void printMenu() {
        System.out.println("\n===== AIRLINE RESERVATION SYSTEM =====");
        System.out.println("1. View all flights");
        System.out.println("2. Search flights");
        System.out.println("3. Create booking");
        System.out.println("4. Confirm booking");
        System.out.println("5. Pay and get ticket");
        System.out.println("6. Check in");
        System.out.println("7. Cancel booking");
        System.out.println("8. Find booking by ID");
        System.out.println("0. Exit");
    }

    private String readText(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    private void searchFlight() {
        String from = readText("From: ");
        String to = readText("To: ");
        LocalDate date = LocalDate.parse(readText("Date (dd/MM/yyyy): "), DATE_FMT);
        flightView.showFlights(flightController.searchFlights(from, to, date));
    }

    private void createBooking() {
        flightView.showFlights(flightController.getAllFlights());
        String flightNo = readText("Flight No: ");
        Flight flight = flightController.findFlight(flightNo);
        if(flight == null) {
            bookingView.showError("Flight not found: " + flightNo);
            return;
        }
        flightView.showSeats(flight);
        String seatNumber = readText("Seat number: ");

        String passengerId = readText("Passenger ID: ");
        String fullName = readText("Full name: ");
        String email = readText("Email: ");
        String phone = readText("Phone: ");
        String passportNo = readText("Passport No: ");
        Passenger passenger = new Passenger(passengerId, fullName, email, phone, passportNo);

        Booking booking = bookingController.createBooking(passenger, flight, seatNumber);
        bookingView.showBooking(booking);
        bookingView.showMessage("Booking created. Use Booking ID to confirm and pay.");
    }

    private int readInt(String prompt) {
        System.out.print(prompt);
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void payBooking() {
        String bookingId = readText("Booking ID: ");
        System.out.println("Payment method: 1. Credit Card 2. E-Wallet 3. Cash");
        int m = readInt("Choose method: ");
        PaymentMethod method;
        switch (m) {
            case 2:
                method = PaymentMethod.E_WALLET;
                break;
            case 3:
                method = PaymentMethod.CASH;
                break;
            default:
                method = PaymentMethod.CREDIT_CARD;
                break;
        };
        Ticket ticket = bookingController.payBooking(bookingId, method);
        bookingView.showTicket(ticket);
    }

    private void findBooking() {
        String bookingId = readText("Booking ID: ");
        Booking booking = bookingController.findBooking(bookingId);
        if (booking == null) {
            bookingView.showError("Booking not found: " + bookingId);
        } else {
            bookingView.showBooking(booking);
        }
    }

    public void start() {
        while (true) {
            printMenu();
            int choice = readInt("Choose: ");
            try {
                switch (choice) {
                    case 1:
                        flightView.showFlights(flightController.getAllFlights());
                        break;
                    case 2:
                        searchFlight();
                        break;
                    case 3:
                        createBooking();
                        break;
                    case 4:
                        bookingController.confirmBooking(readText("Booking ID: "));
                        bookingView.showMessage("Booking confirmed.");
                        break;
                    case 5:
                        payBooking();
                        break;
                    case 6:
                        bookingController.checkInBooking(readText("Booking ID: "));
                        bookingView.showMessage("Checked in successfully");
                        break;
                    case 7:
                        bookingController.cancelBooking(readText("Booking ID: "));
                        bookingView.showMessage("Booking cancelled.");
                        break;
                    case 8:
                        findBooking();
                        break;
                    case 0:
                        bookingView.showMessage("Thanks for visiting");
                        return;
                    default:
                        bookingView.showError("Invalid choice.");
                }
            } catch (IllegalStateException | IllegalArgumentException e) {
                bookingView.showError(e.getMessage());
            }
        }
    }
}
