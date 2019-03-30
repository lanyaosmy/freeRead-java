package com.scu.freeread.service;

import com.alibaba.fastjson.JSONObject;
import com.scu.freeread.entity.Comment;
import com.scu.freeread.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentMapper commentMapper;

    /**
     * 个人中心：获取评论列表
     * @param userid
     * @return
     */
    public JSONObject selectUserComment(int userid){
        try {
            JSONObject json=new JSONObject();
            List<JSONObject> list=commentMapper.selectUserComment(userid);
            int size=list.size();
            for (int i = 0; i < size ; i++) {
                JSONObject temp=list.get(i);
                temp.put("commenttime",(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(list.get(i).get("commenttime")));
            }
            json.put("commentlist",list);
            return json;
        }catch (Exception e){
            JSONObject error=new JSONObject();
            error.put("error","获取评论列表失败");
            return error;
        }

    }

    public JSONObject selectNoteComment(int noteid){
        try {
            JSONObject json=new JSONObject();
            List<JSONObject> list=commentMapper.selectNoteComment(noteid);
            int size=list.size();
            for (int i = 0; i < size ; i++) {
                JSONObject temp=list.get(i);
                temp.put("commenttime",(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(list.get(i).get("commenttime")));
            }
            json.put("commentlist",list);
            return json;
        }catch (Exception e){
            JSONObject error=new JSONObject();
            error.put("error","获取评论列表失败");
            return error;
        }

    }

//  添加评论
    public JSONObject addComment(Comment comment){
        JSONObject error=new JSONObject();
        try {
            if(commentMapper.addComment(comment)==1) {
                return error;
            }
            error.put("error","添加评论失败");
            return error;
        }catch (Exception e){
            error.put("error","添加评论失败");
            return error;
        }
    }
}
