package alpacaive.auctionv2.member;

import alpacaive.auctionv2.coupon.MemberCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberDao extends JpaRepository<Member, String> {
   ArrayList<Member> findByType(String type);
   ArrayList<Member> findAllByOrderByExpDesc();
   //member_id로 쿠폰 목록조회(페치 조인)
   @Query("select m from Member m join fetch m.member  where m.id = :id")
   Optional<Member> findByIdWithMember(@Param("id") String id);
   //member_id로 쿠폰 목록조회(컬렉션 조인)
   @Query("select mc from Member m LEFT join m.member mc where m.id=:id")
   List<MemberCoupon> findByMember(String id);
}