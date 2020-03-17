package com.example.demo.app.inquiry;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Inquiry;
import com.example.demo.service.InquiryService;

@Controller
@RequestMapping("/inquiry")	
public class InquiryController {
	/* ★マトリョーシカのように入れ子構造になっている。
	 * InquiryControllerはInquiryServiceを用いるのでコンストラクタで用意する必要がある。(本ファイルを参照)
	 * InquiryServiceはInquiryDaoを使うので、コンストラクタで用意する必要がある。(InquiryServiceImpleを参照)
	 * InquiryDaoはJdbcTemplateを使うので、コンストラクタで用意する必要がある。(InquiryDaoImpleを参照)
	 */
	
 	private final InquiryService inquiryService;
	
	// ↓DAOやServiceはインターフェース型でインスタンスされるのが一般的。
	@Autowired
 	public InquiryController(InquiryService inquiryService){
 		this.inquiryService = inquiryService;
 	}
	
	@GetMapping
	public String index(Model model) {
		List<Inquiry> list = inquiryService.getAll();
		model.addAttribute("inquiryList", list);
		model.addAttribute("title", "Inquiry Index");
		
		return "inquiry/index";
	}
	
	@GetMapping("/form")
	public String form(
			InquiryForm inquiryForm,
			Model model,
			@ModelAttribute("complete") String complete
			) {
		/* 
		 * ↑ ModelAttributeはredirectAttributes.addFlashAttribute("complete", "Registered!");で渡した値を受け取るためのもの
		 * 引数にはaddFlashAttributeのキー名を渡す。今回は文字列型でくるのでStringで変数を定義。
		 * 定義した変数はリクエストスコープ(model.addAttribute)で渡す必要はない。
		 */
		model.addAttribute("title", "Inquiry Form");
		// ↓inquiryフォルダの中のform.htmlを見に行くという意味。
		return "inquiry/form";
	}
	
	@PostMapping("/form")
	public String formGoBack(InquiryForm inquiryForm, Model model) {
		model.addAttribute("title", "Inquiry Form");
		return "inquiry/form";
	}
	
	
	@PostMapping("/confirm")
	public String confirm(
			@Validated InquiryForm inquiryForm,
			BindingResult result,
			Model model
			) {
		/*
		 * ＠Validatedをつけることで対象のFormにバリデーションをかけることができる
		 * バリデーションの結果がBindingResultに帰ってくる
		 * バリデーションにエラーがある場合、result.hasErros()はtrueを返す
		 */
		
		if(result.hasErrors()) {
			model.addAttribute("title", "Inquiry Form");
			return "inquiry/form";
		}
		model.addAttribute("title", "Confirm Page");
		return "inquiry/confirm";
	}
	
	@PostMapping("/complete")
	public String complete(
			@Validated InquiryForm inquiryForm,
			BindingResult result,
			Model model,
			RedirectAttributes redirectAttributes
			) {
		/*
		 * ↑今回のcreate処理の際は、2重クリック防止のためにredirectさせる。
		 * redirectする場合はリクエストスコープ(model.addAttribute)の値が再リクエスト時に破棄されてしまう。
		 * 従ってセッションスコープで値を保持しなければならない。
		 * RedirectAttributeはspring Bootのフラッシュスコープという仕組みであり、これはセッションスコープを元にしている。
		 * フラッシュスコープは一度実行されると保持していた値を破棄する。
		*/
		if(result.hasErrors()) {
			model.addAttribute("title", "Inquiry Form");
			return "inquiry/form";
		}
		/*
		 * ↓ FormクラスのinquiryFormから、DTOのinquiryに値を移し替える作業。
		 * Formの内容がテーブル2つにまたがっている場合など(複数DTOに詰め直しが必要)、Formの入力欄とDTOにズレが生じる場合が多々あるので、
		 * 大規模開発では基本的にこのように移し替えるのが一般的。
		 * RailsではModelのFormクラスがになっているような役割。
		 */
		Inquiry inquiry = new Inquiry();
		//↓ IDは自動連番なのでいらない。
		inquiry.setName(inquiryForm.getName());
		inquiry.setEmail(inquiryForm.getEmail());
		inquiry.setContents(inquiryForm.getContents());
		inquiry.setCreated(LocalDateTime.now());
		// ↑作成した時間を取得するメソッドはinquiryFormに存在しないので、JavaのライブラリLocalDateTime.now()を使って自動生成する。
		
		inquiryService.save(inquiry);
		redirectAttributes.addFlashAttribute("complete", "Registered!");
		// 【注意】↓redirectの場合、以下の「inquiry/form」部分はviewファイルではなく、URLのパスを意味している。
		return "redirect:/inquiry/form";
	}
	
}
