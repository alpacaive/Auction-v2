package alpacaive.auctionv2.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface EventDao extends JpaRepository<Event, Integer> {

    // 전체목록 최신순으로 불러오기
    ArrayList<Event> findAllByOrderByNumDesc();
}
