package com.scu.freeread.mapper;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagMapper {

//  获取一篇笔记的标签
    @Select("SELECT tagname FROM tag as t join notetag as n WHERE t.tagid=n.tagid AND noteid = #{noteid}")
    List<String> selectTag(@Param("noteid") int noteid);

//  添加笔记的标签
    @Insert("INSERT INTO notetag (noteid, tagid) VALUES (#{noteid}, #{tagid})")
    int addNoteTag(@Param("tagid") int tagid, @Param("noteid") int noteid);

/**
 * 用户中心
 */

//  查看用户关注的标签列表
    @Select("select t.tagid,tagname from followtag as f join tag as t where userid=#{userid} and f.tagid=t.tagid")
    List<JSONObject> userTaglist(@Param("userid") int userid);

//  用户关注标签
    @Insert("INSERT INTO followtag(userid, tagid) VALUES (#{userid}, #{tagid})")
    int followTag(@Param("userid") int userid, @Param("tagid") int tagid);

//  用户取消关注标签
    @Delete("DELETE FROM followtag WHERE (userid=#{userid}) AND (tagid=#{tagid})")
    int unfollowTag(@Param("userid") int userid, @Param("tagid") int tagid);

//  查询标签
    @Select("select * from tag")
    List<JSONObject> searchTag(@Param("keyword") String keyword);

    @Select("select * from tag where tagid not in (select t.tagid from followtag as f join tag as t on f.tagid=t.tagid where userid=#{userid})")
    List<JSONObject> userNotTag(@Param("userid") int userid);
}
