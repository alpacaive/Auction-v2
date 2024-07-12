package alpacaive.auctionv2.attendance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AttendanceController {

    @Autowired
    AttendanceService service;

    @RequestMapping("/auth/attendancecheck")
    public String attendanceCheck(String id) {
        service.attendanceCheck(id);
        return "redirect:/event/list";
    }
}
