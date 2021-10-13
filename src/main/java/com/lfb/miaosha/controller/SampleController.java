package com.lfb.miaosha.controller;


import com.lfb.miaosha.domain.User;
import com.lfb.miaosha.redis.RedisConfig;
import com.lfb.miaosha.redis.RedisService;
import com.lfb.miaosha.redis.UserKey;
import com.lfb.miaosha.result.CodeMsg;
import com.lfb.miaosha.result.Result;
import com.lfb.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/demo")
public class SampleController {

    @Autowired
    UserService userService;
    @Autowired
    RedisService redisService;

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "hello world 你好 lifeibiao";
    }

    //rest api json输出；
    //页面

    @RequestMapping("/hello")
    @ResponseBody
    Result<String> hello() {
        return Result.success("hello.lifeibiao");
//        return new Result(0, "success", "hello,lifeibiao");
    }

    @RequestMapping("/helloError")
    @ResponseBody
    Result helloError() {
        return Result.error(CodeMsg.SERVER_ERROR);
    }

    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model) {
        model.addAttribute("name", "lifeibiao");
        return "hello";
    }


    @RequestMapping("/db/get")
    @ResponseBody
    public Result<User> dbget() {
        User user = userService.getById(1);
        return Result.success(user);
    }
//   数据库事务
    @RequestMapping("/db/tx")
    @ResponseBody
    public Result<Boolean> dbTx() {
        userService.tx();
        return Result.success(true);
    }

    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<User> redisGet() {
        User user = redisService.get(UserKey.getById,""+1, User.class);
        return Result.success(user);
    }

    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet() {
        User user = new User();
        user.setId(1);
        user.setName("11111");
        boolean ret = redisService.set(UserKey.getById, ""+1,user);//"UserKey:id1"
        return Result.success(ret);
    }

}

