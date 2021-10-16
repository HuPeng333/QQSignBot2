package priv.xds.util;

import priv.xds.pojo.AutoSign;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author DeSen Xu
 * @date 2021-10-16 15:42
 */
public class AutoSignPojoUtil {

    private static final Map<String, String> AUTO_SIGN_MAPPER;

    static {
        AUTO_SIGN_MAPPER = new HashMap<>();
        AUTO_SIGN_MAPPER.put("token", "token");
        AUTO_SIGN_MAPPER.put("学号", "yhm");
        AUTO_SIGN_MAPPER.put("联系电话", "lxdh");
        AUTO_SIGN_MAPPER.put("家长姓名", "jjlxr");
        AUTO_SIGN_MAPPER.put("家长联系电话", "jjlxdh");
        AUTO_SIGN_MAPPER.put("打卡地点", "location");
    }

    /**
     * 给指定的AutoSign对象赋值
     * @param attrName 要赋值的属性,如'家长姓名'
     * @param attrValue 要赋给该属性的值
     * @param target 要赋值的目标
     */
    public static void putAttributeToAutoSign(String attrName, String attrValue, AutoSign target) throws IllegalAccessException, NoSuchFieldException {
        Class<AutoSign> autoSignClass = AutoSign.class;
        String attr = AUTO_SIGN_MAPPER.get(attrName);
        if (attr == null) {
            throw new NoSuchFieldException();
        }
        Field declaredField = autoSignClass.getDeclaredField(attr);
        declaredField.setAccessible(true);
        declaredField.set(target, attrValue);
    }

}
