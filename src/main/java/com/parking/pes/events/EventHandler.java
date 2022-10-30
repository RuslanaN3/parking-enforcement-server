//package com.parking.pes.events;
//
//import com.parking.pes.dto.EventDto;
//import java.util.ArrayList;
//import java.util.List;
//
//public class EventHandler {
//
//    private EventRepository eventRepository;
//
//    public void processEvents(List<EventDto> eventDtos) {
//        eventDtos.forEach(eventDto -> {
//            handleEvent(eventDto);
//            // check db for occurrences
//            // payment check
//
//
//        });
//    }
//
//    private void handleEvent(EventDto eventDto) {
//
//
//
//
//
//    }
//
//
//    private void saveEvents(List<EventDto> eventDtos) {
//        List<Event> events = new ArrayList<>();
//        eventDtos.forEach(eventDto -> {
//            Event event = createEvent(eventDto);
//            events.add(event);
//        });
//        eventRepository.saveAll(events);
//    }
//
//
//    private Event createEvent(EventDto eventDto) {
//        Event event = new Event();
//        return event;
//
//    }
//}
