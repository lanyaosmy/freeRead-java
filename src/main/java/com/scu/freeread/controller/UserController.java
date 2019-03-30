package com.scu.freeread.controller;

import com.alibaba.fastjson.JSONObject;
import com.scu.freeread.entity.JsonResult;
import com.scu.freeread.entity.User;
import com.scu.freeread.service.MailService;
import com.scu.freeread.service.UserService;
import com.scu.freeread.utils.RSAUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.net.InetAddress;
import java.util.HashMap;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    private String publicKey;
    private int judgeCode;


    @RequestMapping(value="/showuser/{userid}",method = RequestMethod.GET)
    public JsonResult<JSONObject> selectUser (@PathVariable int userid){
        JSONObject json=userService.selectUser(userid);
        JsonResult<JSONObject> result = new JsonResult<>();
        if(json.containsKey("error")){
            result.setCode("1");
            result.setMsg(json.getString("error"));
        }else{
            result.setData(json);
        }
        return result;
    }

    @RequestMapping(value="/getKey",method = RequestMethod.GET)
    public JsonResult<JSONObject> getPublicKey(){
        publicKey = RSAUtils.generateBase64PublicKey();
        JSONObject json=new JSONObject();
        json.put("key",publicKey);
        JsonResult<JSONObject> result = new JsonResult<>();
        result.setData(json);
        return result;
    }

    @RequestMapping(value="/login",method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    public JsonResult<JSONObject> userLogin (@RequestBody JSONObject data, HttpSession session) {
        String useremail = RSAUtils.decryptBase64(data.getString("email"));
        String password = RSAUtils.decryptBase64(data.getString("password"));
        JSONObject json=userService.userLogin(useremail,password);
        JsonResult<JSONObject> result = new JsonResult<>();
        if(json.containsKey("error")){
            result.setCode("1");
            result.setMsg(json.getString("error"));
        }else{
            session.setAttribute("userid",json.getInteger("userid"));
            session.setAttribute("key",publicKey);
            result.setData(json);
        }
        return result;
    }

    @RequestMapping(value="/registermail",method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    public JsonResult<JSONObject> userRegisterMail (@RequestBody JSONObject data, HttpSession session) {
        String useremail = RSAUtils.decryptBase64(data.getString("email"));
        judgeCode=(int)Math.ceil(1000+Math.random()*8999);
        JSONObject json=mailService.sendMail("Free&Read验证码","您的验证码为："+judgeCode,useremail);
        JsonResult<JSONObject> result = new JsonResult<>();
        if(json.containsKey("error")){
            result.setCode("1");
            result.setMsg(json.getString("error"));
        }
        return result;
    }

    @RequestMapping(value="/registerjudge",method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    public JsonResult<JSONObject> userRegisterJudge (@RequestBody JSONObject data, HttpSession session) {
        String useremail = RSAUtils.decryptBase64(data.getString("email"));
        String password = RSAUtils.decryptBase64(data.getString("password"));
        int code = data.getInteger("code");
        JSONObject json=new JSONObject();
        JsonResult<JSONObject> result = new JsonResult<>();
        User user=new User();
        user.setUserEmail(useremail);
        user.setUserPassword(password);
        user.setUserName("一只小鲜肉");
        if(code==judgeCode){
            json=userService.addUser(user);
            if(json.containsKey("error")){
                result.setCode("1");
                result.setMsg(json.getString("error"));
            }
        }else{
            result.setCode("1");
            result.setMsg("验证码不匹配！");
        }

        return result;
    }

    @RequestMapping(value="/alluser",method = RequestMethod.GET)
    public JsonResult<HashMap<String,Object>> selectAllUser (){
        HashMap<String,Object> map=new HashMap<>();
        map.put("info",userService.getAllUsers());
        map.put("other","none");
        JsonResult<HashMap<String,Object>> result = new JsonResult<>(map);
        return result;
    }


    @RequestMapping(value="/deleteuser/{id}",method = RequestMethod.GET)
    public int deleteUser (@PathVariable int id){
        return userService.deleteUser(id);
    }

    @RequestMapping(value="/updateuser",method = RequestMethod.POST)
    public JsonResult<JSONObject> updateUser (@RequestBody User user){
        JSONObject json=userService.updateUser(user);
        JsonResult<JSONObject> result = new JsonResult<>();
        if(json.containsKey("error")){
            result.setCode("1");
            result.setMsg(json.getString("error"));
        }else{
            result.setData(json);
        }
        return result;
    }

    @RequestMapping(value="/followauthor",method = RequestMethod.GET)
    public JsonResult<JSONObject> followAuthor (@RequestParam int followid,HttpSession session){
        int userid= (int) session.getAttribute("userid");
        JSONObject json=userService.followAuthor(userid,followid);
        JsonResult<JSONObject> result = new JsonResult<>();
        if(json.containsKey("error")){
            result.setCode("1");
            result.setMsg(json.getString("error"));
        }
        return result;
    }

    @RequestMapping(value="/getfollowuserlist",method = RequestMethod.GET)
    public JsonResult<JSONObject> followUserlist (HttpSession session){
        int userid= (int) session.getAttribute("userid");
        JSONObject json=userService.followUserlist(userid);
        JsonResult<JSONObject> result = new JsonResult<>();
        if(json.containsKey("error")){
            result.setCode("1");
            result.setMsg(json.getString("error"));
        }else{
            result.setData(json);
        }
        return result;
    }

    @RequestMapping(value="/unfollowauthor",method = RequestMethod.GET)
    public JsonResult<JSONObject> unFollowUser (@RequestParam int followid,HttpSession session){
        int userid= (int) session.getAttribute("userid");
        JSONObject json=userService.unFollowUser(userid,followid);
        JsonResult<JSONObject> result = new JsonResult<>();
        if(json.containsKey("error")){
            result.setCode("1");
            result.setMsg(json.getString("error"));
        }
        return result;
    }


    @Value("${server.port}")
    //获取主机端口
    private String post;
    //获取本机ip
    private String host;
    //图片存放根路径
    @Value("${file.rootPath}")
    private String rootPath;
    //图片存放根目录下的子目录
    @Value("${file.sonPath}")
    private String sonPath;
    //获取图片链接
    private String imgPath;

    @RequestMapping("/upload")
    @ResponseBody
    public JsonResult<JSONObject> uploadImg(@RequestParam("headimage") MultipartFile file,HttpSession session)  {
        int userid= (int) session.getAttribute("userid");
        JSONObject json=new JSONObject();
        JsonResult<JSONObject> result = new JsonResult<>();
        //返回上传的文件是否为空，即没有选择任何文件，或者所选文件没有内容。
        //防止上传空文件导致奔溃
        if (file.isEmpty()) {
            result.setCode("1");
            result.setMsg("文件为空");
            return result;
        }

        //获取本机IP
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            System.out.println("get server host Exception e:"+e);
        }

        // 获取文件名
        String fileName = file.getOriginalFilename();
        //logger.info("上传的文件名为：" + fileName);
        // 设置文件上传后的路径
        String filePath = rootPath + sonPath;
        System.out.println("上传的文件路径" + filePath);
        System.out.println("整个图片路径：" + host + ":" + post + sonPath + fileName);
        //创建文件路径
        File dest = new File(filePath + fileName);

        String imgPath = ("http://"+host + ":" + post + "/img/" + fileName).toString();

        // 解决中文问题，liunx下中文路径，图片显示问题
        // fileName = UUID.randomUUID() + suffixName;

        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            //假如文件不存在即重新创建新的文件已防止异常发生
            dest.getParentFile().mkdirs();
        }
        try {
            //transferTo（dest）方法将上传文件写到服务器上指定的文件
            file.transferTo(dest);
            //将链接保存到URL中
            json=userService.setHeadImage(imgPath,userid);
            if(json.containsKey("error")){
                result.setCode("1");
                result.setMsg(json.getString("error"));
            }else{
                result.setData(json);
            }
            return result;
        } catch (Exception e) {
            result.setCode("1");
            result.setMsg("上传失败");
            return result;
        }
    }
}
