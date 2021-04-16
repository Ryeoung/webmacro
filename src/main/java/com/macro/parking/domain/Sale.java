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
@Table(name = "sale")
public class Sale {
    @Id
    @GeneratedValue
    @Column(name = "sale_id", nullable = false)
    private int saleId;

    @Column(name = "item", nullable = false)
    private String item;

    @OneToMany(mappedBy = "sale")
    private List<CarSale> carSales = new ArrayList<>();
}
