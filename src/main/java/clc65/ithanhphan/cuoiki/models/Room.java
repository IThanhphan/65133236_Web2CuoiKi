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

    private String roomCode;

    private String roomName;

    private BigDecimal price;

    @Column(precision = 6, scale = 2)
    private BigDecimal area;

    private Integer maxPeople;

    @Enumerated(EnumType.STRING)
    private RoomStatus status;

    private String description;

    private LocalDateTime createdAt;
}
