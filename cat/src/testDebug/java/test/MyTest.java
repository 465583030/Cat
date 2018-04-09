package test;

import com.zhr.cat.tools.ChatImpl;
import com.zhr.cat.tools.IChat;

import org.junit.Test;

/**
 * Created by ZL on 2018/4/8.
 */
public class MyTest {
    @Test
    public void test() throws Exception {
        IChat chater = new ChatImpl();
        for (int i = 0; i < 5; i++) {
            String s = chater.chat(IChat.ChatType.TYPE_TEXT, "你好");
            System.out.println("Cat:" + s);
        }
    }
}