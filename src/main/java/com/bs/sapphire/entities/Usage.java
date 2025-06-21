package com.bs.sapphire.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "usage")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Usage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usage_seq")
    @SequenceGenerator(name = "usage_seq", sequenceName = "usage_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "amount_used", nullable = false)
    private Integer amountUsed;

    @Column(name = "usage_date", nullable = false)
    private LocalDate usageDate;

    @Column(name = "comment", length = 500)
    private String comment;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, updatable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}