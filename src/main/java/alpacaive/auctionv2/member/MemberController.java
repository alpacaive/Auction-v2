package alpacaive.auctionv2.member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alpacaive.auctionv2.coupon.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import alpacaive.auctionv2.auction.Auction;
import alpacaive.auctionv2.auction.AuctionService;
import alpacaive.auctionv2.card.Card;
import alpacaive.auctionv2.card.CardDto;
import alpacaive.auctionv2.card.CardService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MemberController {

	@Autowired
	private MemberService service;
	@Autowired
	private CardService cservice;
	@Autowired
	private AuctionService aservice;
	@Autowired
	private MemberCouponService couponService;

	// 관리자 추가 페이지 -> 관리자 페이지에 있음
	@GetMapping("/adjoin")
	public String adjoinForm() {
		return "member/adjoin";
	}

	// 회원가입 페이지 불러오기
	@GetMapping("/join")
	public String joinForm() {
		return "member/login";
	}

	// 회원가입 폼
	@PostMapping("/join")
	public String join( MemberDto f) {
		service.save(f);
		return "redirect:/";
	}

	// 로그인 폼 불러오기
	@GetMapping("/loginform")
	public String loginForm() {
		return "member/login";
	}

	// 로그인 폼, index에 List 리스트 띄워주기 위해 map에 넣음
	@RequestMapping("/auth/login")
	public String alogin(ModelMap map) {
		map.addAttribute("HBA",aservice.getAllByBids("경매중"));
		map.addAttribute("BA",aservice.getAll(Auction.Type.BLIND));
		map.addAttribute("EA",aservice.getAll(Auction.Type.EVENT));
		map.addAttribute("LA",aservice.getlatestAuction());
		return "index";
	}

	// 관리자가 로그인 후 이동할 경로
	@RequestMapping("/auth/index_admin")
	public String adminHome() {
		return "index_admin";
	}

	// 회원이 로그인 후 이동할 경로
	@RequestMapping("/auth/index_member")
	public String memberHome() {
		return "index_member";
	}

	// 로그아웃
	@RequestMapping("/auth/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}

	// 회원 삭제 -> 관리자 권한
	@RequestMapping("/auth/out")
	public String out(String id, ModelMap map) {
		service.delMember(id);
		map.addAttribute("list",service.getAll());
		return "member/list";
	}

	// 회원 목록 -> 관리자 권한
	@RequestMapping("/auth/member/list")
	public String list(ModelMap map) {
		map.addAttribute("list",service.getAll());
		return "member/list";
	}

	// 내 정보 및 수정 페이지 불러오기
	@GetMapping("/auth/member/edit2")
	public ModelAndView editForm(String id) {
		MemberDto m = service.getUser(id);
		ModelAndView mav = new ModelAndView("member/edit");
		mav.addObject("m", m);
		return mav;
	}

	// 내 정보 및 수정 폼
	@PostMapping("/auth/member/edit2")
	public String edit(MemberDto m) {
		MemberDto d = service.getUser(m.getId());
		if(!m.getPwd().isEmpty()) {
			service.save(d);
			return "/index_member";
		}
		service.editByNameAndEmail(d);
		return "/index_member";
	}

	// 카드 등록 페이지 불러오기
	@GetMapping("/auth/member/card")
	public String cardForm(String id, ModelMap map) {
		MemberDto m = service.getUser(id);
		if(m.getCardnum()!=null){ // 등록한 카드가 있을 때
			map.addAttribute("flag",false);
		} else{ // 등록한 카드가 없을 때
			map.addAttribute("flag",true);
		}
		map.addAttribute("member", m);
		return "member/card";
	}

	// 카드 등록 폼
	@PostMapping("/auth/member/card")
	public String card(CardDto dto, String id, ModelMap map) {
		//일치하는 카드 가져오기
		MemberDto m = service.getUser(id);
		CardDto c = cservice.get(Card.create(dto));
		if(c==null){
			map.addAttribute("msg","일치하는 카드가 없습니다");
			map.addAttribute("flag",true);
			map.addAttribute("member", m);
			return "member/card";
		}
		Member entity = m.toEntity();
		entity.addCardNum(Card.create(c));
		//같은카드를 두명이서 등록하면 오류 발생
		try {
			service.cardEdit(entity);
		}catch(Exception e){
			map.addAttribute("msg","이미 등록된 카드입니다.");
			map.addAttribute("flag",true);
			map.addAttribute("member", m);
			return "member/card";
		}
		return "redirect:/auth/member/card?id="+id;
	}

	// 포인트 충전 페이지 불러오기
	@GetMapping("/auth/member/point")
	public String pointform(String id, ModelMap map) {
		MemberDto m = service.getUser(id);
		map.addAttribute("member", m);
		if(m.getCardnum() == null) {
			map.addAttribute("flag",true);
			return "member/card";
		}
		return "member/point";
	}

	// 포인트 등록 폼
	@PostMapping("/auth/member/point")
	public String point(String id, String point, String customPoint, ModelMap map) {
		Member m = service.getUser(id).toEntity();
		//point가 한글일때 숫자가 아닐때 오류처리
		if(point.equals("custom")){
			m.updatePoint(m.getPoint()+Integer.parseInt(customPoint));
			m.updateExp(m.getExp()+Integer.parseInt(customPoint));
		}else {
			m.updatePoint(m.getPoint() + Integer.parseInt(point));
			m.updateExp(m.getExp() + Integer.parseInt(point));
		}
		m.updateGrade();
		service.edit(m);
		map.addAttribute("member", m);
		return "member/point";
	}

	@GetMapping("/auth/member/exchange")
	public String exchangeform(String id, ModelMap map) {
		MemberDto m = service.getUser(id);
		List<Coupon> couponByMemberId = couponService.findCouponByMemberId(id);
		map.addAttribute("member", m);
		if(m.getCardnum() == null) {
			map.addAttribute("flag",true);
			return "member/card";
		}
			map.addAttribute("myCouponList",couponByMemberId);

		return "member/exchange";
	}



	@ResponseBody
	@GetMapping("/idcheck")
	public Map<String, Boolean> idcheck(String id) {
		Map<String, Boolean> map = new HashMap();
		boolean flag = service.idCheck(id);
		map.put("flag", flag);
		return map;
	}
	@PostMapping("/auth/member/exchange")
	public String exchange(HttpSession session,int point, int coupon_id) {
		String loginId = (String) session.getAttribute("loginId");
		if(coupon_id!=0) {
			couponService.updateUsed(coupon_id, (String) session.getAttribute("loginId")); // 쿠폰 사용 변경
		}
		service.exchange(loginId,point);
		return "index_member";
	}
}
