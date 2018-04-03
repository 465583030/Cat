package com.zhr.cat.tools.net;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by ZHR on 2017/05/25.
 */

public class CommonTools {
    public static String readInputStream(InputStream is)
    {
        //内存输出流
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        try {
            int len =0;
            byte buffer[]=new byte[1024];
            while((len=is.read(buffer))!=-1)
            {
                baos.write(buffer, 0, len);
            }
            is.close();
            byte result[]=baos.toByteArray();
            baos.close();
            return new String(result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
