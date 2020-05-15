package org.egc.gis.ows;

import com.alibaba.fastjson.serializer.ValueFilter;

import java.util.ArrayList;

/**
 * Description:
 * <pre>
 * convert keywords string to {@link Keywords}
 * </pre>
 *
 * @author houzhiwei
 * @date 2019/11/5 19:30
 */
public class KeywordsFilter implements ValueFilter {
    @Override
    public Object process(Object object, String name, Object value) {
        if ("Keywords".equalsIgnoreCase(name)) {
            if (value instanceof String) {
                Keywords keywords = new Keywords();
                ArrayList<String> keyword = new ArrayList<>();
                keyword.add((String) value);
                keywords.setKeyword(keyword);
                return keywords;
            }
        }
        return value;
    }
}
