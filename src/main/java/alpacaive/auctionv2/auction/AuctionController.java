package alpacaive.auctionv2.auction;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import alpacaive.auctionv2.bid.BidAddDto;
import alpacaive.auctionv2.bid.BidService;
import alpacaive.auctionv2.member.MemberService;
import alpacaive.auctionv2.product.ProductDto;
import alpacaive.auctionv2.product.ProductService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("/auth/auction")
@RequiredArgsConstructor
public class AuctionController {

	@Autowired
	private AuctionService aservice;
	@Autowired
	private BidService bservice; // 추가, 수정 / parent로 검색
	@Autowired
	private ProductService pservice;
	@Autowired
	private MemberService mservice;
	
	@GetMapping("add")
	public String addform(int prodnum,ModelMap map) {
		ProductDto prod=pservice.getProd(prodnum);
		map.addAttribute("prod", prod);
		return "/auction/add";
	}
	@GetMapping("/event")
	public String eventform(String mino,ModelMap map) {
		System.out.println(mino);
		map.addAttribute("mino", mino);
		return "/auction/event";
	}
	
	@PostMapping("/add")
	public String add(AuctionDto a) {
		a.setMax(a.getMin());
		a.setStatus("경매중");
		if(a.getType().equals(Auction.Type.EVENT)) {
			a.setMax(0);
		}
		a.setStart_time(new Date());
		aservice.setTime(a, a.getTime());
		aservice.save(a);
		
		return "/index_member";
	}
	
	@MessageMapping("/price")
	@SendTo("/sub/bid")
	public Map send(BidAddDto b) throws InterruptedException {
		Map map=new HashMap();
		map.put("parent", b.getParent());
		int setMax=0;
		switch(aservice.get(b.getParent()).getType()) {
		case NORMAL:
			setMax=aservice.normalBid(b);
			break;
		case BLIND:
			setMax=aservice.blindBid(b);
			break;
		default:
			setMax=aservice.eventBid(b);
		}
		if(setMax>0) {
		map.put("price", setMax);
		}else if(setMax==-1){
			map.put("msg", "need more point");
		}else if(setMax==-2) {
			map.put("msg", "current price is higher than you bid");
		}
		return map;
	}
	
//	@MessageMapping("/status")
//	@SendTo("/sub/bid")
//	public Map change(int parent) throws InterruptedException {
//		Map map=new HashMap();
//		AuctionDto auction=aservice.get(parent);
//		map.put("parent", parent);
//		if(auction.getEnd_time().before(new Date())) {
//			auction.setStatus("경매 마감");
//			aservice.save(auction);
//			map.put("msg", "경매 마감");
//		}
//		return map;
//	}  이젠 안 쓰는 코드  
	
	
	@RequestMapping("/detail")
	public String detail(int num,ModelMap map,HttpSession session) {
		AuctionDto dto=aservice.get(num);
		map.addAttribute("s", aservice.get(num));
		map.addAttribute("start_time", dto.getTime(0));
		map.addAttribute("end_time", dto.getTime(1));
		String id=(String) session.getAttribute("loginId");
		map.addAttribute("point",mservice.getUser(id).getPoint());
		return "/auction/detail";
	}

	@GetMapping("/list")
	public String list(ModelMap map) {
		map.addAttribute("list", aservice.getAll());
		return "auction/adminlist";
	}

	@GetMapping("/myauction")
	public String myauction(ModelMap map,HttpSession session) {
		String seller = (String) session.getAttribute("loginId");
		map.addAttribute("list", aservice.getBySeller(seller));
		return "auction/myauction";
	}

	@GetMapping("/mybidauction")
	public String mybidauction(ModelMap map,HttpSession session) {
		String buyer = (String) session.getAttribute("loginId");
		map.addAttribute("list", bservice.getByBuyer2(buyer));
		return "auction/mybidauction";
	}

	@GetMapping("/stop")
	public String stop(int num,HttpSession session){
		boolean flag=aservice.stopAuction(num);
		if(flag) {
			return "redirect:/auth/report/list";
		}
		session.setAttribute("auctionStopMsg","error!");
		return "redirect:/auth/report/list";
	}

	@RequestMapping("/del")
	public String del(int num, ModelMap map) {
		aservice.delete(num);
		map.addAttribute("list", aservice.getAll());
		return "auction/adminlist";
	}
	
	
}
