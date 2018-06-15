package com.mmall.beans;

import lombok.*;

import java.util.Set;

/**
 * @author Alan Brown
 * @date 2018/5/27 20:56
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mail {

	private String subject; // 主题

	private String message; // 邮件内容

	private Set<String> receivers;   // 接收者

}
