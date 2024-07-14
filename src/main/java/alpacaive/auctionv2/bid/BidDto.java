package alpacaive.auctionv2.bid;

import alpacaive.auctionv2.auction.Auction;
import alpacaive.auctionv2.member.Member;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BidDto implements Serializable{
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
	    public static BidDto create(BidAddDto dto) {
	    	return BidDto.builder()
	    			.num(dto.getNum())
	    			.parent(new Auction(dto.getParent()))
	    			.buyer(new Member(dto.getBuyer()))
	    			.price(dto.getPrice())
	    			.bidtime(dto.getBidtime())
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
