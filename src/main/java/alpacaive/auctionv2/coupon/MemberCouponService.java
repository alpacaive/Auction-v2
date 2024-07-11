package alpacaive.auctionv2.coupon;

import alpacaive.auctionv2.member.Member;
import alpacaive.auctionv2.member.MemberDao;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberCouponService {
    private static final Logger log = LoggerFactory.getLogger(MemberCouponService.class);
    private final MemberDao memberDao;
    private final MemberCouponDao memberCouponDao;
    private final CouponDao couponDao;

    //회원에게 쿠폰 발급
    public void create(long coupon_id, String member_id) {
        Optional<Member> byId = Optional.of(memberDao.findById(member_id).orElseThrow());
        Member member = byId.get();
        Optional<Coupon> findCoupon = Optional.of(couponDao.findById(coupon_id).orElseThrow());
        Coupon coupon = findCoupon.get();
        if (coupon.getAmount()==0){
            log.debug("물량이 없습니다.");
            return;
        }
        MemberCoupon newCoupon = MemberCoupon.create(member, coupon);
        memberCouponDao.save(newCoupon);
        log.debug("Created coupon for member {}", member_id);
        discountCoupon(coupon_id);
    }

    //내 쿠폰 목록 검색
    public List<Coupon> findCouponByMemberId(String member_id) {
        Member member = memberDao.findByIdWithMember(member_id);
        List<Coupon> couponList = new ArrayList<>();
        for (MemberCoupon mc : member.getMember()) {
            couponList.add(mc.getCoupon());
        }
        return couponList;
    }

    //쿠폰 사용시 사용여부 변경
    //사용하면 true 아니면 false
    public void updateUsed(long coupon_id, String member_id) {
        MemberCoupon coupon = memberCouponDao.findByCouponIdAndMemberId(coupon_id, member_id);
        coupon.updateUsed();
        memberCouponDao.save(coupon);
        log.debug("Updated coupon for member={},coupon={}", member_id, coupon.getCoupon().getName());
    }

    //쿠폰재고 감소
    public void discountCoupon(long coupon_id) {
        Optional<Coupon> coupon = Optional.of(couponDao.findById(coupon_id).orElseThrow());
        coupon.get().discountAmount();
        int amount = coupon.get().getAmount();
        log.debug(String.valueOf(amount));
    }
}
