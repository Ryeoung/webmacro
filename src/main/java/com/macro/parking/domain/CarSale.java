package com.macro.parking.domain;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name = "car_sale")
public class CarSale {
	@Id
    @GeneratedValue
    @Column(name = "car_sale_id", nullable = false)
    private int carSaleId;
    
	@ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    @ManyToOne
    @JoinColumn(name = "sale_id")
    private Sale sale;

    @Column
    private int discountRate;
}
