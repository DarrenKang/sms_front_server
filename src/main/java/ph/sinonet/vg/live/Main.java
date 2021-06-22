package ph.sinonet.vg.live;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by jay on 11/15/16.
 */
public class Main {

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("/conf/spring/applicationContext.xml");
        String[] beans = ctx.getBeanDefinitionNames();
        for (int i =0; i< beans.length; i++) {
            System.out.println(beans[i]);
        }
    }
}
