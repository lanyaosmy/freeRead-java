package com.scu.freeread.mapper;

import com.alibaba.fastjson.JSONObject;
import com.scu.freeread.entity.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {

    @Select("SELECT username,useremail,headimage,userintro FROM user WHERE userid = #{id}")
    JSONObject selectUser(int id);

    @Select("SELECT * FROM user WHERE useremail = #{email}")
    JSONObject userLogin(@Param("email") String email);

    @Select("SELECT * FROM user")
    List<User> getAllUsers();

//    设置头像
    @Update("UPDATE user SET headimage=#{path} WHERE (userid=#{userid})")
    int setHeadImage(@Param("path") String filepath, @Param("userid") int userid);
//    新增用户
    @Insert("INSERT INTO user(username, userpassword, useremail) "+
            "VALUES (#{userName}, #{userPassword}, #{userEmail})")
    @Options(useGeneratedKeys = true, keyColumn = "userid", keyProperty = "userId")
    int addUser(User user);

//  删除用户
    @Delete("DELETE FROM user WHERE userid = #{id}")
    int deleteUser(int id);

//  更新信息
    @UpdateProvider(type = UserProvider.class, method = "changeUserInfo")
    int updateUser(User user);

    class UserProvider {
        public String changeUserInfo(User user) {
            return new SQL(){{
                UPDATE("user");
                if(user.getUserName()!=null){
                    SET("username=#{userName}");
                }
                if(user.getHeadImage()!=null){
                    SET("headimage=#{headImage}");
                }
                if(user.getUserPassword()!=null){
                    SET("userpassword=#{userPassword}");
                }
                if(user.getUserIntro()!=null){
                    SET("userintro=#{userIntro}");
                }
                WHERE("userid = #{userId}");

            }}.toString();
        }
    }

//  关注动态的用户
    @Select("select distinct u.userid,username,headimage,userintro from note as n join notetag as t join user as u where n.noteid=t.noteid "+
            "and n.userid=u.userid and t.tagid in (select tagid from followtag where userid=#{userid}) order by publishtime desc LIMIT 10 offset ${(page-1)*10}")
    List<User> selectNewUser(@Param("userid") int userid, @Param("page") int page);

//  热门笔记的用户
    @Select("SELECT u.userid,username,headimage,userintro FROM note as n join user as u where n.userid=u.userid ORDER BY visitnum desc LIMIT 10 offset ${(page-1)*10}")
    List<User> selectHotUser(@Param("page") int page);

//  获取笔记详情：根据笔记id获取作者信息
    @Select("select u.userid,username,headimage,userintro from note as n join user as u where n.userid=u.userid and noteid=#{noteid}")
    User selectAuthor(@Param("noteid") int noteid);

//  关注作者
    @Insert("INSERT INTO followuser(userid, followid) VALUES (#{userid}, #{followid})")
    int followAuthor(@Param("userid") int userid, @Param("followid") int followid);

/**
 *个人中心
 */

//  查看用户关注的作者列表
    @Select("select u.userid,username,headimage,userintro from user as u join followuser as f where u.userid=f.followid and f.userid=#{userid}")
    List<JSONObject> followUserlist(@Param("userid") int userid);

//  取消关注
    @Delete("DELETE FROM followuser WHERE userid = #{userid} AND followid = #{followid}")
    int cancelFollow(@Param("userid") int userid, @Param("followid") int followid);
}
