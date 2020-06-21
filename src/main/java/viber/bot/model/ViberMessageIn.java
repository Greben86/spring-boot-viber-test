package viber.bot.model;

public class ViberMessageIn {
    private EventTypes event;
    private ViberSenderIn sender;
    private Message message;

    public ViberMessageIn() {}

    public EventTypes getEvent() {
        return event;
    }

    public void setEvent(EventTypes event) {
        this.event = event;
    }

    public ViberSenderIn getSender() {
        return sender;
    }

    public void setSender(ViberSenderIn sender) {
        this.sender = sender;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
