package com.zhr.cat.tools;

import com.alibaba.fastjson.JSONObject;
import com.zhr.cat.tools.http.HttpUtils;

import java.util.HashMap;
import java.util.Map;

import static com.zhr.cat.tools.Constant.CHAT_URL;

/**
 * Created by ZHR on 2018/4/8.
 */

public class ChatImpl implements IChat {
    @Override
    public String chat(ChatType type, String data) throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("content", data);
        map.put("type", String.valueOf(type.getType()));
        String result = HttpUtils.post(CHAT_URL, map, 1000);
        JSONObject jsonObject = JSONObject.parseObject(result);
        String s = jsonObject.getString("data");
        return s;
    }
}
