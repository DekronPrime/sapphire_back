package com.bs.sapphire.entities;

import com.bs.sapphire.entities.enums.MaterialCategory;
import com.bs.sapphire.entities.enums.Unit;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "materials")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "material_seq")
    @SequenceGenerator(name = "material_seq", sequenceName = "material_seq", allocationSize = 1)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "unit", nullable = false)
    @Enumerated(EnumType.STRING)
    private Unit unit;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToMany(mappedBy = "materials")
    private List<Supplier> suppliers = new ArrayList<>();

    @Column(name = "price", nullable = false, precision = 7, scale = 2)
    private BigDecimal price;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    private MaterialCategory category;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "min_amount_threshold", nullable = false)
    private Integer minAmountThreshold;

    @Column(name = "enough_amount_threshold", nullable = false)
    private Integer enoughAmountThreshold;

    @OneToMany(mappedBy = "material", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Supply> supplies = new ArrayList<>();

    @OneToMany(mappedBy = "material", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Usage> usages = new ArrayList<>();

    public void addSuppliers(List<Supplier> suppliers) {
        this.suppliers.addAll(suppliers);
        for (Supplier supplier : suppliers) {
            supplier.getMaterials().add(this);
        }
    }

    public void removeSuppliers(List<Supplier> suppliers) {
        for (Supplier supplier : suppliers) {
            supplier.getMaterials().remove(this);
        }
        this.suppliers.clear();
    }
}