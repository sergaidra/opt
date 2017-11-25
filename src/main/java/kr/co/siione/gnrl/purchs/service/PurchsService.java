package kr.co.siione.gnrl.purchs.service;

import java.util.HashMap;
import java.util.List;

public interface PurchsService {
	public void addPurchs(HashMap map) throws Exception;
	public int getTotalPoint(HashMap map) throws Exception;
	public int getPurchsListCount(HashMap map) throws Exception;
	public List<HashMap> getPurchsList(HashMap map) throws Exception;
	public List<HashMap> selectPurchsDetail(HashMap map) throws Exception;
	public void insertPurchsReview(HashMap map) throws Exception;
	public HashMap selectPurchsReview(HashMap map) throws Exception;
}
