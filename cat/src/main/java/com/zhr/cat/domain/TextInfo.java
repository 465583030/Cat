package com.zhr.cat.domain;

/**
 * @author 赵浩燃
 * 聊天消息类
 */
public class TextInfo {
	/**
	 * 信息来源 0表示来自机器人   1表示来自客户
	 */
	private int type;
	/**
	 * 聊天内容
	 */
	private String content;
	/**
	 * 当前聊天内容的时间
	 */
	private long time;

	public TextInfo(int type, String content, long time) {
		this.type = type;
		this.content = content;
		this.time = time;
	}

	public TextInfo() {
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public String toString() {
		String from ;
		if(type==0)
		{
			from=new String("机器人");
		}
		else
		{
			from=new String("客户");
		}
		return "聊天信息 [来自:" + from + ", 内容是:" + content + ", 时间是:" + time + "]";
	}
	
}
