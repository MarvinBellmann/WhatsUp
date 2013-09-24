import javax.swing.ImageIcon;

public class Smiley extends ImageIcon {

	private static final long serialVersionUID = 2862630175346578045L;

	public Smiley(String smiley) {
		checkSmiley(smiley);
	}

	public static ImageIcon checkSmiley(String smiley) {
		ImageIcon image = null;
		String name = null;
		switch (smiley) {
		case "=)":
		case ":)":
			name = "smile";
			break;
		case ";)":
			name = "wink";
			break;
		case ":D":
			name = "laugh";
			break;
		case ":(":
			name = "sad";
			break;
		case ":/":
			name = "neutral";
			break;
		default:
			break;
		}
		return image = new ImageIcon("data/icon_" + name + ".gif");
	}
}
