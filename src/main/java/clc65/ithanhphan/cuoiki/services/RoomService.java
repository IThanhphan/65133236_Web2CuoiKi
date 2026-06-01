package clc65.ithanhphan.cuoiki.services;

import clc65.ithanhphan.cuoiki.models.Room;
import clc65.ithanhphan.cuoiki.models.RoomStatus;
import clc65.ithanhphan.cuoiki.repositories.RoomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room getRoomById(Long id) {
        return roomRepository.findById(id).orElse(null);
    }

    public Room save(Room room) {
        return roomRepository.save(room);
    }

    public void delete(Long id) {
        roomRepository.deleteById(id);
    }

    public Page<Room> getRooms(
            String roomCode,
            RoomStatus status,
            int page,
            int size
    ) {
        Specification<Room> spec = Specification.allOf();
        if (roomCode != null && !roomCode.isBlank()) {
            spec = spec.and(
                    (root, query, cb) ->
                            cb.like(
                                    cb.lower(root.get("roomCode")),
                                    "%" + roomCode.toLowerCase() + "%"
                            )
            );
        }
        if (status != null) {
            spec = spec.and(
                    (root, query, cb) ->
                            cb.equal(root.get("status"), status)
            );
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        return roomRepository.findAll(spec, pageable);
    }
}
