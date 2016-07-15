package kr.scg.web.a;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import kr.scg.web.KrScgWebApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = KrScgWebApplication.class)
@WebAppConfiguration
public class KrScgWebApplicationTests {

	@Test
	public void contextLoads() {
	}
}
