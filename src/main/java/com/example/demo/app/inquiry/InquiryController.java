package com.example.demo.app.inquiry;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/inquiry")	
public class InquiryController {
	
// 	private final InquiryServiceImpl inquiryService;
	
	//Add an annotation here 
// 	public InquiryController(InquiryServiceImpl inquiryService){
// 		this.inquiryService = inquiryService;
// 	}
	
	@GetMapping
	public String index(Model model) {
		
		//hands-on
		
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
		redirectAttributes.addFlashAttribute("complete", "Registered!");
		// 【注意】↓redirectの場合、以下の「inquiry/form」部分はviewファイルではなく、URLのパスを意味している。
		return "redirect:/inquiry/form";
	}
	
}
