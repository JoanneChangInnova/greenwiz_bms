package com.greenwiz.bms.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user_factory", uniqueConstraints = {
        @UniqueConstraint(name = "unique_user_factory", columnNames = {"user_id", "factory_id"})
})
public class UserFactory extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "factory_id", nullable = false)
    private Long factoryId;

}