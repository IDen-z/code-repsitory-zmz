package com.zmz.mybatisplus;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zmz.mybatisplus.entity.User;
import com.zmz.mybatisplus.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MybatisplusApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelectPage(){
        Page<User> page=new Page<>(1,3);
        Page<User> userPage = userMapper.selectPage(page, null);
        //分页的所有数据都在这个userPage中封装了
        long pageNum = userPage.getPages();//总页数
        List<User> records = userPage.getRecords();//查询数据的集合（当前页）
        long total = userPage.getTotal();

        System.err.println(pageNum);
        System.err.println(records);
        System.err.println(total);
    }

    @Test
    public void testLogicDelete() {
        int result = userMapper.deleteById(1405475056003387394L);
        System.err.println(result);
    }



    @Test
    public void testCAS(){
        /*测试乐观锁*/
        User user = userMapper.selectById(1405429404473053186L);
        user.setName("genggai");
        userMapper.updateById(user);
    }


    @Test
    public void testAdd(){

        int insertRes = userMapper.insert(User.builder().name("aini").age(1).email("111@com").build());
        /*返回的是影响行数*/
        System.err.println(insertRes);
    }

    @Test
    public void testAdd2(){
        User user =new User();
        user.setName("lililili");
        user.setAge(12);
        user.setEmail("@@@");

        int insertRes = userMapper.insert(user);
        /*返回的是影响行数*/
        System.err.println(insertRes);
    }

    @Test
    public void findAll() {
        List<User> users = userMapper.selectList(null);
        System.err.println(users);
    }

    @Test
    public void updateTest(){
        int resLines = userMapper.updateById(User.builder().id(1405429404473053186L).email("123update@qq.com").build());
        System.err.println(resLines);
    }

}
