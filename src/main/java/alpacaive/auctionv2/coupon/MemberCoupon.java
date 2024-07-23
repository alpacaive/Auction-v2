package alpacaive.auctionv2.coupon;

import alpacaive.auctionv2.member.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.parameters.P;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class MemberCoupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
//    @ManyToOne(fetch = FetchType.LAZY)

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    private boolean isUsed = false;

    @Builder
    public MemberCoupon(Member member, Coupon coupon) {
        this.member = member;
        this.coupon = coupon;
    }

    public static MemberCoupon create(Member member, Coupon coupon) {
        return MemberCoupon.builder()
                .member(member)
                .coupon(coupon)
                .build();
    }

    public void updateUsed(){
        isUsed = true;
    }
}
