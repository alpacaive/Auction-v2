package alpacaive.auctionv2.event;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Event {

    @Id
    @SequenceGenerator(name = "seq_gen", sequenceName = "seq_event", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_event")
    private int num;

    private Date wdate;
    private String img;
    private String title;
    private String content;

    @PrePersist
    public void setDate() {
        wdate = new Date();
    }

    public static Event create(EventDto dto) {
        return Event.builder()
                .num(dto.getNum())
                .wdate(dto.getWdate())
                .img(dto.getImg())
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();
    }

    @Builder
    public Event(int num, Date wdate, String img, String title, String content) {
        this.num = num;
        this.wdate = wdate;
        this.img = img;
        this.title = title;
        this.content = content;
    }

}
