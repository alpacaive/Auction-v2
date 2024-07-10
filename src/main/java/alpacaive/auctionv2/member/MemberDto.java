package alpacaive.auctionv2.member;

import alpacaive.auctionv2.card.Card;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class MemberDto {
       private String id;

       private String pwd;
       private String name;
       private String email;

       private Card cardnum;

       private int point;
       private String rank;
       private int exp;
       private String type;
       
       public static MemberDto create(Member u) {
       	return MemberDto.builder()
       			.id(u.getId())
       			.pwd(u.getPwd())
       			.name(u.getName())
       			.email(u.getEmail())
       			.cardnum(u.getCardnum())
       			.point(u.getPoint())
       			.rank(u.getRank())
       			.exp(u.getExp())
       			.type(u.getType())
       			.build();
       }

    @Builder
   	public MemberDto(String id, String pwd, String name, String email, Card cardnum, int point, String rank, int exp,
					 String type) {
   		this.id = id;
   		this.pwd = pwd;
   		this.name = name;
   		this.email = email;
   		this.cardnum = cardnum;
   		this.point = point;
   		this.rank = rank;
   		this.exp = exp;
   		this.type = type;
   	}
    public int getFee(int point) {
		switch(rank) {
		case "Bronze":
			return (int)(point*0.9);
		case "Sliver":
			return (int) (point*0.93);
		case "Gold":
			return (int) (point*0.95);
		 default:
			return (int) (point*0.97);
		}
	}
}