package alpacaive.auctionv2.coupon;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CouponDao extends JpaRepository<Coupon, Long> {
    //쿠폰 검색
    Coupon findById(long id);
}
