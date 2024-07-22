package alpacaive.auctionv2.member;

import alpacaive.auctionv2.card.Card;
import alpacaive.auctionv2.card.CardDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Slf4j
public class MemberServiceTest {
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private CardDao cardDao;

    @DisplayName("id로 멤버 불러오기")
    @Test
    void getUser() throws Exception {
        //given
        Member member = Member.builder()
                .id("ccc")
                .build();
        //when
        memberDao.save(member);
        MemberDto user = memberService.getUser("ccc");
        //then
        assertThat(user.getId()).isEqualTo("ccc");
    }

    @Test
    @DisplayName("환전시 수수료 적용 잘 되는지 확인 해볼께용")
    void exchange() throws Exception {
        //given
        MemberDto user = memberService.getUser("aaa");
        //when
        user.exchange(10000);
        //then
        assertThat(user.getPoint()).isEqualTo(990000);
        assertThat(user.getCardnum().getPrice()).isEqualTo(9010);
    }

    @Test
    @DisplayName("랭커 서비스 포인트 지급 테스트")
    void Ranking() throws Exception {
        // given
        Member m1 = new Member("aaa", "aaa", "aaa", "aaa", null, 0, "Bronze", 3, "member");
        Member m2 = new Member("bbb", "bbb", "bbb", "bbb", null, 0, "Bronze", 2, "member");
        Member m3 = new Member("ccc", "ccc", "ccc", "ccc", null, 0, "Bronze", 1, "member");
        memberDao.save(m1);
        memberDao.save(m2);
        memberDao.save(m3);
        // when
        List<Member> rank = memberService.getRanker();
        // then
        assertThat(rank.size()).isEqualTo(3);
        assertThat(rank.get(0).getId()).isEqualTo("aaa");
        assertThat(rank.get(0).getPoint()).isEqualTo(10000);
        assertThat(rank.get(1).getId()).isEqualTo("bbb");
        assertThat(rank.get(1).getPoint()).isEqualTo(5000);
        assertThat(rank.get(2).getId()).isEqualTo("ccc");
        assertThat(rank.get(2).getPoint()).isEqualTo(3000);


    }

    @Test
    @DisplayName("카드 등록 테스트")
    void card() throws Exception {
        // given
        Card c1 = new Card("1111111111111111", 1111, 111, 1111, 100000, Card.Type.BC);
        cardDao.save(c1);
        Member m1 = new Member("aaa", "aaa", "aaa", "aaa", null, 0, "Bronze", 3, "member");
        memberDao.save(m1);

        // when
        m1.addCardNum(c1);
        memberService.cardEdit(m1);

        // then
        assertThat(m1.getCardnum().getCardnum()).isEqualTo("1111111111111111");

        }

}
