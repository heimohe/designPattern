package com.snmpdis;


public class TreeNode {
	
	private float id;
	private float pId;
	private String name;
	private boolean open;
	private String icon;
	public TreeNode(){
		
	}
	
	
	public TreeNode(float id, float pId, String name, String icon) {
		super();
		this.id = id;
		this.pId = pId;
		this.name = name;
		this.icon = icon;
	}


	public TreeNode(float id, float pId, String name, boolean open, String icon) {
		super();
		this.id = id;
		this.pId = pId;
		this.name = name;
		this.open = open;
		this.icon = icon;
	}


	public float getId() {
		return id;
	}
	public void setId(float id) {
		this.id = id;
	}
	public float getpId() {
		return pId;
	}
	public void setpId(float pId) {
		this.pId = pId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isOpen() {
		return open;
	}
	public void setOpen(boolean open) {
		this.open = open;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	
	
}