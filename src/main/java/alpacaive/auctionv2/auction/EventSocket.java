package alpacaive.auctionv2.auction;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventSocket {
	private final SimpMessagingTemplate eventTemplate ;

    public EventSocket(SimpMessagingTemplate messagingTemplate) {
        this.eventTemplate = messagingTemplate;
    }

    public void sendMessage(String destination, Object payload) {
    	eventTemplate.convertAndSend(destination, payload);
    }

}
