package View;

import Controller.*;
import Model.*;
import Model.enums.*;
import Model.factory.*;

import java.time.*;
import java.time.format.*;
import java.util.*;


public class MainView {
    private static final DateTimeFormatter DATE_TIME_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Scanner sc = new Scanner(System.in);
    private FlightController flightController = new FlightController();
    private BookingController bookingController = new BookingController();
    private FlightView flightView = new FlightView();
    private BookingView bookingView = new BookingView();

    public MainView() {
        seedSampleFlights();
    }

    private List<Seat> createSeats(int totalSeats) {
        List<Seat> seats = new ArrayList<>();
        String letters = "ABCDEF";
        for(int i=0; i<totalSeats; i++) {
            String seatNumber = (i/6 + 1) + "" + letters.charAt(i%6);
            SeatClass seatClass;
            if(i<6) {
                seatClass = SeatClass.FIRST;
            } else if (i<12) {
                seatClass = SeatClass.BUSINESS;
            } else {
                seatClass = SeatClass.ECONOMY;
            }
            seats.add(SeatFactory.createSeat(seatClass, seatNumber));
        }
        return seats;
    }

    private void seedSampleFlights() {
        Aircraft a1 = new Aircraft("AC001", "Airbus A320", "Airbus", 12);
        Aircraft a2 = new Aircraft("AC002", "Boeing 787", "Boeing", 12);
        Aircraft a3 = new Aircraft("AC003", "Airbus A321", "Airbus", 12);

        LocalDateTime dep1 = LocalDateTime.now().plusDays(1).withHour(8).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime dep2 = LocalDateTime.now().plusDays(1).withHour(14).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime dep3 = LocalDateTime.now().plusDays(2).withHour(9).withMinute(30).withSecond(0).withNano(0);

        flightController.addFlight(new Flight("VN123", "HAN", "SGN", dep1, dep1.plusHours(2), a1, 1500000, createSeats(12)));
        flightController.addFlight(new Flight("VN456", "SGN", "DAD", dep2, dep2.plusHours(1).plusMinutes(30), a2, 1200000, createSeats(12)));
        flightController.addFlight(new Flight("VN789", "HAN", "DAD", dep3, dep3.plusHours(1).plusMinutes(30), a3, 900000, createSeats(12)));
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
