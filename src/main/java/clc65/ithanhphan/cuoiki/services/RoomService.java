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
        Room room = roomRepository.findById(id).orElse(null);

        if (room == null) {
            throw new IllegalArgumentException("Không tìm thấy phòng có ID: " + id);
        }

        if (room.getStatus() != null && "RENTED".equals(room.getStatus().name())) {
            throw new IllegalStateException("Không thể xóa phòng đang trong trạng thái CÓ KHÁCH THUÊ!");
        }

        try {
            roomRepository.delete(room);
        } catch (Exception e) {
            throw new IllegalStateException("Phòng này đã từng phát sinh hợp đồng/hóa đơn trong quá khứ, không thể xóa để đảm bảo dữ liệu lịch sử kế toán!");
        }
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
