package alpacaive.auctionv2.member;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import alpacaive.auctionv2.card.Card;
import alpacaive.auctionv2.coupon.MemberCoupon;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Getter
@NoArgsConstructor
@ToString
public class Member implements Serializable {

    @Id
    private String id;
    private String pwd;
    private String name;
    private String email;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@OnDelete(action = OnDeleteAction.CASCADE)
    private Card cardnum;

    private int point;

    private String grade;

    private int exp;

    private String type;
	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<MemberCoupon> member;
	@PrePersist
	public void prePersist() {
		if (this.grade == null) {
			this.grade = "Bronze";
		}
	}
    
    public static Member from(MemberDto dto) {
    	return Member.builder()
    			.id(dto.getId())
    			.pwd(dto.getPwd())
    			.name(dto.getName())
    			.email(dto.getEmail())
    			.cardnum(dto.getCardnum())
    			.point(dto.getPoint())
    			.grade(dto.getGrade())
    			.exp(dto.getExp())
    			.type(dto.getType())
    			.build();
    }

    @Builder
	public Member(String id, String pwd, String name, String email, Card cardnum, int point, String grade, int exp,
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
	public Member(String id) {
		this.id=id;
	}

	public void updatePassword(String password) {
		this.pwd = password;
	}

	public void withNameAndEmail(String name, String email) {
		this.name = name;
		this.email = email;
	}

	public static List<MemberDto> toList(List<Member> l){
		List<MemberDto> list = new ArrayList<>();
		for (Member member : l) {
			list.add(MemberDto.from(member));
		}
		return list;
	}
}