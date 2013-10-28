package Client;

import java.awt.Component;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import net.miginfocom.swing.MigLayout;

public class ContactCardRenderer implements TableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		ContactCard card = (ContactCard) value;

		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("fill", "[55px::55px][]", "[][]"));

		JLabel image = new JLabel();
		ImageIcon avatar = card.getAvatar();
		avatar.setImage(avatar.getImage().getScaledInstance(50, 50,
				Image.SCALE_DEFAULT));
		image.setIcon(avatar);
		panel.add(image, "span 1 2");

		JLabel user = new JLabel();
		user.setText(card.getName());
		panel.add(user, "wrap");

		JLabel state = new JLabel();
		state.setText(card.getStatus());
		panel.add(state);

		return panel;
	}

}
