package com.mmall.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author Alan Brown
 * @date 2018/6/12 15:53
 */

@Getter
@Setter
@ToString
public class SearchLogDto {

	private Integer type;

	private String beforeSeg;

	private String afterSeg;

	private String operator;

	private Date fromTime; // yyyy-MM-dd HH:mm:ss

	private Date toTime; // yyyy-MM-dd HH:mm:ss

}
