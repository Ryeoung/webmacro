package com.macro.parking.domain;

import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sale")
@ToString
public class Sale {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "item", nullable = false)
    private String item;

    @OneToMany(mappedBy = "sale")
    private List<CarSale> carSales = new ArrayList<>();


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public List<CarSale> getCarSales() {
        return carSales;
    }

    public void setCarSales(List<CarSale> carSales) {
        this.carSales = carSales;
    }

}
