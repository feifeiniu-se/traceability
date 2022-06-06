package Persistence;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class MappingSaverTest {
    private final MappingSaver mappingSaver= new MappingSaver("/Users/neowoodley/Postgraduate/ScientificResearch/CaseStudy/sqlite/data/traceability.sqlite3");

    @Test
    public void save() {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("1", "test1");
        mapping.put("2", "test2");
        mapping.put("3", "test3");
        mapping.put("4", "test4");
        mapping.put("5", "test5");
//        mappingSaver.save(mapping);
    }
}