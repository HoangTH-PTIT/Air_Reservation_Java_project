package Model;

import Model.enums.*;
import java.time.LocalDateTime;

public class Payment {
    private String paymentId;
    private Booking booking;
    private double amount;
    private PaymentMethod method;
    private PaymentStatus status;
    private LocalDateTime paymentDate;

    public Payment(String paymentId, Booking booking, PaymentMethod method) {
        this.paymentId = paymentId;
        this.booking = booking;
        this.method = method;
        this.amount = booking.getTotalPrice(); // lấy giá từ Booking, không tự tính lại
        this.status = PaymentStatus.PENDING;
        this.paymentDate = null; // chưa trả xong thì chưa có thời điểm thanh toán
    }

    public void markSuccess() {
        if (status != PaymentStatus.PENDING) {
            throw new IllegalStateException("Payment is not pending");
        }
        this.status = PaymentStatus.SUCCESS;
        this.paymentDate = LocalDateTime.now();
    }

    public void markFailed() {
        if (status != PaymentStatus.PENDING) {
            throw new IllegalStateException("Payment is not pending");
        }
        this.status = PaymentStatus.FAILED;
    }

    public void refund() {
        if (status != PaymentStatus.SUCCESS) {
            throw new IllegalStateException("Only a successful payment can be refunded");
        }
        this.status = PaymentStatus.REFUNDED;
    }

    public boolean isSuccess() {
        return status == PaymentStatus.SUCCESS;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public Booking getBooking() {
        return booking;
    }

    public double getAmount() {
        return amount;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    @Override
    public String toString() {
        return paymentId + " - " + booking.getPNR() + " - " + amount + " - " + method + " - " + status;
    }
}
