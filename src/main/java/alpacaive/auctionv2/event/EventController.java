package alpacaive.auctionv2.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth/event")
public class EventController {

    @Autowired
    private EventService eservice;

    @GetMapping("/add")
    public String addForm() {
        return "event/add";
    }

    @PostMapping("/add")
    public String add(EventDto dto) {
        eservice.save(dto);
        return "redirect:/all/eventlist";
    }

    @GetMapping("/edit")
    public String editForm(int num, ModelMap map) {
        EventDto e = eservice.getEvent(num);
        map.addAttribute("e", e);
        return "event/edit";
    }

    @PostMapping("/edit")
    public String edit(EventDto dto) {
        EventDto e = eservice.getEvent(dto.getNum());
        e.setWdate(dto.getWdate());
        e.setImg(dto.getImg());
        e.setTitle(dto.getTitle());
        e.setContent(dto.getContent());
        eservice.save(e);
        return "redirect:/event/list";
    }

    @RequestMapping("/del")
    public String del(int num) {
        eservice.delete(num);
        return "redirect:/event/list";
    }

    @GetMapping("/detail")
    public String detail(int num, ModelMap map) {
        map.addAttribute("e", eservice.getEvent(num));
        return "event/detail";
    }
}
