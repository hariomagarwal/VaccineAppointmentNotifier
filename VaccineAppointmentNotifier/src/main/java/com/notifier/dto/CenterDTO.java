package com.notifier.dto;

import java.util.List;

public class CenterDTO {

	private Integer center_id; 
	private String name;
	private String address;
	private String state_name; 
	private String district_name;  
	private String block_name;   
	private Integer pincode;
	private String fee_type; 
	private List<SessionDTO> sessions;
	public Integer getCenter_id() {
		return center_id;
	}
	public void setCenter_id(Integer center_id) {
		this.center_id = center_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getState_name() {
		return state_name;
	}
	public void setState_name(String state_name) {
		this.state_name = state_name;
	}
	public String getDistrict_name() {
		return district_name;
	}
	public void setDistrict_name(String district_name) {
		this.district_name = district_name;
	}
	public String getBlock_name() {
		return block_name;
	}
	public void setBlock_name(String block_name) {
		this.block_name = block_name;
	}
	public Integer getPincode() {
		return pincode;
	}
	public void setPincode(Integer pincode) {
		this.pincode = pincode;
	}
	public String getFee_type() {
		return fee_type;
	}
	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}
	public List<SessionDTO> getSessions() {
		return sessions;
	}
	public void setSessions(List<SessionDTO> sessions) {
		this.sessions = sessions;
	}
	@Override
	public String toString() {
		return "CenterDTO [center_id=" + center_id + ", name=" + name + ", address=" + address + ", state_name="
				+ state_name + ", district_name=" + district_name + ", block_name=" + block_name + ", pincode="
				+ pincode + ", fee_type=" + fee_type + ", sessions=" + sessions + "]";
	}
	
	
}
