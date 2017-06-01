package kr.co.siione.gnrl.cmmn.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.siione.gnrl.cmmn.service.ExcelService;

@Service("ExcelService")
public class ExcelServiceImpl implements ExcelService {

	@Resource(name = "excelDAO")
	private ExcelDAO excelDAO;

    public List excelContents(HashMap map) throws Exception {
        return excelDAO.selectExcelContents(map);
    }
}
