package alpacaive.auctionv2.coupon;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Slf4j
class MemberCouponServiceTest {
    @Autowired
    private MemberCouponService memberCouponService;
    @Autowired
    private MemberCouponDao memberCouponDao;
    @Autowired
    private CouponDao couponDao;

    @Test
    @Transactional
    @Rollback(value = false)
    void 내_쿠폰목록_조회() {
        List<Coupon> aaa = memberCouponService.findCouponByMemberId("aaa");
        assertNotNull(aaa);
        for (Coupon coupon : aaa) {
            log.debug(coupon.getName());
        }
    }

    @Test
    @Transactional
    @Rollback(value = false)
    void 사용여부_변경() {
        memberCouponService.updateUsed(2L,"aaa");
        log.debug(String.valueOf(memberCouponDao.findByMemberId("aaa").get(1).isUsed()));
    }

    @Test
    @Transactional
    @Rollback(value = false)
    void 쿠폰재고감소(){
        memberCouponService.create(1L,"aaa");
        Optional<Coupon> byId = Optional.of(couponDao.findById(1L));
        assertEquals(byId.get().getAmount(), 0);
    }
}