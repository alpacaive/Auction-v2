package alpacaive.auctionv2.coupon;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberCouponDao extends JpaRepository<MemberCoupon, Long> {
    List<MemberCoupon> findByMemberId(String memberId);
    MemberCoupon findByCouponIdAndMemberId(long couponId, String memberId);
}
