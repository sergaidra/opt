package kr.co.siione.gnrl.goods.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.siione.gnrl.cmmn.service.FileService;
import kr.co.siione.gnrl.goods.service.GoodsService;
import kr.co.siione.mngr.service.CtyManageService;
import kr.co.siione.utl.UserUtils;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
@RequestMapping(value = "/goods/")
public class GoodsController {

	@Resource
    private GoodsService goodsService;
	
	@Resource
	private FileService fileService;
	
	@Resource
	private CtyManageService ctyManageService;
    
    @RequestMapping(value="/category")
    public String category(HttpServletRequest request, HttpServletResponse response, ModelMap model, @RequestParam HashMap param) throws Exception {
    	String category = UserUtils.nvl(param.get("category"));
    	if("".equals(category))
    		category = "S";
    	HashMap map = new HashMap();
    	map.put("upper_cl_code", "00000");
    	List<HashMap> tourList = goodsService.getUpperTourClMain(map);
    	
        model.addAttribute("upperTourClList", tourList);
        
        model.addAttribute("bp", "01");
        if("S".equals(category))
        	model.addAttribute("btitle", "셀프여행");
        else if("H".equals(category))
            model.addAttribute("btitle", "핫딜여행");
        else if("R".equals(category))
            model.addAttribute("btitle", "추천여행");

        model.addAttribute("mtitle", "여행상품리스트");
        model.addAttribute("category", category);

        return "gnrl/goods/category";
    }

    @RequestMapping(value="/list")
    public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model, @RequestParam HashMap param) throws Exception {
    	try {
          	HashMap map = new HashMap();
        	UserUtils.log("[goods_list]param:", param);

        	String category = UserUtils.nvl(param.get("category"));
        	if("".equals(category))
        		category = "S";

    		String hidUpperClCodeNavi = UserUtils.nvl(param.get("hidUpperClCodeNavi"));  // 선택한 여러개의 분류
    		String[] clArr = hidUpperClCodeNavi.split("@");

    		// 상위 분류목록
           	List<String> clList = new ArrayList<String>();
    		for(String str:clArr){
    			if(!str.isEmpty()) clList.add(str);
    		}
    		HashMap mapT = new HashMap();
    		mapT.put("cl_code_arr", clList);
        	System.out.println("[상위 분류목록]map:"+mapT);
        	List<HashMap> upperTourClList = goodsService.getUpperTourClMain(mapT);
    		
        	model.addAttribute("upperTourClList", upperTourClList);
            model.addAttribute("bp", "01");
            if("S".equals(category))
            	model.addAttribute("btitle", "셀프여행");
            else if("H".equals(category))
                model.addAttribute("btitle", "핫딜여행");
            else if("R".equals(category))
                model.addAttribute("btitle", "추천여행");
            model.addAttribute("mtitle", "여행상품리스트");
            model.addAttribute("category", category);
    	} catch(Exception e) {
    		e.printStackTrace();
    	}

        return "gnrl/goods/list";
    }
    
    @RequestMapping(value="/getClInfo")
    public @ResponseBody Map getClInfo(@RequestBody HashMap param) throws Exception {
      	HashMap map = new HashMap();
    	String hidUpperClCode = UserUtils.nvl(param.get("hidUpperClCode"));  // 선택한 여러개의 분류
    	// 상세 분류목록
    	map.put("upper_cl_code", hidUpperClCode);  
    	System.out.println("[상세 분류목록]map:"+map);
    	List<HashMap> tourClList = goodsService.getUpperTourClMain(map);
    	
    	// 도시 목록
    	Map<String, String> mapT2 = new HashMap<String, String>();
    	mapT2.put("NATION_CODE", "00001");
    	List<Map<String,String>> ctyList = ctyManageService.selectCtyList(mapT2);

    	Map resultMap = new HashMap();
    	resultMap.put("tourClList", tourClList);
    	resultMap.put("ctyList", ctyList);
    	return resultMap;
    }

    @RequestMapping(value="/getGoodsList")
    public @ResponseBody List<HashMap> getGoodsList(@RequestBody HashMap param) throws Exception {
      	HashMap map = new HashMap();
		String hidUpperClCode = UserUtils.nvl(param.get("hidUpperClCode")); // 첫번째 선택한 한개의 분류 OR 선택한 한개의 분류
		String hidClCode = UserUtils.nvl(param.get("hidClCode")); // 첫번째 선택한 한개의 분류 OR 선택한 한개의 분류
		String hidCtyCode = UserUtils.nvl(param.get("hidCtyCode")); // 도시
		String hidSortOrd = UserUtils.nvl(param.get("hidSortOrd")); // 정렬기준
		String hidKeyword = UserUtils.nvl(param.get("hidKeyword")); // 검색어

    	map.put("cty_code", hidCtyCode);   
    	map.put("cl_code", hidClCode);   
    	map.put("upper_cl_code", hidUpperClCode);   
    	map.put("sortOrd", hidSortOrd);   
    	map.put("keyword", hidKeyword);   
    	System.out.println("[상품목록]map:"+map);
    	List<HashMap> list = goodsService.getGoodsList(map);

    	return list;
    }

    @RequestMapping(value="/detail")
    public String detail(HttpServletRequest request, HttpServletResponse response, ModelMap model, @RequestParam HashMap param) throws Exception {
    	try {
          	HashMap map = new HashMap();
        	UserUtils.log("[goods_detail]param:", param);
        	
        	String category = UserUtils.nvl(param.get("category"));
        	if("".equals(category))
        		category = "S";

            String goods_code = UserUtils.nvl(param.get("hidGoodsCode"));
        	map.put("goods_code", goods_code);
        	HashMap result = goodsService.getGoodsDetail(map);

        	List<HashMap> lstNmpr = null;
        	List<HashMap> lstRoom = null;
        	List<HashMap> lstEat = null;
        	List<HashMap> lstCheck = null;
        	
        	if(UserUtils.nvl(result.get("CL_SE")).equals("S")) {
        		map.put("setup_se", "R"); // 객실(필수)
        		lstRoom = goodsService.getGoodsNmprBySetupSeList(map);
        		map.put("setup_se", "E"); // 식사
        		lstEat = goodsService.getGoodsNmprBySetupSeList(map);
        		map.put("setup_se", "C"); // 체크인/아웃
        		lstCheck = goodsService.getGoodsNmprBySetupSeList(map);
        		model.addAttribute("lstRoom", lstRoom);
        		model.addAttribute("lstEat", lstEat);
        		model.addAttribute("lstCheck", lstCheck);
        	} else {
        		map.put("setup_se", "P"); // 가격/단가(필수) > 숙박 외
        		lstNmpr = goodsService.getGoodsNmprBySetupSeList(map);
        		model.addAttribute("lstNmpr", lstNmpr);
        	}
        	map.remove("setup_se");
        	
        	List<HashMap> lstSchdul = goodsService.getGoodsSchdulList(map);
        	List<HashMap> lstTime = goodsService.getGoodsTimeList(map);
        	
        	map.put("file_code", result.get("FILE_CODE"));
        	List<HashMap> lstFile = fileService.getFileList(map);
        	
        	List<Map<String, String>> lstVoucher = new ArrayList();
        	List<Map<String, String>> lstOpGuide = new ArrayList();
        	List<Map<String, String>> lstEtcInfo = new ArrayList();

        	// 바우처
        	if(!isEmpty(result, "VOCHR_TICKET_TY")) {
        		Map<String, String> m = new HashMap<String, String>();
        		m.put("text", "티켓형태");
        		String ty = String.valueOf(result.get("VOCHR_TICKET_TY"));
        		if("V".equals(ty))
        			m.put("value", "E-바우처");
        		else if("T".equals(ty))
        			m.put("value", "E-티켓(캡쳐가능)");
        		else if("E".equals(ty))
        			m.put("value", "확정메일(캡쳐가능)");
        		
        		lstVoucher.add(m);
        	}
        	addList(result, "VOCHR_NTSS_REQRE_TIME", lstVoucher, "발권소요시간");
        	addList(result, "VOCHR_USE_MTH", lstVoucher, "사용 방법");
        	addList(result, "GUIDANCE_USE_TIME", lstOpGuide, "이용시간");
        	addList(result, "GUIDANCE_REQRE_TIME", lstOpGuide, "소요시간");
        	addList(result, "GUIDANCE_AGE_DIV", lstOpGuide, "연령구분");
        	addList(result, "GUIDANCE_TOUR_SCHDUL", lstOpGuide, "여행일정");
        	addList(result, "GUIDANCE_PRFPLC_LC", lstOpGuide, "공연장위치");
        	addList(result, "GUIDANCE_EDC_CRSE", lstOpGuide, "교육과정");
        	addList(result, "GUIDANCE_OPTN_MATTER", lstOpGuide, "옵션사항");
        	addList(result, "GUIDANCE_PICKUP", lstOpGuide, "픽업");
        	addList(result, "GUIDANCE_PRPARETG", lstOpGuide, "준비물");
        	addList(result, "GUIDANCE_INCLS_MATTER", lstOpGuide, "포함사항");
        	addList(result, "GUIDANCE_NOT_INCLS_MATTER", lstOpGuide, "불포함사항");
        	addList(result, "ADIT_GUIDANCE", lstEtcInfo, "추가안내");
        	addList(result, "ATENT_MATTER", lstEtcInfo, "유의사항");
        	addList(result, "CHANGE_REFND_REGLTN", lstEtcInfo, "변경/환불규정");
        	
            model.addAttribute("bp", "01");
            if("S".equals(category))
            	model.addAttribute("btitle", "셀프여행");
            else if("H".equals(category))
                model.addAttribute("btitle", "핫딜여행");
            else if("R".equals(category))
                model.addAttribute("btitle", "추천여행");
            model.addAttribute("mtitle", "여행상품리스트");
            model.addAttribute("category", category);
            
            model.addAttribute("goods_code", goods_code);
            model.addAttribute("result", result);
            model.addAttribute("lstSchdul", lstSchdul);
            model.addAttribute("lstTime", lstTime);
            model.addAttribute("lstFile", lstFile);
            model.addAttribute("lstVoucher", lstVoucher);
            model.addAttribute("lstOpGuide", lstOpGuide);
            model.addAttribute("lstEtcInfo", lstEtcInfo);
            model.addAttribute("today", UserUtils.getDate("yyyy-MM-dd"));
            
    	} catch(Exception e) {
    		e.printStackTrace();
    	}

        return "gnrl/goods/detail";
    }
    
    private boolean isEmpty(Map map, String key) {
    	if(!map.containsKey(key) || map.get(key) == null || "".equals(String.valueOf(map.get(key)).trim()))
    		return true;
    	else
    		return false;
    }

    private void addList(Map map, String key, List<Map<String, String>> lst, String text) {
    	if(!isEmpty(map, key)) {
    		Map<String, String> m = new HashMap<String, String>();
    		m.put("text", text);
    		m.put("value", String.valueOf(map.get(key)));
    		lst.add(m);
    	}
    }

    @RequestMapping(value="/list2")
    public String list2(HttpServletRequest request, HttpServletResponse response, ModelMap model, @RequestParam HashMap param) throws Exception {
    	
    	try {
          	HashMap map = new HashMap();
        	UserUtils.log("[goods_list]param:", param);
        	
    		String hidUpperClCodeNavi = UserUtils.nvl(param.get("hidUpperClCodeNavi"));  // 선택한 여러개의 분류
    		String hidUpperClCode = UserUtils.nvl(param.get("hidUpperClCode")); // 첫번째 선택한 한개의 분류 OR 선택한 한개의 분류
    		String hidClCode = UserUtils.nvl(param.get("hidClCode")); // 첫번째 선택한 한개의 분류 OR 선택한 한개의 분류
    		String hidCtyCode = UserUtils.nvl(param.get("hidCtyCode")); // 도시

    		String[] clArr = hidUpperClCodeNavi.split("@");
           	if(clArr != null && hidUpperClCode.equals("")) hidUpperClCode = clArr[0];

    		// 상위 분류목록
           	List<String> clList = new ArrayList<String>();
    		for(String str:clArr){
    			if(!str.isEmpty()) clList.add(str);
    		}
    		HashMap mapT = new HashMap();
    		mapT.put("cl_code_arr", clList);
        	System.out.println("[상위 분류목록]map:"+mapT);
        	List<HashMap> upperTourClList = goodsService.getUpperTourClMain(mapT);
        	
        	// 상세 분류목록
        	map.put("upper_cl_code", hidUpperClCode);  
        	System.out.println("[상세 분류목록]map:"+map);
        	List<HashMap> tourClList = goodsService.getUpperTourClMain(map);
        	
        	// 도시 목록
        	Map<String, String> mapT2 = new HashMap<String, String>();
        	mapT2.put("NATION_CODE", "00001");
        	List<Map<String,String>> ctyList = ctyManageService.selectCtyList(mapT2);

        	/********************* 페이징 start ****************************************/ 
            //현재 페이지 파라메타
            String hidPage = UserUtils.nvl(request.getParameter("hidPage"));
            int intPage = 1;
    		if(!hidPage.equals(""))		
    			intPage = Integer.parseInt((String)hidPage);
    		
    		//페이지 기본설정
    		int pageBlock = 6;
    		int pageArea = 10;
        	
        	PaginationInfo paginationInfo = new PaginationInfo();
    		paginationInfo.setCurrentPageNo(intPage);
    		paginationInfo.setRecordCountPerPage(pageBlock);
    		paginationInfo.setPageSize(pageArea);

        	map.put("startRow", paginationInfo.getFirstRecordIndex());
        	map.put("endRow", paginationInfo.getLastRecordIndex());
        	/********************* 페이징 end ******************************************/

    		// 상품목록
        	map.put("cty_code", hidCtyCode);   
        	map.put("cl_code", hidClCode);   
        	System.out.println("[상품목록]map:"+map);
        	List<HashMap> list = goodsService.getGoodsList(map);

        	/********************* 페이징(2) start ****************************************/ 
    		if(list.size() > 0){
    			int list_cnt = Integer.parseInt(list.get(0).get("TOT_CNT").toString());
    			paginationInfo.setTotalRecordCount(list_cnt);
    		}
            model.addAttribute("paginationInfo", paginationInfo);    		
        	/********************* 페이징(2) end ******************************************/    		

            model.addAttribute("goods_list_yn", "Y");
            
            model.addAttribute("hidPage", hidPage);
            model.addAttribute("hidCtyCode", hidCtyCode);
            model.addAttribute("hidClCode", hidClCode);
            model.addAttribute("hidUpperClCode", hidUpperClCode);
            model.addAttribute("hidUpperClCodeNavi", hidUpperClCodeNavi);

            model.addAttribute("upperTourClList", upperTourClList);
            model.addAttribute("tourClList", tourClList);
            model.addAttribute("ctyList", ctyList);
            model.addAttribute("goodsList", list);    		
    	} catch(Exception e) {
    		e.printStackTrace();
    	}

        return "gnrl/goods/list";
    }


    @RequestMapping(value="/detail2")
    public String detail2(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

    	try{
    	
        String goods_code = UserUtils.nvl(request.getParameter("hidGoodsCode"));
        String hidUpperClCodeNavi = UserUtils.nvl(request.getParameter("hidUpperClCodeNavi"));
        String hidUpperClCode = UserUtils.nvl(request.getParameter("hidUpperClCode"));
        String hidClCode = UserUtils.nvl(request.getParameter("hidClCode"));
        String hidPage = UserUtils.nvl(request.getParameter("hidPage"));

    	HashMap map = new HashMap();
    	map.put("goods_code", goods_code);
    	HashMap result = goodsService.getGoodsDetail(map);
    	
    	HashMap mapT = new HashMap();
    	mapT.put("goods_code", goods_code);
    	List<HashMap> nmprList = null;
    	List<HashMap> roomList = null;
    	List<HashMap> eatList = null;
    	List<HashMap> checkList = null;
    	
    	if(UserUtils.nvl(result.get("CL_SE")).equals("S")) { // > 숙박
    		mapT.put("setup_se", "R"); // 객실(필수)
    		roomList = goodsService.getGoodsNmprBySetupSeList(mapT);
    		mapT.put("setup_se", "E"); // 식사
    		eatList = goodsService.getGoodsNmprBySetupSeList(mapT);
    		mapT.put("setup_se", "C"); // 체크인/아웃
    		checkList = goodsService.getGoodsNmprBySetupSeList(mapT);
    	} else {
    		mapT.put("setup_se", "P"); // 가격/단가(필수) > 숙박 외
    		nmprList = goodsService.getGoodsNmprBySetupSeList(mapT);
    	}
    	
   	
    	List<HashMap> schdulList = goodsService.getGoodsSchdulList(map);
    	List<HashMap> timeList = goodsService.getGoodsTimeList(map);
    	
    	map.put("file_code", result.get("FILE_CODE"));
    	List<HashMap> fileList = fileService.getFileList(map);
    	
    	model.addAttribute("goods_detail_yn", "Y");

        model.addAttribute("hidPage", hidPage);
        model.addAttribute("hidUpperClCodeNavi", hidUpperClCodeNavi);
        model.addAttribute("hidUpperClCode", hidUpperClCode);
        model.addAttribute("hidClCode", hidClCode);
        model.addAttribute("hidGoodsCode", goods_code);
        model.addAttribute("goods_code", goods_code);

        model.addAttribute("result", result);
        model.addAttribute("schdulList", schdulList);
        model.addAttribute("timeList", timeList);
        model.addAttribute("fileList", fileList);
        
        if(UserUtils.nvl(result.get("CL_SE")).equals("S")) {
        	model.addAttribute("roomList", roomList);
        	model.addAttribute("eatList", eatList);
        	model.addAttribute("checkList", checkList);
        } else {
        	model.addAttribute("nmprList", nmprList);
        }
        
        model.addAttribute("today", UserUtils.getDate("yyyy-MM-dd"));

    	} catch (Exception e) {e.printStackTrace();}
        
        return "gnrl/goods/detail";
    }
 
}
