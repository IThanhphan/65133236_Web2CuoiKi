package clc65.ithanhphan.cuoiki.controllers;

import clc65.ithanhphan.cuoiki.models.Room;
import clc65.ithanhphan.cuoiki.models.RoomStatus;
import clc65.ithanhphan.cuoiki.services.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping
    public String listRooms(@RequestParam(value = "roomCode", required = false) String roomCode,
                            @RequestParam(value = "status", required = false) RoomStatus status,
                            @RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "size", defaultValue = "5") int size,
                            Model model) {

        Page<Room> pageResult = roomService.getRooms(roomCode, status, page, size);

        model.addAttribute("rooms", pageResult.getContent());

        model.addAttribute("roomPage", pageResult);

        model.addAttribute("roomCode", roomCode);
        model.addAttribute("status", status);

        return "rooms/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {

        model.addAttribute(
                "room",
                new Room()
        );

        return "rooms/create";
    }

    @PostMapping("/create")
    public String createRoom(
            @ModelAttribute Room room
    ) {

        roomService.save(room);

        return "redirect:/rooms";
    }

    @GetMapping("/edit/{id}")
    public String editForm(
            @PathVariable Long id,
            Model model
    ) {

        Room room = roomService.getRoomById(id);

        model.addAttribute(
                "room",
                room
        );

        return "rooms/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateRoom(
            @PathVariable Long id,
            @ModelAttribute Room room
    ) {

        room.setId(id);

        roomService.save(room);

        return "redirect:/rooms";
    }

    @PostMapping("/delete/{id}")
    public String deleteRoom(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            roomService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa căn phòng thành công thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể xóa phòng: " + e.getMessage());
        }
        return "redirect:/rooms";
    }
}
