package alpacaive.auctionv2.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Slf4j
public class MemberServiceTest {
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberDao memberDao;

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
}
