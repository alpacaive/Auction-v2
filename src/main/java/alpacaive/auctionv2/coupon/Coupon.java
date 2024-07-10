package alpacaive.auctionv2.coupon;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cou")
    @SequenceGenerator(name = "seq_cou", allocationSize = 1, sequenceName = "seq_cou")
    private long id;

    // ex) 10% 할인 쿠폰
    private String name;
    //영속화된 엔티티와 관련된 엔티티 영속화
    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<MemberCoupon> coupons;
    private double discount;
    private Date end_date;
    private boolean isExpired;

    @Builder
    public Coupon(String name, double discount, Date end_date, boolean isExpired, List<MemberCoupon> coupons) {
        this.name = name;
        this.discount = discount;
        this.end_date = end_date;
        this.isExpired = isExpired;
        this.coupons = coupons;
    }

    public static Coupon couponCreate(String name, double discount, Date end_date, List<MemberCoupon> coupons) {
        return Coupon.builder()
                .name(name)
                .discount(discount)
                .end_date(end_date)
                .isExpired(false)
                .coupons(coupons)
                .build();
    }

    public void addMemberCoupon(MemberCoupon coupon) {
        coupons.add(coupon);
        coupon.setCoupon(this);
    }
}
