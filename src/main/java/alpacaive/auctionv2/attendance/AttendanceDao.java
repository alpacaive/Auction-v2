package alpacaive.auctionv2.attendance;

import alpacaive.auctionv2.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;

@Repository
public interface AttendanceDao extends JpaRepository<Attendance, Integer> {

    Attendance findByMidAndToday(Member mid, LocalDate today);
}
