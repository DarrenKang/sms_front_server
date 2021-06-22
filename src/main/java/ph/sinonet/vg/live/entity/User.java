package ph.sinonet.vg.live.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Announcement entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "users")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class User implements java.io.Serializable {


	private String username;
	private String password;
	
	public User() {
	}


	// Property accessors
	@Id
	@Column(name = "username", unique = true)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "password", nullable = false)
	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	@Override
	public String toString() {
		return "Apps [username = " + username + ", password =" + password + "]";
	}
	
	
	
	
}