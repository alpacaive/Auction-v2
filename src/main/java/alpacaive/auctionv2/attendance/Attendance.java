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
    @SequenceGenerator(name = "seq_attendance", sequenceName = "seq_attendance", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_attendance")
    private int num;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member mid;

    @Column(nullable = false)
    private LocalDate today;

}
