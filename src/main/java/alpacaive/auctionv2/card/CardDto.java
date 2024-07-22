package alpacaive.auctionv2.card;

import lombok.*;
import alpacaive.auctionv2.card.Card.Type;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class CardDto {
    private String cardnum;
    private int validDate;
    private int cvc;
    private int pwd;
    private int price;
    private Type type;

    public static CardDto create(Card dto){
        return CardDto.builder()
                .cardnum(dto.getCardnum())
                .validDate(dto.getValidDate())
                .cvc(dto.getCvc())
                .pwd(dto.getPwd())
                .price(dto.getPrice())
                .type(dto.getType())
                .build();
    }

    @Builder
    public CardDto(String cardnum,int validDate, int cvc, int pwd, int price, Card.Type type) {
        this.cardnum = cardnum;
        this.validDate=validDate;
        this.cvc = cvc;
        this.pwd = pwd;
        this.price = price;
        this.type = type;
    }

    public void updatePrice(int feePoint){
        this.price += feePoint;
    }
}
