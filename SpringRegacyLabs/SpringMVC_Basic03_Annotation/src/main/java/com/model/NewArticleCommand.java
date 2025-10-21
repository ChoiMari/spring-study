package com.model;

import lombok.Data;

@Data
public class NewArticleCommand {
	private int parentId;
	private String title;
	private String content;
}
