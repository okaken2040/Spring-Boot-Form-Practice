package com.example.demo.service;

// === 開発者による独自の例外を定義する ===
// springが投げる例外とは異なり、例外の定義や、ビジネスロジック内でのインスタンス化など、自分で記述する必要がある。

// RuntimeException配下に来る例外は「非検査例外」と言う。
// RuntimeExceptionクラスを継承することでエラーを処理することができる。
public class InquiryNotFoundException extends RuntimeException{

	/*
	 * シリアルUIDは、インターフェースjava.io.Serializableを実装した場合に必要。
	 *   RuntimeException は Exceptionを拡張しており、Exception は Throwableを拡張しており、
	 *   Throwable は Serializableを実装(実行)している。
	 */
	private static final long serialVersionUID = 1L;

	// ↓デフォルトコンストラクタ
	public InquiryNotFoundException(String message) {
		// ↓ 親クラスにも渡しておく。
		super(message);
	}
	// この例外処理はビジネスロジック上でthrowされる。
	// 今回だとInquiryServiceImpl内でインスタンス化されている。

}