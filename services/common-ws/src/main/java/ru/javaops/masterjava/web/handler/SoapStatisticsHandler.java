package ru.javaops.masterjava.web.handler;

import com.sun.xml.txw2.output.IndentingXMLStreamWriter;
import com.sun.xml.ws.api.handler.MessageHandlerContext;
import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.api.streaming.XMLStreamWriterFactory;
import lombok.extern.slf4j.Slf4j;
import ru.javaops.masterjava.web.Statistics;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.xml.stream.XMLStreamWriter;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static javax.xml.ws.handler.MessageContext.MESSAGE_OUTBOUND_PROPERTY;

@Slf4j
public class SoapStatisticsHandler extends SoapBaseHandler {
    protected String handlerName = "SoapStatisticsHandler";
    public static final PrintStream out = System.out;
    Date startTime, endTime;
//    Statistics statistics = Statistics.getInstance();

    public SoapStatisticsHandler() {
        super();
    }

    @Override
    public boolean handleMessage(MessageHandlerContext context) {
        Boolean outboundProperty = (Boolean) context
                .get(MESSAGE_OUTBOUND_PROPERTY);
        if (outboundProperty) {

            out.println("In LogicalHandler " + handlerName + ":handleMessage()");
            try {
                String payload = getMessageText(context.getMessage());
                Statistics.RESULT result = (int) context.get("success") > 0 ? Statistics.RESULT.SUCCESS : Statistics.RESULT.FAIL;
                if((int)context.get("success")>0){
                    Statistics.count(payload, startTime.getTime(), Statistics.RESULT.SUCCESS);
                }
                if((int)context.get("failed")>0){
                    Statistics.count(payload, startTime.getTime(), Statistics.RESULT.FAIL);
                }
                startTime = (Date) context.get(handlerName + "StartTime");
                endTime = new Date();
                long elapsedTime = endTime.getTime() - startTime.getTime();
                out.println("Elapsed time in handler " + handlerName + " is "
                        + elapsedTime);
                out.println("");
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else {
            startTime = new Date();
            context.put(handlerName + "StartTime", startTime);
        }
        return true;
    }

    @Override
    public boolean handleFault(MessageHandlerContext context) {
        out.println("------------------------------------");
        out.println("In Handler " + handlerName + ":handleFault()");
        out.println("Exiting Handler " + handlerName + ":handleFault()");
        out.println("------------------------------------");
        return true;
//        return false;
    }

    protected static String getMessageText(Message msg) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            XMLStreamWriter writer = XMLStreamWriterFactory.create(out, "UTF-8");
            IndentingXMLStreamWriter wrap = new IndentingXMLStreamWriter(writer);
            msg.writeTo(wrap);
            return out.toString(StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            log.warn("Coudn't get SOAP message for logging", e);
            return null;
        }
    }


    @PostConstruct
    public void init() {
        out.println("------------------------------------");
        out.println("In Handler " + handlerName + ":init()");
        out.println("Exiting Handler " + handlerName + ":init()");
        out.println("------------------------------------");
    }

    @PreDestroy
    public void destroy() {
        out.println("------------------------------------");
        out.println("In Handler " + handlerName + ":destroy()");
        out.println("Exiting Handler " + handlerName + ":destroy()");
        out.println("------------------------------------");
    }
}
