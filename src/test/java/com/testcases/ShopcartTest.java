package com.testcases;

import com.ApiDefinition.ApiCall;
import com.common.BaseTest;
import com.pojo.CaseData;
import com.service.BusinessFlow;
import com.util.Environment;
import com.util.ExcelUtil;
import com.util.JDBCUtils;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class ShopcartTest extends BaseTest{

    @Test
    public void test_add_shopcart_success() {
        //添加购物车用例
        //1.准备测试数据
        //登录数据
 /*       String jsonData = "{\"principal\":\"waiwai\",\"credentials\":\"lemon123456\",\"appType\":3,\"loginType\":0}";

        //2.发起接口请求
        //2-1 登录
        Response loginRes = ApiCall.login(jsonData);
        //提取token
        String token =loginRes.jsonPath().get("access_token");
        //2-2 搜索商品
        String searchData = "prodName=&categoryId=&sort=0&orderBy=0&current=1&isAllProdType=true&st=0&size=12";
        Response searchRes = ApiCall.searchProduct(searchData);
        //提取商品ID
        int prodId = searchRes.jsonPath().get("records[4].prodId");
        //2-3 商品信息
        Response infoRes =ApiCall.ProductInfo(prodId);*/
        Response infoRes = BusinessFlow.login_search_info();
        //提取skuId
        int skuId = infoRes.jsonPath().get("skuList[0].skuId");

       // System.out.println(skuId);

        //保存到环境变量中
        Environment.saveToEnvironment("skuId",skuId);

        //2-4 添加到购物车
        String shopCartData = "{\"basketId\":0,\"count\":1,\"prodId\":#prodId#,\"shopId\":1,\"skuId\":#skuId#}";
        Response shopCartRes = ApiCall.ShopcartTest(shopCartData,"#token#");

        //3. 断言
        int status = shopCartRes.getStatusCode();
        Assert.assertEquals(status,200);

        //4.数据库断言
        String assertSql = "select count(*) from tz_basket where user_id = (select user_id from tz_user where user_name = 'waiwai')";

        //根据购物车商品的数量断言
       Assert.assertEquals((long)JDBCUtils.querySingleData(assertSql),1);
}

      @Test(dataProvider = "getShopCartDatas")
      public void test_shopcart(CaseData caseData){
        //测试数据
        //1.登录获取token
          Response loginRes = ApiCall.login("{\"principal\":\"waiwai\",\"credentials\":\"lemon123456\",\"appType\":3,\"loginType\":0}");
          String token = loginRes.jsonPath().get("access_token");
          String inputParams = caseData.getInputParams();
          Response addShopCartRes = ApiCall.ShopcartTest(inputParams,token);
          //断言
          assertResponse(caseData.getAssertResponse(),addShopCartRes);
          //数据库断言
          //根据购物车商品的数量断言
          assertDB(caseData.getAssertDB());
      }

      @DataProvider
      public Object[] getShopCartDatas(){
          return ExcelUtil.readExcel(2).toArray();
      }



}
