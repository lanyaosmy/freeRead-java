package com.scu.freeread.controller;

import com.alibaba.fastjson.JSONObject;
import com.scu.freeread.entity.JsonResult;
import com.scu.freeread.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/tag")
public class TagController {
    @Autowired
    TagService tagService;

    @RequestMapping(value="/userlist",method = RequestMethod.GET)
    public JsonResult<JSONObject> userTaglist (HttpSession session){
        int userid= (int) session.getAttribute("userid");
        JSONObject taglist=tagService.userTaglist(userid);
        JsonResult<JSONObject> result = new JsonResult<>();
        if(taglist.containsKey("error")){
            result.setCode("1");
            result.setMsg(taglist.getString("error"));
        }else{
            result.setData(taglist);
        }
        return result;
    }

    @RequestMapping(value="/follow",method = RequestMethod.POST)
    public JsonResult<JSONObject> followTag (@RequestBody JSONObject data, HttpSession session){
        int userid= (int) session.getAttribute("userid");
        List<Integer> tag= JSONObject.parseArray(data.getJSONArray("taglist").toJSONString(),Integer.class);
        JSONObject json=tagService.followTag(userid,tag);
        JsonResult<JSONObject> result = new JsonResult<>();
        if(json.containsKey("error")){
            result.setCode("1");
            result.setMsg(json.getString("error"));
        }else{
            result.setData(null);
        }
        return result;
    }

    @RequestMapping(value="/unfollow",method = RequestMethod.GET)
    public JsonResult<JSONObject> unFollowTag (@RequestParam int tagid,HttpSession session){
        int userid= (int) session.getAttribute("userid");
        JSONObject json=tagService.unFollowTag(userid,tagid);
        JsonResult<JSONObject> result = new JsonResult<>();
        if(json.containsKey("error")){
            result.setCode("1");
            result.setMsg(json.getString("error"));
        }else{
            result.setData(null);
        }
        return result;
    }

    @RequestMapping(value="/search",method = RequestMethod.GET)
    public JsonResult<JSONObject> searchTag (@RequestParam String keyword){
        JSONObject json=tagService.searchTag(keyword);
        JsonResult<JSONObject> result = new JsonResult<>();
        if(json.containsKey("error")){
            result.setCode("1");
            result.setMsg(json.getString("error"));
        }else{
            result.setData(json);
        }
        return result;
    }

    @RequestMapping(value="/usernotfollow",method = RequestMethod.GET)
    public JsonResult<JSONObject> notTag (HttpSession session){
        int userid= (int) session.getAttribute("userid");
        JSONObject json=tagService.userNotTag(userid);
        JsonResult<JSONObject> result = new JsonResult<>();
        if(json.containsKey("error")){
            result.setCode("1");
            result.setMsg(json.getString("error"));
        }else{
            result.setData(json);
        }
        return result;
    }
}
