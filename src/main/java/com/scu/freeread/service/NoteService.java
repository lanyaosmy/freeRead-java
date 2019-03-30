package com.scu.freeread.service;

import com.alibaba.fastjson.JSONObject;
import com.scu.freeread.entity.Note;
import com.scu.freeread.mapper.CommentMapper;
import com.scu.freeread.mapper.NoteMapper;
import com.scu.freeread.mapper.TagMapper;
import com.scu.freeread.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class NoteService {
    static private Integer limitSize=200;

    @Autowired
    NoteMapper noteMapper;

    @Autowired
    TagMapper tagMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    CommentMapper commentMapper;

//  获取笔记详情
    public JSONObject selectNote(int noteid){
        JSONObject json=new JSONObject();
        try {
            json.put("note",noteMapper.selectNote(noteid));
            return json;
        }catch (Exception e){
            JSONObject error=new JSONObject();
            error.put("error","获取笔记详情失败");
            return error;
        }

    }


//  增加访问量
    public int addVisitNum(int noteid){
        return noteMapper.addVisitNum(noteid);
    }

    /**
     * 添加笔记信息
     * @param note
     * @return
     */
    public int addNote(Note note){
        return noteMapper.addNote(note);
    }

    public JSONObject getNoteid(int userid,Date publishtime){
        try {
            return noteMapper.getNoteidByTime(userid,publishtime);
        }catch (Exception e){
            JSONObject error=new JSONObject();
            error.put("error","获取笔记详情失败");
            return error;
        }
    }

    /**
     * 给笔记添加标签
     * @param taglist
     * @param noteid
     * @return
     */
    public int addNoteTag(List<Integer> taglist,int noteid){
        int size=taglist.size();
        if(size<=0){
            return 0;
        }
        for (int i = 0; i < size; i++) {
            if(tagMapper.addNoteTag(taglist.get(i), noteid)!=1){
                return 0;
            }
        }
        return 1;
    }

    /**
     * 获取热门笔记
     * @param page
     * @return
     */
    public JSONObject selectHot(int page){
        try {
            List<JSONObject> hotlist = noteMapper.selectHot(page);
            return noteFormat(hotlist);
        }catch (Exception e){
            JSONObject error=new JSONObject();
            error.put("error","数据请求出错");
            return error;
        }


    }

    /**
     * 获取用户关注标签动态
     * @param userid
     * @param page
     * @return
     */
    public JSONObject selectNew(int userid, int page){
        try {
            List<JSONObject> newlist = noteMapper.selectNew(userid,page);
            return noteFormat(newlist);
        }catch (Exception e){
            JSONObject error=new JSONObject();
            error.put("error","数据请求出错");
            return error;
        }
    }

    // 获取关注作者最新动态
    public JSONObject authorLatestNote(int userid){
        try {
            JSONObject json= new JSONObject();
            List<JSONObject> list = noteMapper.authorLatestNote(userid);
            int size=list.size();
            for (int i = 0; i < size; i++) {
                JSONObject item=list.get(i);
                item.put("publishtime",(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(item.get("publishtime")));
            }
            json.put("authorlatest",list);
            return json;
        }catch (Exception e){
            JSONObject error=new JSONObject();
            error.put("error","数据请求出错");
            return error;
        }
    }
    /**
     * 格式化笔记列表
     * @param list
     * @return
     */

    public JSONObject noteFormat(List<JSONObject> list){
        JSONObject json=new JSONObject();
        int size=list.size();
        if(size<10){
            json.put("hasMore",false);
        }else{
            json.put("hasMore",true);
        }
        for (int i = 0; i < size; i++) {
            JSONObject item=list.get(i);
            if(item.containsKey("content")) {
                String content = item.getString("content");
                item.put("content", content.substring(0, content.length() > limitSize ? limitSize : content.length()));
            }
            item.put("taglist",tagMapper.selectTag(item.getInteger("noteid")));
            item.put("commentCount",commentMapper.countComment(item.getInteger("noteid")));
            item.put("publishtime",(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(item.get("publishtime")));
        }
        json.put("notelist",list);

        return json;
    }

    /**
     * 收藏笔记
     * @param userid
     * @param noteid
     * @return
     */
    public JSONObject collectNote(int userid, int noteid){
        JSONObject error=new JSONObject();
        try {
            if(noteMapper.collectNote(userid,noteid)==1) {
                return error;
            }
            error.put("error","收藏笔记失败");
            return error;
        }catch (Exception e){
            error.put("error","收藏笔记失败");
            return error;
        }
    }

    /**
     * 取消收藏笔记
     * @param userid
     * @param noteid
     * @return
     */
    public JSONObject deleteCollect(int userid, int noteid){
        JSONObject error=new JSONObject();
        try {
            if(noteMapper.deleteCollect(userid,noteid)==1) {
                return error;
            }
            error.put("error","取消收藏笔记失败");
            return error;
        }catch (Exception e){
            error.put("error","取消收藏笔记失败");
            return error;
        }
    }

//    个人中心

    //  查看用户已发表的笔记——分页
    public JSONObject publishedNote(int userid, int page){
        JSONObject json=new JSONObject();
        try {
            List<JSONObject> list = noteMapper.publishedNote(userid,page);
            int size=list.size();
            for (int i = 0; i < size; i++) {
                JSONObject item=list.get(i);
                item.put("publishtime",(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(item.get("publishtime")));
            }
            json.put("notelist",list);
            json.put("totalpage",noteMapper.countPublishedNote(userid));
            return json;
        }catch (Exception e){
            JSONObject error=new JSONObject();
            error.put("error","数据请求出错");
            return error;
        }
    }

    //  查看用户草稿箱中的笔记
    public JSONObject notPublishedNote(int userid){
        JSONObject json=new JSONObject();
        try {
            List<JSONObject> list = noteMapper.notPublishedNote(userid);
            json.put("notelist",list);
            return json;
        }catch (Exception e){
            JSONObject error=new JSONObject();
            error.put("error","数据请求出错");
            return error;
        }

    }

    //  删除笔记（已发表or草稿箱）
    public JSONObject deleteNote(int noteid, int userid){
        JSONObject error=new JSONObject();
        try {
            if(noteMapper.selectNote(noteid).getInteger("userid")!=userid){
                error.put("error","不可以删除他人的笔记");
                return error;
            }
            if(noteMapper.deleteNote(noteid)==1) {
                return error;
            }
            error.put("error","删除笔记失败");
            return error;
        }catch (Exception e){
            error.put("error","删除笔记失败");
            return error;
        }
    }

    //  查看用户收藏的笔记
    public JSONObject selectCollectNote(int userid, int page){
        JSONObject json=new JSONObject();
        try {
            List<JSONObject> list = noteMapper.selectCollectNote(userid,page);
            int size=list.size();
            for (int i = 0; i < size; i++) {
                JSONObject item=list.get(i);
                item.put("publishtime",(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(item.get("publishtime")));
            }
            json.put("notelist",list);
            json.put("totalpage",noteMapper.countCollectNote(userid));
            return json;
        }catch (Exception e){
            JSONObject error=new JSONObject();
            error.put("error","数据请求出错");
            return error;
        }

    }


//  修改草稿箱中笔记 or 发表
    public int changeBoxNote(Note note){
        return noteMapper.changeBoxNote(note);
    }

}
