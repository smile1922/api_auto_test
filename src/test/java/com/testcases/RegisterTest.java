package com.testcases;

import com.ApiDefinition.ApiCall;
import com.common.BaseTest;
import com.util.Environment;
import com.util.JDBCUtils;
import com.util.RandomDataUtil;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RegisterTest extends BaseTest {
    @Test
    public void test_register_success(){
        //手机号码没有被注册？？
        //思路1：生成随机的手机号码（和数据库中的有一定概率重复）
        //思路2：查询数据库，去看手机号码（难度比较高，工作量大）
        //思路3：随机生成手机号码-->查库看是否已注册 faker库 伪造数据
        String randomPhone = RandomDataUtil.getUnregisterPhone();
        String randomName = RandomDataUtil.getUnregisterName();
        //保存到环境变量
        Environment.saveToEnvironment("randomPhone",randomPhone);
        Environment.saveToEnvironment("randomName",randomName);

        //1.发起验证码接口
        String data01 = "{\"mobile\": \"#randomPhone#\"}";
        ApiCall.sendRegisterSms(data01);

        //2,校验验证码接口
        //关键问题，验证码该怎么获取？？？查询数据库表
        String sql = "select mobile_code from tz_sms_log where id =(select max(id) from tz_sms_log);";
        String code = (String) JDBCUtils.querySingleData(sql);
        //将验证码保存到环境变量中
        Environment.saveToEnvironment("code",code);
        String data02 = "{\"mobile\": \"#randomPhone#\",\"validCode\": \"#code#\"}";
        Response checkRes = ApiCall.checkRegisterSms(data02);

        //拿到接口响应纯文本类型的数据
        String checkSms =checkRes.body().asString();
        //将验证码校验字符串保存到环境变量中
        Environment.saveToEnvironment("checkSms",checkSms);

        //3.注册接口请求
        String data03 = "{\"appType\":3,\"checkRegisterSmsFlag\": \"#checkSms#\"," +
                "\"mobile\": \"#randomPhone#\",\"userName\": \"#randomName#\",\"password\": \"aA123456\"," +
                "\"registerOrBind\": 1,\"validateType\": 1}";
        Response registerRes = ApiCall.register(data03);

        //4.响应断言
        Assert.assertEquals(registerRes.getStatusCode(),200);
        Assert.assertEquals(registerRes.jsonPath().get("nickName"),randomName);

        //5.数据库断言
        String assertSql = "select count(*) from tz_user where user_mobile = '#randomPhone#';";
        long actual = (long)JDBCUtils.querySingleData(assertSql);
        Assert.assertEquals(actual,1);


    }
}
