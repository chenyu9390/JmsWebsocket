package vo;

import java.io.Serializable;

public class User implements Serializable{

	private static final long serialVersionUID = 1L;
	private int userid;
	private String name;

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
