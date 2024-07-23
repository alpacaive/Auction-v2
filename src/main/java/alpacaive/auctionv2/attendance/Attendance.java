package alpacaive.auctionv2.attendance;

import alpacaive.auctionv2.member.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int num;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member mid;

    @Column(nullable = false)
    private LocalDate today;

}
