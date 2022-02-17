package com.test.day02;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class ResponseTest {

    @Test
    public void test01() {
        String jsonData = "{\"principal\":\"waiwai\",\"credentials\":\"lemon123456\",\"appType\":3,\"loginType\":0}";
        //post请求：json类型传参
        Response res =
                given().
                        header("Content-Type", "application/json").
                        body(jsonData).
                        when().
                        post("http://mall.lemonban.com:8107/login").
                        then().
                        extract().response();

        //响应状态码
        System.out.println(res.getStatusCode());
        //接口的响应时间(ms)
        System.out.println(res.time());
        //获取接口的响应头信息getHeaders() 重写了toString(),所以能打印出来
        // System.out.println(res.getHeaders());
        //log().all() 只能打印到控制台，需要获取具体的值进一步处理（需要具体的值进行断言）
        System.out.println(res.getHeader("Content-Type"));
        System.out.println(res.getHeader("Set-Cookie"));
        //解析响应体
        //获取昵称
        System.out.println(res.jsonPath().get("nickName").toString());
        //获取过期时间
        System.out.println(res.jsonPath().get("expires_in").toString());


    }

/*    @Test
    public void test02() {
        String jsonData = "{\"principal\":\"waiwai\",\"credentials\":\"lemon123456\",\"appType\":3,\"loginType\":0}";
        //post请求：json类型传参
        Response res =
                given().
                when().
                        get("http://mall.lemonban.com:8107/prod/prodListByTagId?tagId=2&size=12").
                then().
                        log().all().
                        extract().response();
        //下标 为0 表示json数组的第一个元素
        //下标为-1 表示为json数组的最后一个元素，-2表示倒数第二个，以此类推
        //
        System.out.println(res.jsonPath().get("records[4].price"));
    }*/

 /*   @Test
    public void test03() {

        Response res =
                given().
                        when().
                        get("http://httpbin.org/xml").
                        then().
                        log().all().
                        extract().response();
        System.out.println(res.xmlPath().get("slideshow.slide[0].title"));
        System.out.println(res.xmlPath().get("slideshow.slide[1].item[0].em"));
    }*/

    @Test
    public void test04() {

        Response res =
                given().
                        when().
                        get("https://www.baidu.com/").
                        then().
                        log().all().
                        extract().response();
       // System.out.println(res.htmlPath().get("html.head.title"));
        //获取属性 @选择对应的属性
        System.out.println(res.htmlPath().get("html.head.meta[2].@content").toString());
        System.out.println(res.htmlPath().get("html.head.meta[1].@http-equiv").toString());

    }

    @Test
    public void test05() {
        String jsonData = "{\"principal\":\"waiwai\",\"credentials\":\"lemon123456\",\"appType\":3,\"loginType\":0}";
        //post请求：json类型传参
        Response res =
                given().
                        header("Content-Type", "application/json").
                        body(jsonData).
                        when().
                        post("http://mall.lemonban.com:8107/login").
                        then().
                        extract().response();
        int statusCode = res.getStatusCode();
        Assert.assertEquals(statusCode,200);
        System.out.println("hello assertion");
        String nickName = res.jsonPath().get("nickName");
        Assert.assertEquals(nickName,"waiwai");


    }
}