package kr.or.demo.controller;

import java.text.MessageFormat;

import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


import kr.or.demo.model.Product;
import lombok.RequiredArgsConstructor;

@Controller @RequiredArgsConstructor
public class MarketingController {
	private final OpenAiChatModel chatModel;
	
	@GetMapping("/marketing")
	public String getMarketing() {
		
		return "marketing_request"; // 뷰 경로
	}
	
	@PostMapping("/marketing")
	public String postMareting(Product product, Model model) {
		var systemMessage = new SystemMessage(
				""" 
				나는 마케팅 카피라이터 전문가야
				입력된 제품 정보를 기반으로 온라인 쇼핑몰/블로그/홍보 페이지에 사용할 마케팅 문구를 작성해 줘
				작성조건
				1. 소비자의 관심을 끌 수 있도록 첫 문장은 강렬하거나 공감 가는 표현을 사용해
				2. 제품 특징을 자연스럽게 녹여서 장점이 잘 드러나게 작성해
				3. 가격과 구매 링크는 구매를 자극하는 문구화 함께 포하시켜
				-예: "지금 {가격}에 만나 보세요 {구매링크}"
				4. 글자 수는 약 300~500 자로 하고 캐주얼 하지만 설득력 있는 톤으로 작성해
				5. 필요하면 감각적인 이모지도 활용해
				"""
				);
		
		//사용자 질의
		var useMessage = new UserMessage(MessageFormat.format(
				"""
				입력정보
				-제품명 : {0}
				-가격 : {1}
				-구매링크 : {2}
				-제품 특징 : {3}
				""", 
				product.getName(), 
				product.getPrice(), 
				product.getLink(), 
				product.getFeatures()));
		
		String result = chatModel.call(systemMessage, useMessage);
		model.addAttribute("marketingResult", result);
		
		return "marketing_response";
	}
}
