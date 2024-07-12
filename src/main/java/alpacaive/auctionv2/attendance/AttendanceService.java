package alpacaive.auctionv2.attendance;

import alpacaive.auctionv2.member.Member;
import alpacaive.auctionv2.member.MemberDao;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceDao dao;
    @Autowired
    private MemberDao mdao;

    public Map attendanceCheck(String id) {
        Map map = new HashMap();

        LocalDate today = LocalDate.now();
        Member m = mdao.findById(id).orElse(null);
        if (m == null) {
            map.put("msg", "없는 회원입니다.");
            return map;
        }

        Attendance a = dao.findByMidAndToday(m, today);
        if (a != null) {
            map.put("msg", "이미 출석체크를 완료하였습니다.");
            return map;
        }

        m.setPoint(m.getPoint() + 100);
        mdao.save(m);

        Attendance a1 = new Attendance();
        a1.setMid(m);
        a1.setToday(today);
        dao.save(a1);
        map.put("msg", "출석체크 완료.");
        return map;

    }

}
