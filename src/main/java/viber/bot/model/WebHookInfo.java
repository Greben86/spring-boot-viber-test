package viber.bot.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class WebHookInfo {
    private String url;
    private EventTypes[] event_types;

    public WebHookInfo() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public EventTypes[] getEvent_types() {
        return event_types;
    }

    public void setEvent_types(EventTypes[] event_types) {
        this.event_types = event_types;
    }
}
