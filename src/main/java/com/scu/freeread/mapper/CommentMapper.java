package com.scu.freeread.mapper;

import com.alibaba.fastjson.JSONObject;
import com.scu.freeread.entity.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentMapper {
//  查看笔记详情：获取评论列表
    @Select("select senderid,u.username as sendername,receiverid,u1.username as receivername,commentcontent,commenttime,u.headimage,u.userintro from comment join user as u join user as u1"+
            " where senderid=u.userid and receiverid=u1.userid and noteid=#{noteid} order by commenttime desc")
    List<JSONObject> selectNoteComment(@Param("noteid") int noteid);


//  获取当前用户收到的评论
    @Select("select commentcontent,commenttime,username,headimage,title,userintro,n.noteid,senderid from comment as c join user as u join note as n "+
            "where senderid=u.userid and c.noteid = n.noteid and receiverid=#{userid}")
    List<JSONObject> selectUserComment(@Param("userid") int userid);

//  新建评论
    @Insert("INSERT INTO `comment` (`noteid`, `senderid`, `receiverid`, `commentcontent`, `commenttime`) "+
            "VALUES (#{noteId}, #{senderId}, #{receiverId}, #{commentContent}, NOW())")
    int addComment(Comment comment);

//  查询笔记总评论数
    @Select("select count(*) from comment where noteid=#{noteid}")
    int countComment(@Param("noteid") int noteid);
}
