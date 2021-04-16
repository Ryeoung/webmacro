package com.macro.parking.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "parking_ticket")
public class ParkingTicket {
    @Id
    @GeneratedValue
    @Column(name = "parking_ticket_id", nullable = false)
    private int parkingTicketId;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany( mappedBy = "parkingTicket")
    private List<ParkingLotParkingTicket> parkingLotParkingTickets = new ArrayList<>();
}
