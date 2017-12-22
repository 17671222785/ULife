package com.andywang.ulife.util.network;

import android.text.TextUtils;

import com.andywang.ulife.entity.calendar.bean.Joke;
import com.andywang.ulife.entity.calendar.bean.News;
import com.andywang.ulife.entity.calendar.bean.WeiChat;
import com.andywang.ulife.util.support.CommonInfo;
import com.andywang.ulife.util.support.LogUtils;
import com.andywang.ulife.view.fragment.weichat.WeiChatFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by parting_soul on 2016/10/5.
 * json数据解析类
 */

public class JsonParseTool {
    /**
     * 将json数据解析为对应的新闻数组
     *
     * @param jsonString json数据
     * @return List<News> 新闻类的数组
     */
    public static List<News> parseNewsJsonWidthJSONObject(String jsonString) {
        if (jsonString == null) return null;
        List<News> lists = new ArrayList<News>();
        try {
            //得到根json对象
            JSONObject root = new JSONObject(jsonString);
            //得到json对象返回的状态码
            int error_code = root.getInt(CommonInfo.NewsAPI.JSONKEY.RESPONSE_JSON_ERROR_CODE_KEY_NAME);
            if (checkResultCode(error_code)) {
                JSONObject result = root.getJSONObject(CommonInfo.NewsAPI.JSONKEY.RESPONSE_JSON_RESULT_KEY_NAME);
                //得到新闻数据数组的json对象数组
                JSONArray dataArray = result.getJSONArray(CommonInfo.NewsAPI.JSONKEY.RESPONSE_JSON_RESULT_DATA_KEY_NAME);
                //遍历每个json对象，并生成对应的新闻类
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject newsJsonObject = dataArray.getJSONObject(i);
                    boolean isError = false;
                    News news = new News();
                    //根据各json的键值得到对用的数据值
                    try {
                        String title = newsJsonObject.getString(CommonInfo.NewsAPI.JSONKEY.RESPONSE_JSON_RESULT_NEWS_TITLE);
                        String date = newsJsonObject.getString(CommonInfo.NewsAPI.JSONKEY.RESPONSE_JSON_RESULT_NEWS_DATE);
                        String picPath = newsJsonObject.getString(CommonInfo.NewsAPI.JSONKEY.RESPONSE_JSON_RESULT_NEWS_PICTURE_PATH);
                        String url = newsJsonObject.getString(CommonInfo.NewsAPI.JSONKEY.RESPONSE_JSON_RESULT_NEWS_URL);
                        String author_name = newsJsonObject.getString(CommonInfo.NewsAPI.JSONKEY.RESPONSE_JSON_RESULT_NEWS_AUTHOR_NAME);

                        news.setTitle(title);
                        news.setDate(date);
                        news.setPicPath(picPath);
                        news.setUrl(url);
                        news.setAuthor_name(author_name);
                    } catch (Exception e) {
                        isError = true;
                    }
                    //                  String uniqueKey = newsJsonObject.getString(CommonInfo.NewsAPI.JSONKEY.RESPONSE_JSON_RESULT_NEWS_UNIQUE_KEY);
                    //                  String realType = newsJsonObject.getString(CommonInfo.NewsAPI.JSONKEY.RESPONSE_JSON_RESULT_NEWS_REALTYPE);

                    //若数据合法则添加到数组中
                    if (!isError && isAvailableData(news)) {
                        lists.add(news);
                    }
                }
            } else {
                lists = null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lists;
    }

    /**
     * 根据不同的返回码返回不同的状态
     *
     * @param error_code json数据中返回的状态码
     * @return boolean 返回正常为true
     */
    private static boolean checkResultCode(int error_code) {
        switch (error_code) {
            //返回成功
            case CommonInfo.NewsAPI.ResponseCode.RESPONSE_JSON_NORMALL_CODE:
                return true;
            //接口维护
            case CommonInfo.NewsAPI.ResponseCode.RESPONSE_JSON_API_INTERFACE_MAINTAIN:
                return false;
            //接口停止服务
            case CommonInfo.NewsAPI.ResponseCode.RESPONSE_JSON_API_INTERFACE_STOP:
                return false;
            //服务器异常
            case CommonInfo.NewsAPI.ResponseCode.RESPONSE_JSON_SERVER_ERROR:
                return false;
            default:
                return false;
        }
    }

    /**
     * 判断解析出的数据是否有效
     *
     * @param news
     * @return boolean
     */
    public static boolean isAvailableData(News news) {
        if (!TextUtils.isEmpty(news.getTitle()) && !TextUtils.isEmpty(news.getAuthor_name())
                && !TextUtils.isEmpty(news.getUrl()) && !TextUtils.isEmpty(news.getDate()) &&
                !TextUtils.isEmpty(news.getPicPath())) {
            return true;
        }
        return false;
    }

    public static List<WeiChat> parseWeiChatJsonWidthJSONObject(String jsonString) {
        if (jsonString == null) return null;
        List<WeiChat> lists = new ArrayList<WeiChat>();
        try {
            JSONObject root = new JSONObject(jsonString);
            //           String state = root.getString(CommonInfo.WeiChatAPI.JSONKEY.REQUEST_JSON_REASON_KEY_NAME);
            int state = root.getInt(CommonInfo.WeiChatAPI.JSONKEY.REQUEST_JSON_ERROR_CODE);
            if (state == CommonInfo.WeiChatAPI.JSONKEY.REQUEST_JSON_ERROR_NORMAL) {
                JSONObject result = root.getJSONObject(CommonInfo.WeiChatAPI.JSONKEY.RESPONSE_JSON_RESULT_KEY_NAME);
                JSONArray array = result.getJSONArray(CommonInfo.WeiChatAPI.JSONKEY.REQUEST_JSON_DATA_LISTS);
                int totalPage = result.getInt(CommonInfo.WeiChatAPI.JSONKEY.REQUEST_JSON_TOLALPAGE_KEY_NAME);
                int ps = result.getInt(CommonInfo.WeiChatAPI.JSONKEY.REQUEST_JSON_ITEM_NUMS_KEY_NAME);
                int pno = result.getInt(CommonInfo.WeiChatAPI.JSONKEY.REQUEST_JSON_PAGE_NO_KEY_NAME);
                WeiChatFragment.REQUEST_ITEM_NUMS = ps;
                WeiChatFragment.REQUEST_MAX_PAGE_NUMS = totalPage;
                for (int i = 0; i < array.length(); i++) {
                    WeiChat weiChat = new WeiChat();
                    JSONObject jsonObject = array.getJSONObject(i);
                    weiChat.setTitle(jsonObject.getString(CommonInfo.WeiChatAPI.JSONKEY.REQUEST_JSON_TITLE_KEY_NAME));
                    weiChat.setPicPath(jsonObject.getString(CommonInfo.WeiChatAPI.JSONKEY.REQUEST_JSON_IMG_KEY_NAME));
                    weiChat.setId(jsonObject.getString(CommonInfo.WeiChatAPI.JSONKEY.REQUEST_JSON_ID_KEY_NAME));
                    weiChat.setUrl(jsonObject.getString(CommonInfo.WeiChatAPI.JSONKEY.REQUEST_JSON_URL_KEY_NAME));
                    weiChat.setSource(jsonObject.getString(CommonInfo.WeiChatAPI.JSONKEY.REQUEST_JSON_SOURCE_KEY_NAME));
                    LogUtils.d(CommonInfo.TAG, "-->source " + weiChat.getSource());
                    weiChat.setPage(pno);
                    if (weiChat.getPicPath().equals("")) {
                        //图片url作为寻找imageview的标志不能重复
                        weiChat.setPicPath(new Date().toString() + new Random(100000));
                    }
                    lists.add(weiChat);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lists;
    }

    public static List<Joke> parseJokeJsonWidthJSONObject(String jsonString, int page) {
        if (jsonString == null) return null;
        List<Joke> lists = new ArrayList<Joke>();
        try {
            JSONObject root = new JSONObject(jsonString);
            String success = root.getString(CommonInfo.JokeApI.JsonKey.JOKE_REQUEST_JSON_REASON);
            if (CommonInfo.JokeApI.JsonKey.JOKE_REQUEST_JSON_REASON_SUCCESS.equals(success)) {
                JSONObject result = root.getJSONObject(CommonInfo.JokeApI.JsonKey.JOKE_REQUEST_JSON_RESULT);
                JSONArray data = result.getJSONArray(CommonInfo.JokeApI.JsonKey.JOKE_REQUEST_JSON_DATA);
                for (int i = 0; i < data.length(); i++) {
                    JSONObject jsonObject = data.getJSONObject(i);
                    Joke joke = new Joke();
                    joke.setIs_collected(false);
                    joke.setContent(jsonObject.getString(CommonInfo.JokeApI.JsonKey.JOKE_REQUEST_JSON_CONTENT));
                    joke.setDate(jsonObject.getString(CommonInfo.JokeApI.JsonKey.JOKE_REQUEST_JSON_UPDATA_TIME));
                    joke.setHashId(jsonObject.getString(CommonInfo.JokeApI.JsonKey.JOKE_REQUEST_JSON_HASHID));
                    joke.setPage(page);
                    lists.add(joke);
                }
                if (lists.size() == 0) {
                    lists = null;
                }
            } else {
                lists = null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lists;
    }

}
