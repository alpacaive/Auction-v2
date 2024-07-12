package alpacaive.auctionv2.attendance;

import alpacaive.auctionv2.member.Member;
import alpacaive.auctionv2.member.MemberDao;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceDao dao;
    @Autowired
    private MemberDao mdao;

    public int attendanceCheck(String id) {
        LocalDate today = LocalDate.now();
        Member m = mdao.findById(id).orElse(null);
        if (m == null) {
            return 3;
        }

        Attendance a = dao.findByMidAndToday(m, today);
        if (a != null) {
            return 2;
        }

        m.setPoint(m.getPoint() + 100);
        mdao.save(m);

        Attendance a1 = new Attendance();
        a1.setMid(m);
        a1.setToday(today);
        dao.save(a1);
        return 1;

    }

}
