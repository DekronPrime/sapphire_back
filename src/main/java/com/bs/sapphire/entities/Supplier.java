package com.bs.sapphire.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "suppliers")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "supplier_seq")
    @SequenceGenerator(name = "supplier_seq", sequenceName = "supplier_seq", allocationSize = 1)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "contact_person", nullable = false)
    private String contactPerson;

    @Column(name = "rating", nullable = false)
    private Double rating;

    @Column(name = "last_supply_date")
    private LocalDate lastSupplyDate;

    @ManyToMany
    @JoinTable(
            name = "supplier_materials",
            joinColumns = @JoinColumn(name = "supplier_id"),
            inverseJoinColumns = @JoinColumn(name = "material_id")
    )
    private List<Material> materials = new ArrayList<>();

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Supply> supplies = new ArrayList<>();

    public void addMaterials(List<Material> materials) {
        this.materials.addAll(materials);
        for (Material material : materials) {
            material.getSuppliers().add(this);
        }
    }

    public void removeMaterials(List<Material> materials) {
        for (Material material : materials) {
            material.getSuppliers().remove(this);
        }
        this.materials.clear();
    }
}