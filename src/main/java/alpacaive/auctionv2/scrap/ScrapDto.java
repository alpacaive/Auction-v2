package alpacaive.auctionv2.scrap;

import alpacaive.auctionv2.auction.Auction;
import alpacaive.auctionv2.member.Member;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class ScrapDto {
    private int num;
    private Member member;
    private Auction auction;

    public static ScrapDto create(Scrap s){
        return ScrapDto.builder()
                .num(s.getNum())
                .member(s.getMember())
                .auction(s.getAuction())
                .build();
    }

    @Builder
    public ScrapDto(int num, Member member, Auction auction){
        this.num = num;
        this.member = member;
        this.auction = auction;
    }
}
