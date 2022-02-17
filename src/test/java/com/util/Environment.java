package com.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.poi.poifs.filesystem.Entry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Environment {
    //设计一个Map结构，类似于postman的环境变量区域
    //是一个接口，不是一个实际的类
    public static Map<String,Object> envMap = new HashMap<String, Object>();


    /**
     * 向环境变量中存储对应的键值对
     * @param varName
     * @param varValue
     */
    public static void saveToEnvironment(String varName,Object varValue){
        Environment.envMap.put(varName,varValue);
    }

    /**
     * 从环境变量中取得对应的值
     * @param varName
     * @return
     */

    public static Object getEnvironment(String varName){
        return Environment.envMap.get(varName);
    }

    /**
     * 字符串参数化替换
     * @param inputParam
     * @return  参数化替换之后的结果
     */
    public static String replaceParams(String inputParam){
        //识别#xx# 正则表达式？
        String regex = "#(.+?)#";
        //编译得到Pattern模式对象
        Pattern pattern = Pattern.compile(regex);
        //通过pattern的matcher匹配，得到匹配器-->搜索
        Matcher matcher = pattern.matcher(inputParam);
        //循环在原始的字符串中来找符合正则表达式对应的字符串

        while(matcher.find()){
            //matcher.group(0) 表示整个匹配到的字符串
            String wholeStr = matcher.group(0);
            //  matcher.group(1)表示分组的第一个 #xx# 号里面的XX
            String subStr = matcher.group(1);
            //替换#xx#  String 不可变 finall修饰， 整数不能强转为字符串类型，但可以通过拼接转换成String类型
            inputParam = inputParam.replace(wholeStr,Environment.getEnvironment(subStr)+"");

        }
        return inputParam;
    }

    /**
     * Map参数化替换  方法重载，方法名一样，参数类型不一样
     * @param headersMap
     * @return  参数化替换之后的结果
     */
    public static Map<String,Object> replaceParams(Map<String,Object> headersMap) {
        //把Map转成字符串
        String datas = JSONObject.toJSONString(headersMap);
        //识别#xx# 正则表达式？
       /* String regex = "#(.+?)#";
        //编译得到Pattern模式对象
        Pattern pattern = Pattern.compile(regex);
        //通过pattern的matcher匹配，得到匹配器-->搜索
        Matcher matcher = pattern.matcher(datas);
        //循环在原始的字符串中来找符合正则表达式对应的字符串

        while (matcher.find()) {
            //matcher.group(0) 表示整个匹配到的字符串
            String wholeStr = matcher.group(0);
            //  matcher.group(1)表示分组的第一个 #xx# 号里面的XX
            String subStr = matcher.group(1);
            //替换#xx#  String 不可变 finall修饰， 整数不能强转为字符串类型，但可以通过拼接转换成String类型
            datas = datas.replace(wholeStr, Environment.getEnvironment(subStr) + "");

        }*/
        datas = replaceParams(datas);
        //把字符串再转成Map
        Map<String,Object> map = JSONObject.parseObject(datas);
        return map;
    }

    public static void main(String[] args){
        String inputParam ="{\"basketId\":0,\"count\":1,\"prodId\":#prodId#,\"shopId\":1,\"skuId\":#skuId#}";
   //{"basketId":0,"count":1,"prodId":"143","shopId":1,"skuId":486}
        //1.将对应的值保存到环境变量中
        Environment.saveToEnvironment("prodId",143);
        Environment.saveToEnvironment("skuId",486);
        //2.replaceParams 方法完成替换
        System.out.println(replaceParams(inputParam));
    }
}
