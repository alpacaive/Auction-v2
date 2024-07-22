package alpacaive.auctionv2.member;

import alpacaive.auctionv2.card.Card;
import alpacaive.auctionv2.card.CardDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@ToString
public class MemberDto {
    private String id;

    private String pwd;
    private String name;
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    private Card cardnum;

    private int point;
    private String grade;
    private int exp;
    private String type;

    public static MemberDto from(Member u) {
        return MemberDto.builder()
                .id(u.getId())
                .pwd(u.getPwd())
                .name(u.getName())
                .email(u.getEmail())
                .cardnum(u.getCardnum())
                .point(u.getPoint())
                .grade(u.getGrade())
                .exp(u.getExp())
                .type(u.getType())
                .build();
    }

    @Builder
    public MemberDto(String id, String pwd, String name, String email, Card cardnum, int point, String grade, int exp,
                     String type) {
        this.id = id;
        this.pwd = pwd;
        this.name = name;
        this.email = email;
        this.cardnum = cardnum;
        this.point = point;
        this.grade = grade;
        this.exp = exp;
        this.type = type;
    }

    public int getFee(int point) {
        return switch (grade) {
            case "Bronze" -> (int) (point * 0.9);
            case "Sliver" -> (int) (point * 0.93);
            case "Gold" -> (int) (point * 0.95);
            default -> (int) (point * 0.97);
        };
    }

    public Member toEntity() {
        return Member.builder()
                .id(this.id)
                .pwd(this.pwd)
                .name(this.name)
                .email(this.email)
                .cardnum(this.cardnum)
                .point(this.point)
                .grade(this.grade)
                .exp(this.exp)
                .type(this.type)
                .build();
    }

    public void exchange(int point) {
        this.point -= point;
        CardDto card = CardDto.create(this.cardnum);
        card.updatePrice(getFee(point));
        this.cardnum = Card.create(card);
    }

}