package com.example.demo.repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Inquiry;

@Repository
public class InquiryDaoImpl implements InquiryDao{
	
	private final JdbcTemplate jdbcTemplate;
	
	// ↓DIコンテナで作成されtJDBCのインスタンスを、デフォルトコンストラクタに引数として設定。これでDBの操作が可能になる。
	@Autowired
	public InquiryDaoImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/* 
	 * entityフォルダの中のファイル(Inquiry.javaなど)はDTO(Data Transfer Object)である。
	 * DTOは、DBのようにデータ格納するフィールド群とアクセスメソッド(getter,setter)で構成されている。
	 * ＝＝＝DAOはDBからデータを取り出したり、DTOにデータを格納処理をする。＝＝＝
	 * 実際のinquiryテーブルの定義はschema.spl内で行っている。
	 */
	@Override
	public void insertInquiry(Inquiry inquiry) {
		jdbcTemplate.update("INSERT INTO inquiry(name, email, contents, created) VALUES(?, ?, ?, ?)",
				inquiry.getName(), inquiry.getEmail(),inquiry.getContents(),inquiry.getCreated());
	}
	/* 
	 * sqlインジェクション(悪意のある攻撃)に対処するためにprepared statementでsqlを作成する。
	 * 編集できる項目をあらかじめ設定し、制限するイメージ。また事前にコンパイルが行われるため高速化も期待できる。
	 */
	
//  This method is used in the latter chapter
//	@Override
//	public int updateInquiry(Inquiry inquiry) {
//		return jdbcTemplate.update("UPDATE inquiry SET name = ?, email = ?,contents = ? WHERE id = ?",
//				 inquiry.getName(), inquiry.getEmail(), inquiry.getContents(), inquiry.getId() );	
//	}

	@Override
	public List<Inquiry> getAll() {
		
		String sql = "SELECT id, name, email, contents, created FROM inquiry";
		
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql);
		/* ↑ Mapの値は全てobject型で取得している。Inquiryクラスの配列
		 * ↓ ArrayList(いろんな型を格納できる可変式の配列)へ、Inquiry型に変換して格納していく。
		 */
		List<Inquiry> list = new ArrayList<Inquiry>();
		/* その際、Inquiryクラスに対して、各フィールドの値の型を指定していく(キャスト,型変換)。この場合はダウンキャスト。
		 * Mapのままでも処理はできるが、オブジェクト指向に則る場合、Inquiryクラス(レコードやテーブルといった"意味・物")に変換する方が好ましい
		 */
		for(Map<String, Object> result : resultList) {
			Inquiry inquiry = new Inquiry();
			inquiry.setId((int)result.get("id"));
			inquiry.setName((String)result.get("name"));
			inquiry.setEmail((String)result.get("email"));
			inquiry.setContents((String)result.get("contents"));
			inquiry.setCreated(((Timestamp)result.get("created")).toLocalDateTime());
			list.add(inquiry);
		}
		return list;
	}
	
}
