package alpacaive.auctionv2.auction;

import java.util.Calendar;
import java.util.Date;

import alpacaive.auctionv2.auction.Auction.Type;
import alpacaive.auctionv2.member.Member;
import alpacaive.auctionv2.product.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class AuctionDto {
	private int num;

	private Member seller;

	private int min;
	private int max;

	private Product product;

	private String status;

	private Date start_time;
	private Date end_time;
	private Type type;
	private String content;
	private String title;
	private int time;
	private int bcnt;
	private Member mino;

	public static AuctionDto create(Auction a) {
		return AuctionDto.builder()
				.num(a.getNum())
				.seller(a.getSeller())
				.min(a.getMin())
				.max(a.getMax())
				.product(a.getProduct())
				.status(a.getStatus())
				.start_time(a.getStart_time())
				.end_time(a.getEnd_time())
				.type(a.getType())
				.content(a.getContent())
				.title(a.getTitle())
				.time(a.getTime())
				.bcnt(a.getBcnt())
				.mino(a.getMino())
				.build();
	}

	@Builder
	public AuctionDto(int num, Member seller, int min, int max, Product product, String status, Date start_time,
			Date end_time, Type type, String content, String title, int time, int bcnt, Member mino) {
		this.num = num;
		this.seller = seller;
		this.min = min;
		this.max = max;
		this.product = product;
		this.status = status;
		this.start_time = start_time;
		this.end_time = end_time;
		this.type = type;
		this.content = content;
		this.title = title;
		this.time = time;
		this.bcnt = bcnt;
		this.mino = mino;
	}

	public String getTime(int state) {
		Calendar cal = Calendar.getInstance();
		if (state == 0) {
			cal.setTime(this.getStart_time());
			String time = "" + cal.get(Calendar.DAY_OF_MONTH);
			time += "일 " + cal.get(Calendar.HOUR_OF_DAY);
			time += "시" + cal.get(Calendar.MINUTE) + "분";
			return time;
		} else {
			cal.setTime(this.getEnd_time());
			String time2 = "" + cal.get(Calendar.DAY_OF_MONTH);
			time2 += "일 " + cal.get(Calendar.HOUR_OF_DAY);
			time2 += "시" + cal.get(Calendar.MINUTE) + "분";
			return time2;
		}

	}
}
