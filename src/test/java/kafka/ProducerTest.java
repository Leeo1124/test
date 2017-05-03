package kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * 
 * localhost测试成功
 * 10.255.33.15测试失败
 *
 */
public class ProducerTest{
    
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
         
        Producer<String, String> producer = new KafkaProducer<>(props);
//        for(int i = 0; i < 100; i++){
//            producer.send(new ProducerRecord<>("my-topic", Integer.toString(i), Integer.toString(i)));
//        }
        
        String jsonString = "{\"JSON\":{\"age\":24,\"availableBalance\":121.345678,\"birthday\":\"2015-12-28 18:47:01\",\"cost\":121.345678,\"name\":\"wjl\",\"sex\":\"female\"}}";
        producer.send(new ProducerRecord<>("my-topic", "json", jsonString));
        
        producer.close(); 
    }
}