package Client;

import java.awt.Color;

import javax.swing.ImageIcon;

public class ContactCard {

	private ImageIcon avatar;
	private String status;
	private String name;
	private Color color1;
	private Color color2;

	public ContactCard(ImageIcon avatar, String status, String name) {
		this.avatar = avatar;
		this.status = status;
		this.name = name;
		this.color1 = Color.gray;
		this.color2 = Color.gray.darker();
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String newStatus) {
		this.status = newStatus;
	}

	public String getName() {
		return this.name;
	}

	public ImageIcon getAvatar() {
		return this.avatar;
	}

	public Color getColor1() {
		return this.color1;
	}

	public void setColor1(Color col1) {
		this.color1 = col1;
	}

	public Color getColor2() {
		return this.color2;
	}

	public void setColor2(Color col2) {
		this.color2 = col2;
	}

}
