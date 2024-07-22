package alpacaive.auctionv2.member;

import java.util.List;

import alpacaive.auctionv2.member.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import alpacaive.auctionv2.card.CardDao;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
	private static final Logger log = LoggerFactory.getLogger(MemberService.class);
	private final MemberDao dao;
	private final CardDao cdao;
	private final PasswordEncoder passwordEncoder;

	// 추가
	public String save(MemberDto dto) {
		Member entity = dto.toEntity();
		String encode = passwordEncoder.encode(entity.getPwd());
		entity.updatePassword(encode);
		Member u = dao.save(entity);
		return u.getId();
	}

	/**
	 * 내 정보 수정
	 * 이름, email
	 */
	public void editByNameAndEmail(MemberDto dto) {
		Member user = getUser(dto.getId()).toEntity();
		user.withNameAndEmail(dto.getName(), dto.getEmail());
		dao.save(user);
	}

	// 카드 등록
	public void cardEdit(Member m) {
		m.withCard(m.getCardnum());
		dao.save(m);
	}

	// id로 검색
	public MemberDto getUser(String id) {
		Member u = dao.findById(id).orElseThrow(()->new MemberNotFoundException("회원이 없습니다."));
		return MemberDto.from(u);
	}

	// 전체검색: findAll()
	public List<MemberDto> getAll() {
		List<Member> l = dao.findAll();
		return MemberDto.listOf(l);
	}

	// 상위 10명 순위
	public List<MemberDto> getRank() {
		List<Member> l = dao.findAllByOrderByExpDesc();
		if (l.size() <= 10) {
			return MemberDto.listOf(l);
		}
		return MemberDto.listOf(l).subList(0, 10);
	}

	// 삭제
	public void delMember(String id) {
		dao.deleteById(id);
	}

	// 환전 ✅
	public void exchange(String id, int point) {
		MemberDto member = getUser(id);
		member.exchange(point);
		dao.save(Member.from(member));
	}
	
	public List<Member> getRanker(){
		List<Member> l = dao.findAllByOrderByExpDesc();
		Member.Ranker(l);
		return l;
	}

	public boolean idCheck(String id) {
		Member u = dao.findById(id).orElse(null);
        return u == null;
    }

	public void edit(Member member) {
		dao.save(member);
	}
}