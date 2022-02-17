package com.ApiDefinition;

import com.util.Environment;
import com.common.GlobalConfig;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.response.Response;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.head;


/**
 * 接口请求通用方法封装
  * @param method 请求方法（get/post/put/delete...）
  * @param url 接口请求地址
   * @param headersMap 请求头，存放到Map结构中
   * @param inputParams 请求参数
  * @return 接口响应结果
 */

public class ApiCall{
public static Response request(String method, String url, Map<String,Object> headersMap,String inputParams){
        //每个接口请求的日志单独的保存到本地的每一个文件中(重定向)
        String logFilePath=null;
        if(!GlobalConfig.IS_DEBUG) {
        PrintStream fileOutPutStream = null;
        //设置日志文件的地址
        String logFileDir = "target/log/";
        File file = new File(logFileDir);
        if (!file.exists()) {
        file.mkdirs();
        }
        logFilePath = logFileDir + "test_" + System.currentTimeMillis() + ".log";
        try {
        fileOutPutStream = new PrintStream(new File(logFilePath));
        } catch (FileNotFoundException e) {
        e.printStackTrace();
        }
        RestAssured.config = RestAssured.config().logConfig(LogConfig.logConfig().defaultStream(fileOutPutStream));
        }
        //参数化替换
        //1、接口入参做参数化替换
        inputParams = Environment.replaceParams(inputParams);
        //2、接口请求头参数化替换
        headersMap = Environment.replaceParams(headersMap);
        //3、接口请求地址参数化替换
        url = Environment.replaceParams(url);
        Response res = null;
        //指定项目base url
        RestAssured.baseURI=GlobalConfig.url;
        if("get".equalsIgnoreCase(method)){
        res = given().log().all().headers(headersMap).when().get(url+"?"+inputParams).then().log().all().extract().response();
        }else if("post".equalsIgnoreCase(method)){
        res = given().log().all().headers(headersMap).body(inputParams).when().post(url).then().log().all().extract().response();
        }else if("put".equalsIgnoreCase(method)){
        res = given().log().all().headers(headersMap).body(inputParams).when().put(url).then().log().all().extract().response();
        }else if("delete".equalsIgnoreCase(method)){
        //TODO
        }else {
        System.out.println("接口请求方法非法，请检查你的请求方法");
        }

        //添加日志信息到Allure报表中
        if(!GlobalConfig.IS_DEBUG) {
        try {
        Allure.addAttachment("接口的请求/响应信息", new FileInputStream(logFilePath));
        } catch (FileNotFoundException e) {
        e.printStackTrace();
        }
        }
        return res;
        }

/**
 * 登录接口请求定义
 * @param inputParams 传入的接口入参
 * {"principal":"waiwai","credentials":"lemon123456","appType":3,"loginType":0}
 * @return
 */
public static Response login(String inputParams){
        Map<String,Object> headMap = new HashMap<>();
        headMap.put("Content-Type","application/json");
        return request("post","/login",headMap,inputParams);
        }

    /**
     *搜索商品
     * @param inputParams
     * @return 响应体
     */
    public static Response searchProduct(String inputParams){
        Map<String,Object> headMap = new HashMap<>();
        headMap.put("Content-Type", "application/json");
       return request("get","/search/searchProdPage",headMap,inputParams);

    }

    /**
     * 商品信息请求接口
     * @param prodId
     * @return 响应体
     */


    public static Response ProductInfo(int prodId){
        Map<String,Object> headMap = new HashMap<>();
        headMap.put("Content-Type", "application/json");
        return request("get","/prod/prodInfo",headMap,"prodId="+prodId);

    }


    /**
     * 商品加入购物车请求接口
     * @param //Authorization: bearer3ac78074-6569-4795-ae29-57b25875344e
     * @return 响应体
     */


    public static Response ShopcartTest(String inputParment ,String token){
      //对接口的入参进行替换(发送请求之前进行替换)
        inputParment = Environment.replaceParams(inputParment);
        token =Environment.replaceParams(token);

        Map<String,Object> headMap = new HashMap<>();
        headMap.put("Content-type","application/json");
        headMap.put("Authorization","bearer" + token);

        return request("post","/p/shopCart/changeItem",headMap,inputParment);

    }

    /**
     * 注册验证码发送接口请求
     * @param inputParams  {"mobile": "13126607090"}
     * @return
     */
    public static Response sendRegisterSms(String inputParams){
        Map<String,Object> headMap = new HashMap<>();
        headMap.put("Content-type","application/json");
        return request("put","/user/sendRegisterSms",headMap,inputParams);

    }

    /**
     *校验验证码接口请求
     * @param inputParams
     * {
     * 	"mobile": "13126607090",
     * 	"validCode":
     * @return
     */
    public static Response checkRegisterSms(String inputParams){
        Map<String,Object> headMap = new HashMap<>();
        headMap.put("Content-type","application/json");
        return request("put","/user/checkRegisterSms",headMap,inputParams);
    }

    /**
     *注册接口请求
     * @param inputParams
     * {"appType": 3,
     * 	"checkRegisterSmsFlag": "60cd58a84d26459890caa73c1a557705",
     * 	"mobile": "13126607090",
     * 	"userName": "songsong",
     * 	"password": "aA123456",
     * 	"registerOrBind": 1,
     * 	"validateType": 1
     * }
     * @return
     */
    public static Response register(String inputParams){
        Map<String,Object> headMap = new HashMap<>();
        headMap.put("Content-type","application/json");
        return request("put","/user/registerOrBindUser",headMap,inputParams);
    }

    /**
     * 确认订单接口定义
     * @param inputParams
     * {"addrId":0,"orderItem":{"prodId":162,"skuId":505,"prodCount":1,"shopId":1},"couponIds":[],"isScorePay":0,"userChangeCoupon":0,"userUseScore":0,"uuid":"ad50c47c-d04a-4a1e-
     * @param token
     * @return
     */

    public static Response confirmOrder(String inputParams,String token){
        Map<String,Object> headMap = new HashMap<>();
        headMap.put("Content-type","application/json");
        headMap.put("Authorization","bearer" + token);
        return request("POST","/p/order/confirm",headMap,inputParams);
    }

    /**
     * 提交订单的接口定义
     * @param inputParams 接口入参
     * {"orderShopParam":[{"remarks":"","shopId":1}],"uuid":"ad50c47c-d04a-4a1e-8b0f-c1986baccb68"}
     * @param token
     * @return
     */
    public static Response submitOrder(String inputParams,String token){
        Map<String,Object> headMap = new HashMap<>();
        headMap.put("Content-type","application/json");
        headMap.put("Authorization","bearer" + token);
        return request("POST","/p/order/submit",headMap,inputParams);
    }


    /**
     * 支付下单接口定义
     * @param inputParams 接口入参
     * {"payType":3,"orderNumbers":"1481249684885606400"}
     * @param token
     * @return
     */
    public static Response placeOrder(String inputParams,String token){
        Map<String, Object> headMap = new HashMap<>();
        headMap.put("Content-Type","application/json");
        headMap.put("Authorization","bearer"+token);
        return request("POST","/p/order/pay",headMap,inputParams);
    }

    /**
     * 模拟支付回调的接口
     * @param inputParams 接口入参
     *{
     *    "payNo":1470015941797744640, #商城支付订单号
     *    "bizPayNo":XXXX, #微信方的订单号
     *    "isPaySuccess":true,#true成功，false失败
     * }
     * @param token
     * @return
     */
    public static Response mockPay(String inputParams,String token){
        Map<String, Object> headMap = new HashMap<>();
        headMap.put("Content-Type","application/json");
        headMap.put("Authorization","bearer"+token);
        return request("POST","/notice/pay/3",headMap,inputParams);
    }


    /**
     * erp项目的登录请求
     * @param inputParams
     * @return
     */
    public static Response erpLogin(String inputParams){
        Map<String, Object> headMap = new HashMap<>();
        headMap.put("Content-Type","application/x-www-form-urlencoded");
        return request("POST","/user/login ",headMap,inputParams);
    }

    /**
     * 前程贷项目的登录接口请求
     * @param inputParams 请求参数
     * @return
     */
    public static Response futureloanLogin(String inputParams){
        Map<String, Object> headMap = new HashMap<>();
        headMap.put("Content-Type","application/json");
        headMap.put("X-Lemonban-Media-Type","lemonban.v3");
        return request("POST","/futureloan/member/login",headMap,inputParams);
    }

    /**
     * 前程贷项目的充值接口请求
     * @param inputParams 接口请求入参
     * {
     *     "member_id": XXX,
     *     "amount": 10000.0,
     *     "timestamp": XXX,
     *     "sign": XXX
     * }
     * @param token
     * @return
     */
    public static Response futureloanRecharge(String inputParams,String token){
        Map<String, Object> headMap = new HashMap<>();
        headMap.put("Content-Type","application/json");
        headMap.put("X-Lemonban-Media-Type","lemonban.v3");
        headMap.put("Authorization","Bearer "+token);
        return request("POST","/futureloan/member/recharge",headMap,inputParams);
    }

}
