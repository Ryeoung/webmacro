package com.macro.parking.domain;

import javax.persistence.*;

@Entity
@Table(name = "car_parking_lot")
public class CarParkingLot {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private int id;

    @ManyToOne
    @JoinColumn(name = "id", insertable=false, updatable = false)
    private Car car;


    @ManyToOne
    @JoinColumn(name = "id", insertable=false, updatable = false)
    private ParkingLot parkingLot;

    @Column
    private String parkingTime;

    @Column
    private String exitTime;

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

    public String getParkingTime() {
        return parkingTime;
    }

    public void setParkingTime(String parkingTime) {
        this.parkingTime = parkingTime;
    }

    public String getExitTime() {
        return exitTime;
    }

    public void setExitTime(String exitTime) {
        this.exitTime = exitTime;
    }

    @Override
    public String toString() {
        return "CarParkingLot{" +
                "id=" + id +
                ", car=" + car +
                ", parkingLot=" + parkingLot +
                ", parkingTime='" + parkingTime + '\'' +
                ", exitTime='" + exitTime + '\'' +
                '}';
    }
}
