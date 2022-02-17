package com.service;

import com.ApiDefinition.ApiCall;
import com.util.Environment;
import io.restassured.response.Response;

public class BusinessFlow {


    /**
     * 1. 登录-->搜索-->商品信息 场景组合接口调用
     * @return 商品信息接口的响应数据
     */
    public static Response login_search_info(){
        //场景组合是由多个接口请求组装的
        //1.登录接口
        String loginData = "{\"principal\":\"waiwai\",\"credentials\":\"lemon123456\",\"appType\":3,\"loginType\":0}";
        Response loginRes = ApiCall.login(loginData);
        //提取token
        String token =loginRes.jsonPath().get("access_token");
        //保存到环境变量中

        Environment.saveToEnvironment("token",token);
        //2.搜索接口
        String searchData = "prodName=&categoryId=&sort=0&orderBy=0&current=1&isAllProdType=true&st=0&size=12";
        Response searchRes = ApiCall.searchProduct(searchData);
        //提取商品ID
        int prodId = searchRes.jsonPath().get("records[0].prodId");
        //保存到环境变量中
        Environment.saveToEnvironment("prodId",prodId);

        //3.商品信息接口
        Response infoRes =ApiCall.ProductInfo(prodId);
        return infoRes;



    }

}
