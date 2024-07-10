package alpacaive.auctionv2.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface MemberDao extends JpaRepository<Member, String> {
   ArrayList<Member> findByType(String type);
   ArrayList<Member> findAllByOrderByExpDesc();
}