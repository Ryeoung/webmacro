package com.macro.parking.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "car")
public class Car {
    @Id
    @GeneratedValue
    @Column(name = "car_id", nullable = false)
    private int carId;

    @Column(name = "number", nullable = false)
    private String number;

    @Column(name = "visit_count", nullable = false)
    private int visitCount;

    @OneToMany(mappedBy = "car")
    private List<CarParkingLot> carParkingLots = new ArrayList<>();

    @OneToMany(mappedBy = "car")
    private List<CarSale> carSales = new ArrayList<>();

}
