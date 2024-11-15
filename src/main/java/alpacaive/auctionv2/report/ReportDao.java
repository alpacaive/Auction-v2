package alpacaive.auctionv2.report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface ReportDao extends JpaRepository<Report, Integer> {
    ArrayList<Report> findByReadNumOrderByWdate(int is_read);
    ArrayList<Report> findByTypeOrderByWdate(int type);
}
