package alpacaive.auctionv2.scrap;

import alpacaive.auctionv2.auction.Auction;
import alpacaive.auctionv2.member.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class Scrap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int num;

    @ManyToOne
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Auction auction;

    public static Scrap create(ScrapDto dto){
        return Scrap.builder()
                .num(dto.getNum())
                .member(dto.getMember())
                .auction(dto.getAuction())
                .build();
    }

    @Builder
    public Scrap(int num, Member member, Auction auction){
        this.num = num;
        this.member = member;
        this.auction = auction;
    }
}
