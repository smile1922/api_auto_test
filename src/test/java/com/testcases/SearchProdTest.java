package com.testcases;

import com.ApiDefinition.ApiCall;
import com.common.BaseTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SearchProdTest extends BaseTest {

    @Test
    public void SearchProd_success(){
        //1.准备测试数据
        String data = "冰箱";
        //2.发起接口请求
        String inputData = "prodName=" +data+"&categoryId=&sort=0&orderBy=0&current=1&isAllProdType=true&st=0&size=12";
        Response res = ApiCall.searchProduct(inputData);
        //3.断言
        //3-1 响应状态码
        int status = res.getStatusCode();
        Assert.assertEquals(status,200);
        //3-1 响应体中是否包含冰箱
        String prodName = res.jsonPath().get("records[0].prodName");
        Assert.assertTrue(prodName.contains(data));


    }
}
