package cn.xiaocool.wxtteacher.utils;

import java.util.List;

/**
 * Created by zhuchongkun.
 */
public class StringUtils {
    /**
     * 提取出城市或者县
     * @param city
     * @param district
     * @return
     */
    public static String extractLocation(final String city, final String district){
        return district.contains("县") ? district.substring(0, district.length() - 1) : city.substring(0, city.length() - 1);
    }


    public static String listToString(List list, String separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) {
                sb.append(list.get(i));
            } else {
                sb.append(list.get(i));
                sb.append(separator);
            }
        }
        return sb.toString();
    }
}
