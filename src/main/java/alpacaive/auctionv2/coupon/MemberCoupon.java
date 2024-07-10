package alpacaive.auctionv2.coupon;

import alpacaive.auctionv2.member.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class MemberCoupon {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_mc")
    @SequenceGenerator(name = "seq_mc", allocationSize = 1,sequenceName = "seq_mc")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

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
}
