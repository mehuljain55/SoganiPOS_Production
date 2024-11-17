package com.Soganis.Model;

public class TransactionModel {
    private  int card;
    private int cash;
    private int upi;

    public TransactionModel(int card, int cash, int upi) {
        this.card = card;
        this.cash = cash;
        this.upi = upi;
    }

    public TransactionModel() {
    }

    public int getCard() {
        return card;
    }

    public void setCard(int card) {
        this.card = card;
    }

    public int getCash() {
        return cash;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }

    public int getUpi() {
        return upi;
    }

    public void setUpi(int upi) {
        this.upi = upi;
    }

    @Override
    public String toString() {
        return "TransactionModel{" +
                "card=" + card +
                ", cash=" + cash +
                ", upi=" + upi +
                '}';
    }
}
