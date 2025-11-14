package cn.lyp.basics.security.utils;

import java.io.*;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import javax.net.ssl.*;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiOperation(value = "HTTP工具类")
public class WeiChatUtils implements X509TrustManager {

    @ApiModelProperty(value = "企业ID")
    public static final String CORPID = "";

    @ApiModelProperty(value = "企业微信密匙")
    public static final String CORPSECRET = "";

    /**
     * 企业微信 应用Token
     * @return
     */
    public static String getToken(){
        String s= httpsRequest("https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=" + CORPID + "&corpsecret=" + CORPSECRET,"GET",null);
        JSONObject err = JSON.parseObject(s);
        if(err.getString("errmsg").equals("ok")){
            return err.getString("access_token");
        }
        return null;
    }

    public static String getCustomerName(String wxid,String inToken,String outToken) {
        if(wxid.startsWith("wo") || wxid.startsWith("wm")) {
            String s= httpsRequest("https://qyapi.weixin.qq.com/cgi-bin/externalcontact/get?access_token=" + outToken + "&external_userid=" + wxid,"GET",null);
            JSONObject jsonObject = JSON.parseObject(s);
            if(jsonObject.getString("errcode").equals("0")) {
                JSONObject externalContact = jsonObject.getJSONObject("external_contact");
                return externalContact.getString("name");
            }else {
                return "未知";
            }
        }
        if(wxid.startsWith("wb")) {
            return "机器人";
        }
        String s= httpsRequest("https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=" + inToken + "&userid=" + wxid,"GET",null);
        JSONObject jsonObject = JSON.parseObject(s);
        if(jsonObject.getString("errcode").equals("0")) {
            return jsonObject.getString("name");
        }else {
            return "未知";
        }
    }


    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

    }

    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }

    /*
     * 处理https GET/POST请求
     * 请求地址、请求方法、参数
     * */
    public static String httpsRequest(String requestUrl,String requestMethod,String outputStr){
        StringBuffer buffer=null;
        try{
            SSLContext sslContext=SSLContext.getInstance("SSL");
            TrustManager[] tm={new WeiChatUtils()};
            sslContext.init(null, tm, new java.security.SecureRandom());;
            SSLSocketFactory ssf=sslContext.getSocketFactory();
            URL url=new URL(requestUrl);
            HttpsURLConnection conn=(HttpsURLConnection)url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod(requestMethod);
            conn.setSSLSocketFactory(ssf);
            conn.connect();
            if(null!=outputStr){
                OutputStream os=conn.getOutputStream();
                os.write(outputStr.getBytes("utf-8"));
                os.close();
            }

            InputStream is=conn.getInputStream();
            InputStreamReader isr=new InputStreamReader(is,"utf-8");
            BufferedReader br=new BufferedReader(isr);
            buffer=new StringBuffer();
            String line=null;
            while((line=br.readLine())!=null){
                buffer.append(line);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return buffer.toString();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Wechatdepartment {
        private String name;
        private String name_en;
        private int parentid;
        private int order;
        private int id;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class WeChatUser {
        private String name;
        private String alias;
        private String mobile;
        private String userid;
        private String[] department;
        private String[] order;
        private  String position;
        private  String gender;
        private  String email;
        private  String is_leader_in_dept;
        private  String main_department;
        private List<String> useridlist;
        private String telephone;
        private ExternalProfile external_profile;
    }

    @Data
    private static class ExternalProfile {
        private ExternalAttr[] external_attr;
    }

    @Data
    private static class ExternalAttr {
        private String type;
        private ExternalText text;
        private String name;
    }

    @Data
    private static class ExternalText {
        private String value;
    }
}

