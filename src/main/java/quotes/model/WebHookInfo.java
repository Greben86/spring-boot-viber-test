package quotes.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class WebHookInfo {
    private String auth_token;
    private String url;
    private String[] event_types;

    public WebHookInfo() {
    }

    public String getAuth_token() {
        return auth_token;
    }

    public void setAuth_token(String auth_token) {
        this.auth_token = auth_token;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String[] getEvent_types() {
        return event_types;
    }

    public void setEvent_types(String[] event_types) {
        this.event_types = event_types;
    }
}
