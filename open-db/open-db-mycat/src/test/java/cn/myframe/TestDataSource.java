package cn.myframe;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;

/*@SpringBootTest
@RunWith(SpringRunner.class)*/
public class TestDataSource {

    @Resource
    private DataSource dataSource;

    @Test
    public void testConnection() throws Exception {
        System.out.println(this.dataSource);
    }
}
