package com.testcases;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.ApiDefinition.ApiCall;
import com.alibaba.fastjson.JSONObject;
import com.common.BaseTest;
import com.pojo.CaseData;
import com.util.ExcelUtil;
import io.restassured.response.Response;
import org.kohsuke.rngom.parse.host.Base;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.request;

public class LoginTest extends BaseTest {

    @DataProvider
    public Object[] getLoginDatas() {
        //一维数组/二维数组
        Object[] data = {"{\"principal\":\"waiwai\",\"credentials\":\"lemon123456\",\"appType\":3,\"loginType\":0}",
                "{\"principal\":\"\",\"credentials\":\"lemon123456\",\"appType\":3,\"loginType\":0}",
                "{\"principal\":\"waiwai\",\"credentials\":\"\",\"appType\":3,\"loginType\":0}",
                "{\"principal\":\"waiwai1\",\"credentials\":\"lemon123456\",\"appType\":3,\"loginType\":0}"};

        return data;

    }

    @Test(dataProvider = "getLoginDatas")
    public void test_login_from_array(String caseData) {
        //1.准备测试数据
        //String jsonData = "{\"principal\":\"waiwai\",\"credentials\":\"lemon123456\",\"appType\":3,\"loginType\":0}";
        //2.直接调用登录的接口请求
        Response res = ApiCall.login(caseData);
        //3.断言
        int statusCode = res.getStatusCode();
        Assert.assertEquals(statusCode,200);
        System.out.println("hello assertion");
        String nickName = res.jsonPath().get("nickName");
        Assert.assertEquals(nickName, "waiwai");
    }
        @DataProvider
        public  Object[] getLoginDatasFromExcel() {
          /*  //1.读取Excel？？Easypoi  不支持sheet的名字来获取，需要用索引 0,1,2
            ImportParams importParams = new ImportParams();
            importParams.setStartSheetIndex(0);
            //读取的文件src caseData 映射类
            List<CaseData> datas = ExcelImportUtil.importExcel(new File("src\\test\\resources\\caseData.xlsx"),
                 CaseData.class, importParams);
             return datas.toArray();*/
            return ExcelUtil.readExcel(0).toArray();
            //集合转成一维数组


}


    @Test(dataProvider = "getLoginDatasFromExcel")
    public void test_login_from_excel(CaseData caseData) {
        //1.准备测试数据
        //String jsonData = "{\"principal\":\"waiwai\",\"credentials\":\"lemon123456\",\"appType\":3,\"loginType\":0}";
        //2.直接调用登录的接口请求
        Response res = ApiCall.login(caseData.getInputParams());
        //3.断言
        String assertDatas = caseData.getAssertResponse();
        assertResponse(assertDatas,res);
    }


    }

