package alpacaive.auctionv2.auction;

import alpacaive.auctionv2.member.Member;
import alpacaive.auctionv2.product.Product;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class Auction {

    @Id
    @SequenceGenerator(name = "seq_auction", sequenceName = "seq_auction", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_auction")
    private int num;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Member seller;

    private int min;
    private int max;

    @OneToOne
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;

    private String status;

    private Date start_time;
    private Date end_time;

    
    @Enumerated(EnumType.STRING)
    private Type type;
    private String content;
    private String title;
    private int time;
    @Column(columnDefinition = "INT DEFAULT 0")
    private int bcnt;
    @ManyToOne
    private Member mino;
    public enum Type {
        NORMAL, BLIND, EVENT
    }
    
    public static Auction create(AuctionDto dto) {
    	return Auction.builder()
    			.num(dto.getNum())
    			.seller(dto.getSeller())
    			.min(dto.getMin())
    			.max(dto.getMax())
    			.product(dto.getProduct())
    			.status(dto.getStatus())
    			.start_time(dto.getStart_time())
    			.end_time(dto.getEnd_time())
    			.type(dto.getType())
    			.content(dto.getContent())
    			.title(dto.getTitle())
    			.time(dto.getTime())
    			.bcnt(dto.getBcnt())
    			.mino(dto.getMino())
    			.build();
    }

    @Builder
	public Auction(int num, Member seller, int min, int max, Product product, String status, Date start_time, Date end_time,
                   Type type,String content,String title,int time,int bcnt,Member mino) {
		this.num = num;
		this.seller = seller;
		this.min = min;
		this.max = max;
		this.product = product;
		this.status = status;
		this.start_time = start_time;
		this.end_time = end_time;
		this.type = type;
		this.content=content;	
		this.title=title;
		this.time=time;
		this.bcnt=bcnt;
		this.mino=mino;
    }
    public Auction(int num) {
    	this.num=num;
    }
    
	
}
