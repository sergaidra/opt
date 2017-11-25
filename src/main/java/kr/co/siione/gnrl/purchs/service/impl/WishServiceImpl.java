package kr.co.siione.gnrl.purchs.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import kr.co.siione.gnrl.purchs.service.PointService;
import kr.co.siione.gnrl.purchs.service.PurchsService;
import kr.co.siione.gnrl.purchs.service.WishService;

@Service("WishService")
public class WishServiceImpl implements WishService {

	@Resource(name = "wishDAO")
	private WishDAO wishDAO;

	public int getWishListCount(HashMap map) throws Exception {
		return wishDAO.getWishListCount(map);
	}
		
    public List<HashMap> getWishList(HashMap map) throws Exception {
        return wishDAO.getWishList(map);
    }

}
