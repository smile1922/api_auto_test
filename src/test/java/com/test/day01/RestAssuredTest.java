package com.test.day01;
import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.RestAssured.given;

public class RestAssuredTest {
    public static void main(String[] args) {

       //1.简单的get请求
  /*      given().
        when().
               get("http://mall.lemonban.com:8107/prod/prodListByTagId?tagId=2&size=12").
        then().
                log().body();*/

        //2.简单的post请求
    /*    String jsonData = "{\"principal\":\"waiwai\",\"credentials\":\"lemon123456\",\"appType\":3,\"loginType\":0}";
        given().
                log().all().
                header("Content-Type","application/json").
                body(jsonData).
                when().
                        post("http://mall.lemonban.com:8107/login").
                then().
                        log().all();*/


        }
    @Test
    public void getDemo01() {
        //get请求参数直接拼接到URL地址的后面
        given().
                when().
                get("http://mall.lemonban.com:8107/prod/prodListByTagId?tagId=2&size=12").
                then().
                log().body();


    }
    @Test
    public void getDemo02(){
        //get 请求参数写到given()后面
        given().
                queryParam("tagId",2).
                queryParam("size",12).
         when().
                get("http://mall.lemonban.com:8107/prod/prodListByTagId").
         then().
                log().body();

    }

    @Test
    public void postDemo01(){
        //post请求:form 表单传参
        given().
                header("Content-Type","application/x-www-form-urlencoded").
                body("loginame=admin&password=e10adc3949ba59abbe56e057f20f883e").
         when().
                post("http://erp.lemfix.com/user/login").
         then().
                log().body();

    }
/*    @Test
    public void postDemo02(){
        //post请求:form 表单传参
        String jsonData = "{\"principal\":\"waiwai\",\"credentials\":\"lemon123456\",\"appType\":3,\"loginType\":0}";
        given().
                header("Content-Type","application/json").
                body(jsonData).
        when().
                post("http://mall.lemonban.com:8107/login").
        then().
                log().all();

    }*/
/*    @Test
    public void postDemo03(){
        //xml传参方式
        String xmlStr = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<suite>\n" +
                "    <class>测试xml</class>\n" +
                "</suite>";
        given().
                contentType("application/xml").
                body(xmlStr).
        when().
                post("http://www.httpbin.org/post").
        then().
                log().body();
    }*/

    @Test
    public void postDemo04(){
        //post请求：文件上传
        given().
                log().all().
                header("Authorization","bearerb94d67cc-5349-4980-ad56-2d2fa64db44a").
                multiPart(new File("D:\\test\\test.png")).
        when().
                post("http://mall.lemonban.com:8108/admin/file/upload/img").
        then().
                log().body();
    }

    }



