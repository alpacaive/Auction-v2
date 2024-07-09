package alpacaive.auctionv2.bid;

import alpacaive.auctionv2.auction.Auction;
import alpacaive.auctionv2.member.Member;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BidDto {

	    private int num;
	    private Auction parent;
	    private Member buyer;
	    private int price;
	    private Date bidtime;
	   
	    
	    public static BidDto create(Bid b) {
	    	return BidDto.builder()
	    			.num(b.getNum())
	    			.parent(b.getParent())
	    			.buyer(b.getBuyer())
	    			.price(b.getPrice())
	    			.build();
	    }

	    @Builder
		public BidDto(int num, Auction parent, Member buyer, int price,Date bidtime) {
			this.num = num;
			this.parent = parent;
			this.buyer = buyer;
			this.price = price;
			this.bidtime=bidtime;
		}
}
