package com.zmz.hospitalmanage;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class HospitalManageApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void mainTest(){
        String a= "1,2,3";
        List<Long> list = Arrays.stream(a.split(",")).mapToLong(Long::parseLong).boxed().collect(Collectors.toList());
        List<Long> roleids=new ArrayList<>();
        roleids.add(3L);
        roleids.add(4L);
        roleids.add(5L);
        List<Long> collect = list.stream().filter(roleids::contains).collect(Collectors.toList());
        System.err.println(list);
        System.err.println(roleids);
        System.err.println(collect);
        roleids.addAll(list);
        System.err.println(roleids);





    }



}
