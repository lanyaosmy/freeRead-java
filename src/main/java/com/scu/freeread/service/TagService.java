package com.scu.freeread.service;

import com.alibaba.fastjson.JSONObject;
import com.scu.freeread.mapper.TagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {
    @Autowired
    TagMapper tagMapper;

    public JSONObject userTaglist(int userid){
        JSONObject json=new JSONObject();
        try {
            List<JSONObject> list = tagMapper.userTaglist(userid);
            json.put("taglist",list);
            return json;
        }catch (Exception e){
            JSONObject error=new JSONObject();
            error.put("error","数据请求出错");
            return error;
        }
    }

    public JSONObject followTag(int userid, List<Integer> tag){
        JSONObject json=new JSONObject();
        try {
            int size=tag.size();
            for (int i = 0; i < size; i++) {
                if(tagMapper.followTag(userid,tag.get(i))==1) {
                    json.put("error","关注标签失败");
                    return json;
                }
            }
            return json;
        }catch (Exception e){
            JSONObject error=new JSONObject();
            error.put("error","关注标签失败");
            return error;
        }
    }

    public JSONObject unFollowTag(int userid, int tagid){
        JSONObject error=new JSONObject();
        try {
            if(tagMapper.unfollowTag(userid,tagid)==1) {
                return error;
            }
            error.put("error","取消关注标签失败");
            return error;
        }catch (Exception e){
            error.put("error","取消关注标签失败");
            return error;
        }
    }

    public JSONObject searchTag(String keyword){
        JSONObject json=new JSONObject();
        try {
            List<JSONObject> list = tagMapper.searchTag(keyword);
            json.put("taglist",list);
            return json;
        }catch (Exception e){
            JSONObject error=new JSONObject();
            error.put("error","查询标签出错");
            return error;
        }
    }

    public JSONObject userNotTag(int userid){
        JSONObject json=new JSONObject();
        try {
            List<JSONObject> list = tagMapper.userNotTag(userid);
            json.put("taglist",list);
            return json;
        }catch (Exception e){
            JSONObject error=new JSONObject();
            error.put("error","查询标签出错");
            return error;
        }
    }
}
