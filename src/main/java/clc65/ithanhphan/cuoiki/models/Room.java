package clc65.ithanhphan.cuoiki.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_code", nullable = false, unique = true, length = 20)
    private String roomCode;

    @Column(name = "room_name", nullable = false, length = 100)
    private String roomName;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(precision = 6, scale = 2)
    private BigDecimal area;

    @Column(name = "max_people")
    private Integer maxPeople;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('AVAILABLE', 'RENTED', 'MAINTENANCE') DEFAULT 'AVAILABLE'")
    private RoomStatus status = RoomStatus.AVAILABLE;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
