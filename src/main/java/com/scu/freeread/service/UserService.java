package com.scu.freeread.service;

import com.alibaba.fastjson.JSONObject;
import com.scu.freeread.entity.User;
import com.scu.freeread.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    //图片存放根路径
    @Value("${file.rootPath}")
    private String ROOT_PATH;
    //图片存放根目录下的子目录
    @Value("${file.sonPath}")
    private String SON_PATH;

    @Value("${server.port}")
    //获取主机端口
    private String POST;

    @Autowired
    UserMapper userMapper;


//  根据userid获取用户信息
    public JSONObject selectUser(int id){
        JSONObject json=new JSONObject();
        try {
            JSONObject list = userMapper.selectUser(id);
            json.put("userinfo",list);
            return json;
        }catch (Exception e){
            JSONObject error=new JSONObject();
            error.put("error","数据请求出错");
            return error;
        }
    }

//  用户登录
    public JSONObject userLogin(String email,String password){
        JSONObject error=new JSONObject();
        try {
            JSONObject json = userMapper.userLogin(email);
            if(!json.containsKey("useremail")){
                error.put("error","登录邮箱错误");
                return error;
            }
            if(!json.getString("userpassword").equals(password)){
                error.put("error","密码错误");
                return error;
            }
            json.remove("userpassword");
            return json;
        }catch (Exception e){
            error.put("error","登录失败");
            return error;
        }
    }



    public List<User> getAllUsers(){
        return userMapper.getAllUsers();
    }

    public JSONObject addUser(User user){
        JSONObject error=new JSONObject();
        try {
            if(userMapper.addUser(user)==1) {
                return error;
            }
            error.put("error","注册用户失败");
            return error;
        }catch (Exception e){
            error.put("error","注册用户失败");
            return error;
        }
    }

    public int deleteUser(int id){
        return userMapper.deleteUser(id);
    }

//  更新用户信息
    public JSONObject updateUser(User user){
        JSONObject error=new JSONObject();
        try {
            if(userMapper.updateUser(user)==1) {
                return error;
            }
            error.put("error","更新用户信息失败");
            return error;
        }catch (Exception e){
            error.put("error","更新用户信息失败");
            return error;
        }
    }

    //  设置用户头像
    public JSONObject setHeadImage(String filepath, int userid){
        JSONObject error=new JSONObject();
        try {
            if(userMapper.setHeadImage(filepath,userid)==1) {
                return error;
            }
            error.put("error","更新用户头像失败");
            return error;
        }catch (Exception e){
            error.put("error","更新用户头像失败");
            return error;
        }
    }




    public List<User> selectNewUser(int userid, int page){
        return userMapper.selectNewUser(userid,page);
    }

    public List<User> selectHotUser(int page){
        return userMapper.selectHotUser(page);
    }

//  关注作者
    public JSONObject followAuthor(int userid, int followid){
        JSONObject error=new JSONObject();
        try {
            if(userMapper.followAuthor(userid,followid)==1) {
                return error;
            }
            error.put("error","关注作者失败");
            return error;
        }catch (Exception e){
            error.put("error","关注作者失败");
            return error;
        }
    }

    //  查看用户关注的作者列表
    public JSONObject followUserlist(int userid){
        JSONObject json=new JSONObject();
        try {
            List<JSONObject> list = userMapper.followUserlist(userid);
            json.put("userlist",list);
            return json;
        }catch (Exception e){
            JSONObject error=new JSONObject();
            error.put("error","数据请求出错");
            return error;
        }
    }

//  取消关注作者
    public JSONObject unFollowUser(int userid, int followid){
        JSONObject error=new JSONObject();
        try {
            if(userMapper.cancelFollow(userid,followid)==1) {
                return error;
            }
            error.put("error","取消关注作者失败");
            return error;
        }catch (Exception e){
            error.put("error","取消关注作者失败");
            return error;
        }
    }

}
