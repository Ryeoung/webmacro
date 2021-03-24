package com.macro.parking.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Table(name = "parking_ticket")
public class ParkingTicket {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany( mappedBy = "parkingTicket")
    private List<ParkingLotParkingTicket> parkingLotParkingTickets = new ArrayList<>();
}
