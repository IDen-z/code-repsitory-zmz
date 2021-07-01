import com.zmz.yygh.common.util.HttpRequestHelper;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class MapEntryTest {

    @Test
    public void testMap(){
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, String[]> parameterMap = new HashMap<>();
        parameterMap.put("key1",new String[] {"value1","value2","value3"});
        parameterMap.put("key2",new String[] {"value4","value5"});
        System.err.println(parameterMap);
        for (Map.Entry<String,String[]> entry:parameterMap.entrySet()){
            resultMap.put(entry.getKey(),entry.getValue());
        }
        Map<String, Object> switchMap = HttpRequestHelper.switchMap(parameterMap);

        System.err.println(resultMap.toString());
        System.err.println(switchMap.toString());
    }


}
