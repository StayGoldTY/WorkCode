//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.demo.dzxx;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.dzxx.tongtech.TongTechEncode;
import java.util.ArrayList;
import java.util.List;

import com.entity.DataMain;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class restfulDemoVillageBuild {
    static final String signKeyUrl = "http://192.168.181.50:8181/sysapi/refreshappsecret";
    static final String tongTecurl = "http://192.168.181.50:8181/httpproxy";
    static final String appkey = "b15eb8f40546a392888386e408d67de5";
    static final String rid = "11460000008173790G@59";
    static final String sid = "s_2746000000000_15093";

    public restfulDemoVillageBuild() {
    }


    public static void main(String[] args) throws Exception {


    }
    public static void GetData()  throws Exception {
        String rtime = "" + System.currentTimeMillis();
        //第一步 获取sign
        String sign = TongTechEncode.HmacSHA256(sid, rid, appkey,rtime);
        System.out.println("对加解密后的秘钥第一次签名后的sign：" + sign);
        //
        //第二步 刷新秘钥
        String secret=TongTechEncode.getSercret(sid, rid, rtime, sign,signKeyUrl);
        ParserConfig.getGlobalInstance().setAsmEnable(false);
        DataMain dataMain=(DataMain) JSON.parseObject(secret, DataMain.class);
        System.out.println("新密码："+dataMain.getData().getSecret());
        Thread.sleep(61000);

//		第三步 解密
        String newSecret = TongTechEncode.AESDncode(appkey, dataMain.getData().getSecret());
        System.out.println("------newSecret---"+newSecret);
        //
        //第四步 根据新秘钥获取新sign
        String rtime2 = "" + System.currentTimeMillis();
        String sign2 = TongTechEncode.HmacSHA256(sid, rid, newSecret,rtime2);
        System.out.println("对加解密后的秘钥第二次签名后的sign2：" + sign2);

        String jsonStr = accessTongtec(rid,sid,sign2,rtime2);
        System.out.println("接口返回数据："+jsonStr);
    }

    // 访问ESB服务
    public static String accessTongtec(String rid ,String sid,String sign2,String rtime) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(tongTecurl);// 创建httpPost
        httpPost.setHeader(new BasicHeader("Accept", "application/json;charset=utf-8"));

        String charSet = "UTF-8";
        // 封装请求头
        httpPost.setHeader("hnjhpt_rid", rid);
        httpPost.setHeader("hnjhpt_sid", sid);
        httpPost.setHeader("hnjhpt_rtime",rtime);
        httpPost.setHeader("hnjhpt_sign", sign2);
        // 封装请求参数
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("key1", "value1"));
        params.add(new BasicNameValuePair("key2", "value2"));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, charSet);
        httpPost.setEntity(entity);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpPost);
            StatusLine status = response.getStatusLine();
            int state = status.getStatusCode();
            if (state == HttpStatus.SC_OK) {
                HttpEntity responseEntity = response.getEntity();
                String jsonString = EntityUtils.toString(responseEntity);
                return jsonString;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

