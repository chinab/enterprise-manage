package com.juduowang.yongbaota.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "JDW_USER")
public class JdwUser implements Serializable {

	private Long id;
	private String Name;
	private String password;
	private String email;
	private String otherEmail1;
	private String otherEmail2;
	private String phoneNum;
	private String telPahoneNum1;
	private String telPhoneNum2;
	private String mobilePhoneNum1;
	private String mobilePhoneNum2;
	private String state;
	private String sex;
	private String imageUrl;

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "org.hibernate.id.SequenceGenerator", parameters = @Parameter(name = "sequence", value = "SEQ_JDW_USER"))
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "NAME", length = 50)
	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	@Column(name = "PASSWORD", length = 50)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "EMAIL", length = 50)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "OTHER_EMAIL1", length = 50)
	public String getOtherEmail1() {
		return otherEmail1;
	}

	public void setOtherEmail1(String otherEmail1) {
		this.otherEmail1 = otherEmail1;
	}

	@Column(name = "OTHER_EMAIL2", length = 50)
	public String getOtherEmail2() {
		return otherEmail2;
	}

	public void setOtherEmail2(String otherEmail2) {
		this.otherEmail2 = otherEmail2;
	}

	@Column(name = "PHONE_NUM", length = 50)
	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	@Column(name = "TEL_PHONE_NUM1", length = 50)
	public String getTelPahoneNum1() {
		return telPahoneNum1;
	}

	public void setTelPahoneNum1(String telPahoneNum1) {
		this.telPahoneNum1 = telPahoneNum1;
	}

	@Column(name = "TEL_PHONE_NUM2", length = 50)
	public String getTelPhoneNum2() {
		return telPhoneNum2;
	}

	public void setTelPhoneNum2(String telPhoneNum2) {
		this.telPhoneNum2 = telPhoneNum2;
	}

	@Column(name = "MOBILE_PHONE_NUM1", length = 50)
	public String getMobilePhoneNum1() {
		return mobilePhoneNum1;
	}

	public void setMobilePhoneNum1(String mobilePhoneNum1) {
		this.mobilePhoneNum1 = mobilePhoneNum1;
	}

	@Column(name = "MOBILE_PHONE_NUM2", length = 50)
	public String getMobilePhoneNum2() {
		return mobilePhoneNum2;
	}

	public void setMobilePhoneNum2(String mobilePhoneNum2) {
		this.mobilePhoneNum2 = mobilePhoneNum2;
	}

	@Column(name = "STATE", length = 1)
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Column(name = "SEX", length = 1)
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@Column(name = "IMAGE_URL", length = 150)
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

}
