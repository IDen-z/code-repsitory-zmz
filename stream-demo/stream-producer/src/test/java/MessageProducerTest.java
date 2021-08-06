import com.zmz.StreamProducerMain8001;
import com.zmz.producer.MessageProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.PollableChannel;

@SpringBootTest(classes = {StreamProducerMain8001.class})
public class MessageProducerTest {

    @Autowired
    private MessageProducer messageProducer;


    @Test
    public void testSend(){
        messageProducer.sendMessage("hello spring cloud zmz!!!");
    }
//    @Test
//    public void testSend2(){
//        messageProducer.sendMessageTest("resolver 发送消息");
//    }



}
