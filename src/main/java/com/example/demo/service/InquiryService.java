package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Inquiry;
// ＝＝＝Serviceクラスはビジネスロジックを記述する層なので、railsでいうところのModelに相当する。＝＝＝
public interface InquiryService {
	
	void save(Inquiry inquiry);

//  updateInquiryで帰ってくるintの値は実行クラスの中で処理するのでvoidで大丈夫。
	void update(Inquiry inquiry);
	
	List<Inquiry> getAll();

}