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
    @Transactional
    public synchronized void create(long coupon_id, String member_id) {
        if (memberCouponDao.findByCouponIdAndMemberId(coupon_id, member_id) != null) {
            return;
        }
        Optional<Member> byId = Optional.of(memberDao.findById(member_id).orElseThrow());
        Member member = byId.get();
        Optional<Coupon> findCoupon = Optional.of(couponDao.findById(coupon_id));
        Coupon coupon = findCoupon.get();
        log.debug("coupon_amount {}", coupon.getAmount());
        if (coupon.getAmount()==0){
            log.debug("물량이 없습니다.");
            return;
        }
        MemberCoupon newCoupon = MemberCoupon.create(member, coupon);
        discountCoupon(coupon_id);
        memberCouponDao.save(newCoupon);
        log.debug("Created coupon for member {}", member_id);
    }

    //내 쿠폰 목록 검색
    public List<Coupon> findCouponByMemberId(String member_id) {
        Optional<Member> member = Optional.ofNullable(memberDao.findByIdWithMember(member_id).orElse(null));
        if (member.isEmpty()) {
            return null;
        }
        List<Coupon> couponList = new ArrayList<>();
        for (MemberCoupon mc : member.get().getMember()) {
            if (!mc.isUsed()) {
                couponList.add(mc.getCoupon());
            }
            log.debug("Found coupon for member {}", mc.getCoupon().getDiscount());
        }
        return couponList;
    }

    //쿠폰 사용시 사용여부 변경
    //사용하면 true 아니면 false
    public MemberCoupon updateUsed(long coupon_id, String member_id) {
        MemberCoupon coupon = memberCouponDao.findByCouponIdAndMemberId(coupon_id, member_id);
        coupon.updateUsed();
        memberCouponDao.save(coupon);
        log.debug("Updated coupon for member={},coupon={}", member_id, coupon.getCoupon().getName());
        return coupon;
    }

    //쿠폰재고 감소
    public void discountCoupon(long coupon_id) {
        Optional<Coupon> coupon = Optional.of(couponDao.findById(coupon_id));
        coupon.get().discountAmount();
        int amount = coupon.get().getAmount();
        log.debug(String.valueOf(amount));
    }

}
