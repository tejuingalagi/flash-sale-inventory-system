package com.teju.flashsale.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Getter
@Setter
@NoArgsConstructor // empty constructor — required by Hibernate
@AllArgsConstructor // constructor with all fields — required by @Builder
@Builder //@Builder makes object creation much cleaner. gives you the clean .builder() syntax
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;
    private int availableStock;

    @Version
    private Long version;
}