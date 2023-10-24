package com.ape.apecommon.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 驼峰转换工具类
 * @date 2023/10/11 9:30
 */
public class HumpUtils {

    /**
     * @description: 下划线转大驼峰
     * @param: str
     * @return:
     * @author shaozhujie
     * @date: 2023/10/11 9:30
     */
    public static String toCamel(String str,String ch){
        StringBuilder stringBuffer = new StringBuilder();
        if (!str.contains(ch)){
            stringBuffer.append(str.substring(0, 1).toUpperCase()).append(str.substring(1));
            return stringBuffer.toString();
        }
        String[] strings = str.split(ch);
        for (String string : strings) {
            stringBuffer.append(string.substring(0, 1).toUpperCase()).append(string.substring(1).toLowerCase());
        }
        return stringBuffer.toString();
    }

    /**
     * @description: 下划线转小驼峰
     * @param: str
     * @return:
     * @author shaozhujie
     * @date: 2023/10/11 9:30
     */
    public static String toSmallCamel(String str,String ch){
        StringBuilder stringBuffer = new StringBuilder();
        if (!str.contains(ch)){
            stringBuffer.append(str.substring(0, 1).toLowerCase()).append(str.substring(1));
            return stringBuffer.toString();
        }
        String[] strings = str.split(ch);
        for(int i = 0;i < strings.length;i++){
            if (i == 0) {
                stringBuffer.append(strings[i].toLowerCase());
            } else {
                stringBuffer.append(strings[i].substring(0,1).toUpperCase()).append(strings[i].substring(1).toLowerCase());
            }
        }
        return stringBuffer.toString();
    }

    /**
     * @description: 驼峰转下划线
     * @param: str
     * @return:
     * @author shaozhujie
     * @date: 2023/10/11 9:30
     */
    public static String CameltoUnderLine(String str){
        Pattern compile = Pattern.compile("[A-Z]");
        Matcher matcher = compile.matcher(str);
        StringBuffer sb = new StringBuffer();
        while(matcher.find()) {
            //将匹配到的大写字符转换成小写，并且在前面添加下划线然后添加到缓冲区。group(0)在没有分配组的时候匹配所有符合的
            matcher.appendReplacement(sb,  "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
