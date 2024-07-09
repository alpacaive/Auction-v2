package alpacaive.auctionv2.delivery;

import alpacaive.auctionv2.auction.Auction;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
@Entity
@Getter
@NoArgsConstructor
@ToString
public class Delivery {
    @Id
    private String t_invoice;

    private String t_code;

    private String t_key;

    @OneToOne
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Auction auction;

    public static Delivery create(DeliveryDto a) {
        return Delivery.builder()
                .t_invoice(a.getT_invoice())
                .t_code(a.getT_code())
                .t_key(a.getT_key())
                .auction(a.getAuction())
                .build();
    }

    @Builder
    public Delivery(String t_invoice, String t_code, String t_key, Auction auction) {
        this.t_invoice = t_invoice;
        this.t_code = t_code;
        this.t_key = t_key;
        this.auction = auction;
    }
}
