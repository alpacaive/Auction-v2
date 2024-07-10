package alpacaive.auctionv2.event;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class EventDto {

    private int num;
    private Date wdate;
    private String img;
    private String title;
    private String content;
    private MultipartFile f;

    public static EventDto create(Event event) {
        return EventDto.builder()
                .num(event.getNum())
                .wdate(event.getWdate())
                .img(event.getImg())
                .title(event.getTitle())
                .content(event.getContent())
                .build();
    }

    @Builder
    public EventDto(int num, Date wdate, String img, String title, String content, MultipartFile f) {
        this.num = num;
        this.wdate = wdate;
        this.img = img;
        this.title = title;
        this.content = content;
        this.f = f;
    }

}
