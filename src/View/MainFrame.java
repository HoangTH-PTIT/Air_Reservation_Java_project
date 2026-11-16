package View;

import Controller.*;
import Model.*;
import Model.enums.*;
import Model.factory.SeatFactory;
import javax.swing.table.TableCellRenderer;
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
        flightController.seedSampleData();

        tableModel = new DefaultTableModel(
                new String[]{"Flight No", "From", "To", "Departure", "Arrival", "Base Price"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        flightTable = new JTable(tableModel) {
          @Override
          public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
              Component c = super.prepareRenderer(renderer, row, col);
              if(!isRowSelected(row)) {
                  c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(232,240, 248));
              }
              ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
              return c;
          }
        };
        flightTable.setRowHeight(26);
        flightTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        flightTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        flightTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JLabel title = new JLabel("AIRLINE RESERVATION SYSTEM");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(31, 78, 120));
        header.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        header.add(title, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);
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
}
