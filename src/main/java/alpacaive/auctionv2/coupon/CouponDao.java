package alpacaive.auctionv2.coupon;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface CouponDao extends JpaRepository<Coupon, Long> {

}
