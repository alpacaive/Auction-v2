package alpacaive.auctionv2.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alpacaive.auctionv2.coupon.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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

	@GetMapping("/adjoin")
	public String adjoinForm() {
		return "member/adjoin";
	}

	@GetMapping("/join")
	public String joinForm() {
		return "member/login";
	}
	
	@PostMapping("/join")
	public String join(MemberDto u) {
		
		service.save(u);
		return "redirect:/";
	}
	
	@GetMapping("/loginform")
	public String loginForm(String path,ModelMap map,HttpSession session,String msg) {
		map.addAttribute("path",path);
		map.addAttribute("msg",msg);
		return "member/login";
	}

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

	@RequestMapping("/auth/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}

	@RequestMapping("/auth/out")
	public String out(String id, ModelMap map) {
		service.delMember(id);
		map.addAttribute("list",service.getAll());
		return "member/list";
	}

	@RequestMapping("/auth/member/list")
	public String list(ModelMap map) {
		map.addAttribute("list",service.getAll());
		return "member/list";
	}

	@GetMapping("/auth/member/edit")
	public String editform(String id, ModelMap map) {
		MemberDto m = service.getUser(id);
		map.addAttribute("m", m);
		return "member/edit";
	}

	@PostMapping("/auth/member/edit")
	public String edit(MemberDto m) {
		service.edit(m);
		return "redirect:/auth/member/list";
	}

	@GetMapping("/auth/member/edit2")
	public ModelAndView editform2(String id) {
		MemberDto m = service.getUser(id);
		ModelAndView mav = new ModelAndView("member/edit");
		mav.addObject("m", m);
		return mav;
	}

	@PostMapping("/auth/member/edit2")
	public String edit2(MemberDto m) {
		MemberDto d = service.getUser(m.getId());
		d.setName(m.getName());
		d.setEmail(m.getEmail());
		if(!m.getPwd().isEmpty()) {
			service.save(d);
			return "/index_member";
		}
		service.edit(d);
		return "/index_member";
	}

	@GetMapping("/auth/member/card")
	public String cardform(String id, ModelMap map) {
		MemberDto m = service.getUser(id);
		if(m.getCardnum()!=null){
			map.addAttribute("flag",false);
		}
		else{
			map.addAttribute("flag",true);
		}
		map.addAttribute("member", m);
		return "member/card";
	}

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
		log.debug("c: {}", c);
		log.debug("m: {}", m);
		m.setCardnum(Card.create(c));
		//같은카드를 두명이서 등록하면 오류 발생
		try {
			service.edit(m);
		}catch(Exception e){
			map.addAttribute("msg","이미 등록된 카드입니다.");
			map.addAttribute("flag",true);
			map.addAttribute("member", m);
			return "member/card";
		}
		return "redirect:/auth/member/card?id="+id;
	}

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

	@PostMapping("/auth/member/point")
	public String point(String id, String point, String customPoint, ModelMap map) {
		MemberDto m = service.getUser(id);
		
		//point가 한글일때 숫자가 아닐때 오류처리
		if(point.equals("custom")){
			m.setPoint(m.getPoint() + Integer.parseInt(customPoint));
			m.setExp(m.getExp() + Integer.parseInt(customPoint));
		}else {
			m.setPoint(m.getPoint() + Integer.parseInt(point));
			m.setExp(m.getExp() + Integer.parseInt(point));
		}

		if(m.getExp()>=1400000){
			m.setRank("Diamond");
		}else if(m.getExp()>=400000){
			m.setRank("Gold");
		}else if(m.getExp()>=100000){
			m.setRank("Silver");
		}
		service.edit(m);
		map.addAttribute("member", m);
		return "member/point";
	}

	@ResponseBody
	@GetMapping("/idcheck")
	public Map idcheck(String id) {
		Map map = new HashMap();
		MemberDto u = service.getUser(id);
		boolean flag = false;
		if (u == null) {
			flag = true;
		}
		map.put("flag", flag);
		return map;
	}
	@PostMapping("/auth/member/exchange")
	public String exchange(HttpSession session,int point,ModelMap map,Integer discount) {
		String loginId = (String) session.getAttribute("loginId");
		if(discount!=0) {
			MemberCoupon memberCoupon = couponService.updateUsed(discount, loginId);
			log.debug("discount: {}", memberCoupon);
			service.exchage(loginId,point, memberCoupon.getCoupon().getDiscount());
			couponService.updateUsed(discount, (String) session.getAttribute("loginId"));
			return "index_member";
		}
		service.exchage(loginId,point, 0);
		return "index_member";
	}
}
