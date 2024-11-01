package ru.wildberries.analytics.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceStateDTO {

    @JsonProperty("dt")
    private String time;

    @JsonProperty("price")
    private PriceDTO currency;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public PriceDTO getCurrency() {
        return currency;
    }

    public void setCurrency(PriceDTO currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "PriceStateDTO{" +
                "time='" + time + '\'' +
                ", currency=" + currency +
                '}';
    }
}
