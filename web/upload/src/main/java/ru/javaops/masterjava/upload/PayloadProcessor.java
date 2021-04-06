package ru.javaops.masterjava.upload;

import lombok.AllArgsConstructor;
import lombok.val;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.io.InputStream;
import java.util.List;

public class PayloadProcessor {
    private final UserProcessor userProcessor = new UserProcessor();
    private final CityProcessor cityProcessor = new CityProcessor();

    @AllArgsConstructor
    public static class FailedEmails{
        public String emailOrRange;
        public String reason;

        @Override
        public String toString(){
            return emailOrRange+" : "+reason;
        }
    }

    public List<FailedEmails> process(InputStream is,int chunkSize) throws JAXBException, XMLStreamException {
        final StaxStreamProcessor processor = new StaxStreamProcessor(is);
        val cities = cityProcessor.process(processor);
        return userProcessor.process(processor, cities, chunkSize);
    }
}
