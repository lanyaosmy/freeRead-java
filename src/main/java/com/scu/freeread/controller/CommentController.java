package com.scu.freeread.controller;

import com.alibaba.fastjson.JSONObject;
import com.scu.freeread.entity.Comment;
import com.scu.freeread.entity.JsonResult;
import com.scu.freeread.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    CommentService commentService;

    @RequestMapping(value="/notecomment",method = RequestMethod.GET)
    public JsonResult<JSONObject> selectNoteComment (@RequestParam int noteid){
        JSONObject json=commentService.selectNoteComment(noteid);
        JsonResult<JSONObject> result = new JsonResult<>();
        if(json.containsKey("error")){
            result.setCode("1");
            result.setMsg(json.getString("error"));
        }else {
            result.setData(json);
        }
        return result;
    }

    @RequestMapping(value="/mycomment",method = RequestMethod.GET)
    public JsonResult<JSONObject> selectMyComment (HttpSession session){
        int userid= (int) session.getAttribute("userid");
        JSONObject json=commentService.selectUserComment(userid);
        JsonResult<JSONObject> result = new JsonResult<>();
        if(json.containsKey("error")){
            result.setCode("1");
            result.setMsg(json.getString("error"));
        }else {
            result.setData(json);
        }
        return result;
    }

    @RequestMapping(value="/addcomment",method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    public JsonResult<JSONObject> newComment (@RequestBody Comment comment,HttpSession session) {
        int userid= (int) session.getAttribute("userid");
        comment.setSenderId(userid);
        JSONObject json=commentService.addComment(comment);
        JsonResult<JSONObject> result = new JsonResult<>();
        if(json.containsKey("error")){
            result.setCode("1");
            result.setMsg(json.getString("error"));
        }else{
            result.setData(null);
        }
        return result;
    }
}
