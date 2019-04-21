package fullGame;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import objectClasses.CardAffects;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class InGameCardViewer extends JFrame {

	private JPanel contentPane;
	private JLabel lblcn = new JLabel("*CN*"), lblap = new JLabel("*CN*"), lblhp = new JLabel("*CN*"), lblct = new JLabel("*CN*"),
			lblec = new JLabel("*CN*"), lblhb = new JLabel("*CN*"), lblapb = new JLabel("*CN*"), lblerb = new JLabel("*CN*"),
					lblarb = new JLabel("*CN*");

	private String username;
	/**
	 * Create the frame.
	 */
	public InGameCardViewer(String username) {
		setUsername(username);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 550, 300);
		setTitle("InGameCardViewer");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblCardName = new JLabel("Card Name:");
		lblCardName.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblCardName.setBounds(10, 11, 157, 14);
		contentPane.add(lblCardName);
		
		JLabel lblCardAttackPoints = new JLabel("Card Attack Points:");
		lblCardAttackPoints.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblCardAttackPoints.setBounds(10, 36, 157, 14);
		contentPane.add(lblCardAttackPoints);
		
		JLabel lblCardHealthPoints = new JLabel("Card Health Points:");
		lblCardHealthPoints.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblCardHealthPoints.setBounds(10, 61, 157, 14);
		contentPane.add(lblCardHealthPoints);
		
		JLabel lblCardType = new JLabel("Card Type:");
		lblCardType.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblCardType.setBounds(10, 86, 157, 14);
		contentPane.add(lblCardType);
		
		JLabel lblCardEnergyCost = new JLabel("Card Energy Cost:");
		lblCardEnergyCost.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblCardEnergyCost.setBounds(10, 111, 157, 14);
		contentPane.add(lblCardEnergyCost);
		
		JLabel lblCardHealthBuff = new JLabel("Card Health Buff:");
		lblCardHealthBuff.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblCardHealthBuff.setBounds(10, 136, 157, 14);
		contentPane.add(lblCardHealthBuff);
		
		JLabel lblCardAttackPoints_1 = new JLabel("Card Attack Points Buff:");
		lblCardAttackPoints_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblCardAttackPoints_1.setBounds(10, 161, 171, 14);
		contentPane.add(lblCardAttackPoints_1);
		
		JLabel lblCardEnergyReduce = new JLabel("Card Energy Reduce Buff:");
		lblCardEnergyReduce.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblCardEnergyReduce.setBounds(10, 186, 197, 14);
		contentPane.add(lblCardEnergyReduce);
		
		JLabel lblCardArmorBuff = new JLabel("Card Armor Buff:");
		lblCardArmorBuff.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblCardArmorBuff.setBounds(10, 211, 197, 14);
		contentPane.add(lblCardArmorBuff);
		
		lblcn.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblcn.setBounds(91, 11, 433, 14);
		contentPane.add(lblcn);

		lblap.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblap.setBounds(139, 38, 395, 14);
		contentPane.add(lblap);
				
		lblhp.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblhp.setBounds(139, 63, 395, 14);
		contentPane.add(lblhp);
		
		lblct.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblct.setBounds(91, 86, 395, 14);
		contentPane.add(lblct);
		
		lblec.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblec.setBounds(129, 111, 395, 14);
		contentPane.add(lblec);
		
		lblhb.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblhb.setBounds(129, 136, 395, 14);
		contentPane.add(lblhb);
		
		lblapb.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblapb.setBounds(171, 163, 363, 14);
		contentPane.add(lblapb);
		
		lblerb.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblerb.setBounds(181, 186, 353, 14);
		contentPane.add(lblerb);
		
		lblarb.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblarb.setBounds(129, 213, 395, 14);
		contentPane.add(lblarb);
		
		JButton btnAttack = new JButton("Attack");
		btnAttack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String enemyCard = JOptionPane.showInputDialog(null, "Please enter the card name your wish to attack");
				CardAffects attack = new CardAffects();
				attack.attack1(getLblcn(), enemyCard, getUsername());
				attack.attack2(getLblcn(), enemyCard, getUsername());
			}
		});
		btnAttack.setBounds(388, 9, 136, 66);
		contentPane.add(btnAttack);
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public JLabel getLblap() {
		return lblap;
	}

	public void setLblap(int i) {
		lblap.setText(String.valueOf(i));
	}

	public JLabel getLblhp() {
		return lblhp;
	}

	public void setLblhp(int i) {
		lblhp.setText(String.valueOf(i));
	}

	public JLabel getLblct() {
		return lblct;
	}

	public void setLblct(String string) {
		lblct.setText(String.valueOf(string));
	}

	public JLabel getLblec() {
		return lblec;
	}

	public void setLblec(int i) {
		lblec.setText(String.valueOf(i));
	}

	public JLabel getLblhb() {
		return lblhb;
	}

	public void setLblhb(int i) {
		lblhb.setText(String.valueOf(i));
	}

	public JLabel getLblapb() {
		return lblapb;
	}

	public void setLblapb(int i) {
		lblapb.setText(String.valueOf(i));
	}

	public JLabel getLblerb() {
		return lblerb;
	}

	public void setLblerb(int i) {
		lblerb.setText(String.valueOf(i));
	}

	public JLabel getLblarb() {
		return lblarb;
	}

	public void setLblarb(int i) {
		lblarb.setText(String.valueOf(i));;
	}

	public void setLblcn(JLabel lblcn) {
		this.lblcn = lblcn;
	}

	public String getLblcn() {
		return lblcn.getText();
	}

	public void setLblcn(String string) {
		lblcn.setText(string);
	}
}
