package clc65.ithanhphan.cuoiki.repositories;

import clc65.ithanhphan.cuoiki.models.Room;
import clc65.ithanhphan.cuoiki.models.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RoomRepository extends JpaRepository<Room, Long>, JpaSpecificationExecutor<Room> {
    long countByStatus(RoomStatus status);
}
