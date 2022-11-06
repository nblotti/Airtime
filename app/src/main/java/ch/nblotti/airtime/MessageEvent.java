package ch.nblotti.airtime;

public class MessageEvent {

    public static enum EVENT_TYPE {
        VIDEO_CHANGE, VIDEO_CLICK,
        JR_S_CLICK, JR_D_CLICK, JR_X_CLICK,
        R_M_CLICK, R_C_CLICK, R_D_CLICK,
        NEW_CONTROLLED_ROTATION, UPDATE_CONTROLLED_ROTATION;
    }

    private Long uID;


    private final EVENT_TYPE eventType;

    private MessageEvent(EVENT_TYPE eventType) {
        this.eventType = eventType;
    }

    public static MessageEvent build(EVENT_TYPE eventType) {
        return new MessageEvent(eventType);
    }

    public static MessageEvent build(EVENT_TYPE eventType, Long sessionID) {
        MessageEvent event = MessageEvent.build(eventType);
        event.uID = sessionID;
        return event;
    }

    public Long getuID() {
        return uID;
    }

    public EVENT_TYPE getEventType() {
        return eventType;
    }

}
