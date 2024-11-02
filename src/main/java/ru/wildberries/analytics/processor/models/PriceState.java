package ru.wildberries.analytics.processor.models;

public class PriceState {

    private String time;

    private int price;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "PriceState{" +
                "time='" + time + '\'' +
                ", price=" + price +
                '}';
    }
}
