package alpacaive.auctionv2.delivery;

import alpacaive.auctionv2.auction.Auction;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class DeliveryDto {
    private String t_invoice;

    private String t_code;

    private String t_key;

    private Auction auction;

    @Builder
    public DeliveryDto(String t_invoice, String t_code, String t_key, Auction auction) {
        this.t_invoice = t_invoice;
        this.t_code = t_code;
        this.t_key = t_key;
        this.auction = auction;
    }

    public static DeliveryDto create(Delivery a) {
        return DeliveryDto.builder()
                .t_invoice(a.getT_invoice())
                .t_code(a.getT_code())
                .t_key(a.getT_key())
                .auction(a.getAuction())
                .build();
    }
}
