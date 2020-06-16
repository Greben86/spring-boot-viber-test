package quotes.model;

public class ViberMessageOut {
    private String receiver;
    private String type;
    private ViberSenderOut sender;
    private String text;

    public ViberMessageOut() {}

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ViberSenderOut getSender() {
        return sender;
    }

    public void setSender(ViberSenderOut sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
