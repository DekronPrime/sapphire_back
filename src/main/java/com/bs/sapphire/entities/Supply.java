package com.bs.sapphire.entities;

import com.bs.sapphire.entities.enums.SupplyStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "supplies")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Supply {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "supply_seq")
    @SequenceGenerator(name = "supply_seq", sequenceName = "supply_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "supply_date", nullable = false)
    private LocalDateTime supplyDate;

    @Column(name = "note", length = 500)
    private String note;

    @Column(name = "total_price", nullable = false, precision = 8, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private SupplyStatus status = SupplyStatus.PENDING;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, updatable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}