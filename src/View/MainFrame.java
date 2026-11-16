package View;

import Controller.*;
import Model.*;
import Model.enums.*;
import Model.factory.SeatFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private FlightController flightController = new FlightController();
    private BookingController bookingController = new BookingController();

    private DefaultTableModel tableModel;
    private JTable flightTable;
    private JTextField bookingIdField = new JTextField(10);
    private JLabel statusLabel = new JLabel(" ");

    public MainFrame() {
        super("Airline Reservation System");
        seedSampleFlights();

        tableModel = new DefaultTableModel(
                new String[]{"Flight No", "From", "To", "Departure", "Arrival", "Base Price"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        flightTable = new JTable(tableModel);
        add(new JScrollPane(flightTable), BorderLayout.CENTER);

        JButton refreshBtn = new JButton("Refresh");
        JButton bookBtn = new JButton("Create Booking");
        JButton confirmBtn = new JButton("Confirm");
        JButton payBtn = new JButton("Pay");
        JButton checkInBtn = new JButton("Check In");
        JButton cancelBtn = new JButton("Cancel");
        JButton findBtn = new JButton("Find");

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.add(refreshBtn);
        bottom.add(bookBtn);
        bottom.add(new JLabel("Booking ID:"));
        bottom.add(bookingIdField);
        bottom.add(confirmBtn);
        bottom.add(payBtn);
        bottom.add(checkInBtn);
        bottom.add(cancelBtn);
        bottom.add(findBtn);

        JPanel south = new JPanel(new BorderLayout());
        south.add(bottom, BorderLayout.CENTER);
        south.add(statusLabel, BorderLayout.SOUTH);
        add(south, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> refreshTable());
        bookBtn.addActionListener(e -> createBooking());
        confirmBtn.addActionListener(e -> runAction("confirm"));
        payBtn.addActionListener(e -> payBooking());
        checkInBtn.addActionListener(e -> runAction("checkin"));
        cancelBtn.addActionListener(e -> runAction("cancel"));
        findBtn.addActionListener(e -> findBooking());

        refreshTable();
        setSize(820, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Flight f : flightController.getAllFlights()) {
            tableModel.addRow(new Object[]{
                    f.getFlightNo(), f.getFrom(), f.getTo(),
                    f.getDepartureTime().format(FMT), f.getArrivalTime().format(FMT),
                    String.format("%,.0f", f.getBasePrice())
            });
        }
        statusLabel.setText("Loaded " + tableModel.getRowCount() + " flights.");
    }

    private Flight getSelectedFlight() {
        int row = flightTable.getSelectedRow();
        if (row < 0) {
            showError("Select a flight in the table first.");
            return null;
        }
        return flightController.findFlight((String) tableModel.getValueAt(row, 0));
    }

    private void createBooking() {
        Flight flight = getSelectedFlight();
        if (flight == null) return;

        JComboBox<String> seatCombo = new JComboBox<>();
        for (Seat s : flightController.getAvailableSeats(flight)) {
            seatCombo.addItem(s.getSeatNumber());
        }
        if (seatCombo.getItemCount() == 0) {
            showError("No available seats on this flight.");
            return;
        }
        JTextField idF = new JTextField();
        JTextField nameF = new JTextField();
        JTextField emailF = new JTextField();
        JTextField phoneF = new JTextField();
        JTextField passportF = new JTextField();

        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        panel.add(new JLabel("Seat:"));         panel.add(seatCombo);
        panel.add(new JLabel("Passenger ID:")); panel.add(idF);
        panel.add(new JLabel("Full name:"));    panel.add(nameF);
        panel.add(new JLabel("Email:"));        panel.add(emailF);
        panel.add(new JLabel("Phone:"));        panel.add(phoneF);
        panel.add(new JLabel("Passport No:"));  panel.add(passportF);

        int ok = JOptionPane.showConfirmDialog(this, panel,
                "Create Booking - " + flight.getFlightNo(), JOptionPane.OK_CANCEL_OPTION);
        if (ok != JOptionPane.OK_OPTION) return;

        try {
            Passenger p = new Passenger(idF.getText().trim(), nameF.getText().trim(),
                    emailF.getText().trim(), phoneF.getText().trim(), passportF.getText().trim());
            Booking b = bookingController.createBooking(p, flight, (String) seatCombo.getSelectedItem());
            bookingIdField.setText(b.getPNR());
            statusLabel.setText("Booking created: " + b.getPNR());
            JOptionPane.showMessageDialog(this, bookingSummary(b), "Booking Created",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalStateException | IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }

    private void runAction(String action) {
        String id = bookingIdField.getText().trim();
        if (id.isEmpty()) { showError("Enter a Booking ID first."); return; }
        try {
            switch (action) {
                case "confirm":
                    bookingController.confirmBooking(id);
                    statusLabel.setText("Booking confirmed: " + id);
                    break;
                case "checkin":
                    bookingController.checkInBooking(id);
                    statusLabel.setText("Checked in: " + id);
                    break;
                case "cancel":
                    bookingController.cancelBooking(id);
                    statusLabel.setText("Booking cancelled: " + id);
                    break;
            }
        } catch (IllegalStateException | IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }

    private void payBooking() {
        String id = bookingIdField.getText().trim();
        if (id.isEmpty()) {
            showError("Enter a Booking ID first.");
            return;
        }
        PaymentMethod method = (PaymentMethod) JOptionPane.showInputDialog(this, "Payment method:", "Pay", JOptionPane.QUESTION_MESSAGE, null, PaymentMethod.values(), PaymentMethod.CREDIT_CARD);
        if (method == null) return;
        try {
            Ticket t = bookingController.payBooking(id, method);
            statusLabel.setText("Paid. Ticket: " + t.getTicketId());
            JOptionPane.showMessageDialog(this, "Ticket issued: " + t.getTicketId() + "\n" + bookingSummary(bookingController.findBooking(id)), "Payment Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalStateException | IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }

    private void findBooking() {
        String id = bookingIdField.getText().trim();
        if (id.isEmpty()) { showError("Enter a Booking ID first."); return; }
        Booking b = bookingController.findBooking(id);
        if (b == null) {
            showError("Booking not found: " + id);
        } else {
            JOptionPane.showMessageDialog(this, bookingSummary(b), "Booking " + id,
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private String bookingSummary(Booking b) {
        return "Booking ID: " + b.getPNR() + "\nPassenger: " + b.getPassenger().getFullName() + "\nFlight: " + b.getFlight().getFlightNo() + "\nSeat: " + b.getSeat().getSeatNumber() + "\nTotal: " + String.format("%,.0f", b.getTotalPrice()) + "\nStatus: " + b.getState().getName();
    }

    private void showError(String msg) {
        statusLabel.setText("Error: " + msg);
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // --- Seed data (same sample flights as the console version) ---
    private List<Seat> createSeats(int totalSeats) {
        List<Seat> seats = new ArrayList<>();
        String letters = "ABCDEF";
        for (int i = 0; i < totalSeats; i++) {
            String seatNumber = (i / 6 + 1) + "" + letters.charAt(i % 6);
            SeatClass seatClass;
            if (i < 6) seatClass = SeatClass.FIRST;
            else if (i < 12) seatClass = SeatClass.BUSINESS;
            else seatClass = SeatClass.ECONOMY;
            seats.add(SeatFactory.createSeat(seatClass, seatNumber));
        }
        return seats;
    }

    private void seedSampleFlights() {
        Aircraft a1 = new Aircraft("AC001", "Airbus A320", "Airbus", 18);
        Aircraft a2 = new Aircraft("AC002", "Boeing 787", "Boeing", 18);
        Aircraft a3 = new Aircraft("AC003", "Airbus A321", "Airbus", 18);
        LocalDateTime dep1 = LocalDateTime.now().plusDays(1).withHour(8).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime dep2 = LocalDateTime.now().plusDays(1).withHour(14).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime dep3 = LocalDateTime.now().plusDays(2).withHour(9).withMinute(30).withSecond(0).withNano(0);
        flightController.addFlight(new Flight("VN123", "HAN", "SGN", dep1, dep1.plusHours(2), a1, 1500000, createSeats(18)));
        flightController.addFlight(new Flight("VN456", "SGN", "DAD", dep2, dep2.plusHours(1).plusMinutes(30), a2, 1200000, createSeats(18)));
        flightController.addFlight(new Flight("VN789", "HAN", "DAD", dep3, dep3.plusHours(1).plusMinutes(30), a3, 900000, createSeats(18)));
    }
}
