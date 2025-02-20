import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.util.prefs.Preferences;

public class WebOpener {
	private static Preferences prefs = Preferences.userNodeForPackage(WebOpener.class);
	private static String defaultHotkey = prefs.get("hotkey", "F9");
	private static String defaultUrl = prefs.get("url", "https://www.google.com");

	public static void main(String[] args) {
		SwingUtilities.invokeLater(WebOpener::createMainMenu);
		startHotkeyListener();
	}

	private static void createMainMenu() {
		JFrame frame = new JFrame("Web Opener");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300, 200);
		frame.setLayout(new FlowLayout());

		JLabel label = new JLabel("Press " + defaultHotkey + " to open the webpage!");
		JButton settingsButton = new JButton("Settings");

		settingsButton.addActionListener(e -> createSettingsMenu());

		frame.add(label);
		frame.add(settingsButton);
		frame.setVisible(true);
	}

	private static void createSettingsMenu() {
		JFrame settingsFrame = new JFrame("Settings");
		settingsFrame.setSize(350, 200);
		settingsFrame.setLayout(new GridLayout(3, 2));

		JLabel hotkeyLabel = new JLabel("Select Hotkey:");
		String[] hotkeys = {"F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8", "F9", "F10"};
		JComboBox<String> hotkeyBox = new JComboBox<>(hotkeys);
		hotkeyBox.setSelectedItem(defaultHotkey);

		JLabel urlLabel = new JLabel("Set Webpage URL:");
		JTextField urlField = new JTextField(defaultUrl);

		JButton saveButton = new JButton("Save");

		saveButton.addActionListener(e -> {
			prefs.put("hotkey", (String) hotkeyBox.getSelectedItem());
			prefs.put("url", urlField.getText());
			JOptionPane.showMessageDialog(settingsFrame, "Settings Saved! Restart for changes.");
			settingsFrame.dispose();
		});

		settingsFrame.add(hotkeyLabel);
		settingsFrame.add(hotkeyBox);
		settingsFrame.add(urlLabel);
		settingsFrame.add(urlField);
		settingsFrame.add(new JLabel());
		settingsFrame.add(saveButton);

		settingsFrame.setVisible(true);
	}

	private static void startHotkeyListener() {
		new Thread(() -> {
			try {
				Robot robot = new Robot();
				while (true) {
					int keyCode = getKeyCode(defaultHotkey);
					if (keyCode != -1 && Toolkit.getDefaultToolkit().getLockingKeyState(keyCode)) {
						openWebpage(defaultUrl);
						Thread.sleep(500); // Prevent multiple triggers
					}
					Thread.sleep(100);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}

	private static int getKeyCode(String hotkey) {
		switch (hotkey) {
		case "F1":
			return KeyEvent.VK_F1;
		case "F2":
			return KeyEvent.VK_F2;
		case "F3":
			return KeyEvent.VK_F3;
		case "F4":
			return KeyEvent.VK_F4;
		case "F5":
			return KeyEvent.VK_F5;
		case "F6":
			return KeyEvent.VK_F6;
		case "F7":
			return KeyEvent.VK_F7;
		case "F8":
			return KeyEvent.VK_F8;
		case "F9":
			return KeyEvent.VK_F9;
		case "F10":
			return KeyEvent.VK_F10;
		default:
			return -1;
		}
	}

	private static void openWebpage(String url) {
		try {
			Desktop.getDesktop().browse(URI.create(url));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
