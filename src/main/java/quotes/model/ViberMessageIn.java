package quotes.model;

public class ViberMessageIn {
    private String event;
    private ViberSenderIn sender;
    private Message message;

    public ViberMessageIn() {}

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
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
