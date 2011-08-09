package com.juduowang.yongbaota.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * JdwUser entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "jdw_user", catalog = "yongbaota")
public class JdwUser implements java.io.Serializable {

	// Fields

	private Integer id;
	private String name;
	private String password;
	private String email;
	private String otherEmail1;
	private String otherEmail2;
	private String phoneNum;
	private String telPhoneNum1;
	private String telPhoneNum2;
	private String mobilePhoneNum1;
	private String mobilePhoneNum2;
	private String state;
	private String sex;
	private String imageUrl;

	// Constructors

	/** default constructor */
	public JdwUser() {
	}

	/** full constructor */
	public JdwUser(String name, String password, String email,
			String otherEmail1, String otherEmail2, String phoneNum,
			String telPhoneNum1, String telPhoneNum2, String mobilePhoneNum1,
			String mobilePhoneNum2, String state, String sex, String imageUrl) {
		this.name = name;
		this.password = password;
		this.email = email;
		this.otherEmail1 = otherEmail1;
		this.otherEmail2 = otherEmail2;
		this.phoneNum = phoneNum;
		this.telPhoneNum1 = telPhoneNum1;
		this.telPhoneNum2 = telPhoneNum2;
		this.mobilePhoneNum1 = mobilePhoneNum1;
		this.mobilePhoneNum2 = mobilePhoneNum2;
		this.state = state;
		this.sex = sex;
		this.imageUrl = imageUrl;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "name", length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "password", length = 50)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "email", length = 50)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "other_email1", length = 50)
	public String getOtherEmail1() {
		return this.otherEmail1;
	}

	public void setOtherEmail1(String otherEmail1) {
		this.otherEmail1 = otherEmail1;
	}

	@Column(name = "other_email2", length = 50)
	public String getOtherEmail2() {
		return this.otherEmail2;
	}

	public void setOtherEmail2(String otherEmail2) {
		this.otherEmail2 = otherEmail2;
	}

	@Column(name = "phone_num", length = 50)
	public String getPhoneNum() {
		return this.phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	@Column(name = "tel_phone_num1", length = 50)
	public String getTelPhoneNum1() {
		return this.telPhoneNum1;
	}

	public void setTelPhoneNum1(String telPhoneNum1) {
		this.telPhoneNum1 = telPhoneNum1;
	}

	@Column(name = "tel_phone_num2", length = 50)
	public String getTelPhoneNum2() {
		return this.telPhoneNum2;
	}

	public void setTelPhoneNum2(String telPhoneNum2) {
		this.telPhoneNum2 = telPhoneNum2;
	}

	@Column(name = "mobile_phone_num1", length = 50)
	public String getMobilePhoneNum1() {
		return this.mobilePhoneNum1;
	}

	public void setMobilePhoneNum1(String mobilePhoneNum1) {
		this.mobilePhoneNum1 = mobilePhoneNum1;
	}

	@Column(name = "mobile_phone_num2", length = 50)
	public String getMobilePhoneNum2() {
		return this.mobilePhoneNum2;
	}

	public void setMobilePhoneNum2(String mobilePhoneNum2) {
		this.mobilePhoneNum2 = mobilePhoneNum2;
	}

	@Column(name = "state", length = 1)
	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Column(name = "sex", length = 1)
	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@Column(name = "image_url", length = 100)
	public String getImageUrl() {
		return this.imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

}