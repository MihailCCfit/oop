import ParserJSONstudentsData.ParserJsonStudentsData;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import studentsData.core.RecordBook;
import studentsData.core.Student;
import studentsData.core.Subject;


public class TesterRecordBook {
    @Test
    void testJS() throws IOException, ParseException {
        FileReader fileReader;
        fileReader = new FileReader("src/test/resources/js.json");
        JSONParser jsonParser = new JSONParser();
        JSONObject object = (JSONObject) jsonParser.parse(fileReader);

        RecordBook recordBook = ParserJsonStudentsData.recordBookParse(
                object);
        Assertions.assertTrue(recordBook.hasHighScholarship());
        Assertions.assertTrue(recordBook.getAverage() > 4);
        System.out.println(recordBook.shortInfo());
        System.out.println(recordBook);
        FileWriter fileWriter = new FileWriter("Output.txt");
        fileWriter.write(recordBook.toString());
        fileWriter.flush();

        Student me = new Student("Tsukanov", "Mikhail",
                "FIT", 21214, "m.tsukanov@g.nsu.ru");
        Assertions.assertEquals(me, recordBook.getStudent());
        Assertions.assertEquals(me.hashCode(), recordBook.getStudent().hashCode());

        me.setEmail("ru.ru");
        Assertions.assertNotEquals(me, recordBook.getStudent());
        me.setGroup(3228);
        Assertions.assertNotEquals(me, recordBook.getStudent());
        me.setDepartment("CCFIT");
        Assertions.assertNotEquals(me, recordBook.getStudent());
        me.setName("Ba");
        Assertions.assertNotEquals(me, recordBook.getStudent());
        me.setSurname("Abo");
        Assertions.assertNotEquals(me, recordBook.getStudent());
        Assertions.assertEquals(me, me);
        Assertions.assertEquals(me.toString(),
                "Student[surname=Abo, name=Ba, " +
                        "department=CCFIT, group=3228, mail=ru.ru]");
        for (Subject subject : recordBook.getSemesters().get(1).getSubjects()) {
            if (!subject.getMarkString().equals("5")) {
                subject.setMark(5);
            }
        }
        Assertions.assertEquals(recordBook.getAverage(), 5);
        Subject someSubj = recordBook.getSemesters()
                .get(1).getSubjects()
                .get(0);
        Assertions.assertTrue(someSubj.addTeacher("HaHaHa"));
        Assertions.assertFalse(recordBook.getSemesters()
                .get(1).getSubjects()
                .get(0).addTeacher("HaHaHa"));
        someSubj.setMark(5);
        someSubj.setCertificationDate("777.777.777");
        someSubj.setTeachers(List.of("Amogus"));
        Assertions.assertFalse(recordBook.getId() < 0);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> recordBook.setId(-4));
        recordBook.setId(777);
        Assertions.assertEquals(recordBook.getId(), 777);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> someSubj.setMark(-5));
        someSubj.setMark(1);
        Assertions.assertEquals(someSubj.getMarkString(), "Passed");
        someSubj.setMark(0);
        Assertions.assertEquals(someSubj.getMarkString(), "Failed");
        Assertions.assertTrue(recordBook.hasRedDiploma());
        someSubj.setMark(3);
        Assertions.assertEquals(someSubj.getMarkString(), "3");
        recordBook.getSemesters().clear();
        Assertions.assertEquals(recordBook.getAverage(), 0);
    }
}