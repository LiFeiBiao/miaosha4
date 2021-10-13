package com.lfb.miaosha.controller;

import com.lfb.miaosha.result.CodeMsg;
import com.lfb.miaosha.result.Result;
import com.lfb.miaosha.service.MiaoshaUserService;
import com.lfb.miaosha.utils.ValidatorUtil;
import com.lfb.miaosha.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/login")
public class LoginController {

    private static Logger log = LoggerFactory.getLogger(LoginController.class);
    //页面
    @Autowired
    MiaoshaUserService userService;

    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";

    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<String> doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {
        log.info(loginVo.toString());
        //参数校验
//        String passInput = loginVo.getPassword();
//        String mobile = loginVo.getMobile();
//        if (StringUtils.isEmpty(passInput)){
//            return Result.error(CodeMsg.PASSWORD_EMPTY);
//        }
//        if (StringUtils.isEmpty(mobile)){
//            return Result.error(CodeMsg.MOBILE_EMPTY);
//        }
//        if (!ValidatorUtil.isMobile(mobile)){
//            return Result.error(CodeMsg.MOBILE_ERROR);
//        }
        //登录
//        CodeMsg cm = userService.login(loginVo);
//        if (cm.getCode() == 0){
//            return Result.success(true);
//        }else{
//            return Result.error(cm);
//        }
        String token = userService.login(response, loginVo);
        return Result.success(token);
    }
}


