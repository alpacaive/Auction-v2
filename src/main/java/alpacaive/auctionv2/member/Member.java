package alpacaive.auctionv2.member;

import alpacaive.auctionv2.card.Card;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
@Setter
@Getter
@NoArgsConstructor
@ToString
public class Member {

    @Id
    private String id;

    private String pwd;
    private String name;
    private String email;

    @OneToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
    private Card cardnum;

    private int point;

    private String rank;

    private int exp;

    private String type;

	@PrePersist
	public void prePersist() {
		if (this.rank == null) {
			this.rank = "Bronze";
		}
	}
    
    public static Member create(MemberDto dto) {
    	return Member.builder()
    			.id(dto.getId())
    			.pwd(dto.getPwd())
    			.name(dto.getName())
    			.email(dto.getEmail())
    			.cardnum(dto.getCardnum())
    			.point(dto.getPoint())
    			.rank(dto.getRank())
    			.exp(dto.getExp())
    			.type(dto.getType())
    			.build();
    }

    @Builder
	public Member(String id, String pwd, String name, String email, Card cardnum, int point, String rank, int exp,
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
    
	public Member(String id) {
		this.id=id;
	}
}