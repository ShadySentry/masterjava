package ru.javaops.masterjava.upload;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.CityDao;
import ru.javaops.masterjava.persist.model.City;
import ru.javaops.masterjava.xml.schema.ObjectFactory;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.stream.XMLStreamException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class CityProcessor {
    private static final JaxbParser jaxbParser = new JaxbParser(ObjectFactory.class);
    private static CityDao dao = DBIProvider.getDao(CityDao.class);

    public Map<String, City> process(final StaxStreamProcessor processor) throws XMLStreamException {
        Map<String, City> cities = dao.getAsMap();
        List<City> newCities = new ArrayList<>();
        while (processor.startElement("City", "Cities")) {
            val ref = processor.getAttribute("id");
            if (!cities.containsKey(ref)) {
                newCities.add(new City(ref, processor.getText()));
            }
        }
        log.info("Insert batch " + newCities);
        dao.insertBatch(newCities);

        return dao.getAsMap();
    }

}
