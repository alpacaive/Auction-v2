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

	public void withCard(Card cardNum) {
		this.cardnum = cardNum;
	}

	// 포인트 주는 메서드
	public void givePoint(String id, int point) {
		this.id = id;
		this.point += point;
	}

	// 달마다 랭커 포인트 지급
	public static void Ranker(List<Member> list) {
		Member first = list.get(0);
		first.givePoint(first.getId(), 10000); // 1등
		Member second = list.get(1);
		second.givePoint(second.getId(), 5000); // 2등
		Member third = list.get(2);
		third.givePoint(third.getId(), 3000); // 3등

	}

	public void addCardNum(Card card) {
		this.cardnum = card;
	}

	// 포인트 충전
	public void updatePoint(int point) {
		this.point = point;
	}

	// 등급 조정
	public void updateGrade() {
		if(this.getExp()>=1400000){
			this.grade ="Diamond";
		}else if(this.getExp()>=400000){
			this.grade = "Gold";
		}else if(this.getExp()>=100000){
			this.grade = "Silver";
		}
	}

	// 경험치획
	public void updateExp(int exp) {
		this.exp = exp;
	}


}