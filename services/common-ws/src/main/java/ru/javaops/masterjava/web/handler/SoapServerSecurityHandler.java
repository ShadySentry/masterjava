package ru.javaops.masterjava.web.handler;

import com.sun.xml.ws.api.handler.MessageHandlerContext;
import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;
import ru.javaops.masterjava.config.Configs;
import ru.javaops.masterjava.web.AuthUtil;

import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import java.util.List;
import java.util.Map;

@Slf4j
public class SoapServerSecurityHandler extends SoapBaseHandler {
    private static Config HOSTS;
    public static String USER;
    public static String PASSWORD;
    public static String AUTH_HEADER;

    static {
        HOSTS = Configs.getConfig("hosts.conf","hosts");
        USER=HOSTS.getString("mail.user");
        PASSWORD=HOSTS.getString("mail.password");
        AUTH_HEADER= AuthUtil.encodeBasicAuthHeader(USER, PASSWORD);
    }

    @Override
    public boolean handleMessage(MessageHandlerContext context) {
        if(!isOutbound(context)){
            SOAPMessage soapMsg = (SOAPMessage) context.get("soapMsg");
            Map<String, List<String>> headers = (Map<String, List<String>>) context.get(MessageContext.HTTP_REQUEST_HEADERS);
            int code = AuthUtil.checkBasicAuth(headers, AUTH_HEADER);
            if (code != 0) {
                context.put(MessageContext.HTTP_RESPONSE_CODE, code);
                throw new SecurityException();
            }
        }
        return true;
    }

    @Override
    public boolean handleFault(MessageHandlerContext context) {
        return false;
    }
}
