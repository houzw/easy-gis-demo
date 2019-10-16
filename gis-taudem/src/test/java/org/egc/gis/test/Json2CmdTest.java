package org.egc.gis.test;

import com.alibaba.fastjson.JSONReader;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.StringReader;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Description:
 * <pre>
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/10/27 11:24
 */
public class Json2CmdTest {

    /**
     * 针对大的Json文件，不是一次性读取
     * 原文：https://blog.csdn.net/drlyee/article/details/43699427
     * @throws FileNotFoundException
     */
    @Test
    public void testParseJson() throws FileNotFoundException {
        // 测试数据
        String jsonString = "{\"array\":[1,2,3],\"arraylist\":[{\"a\":\"b\",\"c\":\"d\",\"e\":\"f\"},{\"a\":\"b\",\"c\":\"d\",\"e\":\"f\"},{\"a\":\"b\",\"c\":\"d\",\"e\":\"f\"}],\"object\":{\"a\":\"b\",\"c\":\"d\",\"e\":\"f\"},\"string\":\"HelloWorld\"}";
        JSONReader reader = new JSONReader(new StringReader(jsonString));
        // 解析键值对
        reader.startObject();
        while (reader.hasNext()) {
            // key
            String key = reader.readString();

            switch (key) {
                case "array":
                    reader.startArray();
                    while (reader.hasNext()) {
                        String item = reader.readString();
                        System.out.println(item);
                    }
                    reader.endArray();
                    break;
                case "arraylist":
                    reader.startArray();
                    while (reader.hasNext()) {
                        // array list item
                        reader.startObject();
                        while (reader.hasNext()) {
                            String item = reader.readString();
                            String itemValue = reader.readObject().toString();
                            System.out.println(item);
                            System.out.println(itemValue);
                        }
                        reader.endObject();
                    }
                    reader.endArray();
                    break;
                case "object":
                    reader.startObject();
                    while (reader.hasNext()) {
                        String item = reader.readString();
                        String itemValue = reader.readObject().toString();
                        System.out.println(item);
                        System.out.println(itemValue);
                    }
                    reader.endObject();
                    break;
                case "string":
                    String value = reader.readObject().toString();
                    System.out.println(value);
                    break;
            }
        }
        reader.endObject();
        reader.close();
    }

    public <T extends Object> String checkType(T object) {
        if (object == null) {
            return null;
        }
        if (object instanceof Integer)
            return "Integer";
        else if (object instanceof Double)
            return "Double";
        else if (object instanceof Long)
            return "Long";
        else if (object instanceof Float)
            return "Float";
        else if (object instanceof List)
            return "List";
        else if (object instanceof Set)
            return "Set";
        else if (object instanceof String)
            return "String";
        else if (object instanceof Boolean)
            return "Boolean";
        else if (object instanceof Date)
            return "Date";
        else
            return object.getClass().getSimpleName();
    }



}
