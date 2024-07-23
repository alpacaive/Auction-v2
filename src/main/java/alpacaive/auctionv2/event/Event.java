package alpacaive.auctionv2.event;

import alpacaive.auctionv2.coupon.Coupon;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int num;

    private String img;
    private String title;
    private String close;
    private String status;
    @OneToOne
//    @JoinColumn(name = "event")
    private Coupon cnum;

    public static Event create(EventDto dto) {
        return Event.builder()
                .num(dto.getNum())
                .img(dto.getImg())
                .title(dto.getTitle())
                .close(dto.getClose())
                .status(dto.getStatus())
                .cnum(dto.getCnum())
                .build();
    }

    @Builder
    public Event(int num, String img, String title, String close, String status, Coupon cnum) {
        this.num = num;
        this.img = img;
        this.title = title;
        this.close = close;
        this.status = status;
        this.cnum = cnum;
    }

}
