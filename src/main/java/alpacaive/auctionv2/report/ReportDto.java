package alpacaive.auctionv2.report;

import alpacaive.auctionv2.auction.Auction;
import alpacaive.auctionv2.member.Member;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class ReportDto {
    private int num;

    private Member member;

    private int type;

    private String content;

    private Date wdate;

    private Auction auction;

    private int readNum;

    public static ReportDto create(Report r){
        return ReportDto.builder()
                .num(r.getNum())
                .member(r.getMember())
                .type(r.getType())
                .content(r.getContent())
                .wdate(r.getWdate())
                .auction(r.getAuction())
                .readNum(r.getReadNum())
                .build();
    }

    @Builder
    public ReportDto(int num, Member member, int type, String content, Date wdate, Auction auction, int readNum) {
        this.num = num;
        this.member = member;
        this.type = type;
        this.content = content;
        this.wdate = wdate;
        this.auction = auction;
        this.readNum = readNum;
    }
}
