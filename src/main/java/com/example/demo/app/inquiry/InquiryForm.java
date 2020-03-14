package com.example.demo.app.inquiry;



import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class InquiryForm{
	
	// ↓ sourceタブ→「Generate Constructor using Fields 」でコンストラクタが自動生成される
	public InquiryForm() {};

    public InquiryForm(String name, String email, String contents) {
		super();
		this.name = name;
		this.email = email;
		this.contents = contents;
	}



	@Size(min = 1, max = 20, message="Please input 20 characters or less")
    private String name;
    
    @NotNull
    @Email(message = "Invalid E-mail Format")
    private String email;

    // ↓ WebMvcControllerAdviceで空文字をnullにしているので使えている。
    @NotNull
    private String contents;

    // ↓sourceタブ→「Generate Getters and Setters」で自動でゲッター・セッターが作成される。
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}


}