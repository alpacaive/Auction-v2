package alpacaive.auctionv2.bid;

import alpacaive.auctionv2.auction.Auction;
import alpacaive.auctionv2.member.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Bid {

    @Id
    @SequenceGenerator(name = "seq_bid", sequenceName = "seq_bid", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_bid")
    private int num;

    @ManyToOne
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Auction parent;

    @ManyToOne
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member buyer;

    private int price;
    private Date bidtime;
    
    public static Bid create(BidDto dto) {
    	return  Bid.builder()
    			.num(dto.getNum())
    			.parent(dto.getParent())
    			.buyer(dto.getBuyer())
    			.price(dto.getPrice())
    			.bidtime(dto.getBidtime())
    			.build();	
    }

    @Builder
	public Bid(int num, Auction parent, Member buyer, int price,Date bidtime) {
		this.num = num;
		this.parent = parent;
		this.buyer = buyer;
		this.price = price;
		this.bidtime=bidtime;
	}
}
