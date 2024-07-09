package alpacaive.auctionv2;

import alpacaive.auctionv2.auction.Auction;
import alpacaive.auctionv2.auction.AuctionDto;
import alpacaive.auctionv2.auction.AuctionService;
import alpacaive.auctionv2.bid.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;

// 로그인 안 해도 접근 가능한 페이지

@Controller
public class HomeController {

	@Autowired
	private AuctionService aservice;
	
	@Autowired
	private BidService bservice;
	
	@GetMapping("/")
	public String home(ModelMap map) {
		map.addAttribute("HBA",aservice.getAllByBids("경매중"));
		map.addAttribute("BA",aservice.getAll(Auction.Type.BLIND));
		map.addAttribute("EA",aservice.getAll(Auction.Type.EVENT));
		map.addAttribute("LA",aservice.getlatestAuction());
		return "index";
	}
	
	
	
	
}
