package com.common;

import com.alibaba.fastjson.JSONObject;
import com.util.JDBCUtils;
import io.restassured.response.Response;
import org.testng.Assert;

import java.util.Map;
import java.util.Set;

public class BaseTest {

    /**
     * 通用的响应断言方法
     * @param assertDatas
     * @param res
     */
    public void assertResponse(String assertDatas, Response res){
        //判空处理，当从Excel读取的响应数据为空的，表示不需要断言
        if(null != assertDatas){
                //statuscode:200,nikeName:"waiwai"
                //3.断言 响应断言为什么设计为json ，json更容易转换为java中的map 键值对 更容易解析
                //3-1 json字符串怎么转成Java 里面的Map？ 1.Fastjson 2.Jackson
                Map<String,Object> map = JSONObject.parseObject(assertDatas);
                //3-2 遍历Map
                Set<Map.Entry<String,Object>> datas = map.entrySet();
                for(Map.Entry<String,Object> keyValue :datas) {
                    //statuscode nickName
                    String key = keyValue.getKey();
                    //200 waiwai
                    Object value = keyValue.getValue();
                    //3-3 断言
                    if ("statuscode".equals(key)) {
                        //获取接口的响应状态码
                        int statuscode =0;
                        statuscode = res.getStatusCode();
                        System.out.println("断言响应状态码，期望值：" + value + "实际值：" + statuscode);
                        Assert.assertEquals(statuscode, value);
                    } else {
                        //响应体数据断言
                        //nickName --->相当于Gpath表达式
                        Object actualValue = res.jsonPath().get(key);
                        System.out.println("断言响应体字段，期望值：" + value + "实际值：" + actualValue);
                        Assert.assertEquals(actualValue, value);
                    }
                    }
                }
        }

                 /**
                  * 通用数据库断言
                  * @param assertDB
                 */

        public void assertDB(String assertDB){
        //把原始的数据库断言数据（json）转成Map
           Map<String, Object> map = JSONObject.parseObject(assertDB);
           Set<Map.Entry<String, Object>> datas = map.entrySet();
             for (Map.Entry<String, Object> keyValue : datas) {
            //map里面的key就是我们的查询sql语句
            Object actualValue = JDBCUtils.querySingleData(keyValue.getKey());
            System.out.println("实际值："+actualValue.toString());
            System.out.println("期望值："+keyValue.getValue().toString());
            //map里面的value就是我们的期望值
            Assert.assertEquals(actualValue.toString(),keyValue.getValue().toString());
            //类型匹配
            //1、数据类型转换，eg:Long int
            //2、toString
        }
}
}

