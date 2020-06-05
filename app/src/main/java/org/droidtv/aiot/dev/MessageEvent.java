package org.droidtv.aiot.dev;


public class MessageEvent<T> {

    private String eventType;
    private T message;

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public T getMessage() {
        return message;
    }

    public void setMessage(T message) {
        this.message = message;
    }
}
