package alpacaive.auctionv2.dataroom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface DataroomDao extends JpaRepository<Dataroom, Integer> {
    ArrayList<Dataroom> findAllByOrderByWdateDesc();
}
