package com.lfb.miaosha.service;

import java.util.List;

import com.lfb.miaosha.domain.MiaoshaGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lfb.miaosha.dao.GoodsDao;
import com.lfb.miaosha.domain.MiaoshaGoods;
import com.lfb.miaosha.vo.GoodsVo;

@Service
public class GoodsService {
	
	@Autowired(required = false)
	GoodsDao goodsDao;

	public List<GoodsVo> listGoodsVo(){
		return goodsDao.listGoodsVo();
	}
	public GoodsVo getGoodsVoByGoodsId(long goodsId) {
		return goodsDao.getGoodsVoByGoodsId(goodsId);
	}

	public boolean reduceStock(GoodsVo goods) {
		MiaoshaGoods g = new MiaoshaGoods();
		g.setGoodsId(goods.getId());
		int ret = goodsDao.reduceStock(g);
		return ret > 0;
	}
//	public void resetStock(List<GoodsVo> goodsList) {
//		for(GoodsVo goods : goodsList ) {
//			MiaoshaGoods g = new MiaoshaGoods();
//			g.setGoodsId(goods.getId());
//			g.setStockCount(goods.getStockCount());
//			goodsDao.resetStock(g);
//		}
//	}
}
