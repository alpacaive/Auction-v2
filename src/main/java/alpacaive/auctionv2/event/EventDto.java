package alpacaive.auctionv2.event;

import alpacaive.auctionv2.coupon.Coupon;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class EventDto {

    private int num;
    private String img;
    private String title;
    private String close;
    private String status;
    private Coupon cnum;
    private MultipartFile f;

    public static EventDto create(Event event) {
        return EventDto.builder()
                .num(event.getNum())
                .img(event.getImg())
                .title(event.getTitle())
                .close(event.getClose())
                .status(event.getStatus())
                .cnum(event.getCnum())
                .build();
    }

    @Builder
    public EventDto(int num, String img, String title, String close, String status, Coupon cnum, MultipartFile f) {
        this.num = num;
        this.img = img;
        this.title = title;
        this.close = close;
        this.status = status;
        this.cnum = cnum;
        this.f = f;
    }

}
