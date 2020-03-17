package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Inquiry;
import com.example.demo.repository.InquiryDao;
// ＝＝＝＝Serviceクラスはビジネスロジックを記述する層なので、railsでいうところのModelに相当する。＝＝＝
//　↓ DIコンテナで自動的にシングルトンとしてインスタン化されて、呼び出し先で利用できるようになる。
@Service
public class InquiryServiceImpl implements InquiryService{
	/* 
	 * ↓DAOやServiceはインターフェース型でインスタンス化されるのが一般的。
	 * 
	 * 　ProgateのスーパークラスvehicleとサブクラスCarとBicycleの例に似てるかも。
	 * 　↑の慣習は、中身の実装が変わることを想定している。つまり、DIコンテナによって作られる実行ファイルのインスタンスがImpleAになるかもしれないし、
	 * 　ImpleBになるかもしれないという状況の中で(Impleという名前ではないかもしれないし、あとで模擬クラスに差し替えるかもしれない)、コンストラクタの引数がAでもBでも問題なく動作するようにスーパークラスを引数としている。
	 * 　Progate JavaコースV 12.多態性
	 * 
	 *  仮にDI(@Autowiredなど)を使用せずに自分でインスタンスを作成すると、this.dao = new InquiryDaoImpl();を記述することになり、
	 *  後々new以降の部分を擬似クラなどに差し替える時にいちいち書き直さないといけなくなる。DIは、そこら辺を良きに計らってくれる。
	 *  また、仮に実行クラスが完成していないと、呼び出したクラスや、されにそれを呼び出すクラスも実行できない(テストもできない)ので、お互いに「依存」してしまっている。
	 *  これを「結合度の高い状態」と呼ぶ。DIはこの依存関係を弱めるために利用される。
	 */
	private final InquiryDao dao;
	
	@Autowired
	public InquiryServiceImpl(InquiryDao dao) {
		this.dao = dao;
	}
	
	@Override
	public void save(Inquiry inquiry) {
		dao.insertInquiry(inquiry);
		// ↑DBからに値を保存する役割はDAOがになっているので、シンプルに一行で書くことができる。
	}

	
//  This method is used in the latter chapter
//	@Override
//	public void update(Inquiry inquiry) {
//		
//		//return dao.updateInquiry(inquiry);
//		if(dao.updateInquiry(inquiry) == 0) {
//			throw new InquiryNotFoundException("can't find the same ID");
//		}
//	}
	
	@Override
	public List<Inquiry> getAll() {
		return dao.getAll();
		// ↑DBからに値を取得する役割はDAOがになっているので、シンプルに一行で書くことができる。
	}
}
