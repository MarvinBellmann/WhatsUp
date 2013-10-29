package Client;

import javax.swing.ImageIcon;

public class ContactCard {

	private ImageIcon avatar;
	private String status;
	private String name;

	public ContactCard(ImageIcon avatar, String status, String name) {
		this.avatar = avatar;
		this.status = status;
		this.name = name;
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

}
