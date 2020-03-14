package com.example.demo.app;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// ↓Controllerとしての振る舞いを可能にする
@Controller
@RequestMapping("/sample")
public class SampleController {
	
	private final JdbcTemplate jdbcTemplate;
	
	// ↓依存性の注入。DIコンテナというクラスに自動で該当のインスタンスを作成・保持してくれる
	// この場合、コンストラクタの引数になっているJdbcTemplateのインスタンスを呼び出している(要確認)
	@Autowired
	public SampleController(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@GetMapping("/test")
	public String test(Model model) {
		// ↑メソッド名は適当でいい
		// ↓sqlを連結するときは単語の間の半角スペースを忘れないようにする。
		String sql = "SELECT id, name, email "
				+ "FROM inquiry WHERE id = 1";
		Map<String, Object> map = jdbcTemplate.queryForMap(sql); 
		// ↑jdbcに対してsql(string)を渡すとMap型などでデータを返してくれる
		
		// ↓リクエストスコープにデータが格納されサーブレットやJSPでデータを共有できる。
		model.addAttribute("title", "Inquiry Form");
		model.addAttribute("name", map.get("name"));
		model.addAttribute("email", map.get("email"));
		
		// ↓htmlのファイル名をString型で返している
		return "test";
	}

}
