package kr.or.kosa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.image.*;


@Controller
@RequiredArgsConstructor
public class CreateImageController {

 	private final ImageModel imageModel;

  	
	@GetMapping
	public String initialPage() {
		return "index";
	}
	
	@GetMapping("/imgGen")
	@ResponseBody
	public String generateImage(@RequestParam("request") String request) {
		System.out.println("*******************" + request);
		ImageOptions options = ImageOptionsBuilder.builder()
				               .withModel("dall-e-3")
				               .withWidth(1024)
				               .withHeight(1024)
				               .build();
		
		ImagePrompt promt = new ImagePrompt(request,options);
		
		ImageResponse response =  imageModel.call(promt);
		
		String imageUrl = response.getResult().getOutput().getUrl();
		System.out.println("생성된 이미지의 URL : " + imageUrl);
		
		
		return imageUrl;
	}
	

}
