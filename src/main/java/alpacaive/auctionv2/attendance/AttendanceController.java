package alpacaive.auctionv2.attendance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class AttendanceController {

    @Autowired
    AttendanceService service;

    @PostMapping(value = "/attendancecheck", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public Map<String, String> attendanceCheck(@RequestBody Map<String, String> request) {
        String id = request.get("id");
        int res = service.attendanceCheck(id);
        String msg;

        switch (res) {
            case 1:
                msg = "출석체크 완료";
                break;
            case 2:
                msg = "이미 출석체크를 완료했습니다";
                break;
            case 3:
                msg = "없는 회원입니다";
                break;
            default:
                msg = "알 수 없는 오류가 발생했습니다";
                break;
        }

        Map<String, String> response = new HashMap<>();
        response.put("msg", msg);
        return response;
    }
}
