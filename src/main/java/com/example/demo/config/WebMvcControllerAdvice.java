package com.example.demo.config;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;

import com.example.demo.service.InquiryNotFoundException;
// 「@ControllerAdviseと付いたクラスには、全てのコントローラーに共通した処理を記述することができる。」
@ControllerAdvice
public class WebMvcControllerAdvice {

	// ↓ 全ての空文字はnullに変換される処理を定義、このおかげで、htmlにデータが渡されなくてもエラーが発生しない。
    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        dataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    // == springが吐いたエラーに対する処理 ==
	// DBの値が空だとspringが「org.springframework.dao.EmptyResultDataAccessException」というエラーを投げる。
	@ExceptionHandler(EmptyResultDataAccessException.class)
	public String handleException(EmptyResultDataAccessException e,Model model) {
		model.addAttribute("message", e);
		return "error/CustomPage";
	}

	// === 独自に設定したエラーに対する処理 ===
	// === 全てのコントローラーに対して、共通の例外処理を設定する方法(捕捉範囲は全てのController内) ===
	@ExceptionHandler(InquiryNotFoundException.class)
	public String handleException(InquiryNotFoundException e,Model model) {
		model.addAttribute("message", e);
		return "error/CustomPage";
	}
   
}
