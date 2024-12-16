package com.Soganis.Model;

public class PaymentModel {
    private int paymentId;
    private String paymentMode;
    private String storeId;

    public PaymentModel(int paymentId, String paymentMode, String storeId) {
        this.paymentId = paymentId;
        this.paymentMode = paymentMode;
        this.storeId = storeId;
    }

    public PaymentModel() {
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
}
