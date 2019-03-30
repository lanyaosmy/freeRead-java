package com.scu.freeread.mapper;

import com.alibaba.fastjson.JSONObject;
import com.scu.freeread.entity.Note;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface NoteMapper {

//  获取笔记详情
    @Select("SELECT noteid,n.userid,title,bookname,content,idea,publishtime,username,userintro,headimage FROM note as n join user as u on u.userid=n.userid WHERE noteid =#{noteid}")
    JSONObject selectNote(int noteid);

//  获取关注作者最新动态
    @Select("select n.userid, noteid,title,max(publishtime)as publishtime,username,headimage  from note as n join followuser as f join user as u on u.userid= n.userid where f.followid=n.userid and f.userid=#{userid} and publishtime is not NULL " +
            "and DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= publishtime GROUP BY n.userid ORDER BY publishtime desc limit 5")
    List<JSONObject> authorLatestNote(@Param("userid") int userid);


//  热门笔记
    @Select("SELECT n.noteid,n.userid,title,bookname,content,publishtime,username,userintro,headimage,visitnum FROM note as n join user as u "+
            "where n.userid=u.userid and publishtime is not NULL ORDER BY visitnum desc LIMIT 10 offset ${(page-1)*10}")
    List<JSONObject> selectHot(@Param("page") int page);

//  关注动态
    @Select("select n.noteid,n.userid,title,bookname,content,publishtime,username,userintro,headimage,visitnum from note as n join notetag as t join user as u "+
            "where n.noteid=t.noteid and n.userid=u.userid and publishtime is not NULL and tagid in (select tagid from followtag where userid=#{id}) and n.userid!=#{id} order by publishtime desc LIMIT 10 offset ${(page-1)*10}")
    List<JSONObject> selectNew(@Param("id") int id, @Param("page") int page);

//  插入笔记
    @Insert("INSERT INTO note(userid, title, bookname, content, idea, publishtime, visitnum) "+
            "VALUES (#{userId}, #{title}, #{bookName}, #{content}, #{idea}, #{publishTime},0)")
    @Options(useGeneratedKeys = true, keyColumn = "noteid", keyProperty = "noteId")
    int addNote(Note note);

    @Select("select noteid from note where userid=#{userid} and publishtime=#{time}")
    JSONObject getNoteidByTime(@Param("userid") int userid, @Param("time") Date time);

//  增加访问量
    @Update("update note set visitnum=visitnum+1 where noteid=#{noteid}")
    int addVisitNum(@Param("noteid")int noteid);

//  收藏笔记
    @Insert("INSERT INTO collect(userid, noteid, collecttime) VALUES (#{userid}, #{noteid}, NOW())")
    int collectNote(@Param("userid") int userid, @Param("noteid") int noteid);

//  取消收藏笔记
    @Delete("DELETE FROM collect WHERE userid=#{userid} AND noteid=#{noteid}")
    int deleteCollect(@Param("userid") int userid, @Param("noteid") int noteid);

/**
 * 个人中心
 */

//  查看用户已发表的笔记——分页
    @Select("select noteid,title,bookname,publishtime,visitnum from note where userid=#{userid} and publishtime is not NULL ORDER BY publishtime desc LIMIT 10 offset ${(page-1)*10}")
    List<JSONObject> publishedNote(@Param("userid") int userid, @Param("page") int page);

    @Select("select count(*) from note where userid=#{userid} and publishtime is not NULL")
    int countPublishedNote(@Param("userid") int userid);

//  查看用户草稿箱中的笔记
    @Select("select noteid,title,bookname from note where userid=#{userid} and publishtime is NULL")
    List<JSONObject> notPublishedNote(@Param("userid") int userid);

//  修改草稿箱中笔记内容 and 发表
    @UpdateProvider(type = BoxNoteProvider.class, method = "changeBox")
    int changeBoxNote(Note note);

    class BoxNoteProvider {
        public String changeBox(Note note) {
            return new SQL(){{
                UPDATE("note");
                if(note.getTitle()!=null){
                    SET("title=#{title}");
                }
                if(note.getBookName()!=null){
                    SET("bookname=#{bookName}");
                }
                if(note.getContent()!=null){
                    SET("content=#{content}");
                }
                if(note.getIdea()!=null){
                    SET("idea=#{idea}");
                }
                if(note.getPublishTime()!=null){
                    SET("publishtime=#{publishTime}");
                }
                WHERE("noteid=#{noteId}");

            }}.toString();
        }
    }

//  删除笔记（已发表or草稿箱）
    @Delete("DELETE FROM note WHERE noteid=#{noteid}")
    int deleteNote(@Param("noteid") int noteid);

//  查看用户收藏的笔记
    @Select("select n.noteid,title,bookname,publishtime,visitnum from note as n join collect as c where n.noteid=c.noteid "+
            "and c.userid=#{userid} ORDER BY collecttime desc LIMIT 10 offset ${(page-1)*10}")
    List<JSONObject> selectCollectNote(@Param("userid") int userid, @Param("page") int page);

    @Select("select count(*) from note as n join collect as c where n.noteid=c.noteid and c.userid=#{userid}")
    int countCollectNote(@Param("userid") int userid);

}
