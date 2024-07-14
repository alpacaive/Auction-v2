package alpacaive.auctionv2.coupon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CouponController {
    private static final Logger log = LoggerFactory.getLogger(CouponController.class);
    @Autowired
    private MemberCouponService memberCouponService;

    @PostMapping("/auth/coupon")
    @ResponseBody
    public String createCoupon(@RequestParam(value = "coupon_id") Long coupon_id,
                               @RequestParam(value = "id") String id){
        log.info("Creating coupon with id {}", coupon_id);
        log.debug("쿠폰발급 컨트롤러");
        memberCouponService.create(coupon_id, id);
        return "redirect:/eventlist";
    }
}
