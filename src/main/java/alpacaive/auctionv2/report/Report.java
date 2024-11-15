package alpacaive.auctionv2.report;

import alpacaive.auctionv2.auction.Auction;
import alpacaive.auctionv2.member.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@Setter
@Getter
@NoArgsConstructor
@ToString
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int num;

    @ManyToOne
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    private int type;
    //1.스팸/홍보 도배글 2.음란물 3.불법정보 포함 4.청소년 유해정보 5.욕설, 혐오 표현 6.개인정보 노출

    private String content;

    private Date wdate;

    @ManyToOne
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Auction auction;

    private int readNum;

    @PrePersist
    public void setDate() {
        wdate = new Date();
    }

    public static Report create(ReportDto dto){
        return Report.builder()
                .num(dto.getNum())
                .member(dto.getMember())
                .type(dto.getType())
                .content(dto.getContent())
                .wdate(dto.getWdate())
                .auction(dto.getAuction())
                .readNum(dto.getReadNum())
                .build();
            }

    @Builder
    public Report(int num, Member member, int type, String content, Date wdate, Auction auction, int readNum) {
        this.num = num;
        this.member = member;
        this.type = type;
        this.content = content;
        this.wdate = wdate;
        this.auction = auction;
        this.readNum = readNum;
    }

}
