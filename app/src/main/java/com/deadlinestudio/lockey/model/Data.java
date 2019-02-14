package com.deadlinestudio.lockey.model;

/**
 * @brief Model Class with time data
 */
public class Data {
    String category;
    String date;
    String amount;
    String target_time;

    public Data() {}

    /**
     * @brief Constructor
     * @param category
     * @param date
     * @param amount
     * @param target_time
     */
    public Data(String category, String date, String amount, String target_time) {
        this.category = category;
        this.date = date;
        this.amount = amount;
        this.target_time = target_time;
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }

    public String getAmount() {
        return amount;
    }

    public String getTarget_time() {
        return target_time;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setTarget_time(String target_time) {
        this.target_time = target_time;
    }
}
