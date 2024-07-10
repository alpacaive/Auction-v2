package alpacaive.auctionv2.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventDao dao;
    @Value("${spring.servlet.multipart.location}")
    private String path;

    public EventDto save(EventDto dto) {
        Event e = dao.save(Event.create(dto));
        EventDto vo=EventDto.create(e);
        if(!dto.getF().isEmpty()) {
            String oname = dto.getF().getOriginalFilename();
            String img = e.getNum() + oname;
            File f = new File(path + img);
            try {
                dto.getF().transferTo(f);
                vo.setImg(f.getName());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        dao.save(Event.create(vo));

        return EventDto.create(e);
    }

    public void delete(int num) {
        dao.deleteById(num);
    }

    public ArrayList<EventDto> getAll() {
        List<Event> l = dao.findByStatusOrderByNumDesc("진행중"); // 진행중
        ArrayList<EventDto> list = new ArrayList<>();
        for (Event e : l) {
            list.add(EventDto.create(e));
        }
        List<Event> l2 = dao.findByStatusOrderByNumDesc("마감"); // 마감
        for (Event e : l2) {
            list.add(EventDto.create(e));
        }
        return list;
    }

    public EventDto getEvent(int num) {
        Event e = dao.findById(num).orElse(null);
        if(e == null) {
            return null;
        }
        return EventDto.create(e);
    }

    public void update(int num) {
        Event e = dao.findById(num).orElse(null);
        EventDto dto=EventDto.create(e);
        dto.setStatus("마감");
        save(dto);
    }



}
