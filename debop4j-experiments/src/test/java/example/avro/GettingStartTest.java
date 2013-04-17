package example.avro;

import com.google.common.collect.Lists;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * example.avro.GettingStartTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 17. 오후 5:33
 */
@Slf4j
public class GettingStartTest {

    public List<User> getTestUsers() {
        User user1 = new User();
        user1.setName("Alyssa");
        user1.setFavoriteNumber(256);
        // leave favorite color null

        // Alternate constructor
        User user2 = new User("Ben", 7, "red");

        // Constructor via builder
        User user3 = User.newBuilder()
                .setName("Charlie")
                .setFavoriteColor("blue")
                .setFavoriteNumber(null)
                .build();

        return Lists.newArrayList(user1, user2, user3);
    }

    @Test
    public void serializationTestWithGeneratedCode() throws Exception {
        File file = new File("users.avro");

        DatumWriter<User> userDatumWriter = new SpecificDatumWriter<User>(User.class);
        @Cleanup
        DataFileWriter<User> dataFileWriter = new DataFileWriter<User>(userDatumWriter);

        List<User> users = getTestUsers();
        dataFileWriter.create(users.get(0).getSchema(), file);

        for (final User user : users)
            dataFileWriter.append(user);
        dataFileWriter.close();

        // 읽기
        DatumReader<User> userDatumReader = new SpecificDatumReader<User>(User.class);
        @Cleanup
        DataFileReader<User> dataFileReader = new DataFileReader<User>(file, userDatumReader);

        User user = null;
        while (dataFileReader.hasNext()) {
            // Reuse user object by passing it to next().
            // This saves us from allocating and garbage collecting many objects for files with many items.
            user = dataFileReader.next(user);
            assertThat(user).isNotNull();
            assertThat(user.getName()).isNotNull();
            log.debug("User={}", user);
        }
        dataFileReader.close();
    }

    @Test
    public void serializationTestWithoutGeneratedCode() throws Exception {

        Schema schema = new Schema.Parser().parse(new File("./debop4j-experiments/src/main/avro/user.avsc"));
        assertThat(schema).isNotNull();

        GenericRecord user1 = new GenericData.Record(schema);
        user1.put("name", "Alyssa");
        user1.put("favorite_number", 256);

        GenericRecord user2 = new GenericData.Record(schema);
        user2.put("name", "Ben");
        user2.put("favorite_number", 7);
        user2.put("favorite_color", "red");

        File file = new File("users.avro");
        DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<GenericRecord>(schema);
        @Cleanup
        DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<GenericRecord>(datumWriter);
        dataFileWriter.create(schema, file);
        dataFileWriter.append(user1);
        dataFileWriter.append(user2);
        dataFileWriter.close();

        DatumReader<GenericRecord> datumReader = new GenericDatumReader<GenericRecord>(schema);
        @Cleanup
        DataFileReader<GenericRecord> dataFileReader = new DataFileReader<GenericRecord>(file, datumReader);

        GenericRecord record = null;
        while (dataFileReader.hasNext()) {
            record = dataFileReader.next(record);
            assertThat(record).isNotNull();
            assertThat(record.get("name")).isNotNull();
            log.debug("Record={}", record);
        }
        dataFileReader.close();
    }
}
