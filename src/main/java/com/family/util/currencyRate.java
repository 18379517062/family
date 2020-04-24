package com.family.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.text.DecimalFormat;

//注意：使用的是com.alibaba.fastjson.JSONArray ，不是net.sf.json.JSONObjec
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class currencyRate {

    public static final String DEF_CHATSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 30000;
    public static final int DEF_READ_TIMEOUT = 30000;
    public static String userAgent =  "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";

    //配置您申请的APPKEY
    public static final String APPKEY ="6549d3eb2544fe1ac568c58536b6f993";


    public static LinkedHashMap<String,Double> getRate() {
    //public static void main(String[] args) {

    String result1 =null;
        String url ="http://web.juhe.cn:8080/finance/exchange/rmbquot";//请求接口地址
        Map params = new HashMap();//请求参数
        params.put("key",APPKEY);//APP Key
        params.put("type","");//两种格式(0或者1,默认为0)

        Double eRate;  //兑换人民币汇率
        String name;   //币种名称
        LinkedHashMap<String,Double>map = new LinkedHashMap<>(); //map存放返回
        map.put("人民币",1.0);
        try {
            result1 =net(url, params, "GET");
            //将字符串转化成JSON对象
            JSONObject object = JSONObject.parseObject(result1);
            //转化成JSON数组
            JSONArray resultList = object.getJSONArray("result");
            //取出JSON数组中的值
            for (int i=1; i<23;i++){
                JSONObject json = (JSONObject) resultList.get(0);
                String USD = json.getString("data"+i);
                JSONObject rJson = (JSONObject) json.get("data"+i);
                eRate = rJson.getDouble("mBuyPri");
                if(eRate!=null){ //防止空数据集
                    name = rJson.getString("name");
                    //保留6位有效数字
                    DecimalFormat df = new DecimalFormat("#.0000");
                    eRate = Double.parseDouble(df.format(eRate/100.0));
                    map.put(name,eRate);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }


    /**
     *
     * @param strUrl 请求地址
     * @param params 请求参数
     * @param method 请求方法
     * @return  网络请求字符串
     * @throws Exception
     */
    public static String net(String strUrl, Map params,String method) throws Exception {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            StringBuffer sb = new StringBuffer();
            if(method==null || method.equals("GET")){
                strUrl = strUrl+"?"+urlencode(params);
            }
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            if(method==null || method.equals("GET")){
                conn.setRequestMethod("GET");
            }else{
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
            }
            conn.setRequestProperty("User-agent", userAgent);
            conn.setUseCaches(false);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            if (params!= null && method.equals("POST")) {
                try (DataOutputStream out = new DataOutputStream(conn.getOutputStream())) {
                    out.writeBytes(urlencode(params));
                }
            }
            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            rs = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rs;
    }
    //将map型转为请求参数型
    public static String urlencode(Map<String,Object>data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"","UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}