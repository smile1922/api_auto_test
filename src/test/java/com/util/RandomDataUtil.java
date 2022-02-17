package com.util;

import com.github.javafaker.Faker;

import java.util.Locale;

public class RandomDataUtil {
    public static void main(String[] args){
        Faker faker = new Faker();
       // System.out.println(faker.phoneNumber().cellPhone());
      /*  System.out.println(faker.address().city());
        System.out.println(faker.address().fullAddress());*/
        System.out.println(faker.name().lastName());
        getUnregisterPhone();
    }

    /**
     * 获取未被注册的手机号码
     */
    public static String getUnregisterPhone(){
        //1.随机生成手机号码
        Faker faker = new Faker(Locale.CHINA);
        String randomphone = faker.phoneNumber().cellPhone();
        //2.查库-->去判别数据库中有没有这个手机号
        String sql = "Select count(*) from tz_user where user_mobile = '" +randomphone+"';";
        //3.循环遍历
        while(true){
        long count =(long)JDBCUtils.querySingleData(sql);
        if(count == 0){
        //没有被注册---符合要求
            break;
        }else if (count == 1){
            //已经被注册--再一次生成一个新的手机号码
            randomphone = faker.phoneNumber().cellPhone();
            sql = "Select count(*) from tz_user where user_mobile = '" +randomphone+"';";
        }
        }
        return randomphone;
    }

    /**
     * 生成未被注册的用户名
     * @return 用户名
     */

    public static String getUnregisterName(){
        //1.随机生成用户名
        Faker faker = new Faker();
        String randomName = faker.name().lastName();
        //2.查库-->去判别数据库中有没有这个用户名
        String sql = "Select count(*) from tz_user where user_name = '" +randomName+"';";
        //3.循环遍历
        while(true){
            long count =(long)JDBCUtils.querySingleData(sql);
            if(count == 0){
                //没有被注册---符合要求
                break;
            }else if (count == 1){
                //已经被注册--再一次生成一个新的用户名
                randomName = faker.name().lastName();
                sql = "Select count(*) from tz_user where user_name = '" +randomName+"';";
            }
        }
        return randomName;
    }

}
