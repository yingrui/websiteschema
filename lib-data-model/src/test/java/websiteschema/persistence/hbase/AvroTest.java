/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.hbase;

import java.util.ArrayList;
import java.util.List;
import org.apache.avro.reflect.ReflectDatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.Decoder;
import org.apache.avro.reflect.ReflectData;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.JsonEncoder;
import org.codehaus.jackson.JsonGenerator;
import java.io.ByteArrayOutputStream;
import org.apache.avro.reflect.ReflectDatumWriter;
import java.io.IOException;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.generic.GenericDatumWriter;
import websiteschema.model.domain.cluster.DocUnits;
import websiteschema.model.domain.cluster.Unit;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;

import org.junit.Test;
//import org.apache.avro.io.EncoderFactory;
//import static websiteschema.utils.AvroUtil.*;

/**
 *
 * @author ray
 */
public class AvroTest {

//    private static final String schemaDescription =
//            "{ \n"
//            + " \"namespace\": \"com.navteq.avro\", \n"
//            + " \"name\": \"FacebookUser\", \n"
//            + " \"type\": \"record\",\n"
//            + " \"fields\": [\n"
//            + " {\"name\": \"name\", \"type\": [\"string\", \"null\"] },\n"
//            + " {\"name\": \"num_likes\", \"type\": \"int\"},\n"
//            + " {\"name\": \"num_photos\", \"type\": \"int\"},\n"
//            + " {\"name\": \"num_groups\", \"type\": \"int\"} ]\n"
//            + "}";
//    private static final String schemaDescriptionExt =
//            " { \n"
//            + " \"namespace\": \"com.navteq.avro\", \n"
//            + " \"name\": \"FacebookSpecialUser\", \n"
//            + " \"type\": \"record\",\n"
//            + " \"fields\": [\n"
//            + " {\"name\": \"user\", \"type\": com.navteq.avro.FacebookUser },\n"
//            + " {\"name\": \"specialData\", \"type\": \"int\"} ]\n"
//            + "}";
//
//    @Test
//    public void simpleTest() {
//        parseSchema(schemaDescription);
//        Schema extended = parseSchema(schemaDescriptionExt);
//        System.out.println(extended.toString(true));
//    }
//
//    @Test
//    public void testMap() throws IOException {
//        Schema schema = ReflectData.get().getSchema(DocUnits.class);
//        schema.addProp("java-class", "java.util.List");
//        System.out.println(schema.toString(true));
//        Unit unit = new Unit("HTML/HEAD/META", "[]");
//        Unit unit2 = new Unit("HTML/HEAD", "b");
//        Unit[] units = new Unit[2];
//        units[0] = unit;
//        units[1] = unit2;
//        DocUnits docUnits = new DocUnits();
//        docUnits.setUnits(units);
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ReflectDatumWriter<DocUnits> writer = new ReflectDatumWriter<DocUnits>(schema);
//        Encoder encoder = EncoderFactory.get().jsonEncoder(schema, baos);
//        writer.write(docUnits, encoder);
//        encoder.flush();
//        byte[] bytes = baos.toByteArray();
//        String str = baos.toString();
//        System.out.println("json: " + str);
//
//
//        Decoder decoder = DecoderFactory.get().jsonDecoder(schema, str);
//        ReflectDatumReader<DocUnits> reader = new ReflectDatumReader<DocUnits>(schema);
//        DocUnits du = reader.read(null, decoder);
//
//
//        System.out.println(du.getUnits()[0].getText());
//    }
//
//    @Test
//    public void testMap2() throws IOException {
//        Schema schema = ReflectData.get().getSchema(DocUnits2.class);
//        schema.addProp("java-class", "java.util.List");
//        System.out.println(schema.toString(true));
//        Unit unit = new Unit("HTML/HEAD/META", "[]");
//        Unit unit2 = new Unit("HTML/HEAD", "b");
//        List<Unit> units = new ArrayList<Unit>();
//        units.add(unit);
//        units.add(unit2);
//        DocUnits2 docUnits = new DocUnits2();
//        docUnits.setUnits(units);
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ReflectDatumWriter<DocUnits2> writer = new ReflectDatumWriter<DocUnits2>(schema);
//        Encoder encoder = EncoderFactory.get().jsonEncoder(schema, baos);
//        writer.write(docUnits, encoder);
//        encoder.flush();
//        byte[] bytes = baos.toByteArray();
//        String str = baos.toString();
//        System.out.println("json: " + str);
//
//
//        Decoder decoder = DecoderFactory.get().jsonDecoder(schema, str);
//        ReflectDatumReader<DocUnits2> reader = new ReflectDatumReader<DocUnits2>(schema);
//        DocUnits2 du = reader.read(null, decoder);
//
//
//        System.out.println(du.getUnits().get(0).getText());
//    }
//
//    @Test
//    public void testArray() throws IOException {
//        Unit unit1 = new Unit("HTML/HEAD/META", "中国");
//        Unit unit2 = new Unit("HTML/HEAD", "b");
//        Unit[] units = new Unit[2];
//        units[0] = unit1;
//        units[1] = unit2;
//        Schema schema = ReflectData.get().getSchema(units.getClass());
//        schema.addProp("java-class", "java.util.List");
//        System.out.println(schema.toString(true));
//        System.out.println(schema.getProp("java-class"));
//        System.out.println(schema.getProp("java-element-class"));
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ReflectDatumWriter writer = new ReflectDatumWriter(schema);
//        Encoder encoder = EncoderFactory.get().jsonEncoder(schema, baos);
//        writer.write(units, encoder);
//        encoder.flush();
//        String str = baos.toString();
//        System.out.println("json: " + str);
//
//
//        Decoder decoder = DecoderFactory.get().jsonDecoder(schema, str);
//        ReflectDatumReader reader = new ReflectDatumReader(schema);
//        List<Unit> reuse = new ArrayList<Unit>();
//        List<Unit> u =  (List<Unit>) reader.read(reuse, decoder);
//        System.out.println(u.get(0).getText());
//    }
}
