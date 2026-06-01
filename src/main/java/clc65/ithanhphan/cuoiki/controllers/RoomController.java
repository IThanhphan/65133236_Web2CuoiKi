package clc65.ithanhphan.cuoiki.controllers;

import clc65.ithanhphan.cuoiki.models.Room;
import clc65.ithanhphan.cuoiki.models.RoomStatus;
import clc65.ithanhphan.cuoiki.services.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping
    public String roomList(
            @RequestParam(required = false) String roomCode,
            @RequestParam(required = false) RoomStatus status,
            @RequestParam(defaultValue = "0") int page,
            Model model
    ) {
        Page<Room> roomPage =
                roomService.getRooms(
                        roomCode,
                        status,
                        page,
                        5
                );

        model.addAttribute("roomPage", roomPage);
        model.addAttribute("rooms", roomPage.getContent());

        model.addAttribute("roomCode", roomCode);
        model.addAttribute("status", status);

        return "room/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {

        model.addAttribute(
                "room",
                new Room()
        );

        return "room/create";
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

        return "room/edit";
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

    @DeleteMapping("/{id}")
    @ResponseBody
    public String deleteRoom(
            @PathVariable Long id
    ) {

        roomService.delete(id);

        return "Deleted";
    }
}
