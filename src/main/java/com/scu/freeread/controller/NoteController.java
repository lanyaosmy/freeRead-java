package com.scu.freeread.controller;

import com.alibaba.fastjson.JSONObject;
import com.scu.freeread.entity.JsonResult;
import com.scu.freeread.entity.Note;
import com.scu.freeread.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/note")
public class NoteController {
    @Autowired
    private NoteService noteService;

    @RequestMapping(value="/shownote/{noteid}",method = RequestMethod.GET)
    public JsonResult<JSONObject> selectNote (@PathVariable int noteid){

        JSONObject json=noteService.selectNote(noteid);
        JsonResult<JSONObject> result = new JsonResult<>();
        if(json.containsKey("error")){
            result.setCode("1");
            result.setMsg(json.getString("error"));
        }else{
            noteService.addVisitNum(noteid);
            result.setData(json);
        }
        return result;
    }

    @RequestMapping(value="/newnote",method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    public JsonResult<JSONObject> newNote (@RequestBody Note note,HttpSession session) {
        int userid= (int) session.getAttribute("userid");
        note.setUserId(userid);
        note.setVisitNum(0);
        JsonResult result = new JsonResult();
        if(noteService.addNote(note)!=1){
            result.setCode("1");
            result.setMsg("创建笔记失败");
        }
        if(note.getPublishTime()!=null) {
            result.setData(noteService.getNoteid(userid, note.getPublishTime()));
        }
        return result;
    }

    @RequestMapping(value="/notetag",method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    public JsonResult<JSONObject> addNoteTag (@RequestBody JSONObject json) {
        List<Integer> tag= JSONObject.parseArray(json.getJSONArray("tagid").toJSONString(),Integer.class);
        JsonResult result = new JsonResult();
        if(noteService.addNoteTag(tag,json.getInteger("noteid"))!=1){
            result.setCode("1");
            result.setMsg("添加标签失败");
        }
        return result;
    }

    @RequestMapping(value="/boxnote",method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    public JsonResult<JSONObject> boxNote (@RequestBody Note note) {
        JsonResult result = new JsonResult();
        if(noteService.changeBoxNote(note)!=1){
            result.setCode("1");
            result.setMsg("修改笔记失败");
        }
        return result;
    }

    @RequestMapping(value="/hot",method = RequestMethod.GET)
    public JsonResult<JSONObject> selectHot (@RequestParam int page){
        JSONObject json=noteService.selectHot(page);
        JsonResult<JSONObject> result = new JsonResult<>();
        if(json.containsKey("error")){
            result.setCode("1");
            result.setMsg(json.getString("error"));
        }else {
            result.setData(json);
        }
        return result;
    }

    @RequestMapping(value="/latest",method = RequestMethod.GET)
    public JsonResult<JSONObject> selectNew (@RequestParam int page, HttpSession session){
        int userid= (int) session.getAttribute("userid");
        JSONObject json=noteService.selectNew(userid,page);
        JsonResult<JSONObject> result = new JsonResult<>();
        if(json.containsKey("error")){
            result.setCode("1");
            result.setMsg(json.getString("error"));
        }else {
            result.setData(json);
        }
        return result;
    }

    @RequestMapping(value="/authorlatest",method = RequestMethod.GET)
    public JsonResult<JSONObject> authorLatestNote (HttpSession session){
        int userid= (int) session.getAttribute("userid");
        JSONObject json=noteService.authorLatestNote(userid);
        JsonResult<JSONObject> result = new JsonResult<>();
        if(json.containsKey("error")){
            result.setCode("1");
            result.setMsg(json.getString("error"));
        }else {
            result.setData(json);
        }
        return result;
    }

    @RequestMapping(value="/collect",method = RequestMethod.GET)
    public JsonResult<JSONObject> collectNote (@RequestParam int noteid,HttpSession session) {
        int userid= (int) session.getAttribute("userid");
        JSONObject json=noteService.collectNote(userid,noteid);
        JsonResult<JSONObject> result = new JsonResult<>();
        if(json.containsKey("error")){
            result.setCode("1");
            result.setMsg(json.getString("error"));
        }else{
            result.setData(null);
        }
        return result;
    }

    @RequestMapping(value="/deletecollect",method = RequestMethod.GET)
    public JsonResult<JSONObject> deleteCollect (@RequestParam int noteid,HttpSession session){
        int userid= (int) session.getAttribute("userid");
        JSONObject json=noteService.deleteCollect(userid,noteid);
        JsonResult<JSONObject> result = new JsonResult<>();
        if(json.containsKey("error")){
            result.setCode("1");
            result.setMsg(json.getString("error"));
        }else{
            result.setData(null);
        }
        return result;
    }

    @RequestMapping(value="/published",method = RequestMethod.GET)
    public JsonResult<JSONObject> publishedNote (@RequestParam int page,HttpSession session){
        int userid= (int) session.getAttribute("userid");
        JSONObject json=noteService.publishedNote(userid,page);
        JsonResult<JSONObject> result = new JsonResult<>();
        if(json.containsKey("error")){
            result.setCode("1");
            result.setMsg(json.getString("error"));
        }else {
            result.setData(json);
        }

        return result;
    }

    @RequestMapping(value="/notpublished",method = RequestMethod.GET)
    public JsonResult<JSONObject> notPublishedNote (HttpSession session){
        int userid= (int) session.getAttribute("userid");
        JSONObject json=noteService.notPublishedNote(userid);
        JsonResult<JSONObject> result = new JsonResult<>();
        if(json.containsKey("error")){
            result.setCode("1");
            result.setMsg(json.getString("error"));
        }else {
            result.setData(json);
        }
        return result;
    }

    @RequestMapping(value="/deletenote",method = RequestMethod.GET)
    public JsonResult<JSONObject> deleteNote (@RequestParam int noteid,HttpSession session){
        int userid= (int) session.getAttribute("userid");
        JSONObject json=noteService.deleteNote(noteid,userid);
        JsonResult<JSONObject> result = new JsonResult<>();
        if(json.containsKey("error")){
            result.setCode("1");
            result.setMsg(json.getString("error"));
        }else{
            result.setData(null);
        }
        return result;
    }

    @RequestMapping(value="/usercollect",method = RequestMethod.GET)
    public JsonResult<JSONObject> userCollect (@RequestParam int page,HttpSession session){
        int userid= (int) session.getAttribute("userid");
        JSONObject json=noteService.selectCollectNote(userid,page);
        JsonResult<JSONObject> result = new JsonResult<>();
        if(json.containsKey("error")){
            result.setCode("1");
            result.setMsg(json.getString("error"));
        }else {
            result.setData(json);
        }
        return result;
    }
}
