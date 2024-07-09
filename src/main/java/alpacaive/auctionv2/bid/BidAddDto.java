package alpacaive.auctionv2.bid;


import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@ToString
public class BidAddDto {
	 private int num;
	    private int parent;
	    private String buyer;
	    private int price;
	   
	    
	    public static BidAddDto create(Bid b) {
	    	return BidAddDto.builder()
	    			.num(b.getNum())
	    			.parent(b.getParent().getNum())
	    			.buyer(b.getBuyer().getId())
	    			.price(b.getPrice())
	    			.build();
	    }

	    @Builder
		public BidAddDto(int num, int parent, String buyer, int price) {
			this.num = num;
			this.parent = parent;
			this.buyer = buyer;
			this.price = price;
		}
}
