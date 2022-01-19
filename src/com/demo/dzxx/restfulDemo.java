//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.demo.dzxx;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.dzxx.tongtech.TongTechEncode;
import com.entity.DataMain;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class restfulDemo {
    static String signKeyUrl = "http://192.168.181.50:8181/sysapi/refreshappsecret";
    static String tongTecurl = "http://192.168.181.50:8181/httpproxy";
    static String rid = "11460000008173790G@59";

    //默认使用海南省建设工程规划许可证  接口授权码
    static List sidList = new ArrayList();
    static List appkeyList = new ArrayList();

    public restfulDemo() {
        sidList.add("s_2746000000000_15093");
        sidList.add("s_2746000000000_14911");
        sidList.add("s_2746000000000_14914");
        appkeyList.add("b15eb8f40546a392888386e408d67de5");
        appkeyList.add("b2db1a1b548abdc21b7bbe75dc9aac70");
        appkeyList.add("2dccc0522811100bfb7f3fdff4cd91fb");
    }


    public static void main(String[] args) throws Exception {

    }

    public static void GetData(HttpServletRequest req,String EsbType) throws Exception {
        String rtime = "" + System.currentTimeMillis();
        //第一步 获取sign
        String sign = TongTechEncode.HmacSHA256(sid, rid, appkey, rtime);
        System.out.println("对加解密后的秘钥第一次签名后的sign：" + sign);

        //第二步 刷新秘钥
        String secret = TongTechEncode.getSercret(sid, rid, rtime, sign, signKeyUrl);
        ParserConfig.getGlobalInstance().setAsmEnable(false);
        DataMain dataMain = (DataMain) JSON.parseObject(secret, DataMain.class);
        System.out.println("新密码：" + dataMain.getData().getSecret());
        //Thread.sleep(61000);

        //		第三步 解密
        String newSecret = TongTechEncode.AESDncode(appkey, dataMain.getData().getSecret());
        System.out.println("------newSecret---" + newSecret);

        //第四步 根据新秘钥获取新sign
        String rtime2 = "" + System.currentTimeMillis();
        String sign2 = TongTechEncode.HmacSHA256(sid, rid, newSecret, rtime2);
        System.out.println("对加解密后的秘钥第二次签名后的sign2：" + sign2);

        String jsonStr = accessTongtec(rid, sid, sign2, rtime2,EsbType,req);
        System.out.println("接口返回数据：" + jsonStr);
    }

    // 访问ESB服务
    public static String accessTongtec(String rid, String sid, String sign2, String rtime,String EsbType,HttpServletRequest req) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(tongTecurl);// 创建httpPost
        httpPost.setHeader(new BasicHeader("Accept", "application/json;charset=utf-8"));

        String charSet = "UTF-8";
        // 封装请求头
        httpPost.setHeader("hnjhpt_rid", rid);
        httpPost.setHeader("hnjhpt_sid", sid);
        httpPost.setHeader("hnjhpt_rtime", rtime);
        httpPost.setHeader("hnjhpt_sign", sign2);
        httpPost.setHeader("Authorization", "Bearer 7bee4ad2-1d1f-36e0-b9ab-625a7e495de2");
        // 封装请求参数
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        //根据不同的情况来封装不同的参数
        if (EsbType == "1") {

            //持证主体类型代码
            String ownerTypeCode = req.getParameter("ownerTypeCode");
            //ownerTypeCode = "001";
            // 001:统一信用代码;099:其他法人或其他组织有效证件代码;111:公民身份证;999:其他自然人有效证件代码;005持证主体名称
            params.add(new BasicNameValuePair("ownerTypeCode", ownerTypeCode));
            //持证主体编号
            String ownerCode = req.getParameter("ownerCode");
            //ownerCode = "11460103008195308P";
            params.add(new BasicNameValuePair("ownerCode", ownerCode));
        } else if (EsbType == "2") {
            //持证主体类型代码
            String ownerTypeCode = req.getParameter("ownerTypeCode");
            //ownerTypeCode = "001";
            // 001:统一信用代码;099:其他法人或其他组织有效证件代码;111:公民身份证;999:其他自然人有效证件代码;005持证主体名称
            params.add(new BasicNameValuePair("ownerTypeCode", ownerTypeCode));
            //持证主体编号
            String ownerCode = req.getParameter("ownerCode");
            //ownerCode = "91460100MA5THWMM2J";
            params.add(new BasicNameValuePair("ownerCode", ownerCode));
        } else if (EsbType == "3") {
            //持证主体类型代码
            String ownerTypeCode = req.getParameter("ownerTypeCode");
           // ownerTypeCode = "001";
            // 001:统一信用代码;099:其他法人或其他组织有效证件代码;111:公民身份证;999:其他自然人有效证件代码;005持证主体名称
            params.add(new BasicNameValuePair("ownerTypeCode", ownerTypeCode));
            //持证主体编号
            String ownerCode = req.getParameter("ownerCode");
           // ownerCode = "914600007477761066";
            params.add(new BasicNameValuePair("ownerCode", ownerCode));
            //电子证照编号
           // String electLicenseCode = req.getParameter("electLicenseCode");
           // params.add(new BasicNameValuePair("electLicenseCode", electLicenseCode));
        }
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
