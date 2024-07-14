package alpacaive.auctionv2.bid;


import java.util.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@NoArgsConstructor
@ToString
public class BidAddDto {
	 	private int num;
	    private int parent;
	    private String buyer;
	    private int price;
	    private Date bidtime;
	    
	    public static BidAddDto create(Bid b) {
	    	return BidAddDto.builder()
	    			.num(b.getNum())
	    			.parent(b.getParent().getNum())
	    			.buyer(b.getBuyer().getId())
	    			.price(b.getPrice())
	    			.bidtime(b.getBidtime())
	    			.build();
	    }

	    @Builder
		public BidAddDto(int num, int parent, String buyer, int price,Date bidtime) {
			this.num = num;
			this.parent = parent;
			this.buyer = buyer;
			this.price = price;
			this.bidtime=bidtime;
		}
}
