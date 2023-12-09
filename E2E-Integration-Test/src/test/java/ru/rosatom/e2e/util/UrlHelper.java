package ru.rosatom.e2e.util;

import ru.rosatom.e2e.BasicEndpointProvider;

public class UrlHelper {

    public String getEndpointUrl(BasicEndpointProvider endpoint) {
        return "http://127.0.0.1:8080" + endpoint.getUrl();
    }
}
