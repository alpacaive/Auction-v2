package alpacaive.auctionv2.card;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class Card {
    @Id
    private String cardnum;
    private int validDate;
    private int cvc;
    private int pwd;
    private int price;
    @Enumerated(EnumType.STRING)
    private Type type;

    public enum Type {
        BC, 삼성, 국민, 하나
    }

    public static Card create(CardDto dto){
        return Card.builder()
                .cardnum(dto.getCardnum())
                .validDate(dto.getValidDate())
                .cvc(dto.getCvc())
                .pwd(dto.getPwd())
                .price(dto.getPrice())
                .type(dto.getType())
                .build();
    }

    @Builder
    public Card(String cardnum,int validDate, int cvc, int pwd, int price, Type type) {
        this.cardnum = cardnum;
        this.validDate = validDate;
        this.cvc = cvc;
        this.pwd = pwd;
        this.price = price;
        this.type = type;
    }

}
