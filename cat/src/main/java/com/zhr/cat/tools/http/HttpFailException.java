package com.zhr.cat.tools.http;


public class HttpFailException extends Exception {
	private static final long serialVersionUID = 4221854073649050758L;

	private long executeTime;
	private HttpCode code;

	public HttpFailException(String msg) {
		super(msg);
		this.code=new HttpCode(-100);
	}

	public HttpFailException(int code, long executeTime) {
		this.code=new HttpCode(code);
		this.executeTime = executeTime;
	}

	public HttpFailException(int code, long executeTime, Throwable e) {
		this.code=new HttpCode(code);
		this.executeTime = executeTime;
		this.setStackTrace(e.getStackTrace());
	}

	public long getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(long executeTime) {
		this.executeTime = executeTime;
	}

	public HttpCode getCode() {
		return code;
	}
}
