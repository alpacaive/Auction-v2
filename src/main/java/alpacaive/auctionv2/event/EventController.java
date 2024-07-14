package alpacaive.auctionv2.event;

import alpacaive.auctionv2.coupon.CouponDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth/event")
public class EventController {

    private static final Logger log = LoggerFactory.getLogger(EventController.class);
    @Autowired
    private EventService eservice;
    @Autowired
    private CouponDao couponDao;

    @GetMapping("/add")
    public String addForm(ModelMap model) {
        model.addAttribute("couponList", couponDao.findAll());
        return "event/add";
    }

    @PostMapping("/add")
    public String add(EventDto dto) {
        dto.setStatus("진행중");
        log.info("Adding event {}", dto.getCnum());
        eservice.save(dto);
        return "redirect:/all/eventlist";
    }

    @GetMapping("/edit")
    public String editForm(int num, ModelMap map) {
        EventDto e = eservice.getEvent(num);
        map.addAttribute("e", e);
        return "event/edit";
    }

    @RequestMapping("/close")
    public String close(int num) {
        eservice.update(num);
        return "redirect:/event/list";
    }

    @GetMapping("/detail")
    public String detail(int num, ModelMap map) {
        map.addAttribute("e", eservice.getEvent(num));
        return "event/detail";
    }
}
