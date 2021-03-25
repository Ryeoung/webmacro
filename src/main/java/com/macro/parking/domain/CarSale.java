package com.macro.parking.domain;

import javax.persistence.*;

@Entity
@Table(name = "car_sale")
public class CarSale {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    @ManyToOne
    @JoinColumn(name = "sale_id")
    private Sale sale;

    @Column
    private int discountRate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public int getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(int discountRate) {
        this.discountRate = discountRate;
    }

    @Override
    public String toString() {
        return "CarSale{" +
                "id=" + id +
                ", car=" + car +
                ", sale=" + sale +
                ", discountRate=" + discountRate +
                '}';
    }
}
