package com.lfb.miaosha.controller;


import com.alibaba.druid.util.StringUtils;
import com.lfb.miaosha.domain.MiaoshaUser;
import com.lfb.miaosha.redis.GoodsKey;
import com.lfb.miaosha.redis.RedisService;
import com.lfb.miaosha.result.Result;
import com.lfb.miaosha.service.GoodsService;
import com.lfb.miaosha.service.MiaoshaUserService;
import com.lfb.miaosha.vo.GoodsDetailVo;
import com.lfb.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SpringWebConstraintValidatorFactory;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


@Controller
@RequestMapping("/goods")
public class GoodsController {
	@Autowired
	MiaoshaUserService userService;
	@Autowired
	RedisService  redisService;
	@Autowired
	GoodsService goodsService;

	@Autowired
	ThymeleafViewResolver thymeleafViewResolver;

	@Autowired
	ApplicationContext applicationContext;

	@RequestMapping(value = "/to_list",produces="text/html")
	@ResponseBody
	public String list(HttpServletRequest request,
					   HttpServletResponse response,
					   Model model,
					   MiaoshaUser user) {
		model.addAttribute("user", user);
		List<GoodsVo> goodsList = goodsService.listGoodsVo();
		model.addAttribute("goodsList", goodsList);
//		return "goods_list";
		//取缓存
		String html = redisService.get(GoodsKey.getGoodsList,"",String.class);
		if(!StringUtils.isEmpty(html)) {
			return html;
		}
		//手动渲染
		SpringWebContext ctx = new SpringWebContext(request,
				response,
				request.getServletContext(),
				request.getLocale(),
				model.asMap(),
				applicationContext);
		//保存到缓存中
		html = thymeleafViewResolver.getTemplateEngine().process("goods_list",ctx);
		if (!StringUtils.isEmpty(html)){
			redisService.set(GoodsKey.getGoodsList,"",html);
		}
		return html;
	}

	//方法优化

	@RequestMapping(value = "/to_detail2/{goodsId}", produces = "text/html")
	@ResponseBody
	public String detail2(Model model,
						 MiaoshaUser user,
						 @PathVariable("goodsId")long goodsId,
						 HttpServletRequest request,
						 HttpServletResponse response) {

		//取缓存
		String html = redisService.get(GoodsKey.getGoodsDetail,""+goodsId,String.class);
		if(!StringUtils.isEmpty(html)) {
			return html;
		}
		model.addAttribute("user", user);
		GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
		model.addAttribute("goods", goods);
		long startAt = goods.getStartDate().getTime();
		long endAt = goods.getEndDate().getTime();
		long now = System.currentTimeMillis();
		//状态
		int miaoshaStatus = 0;
		//还有多少秒开始
		int remianSeconds = 0;

		if (now < startAt){//秒杀还没有开始，倒计时
			miaoshaStatus = 0;
			remianSeconds = (int)((startAt - now)/1000);
		}else if (now > endAt){//秒杀已经结束
			miaoshaStatus = 2;
			remianSeconds = -1;
		}else{//秒杀正在进行中
			miaoshaStatus = 1;
			remianSeconds = 0;
		}

		model.addAttribute("miaoshaStatus", miaoshaStatus);
		model.addAttribute("remainSeconds", remianSeconds);

//		return "goods_detail";

		//手动渲染
		SpringWebContext ctx = new SpringWebContext(request,
				response,
				request.getServletContext(),
				request.getLocale(),
				model.asMap(),
				applicationContext);
		//保存到缓存中
		html = thymeleafViewResolver.getTemplateEngine().process("goods_detail",ctx);
		if (!StringUtils.isEmpty(html)){
			redisService.set(GoodsKey.getGoodsDetail,""+goodsId,html);
		}
		return html;

	}

	@RequestMapping(value = "/detail/{goodsId}")
	@ResponseBody
	public Result<GoodsDetailVo> detail(Model model,
										MiaoshaUser user,
										@PathVariable("goodsId")long goodsId,
										HttpServletRequest request,
										HttpServletResponse response) {


		GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
		long startAt = goods.getStartDate().getTime();
		long endAt = goods.getEndDate().getTime();
		long now = System.currentTimeMillis();
		//状态
		int miaoshaStatus = 0;
		//还有多少秒开始
		int remianSeconds = 0;

		if (now < startAt){//秒杀还没有开始，倒计时
			miaoshaStatus = 0;
			remianSeconds = (int)((startAt - now)/1000);
		}else if (now > endAt){//秒杀已经结束
			miaoshaStatus = 2;
			remianSeconds = -1;
		}else{//秒杀正在进行中
			miaoshaStatus = 1;
			remianSeconds = 0;
		}

		GoodsDetailVo vo = new GoodsDetailVo();
		vo.setGoods(goods);
		vo.setUser(user);
		vo.setRemainSeconds(remianSeconds);
		vo.setMiaoshaStatus(miaoshaStatus);
		return Result.success(vo);
	}
}


