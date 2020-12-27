import java.io.File;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class MyPanel extends JPanel {
	private ImageIcon icon = new ImageIcon("cards/table.jpg");
	private Image img = icon.getImage();
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
	}
}

public class blackjack extends JFrame implements ActionListener {
	public Player player = new Player("player");
	public Player dealer = new Player("dealer");
	
	private JButton jbtn50 = new JButton("Bet 50");
	private JButton jbtn10 = new JButton("Bet 10");
	private JButton jbtn1 = new JButton("Bet 1");	    		
	private JButton jbtnHit = new JButton("Hit");
	private JButton jbtnStay = new JButton("Stay");
	private JButton jbtnDeal = new JButton("Deal");
	private JButton jbtnAgain = new JButton("Again");
	
	private JLabel jlblBetMoney = new JLabel("$0");
	private JLabel jlblPlayerMoney = new JLabel("You have $1000");
	private JLabel jlblPlayerPts = new JLabel("");
	private JLabel jlblDealerPts = new JLabel("");
	private JLabel jlblStatus = new JLabel("");
	private Font fontstyle = new Font("Times",Font.BOLD,24);
	private Font fontstyle2 = new Font("Times",Font.BOLD,16);

	private int nCardsDealer; //딜러가 손에 쥔 카드 개수
	private int nCardsPlayer; //플레이어가 손에 쥔 카드 개수
	private int betMoney = 0; //베팅한돈
	private int playerMoney = 1000; //초기자본 1000달러
	private JLabel[] jlblCardsPlayer = new JLabel[7]; //플레이어 카드의 이미지 라벨
	private JLabel[] jlblCardsDealer = new JLabel[7]; //딜러 카드의 이미지 라벨
	
	MyPanel tablePanel = new MyPanel();
	private Clip chipclip, flipclip, againclip, winclip,loseclip;
	
	public blackjack() {
		setupSound();
		JFrame gameFrame = new JFrame("BlackJack");
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		tablePanel.setLayout(null); //절대배치자
		
		//버튼위치설정
	    jbtn50.setBounds(50,500,80,40); //50불짜리
	    jbtn10.setBounds(150,500,80,40);
	    jbtn1.setBounds(250,500,80,40);
	    jbtnHit.setBounds(400,500,80,40); //hit
	    jbtnStay.setBounds(500,500,80,40);
	    jbtnDeal.setBounds(600,500,80,40);
	    jbtnAgain.setBounds(700,500,80,40);
	    
	    //라벨위치설정
	    jlblBetMoney.setBounds(200, 450, 100, 50);
	    jlblBetMoney.setFont(fontstyle);
	    jlblBetMoney.setForeground(Color.ORANGE);
	    tablePanel.add(jlblBetMoney);
	    jlblPlayerMoney.setBounds(500, 450, 200, 50);
	    jlblPlayerMoney.setFont(fontstyle);
	    jlblPlayerMoney.setForeground(Color.ORANGE);
	    tablePanel.add(jlblPlayerMoney);
	    
        jlblPlayerPts.setBounds(300,300,100,50);
        jlblPlayerPts.setFont(fontstyle2);
        jlblPlayerPts.setForeground(Color.WHITE);
        tablePanel.add(jlblPlayerPts);
        jlblDealerPts.setBounds(300,100,100,50);
        jlblDealerPts.setFont(fontstyle2);
        jlblDealerPts.setForeground(Color.WHITE);
        tablePanel.add(jlblDealerPts);
        jlblStatus.setBounds(500,300,200,50);
        jlblStatus.setFont(fontstyle);
        jlblStatus.setForeground(Color.WHITE);
        tablePanel.add(jlblStatus);
	    
	    tablePanel.add(jbtn50);
	    tablePanel.add(jbtn10);
	    tablePanel.add(jbtn1);
	    tablePanel.add(jbtnHit);
	    tablePanel.add(jbtnStay);
	    tablePanel.add(jbtnDeal);
	    tablePanel.add(jbtnAgain);
	    
	    jbtn50.addActionListener(this); //액션리스너는 나 자신이다
	    jbtn10.addActionListener(this); 
	    jbtn1.addActionListener(this); 
	    jbtnHit.addActionListener(this); 
	    jbtnStay.addActionListener(this); 
	    jbtnDeal.addActionListener(this); 
	    jbtnAgain.addActionListener(this);

	    jbtnHit.setEnabled(false); //버튼 비활성화
	    jbtnStay.setEnabled(false);
	    jbtnDeal.setEnabled(false);
	    jbtnAgain.setEnabled(false);
		
		gameFrame.add(tablePanel);
		gameFrame.setSize(800, 600);
		gameFrame.setVisible(true);
	}
	
	public void setupSound() {
		String flipSound = "sounds/cardFlip1.wav";
		try {
			AudioInputStream flipAudioInputStream = AudioSystem.getAudioInputStream(new File(flipSound));
			flipclip = AudioSystem.getClip();
			flipclip.open(flipAudioInputStream);
		} catch (Exception e) {
			System.out.println(e);
		}
		String chipSound = "sounds/chip.wav";
		try {
			AudioInputStream chipAudioInputStream = AudioSystem.getAudioInputStream(new File(chipSound));
			chipclip = AudioSystem.getClip();
			chipclip.open(chipAudioInputStream);
		} catch (Exception e) {
			System.out.println(e);
		}
		String loseSound = "sounds/wrong.wav";
		try {
			AudioInputStream loseAudioInputStream = AudioSystem.getAudioInputStream(new File(loseSound));
			loseclip = AudioSystem.getClip();
			loseclip.open(loseAudioInputStream);
		} catch (Exception e) {
			System.out.println(e);
		}
		String againSound = "sounds/ding.wav";
		try {
			AudioInputStream againAudioInputStream = AudioSystem.getAudioInputStream(new File(againSound));
			againclip = AudioSystem.getClip();
			againclip.open(againAudioInputStream);
		} catch (Exception e) {
			System.out.println(e);
		}
		String winSound = "sounds/win.wav";
		try {
			AudioInputStream winAudioInputStream = AudioSystem.getAudioInputStream(new File(winSound));
			winclip = AudioSystem.getClip();
			winclip.open(winAudioInputStream);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void hitPlayer(int n) {
		Card newCard = new Card();
		for (int i=0; i<player.inHand(); i++) { //중복검사
			if ( newCard.equals(player.cards[i]) || newCard.equals(dealer.cards[i]) ) 
				newCard = new Card();
		}
		player.addCard(newCard);
		jlblCardsPlayer[player.inHand()-1] = new JLabel(new ImageIcon("cards/"+newCard.filename()));
		jlblCardsPlayer[player.inHand()-1].setBounds(250+n*30, 350, 80, 100);
		tablePanel.add(jlblCardsPlayer[player.inHand()-1]);
		jlblPlayerPts.setText(""+player.value());
		tablePanel.updateUI();
		flipclip.stop();
		flipclip.setFramePosition(0);
		flipclip.start();
	}
	
	private void hitDealer(int n) {
		Card newCard = new Card();
		for (int i=0; i<dealer.inHand(); i++) {
			if ( newCard.equals(player.cards[i]) || newCard.equals(dealer.cards[i]))
				newCard = new Card();
		}
		dealer.addCard(newCard);
		if (n==0)
			jlblCardsDealer[dealer.inHand()-1] = new JLabel(new ImageIcon("cards/b2fv.png")); //뒤집어두기
		else
			jlblCardsDealer[dealer.inHand()-1] = new JLabel(new ImageIcon("cards/"+newCard.filename()));
		jlblCardsDealer[dealer.inHand()-1].setBounds(250+n*30, 150, 80, 100);
		tablePanel.add(jlblCardsDealer[dealer.inHand()-1]);
		tablePanel.updateUI();
	}
	
	private void checkWinner() {
		tablePanel.remove(jlblCardsDealer[0]);
		jlblCardsDealer[0] = new JLabel(new ImageIcon("cards/"+dealer.cards[0].filename()));
		jlblCardsDealer[0].setBounds(250, 150, 80, 100);
		tablePanel.add(jlblCardsDealer[0]);
		jlblDealerPts.setText(""+dealer.value());
		
		if (player.value() > 21) {
			jlblStatus.setText("Player Busts");
			loseclip.stop();
			loseclip.setFramePosition(0);
			loseclip.start();
		}
		else if (dealer.value() > 21) {
			jlblStatus.setText("Dealer Busts");
			playerMoney += betMoney*2;
			winclip.stop();
			winclip.setFramePosition(0);
			winclip.start();
		}
		else if (player.value() == dealer.value()) {
			jlblStatus.setText("Push");
			playerMoney += betMoney;
		}
		else if (player.value() > dealer.value()) {
			jlblStatus.setText("You won!!");
			playerMoney += betMoney*2;
			winclip.stop();
			winclip.setFramePosition(0);
			winclip.start();
		}
		else if (player.value() < dealer.value()) {
			jlblStatus.setText("Sorry you lost");
			loseclip.stop();
			loseclip.setFramePosition(0);
			loseclip.start();
		}
		betMoney = 0;
		jlblPlayerMoney.setText("You have $"+playerMoney);
		jlblBetMoney.setText("$"+betMoney);
		tablePanel.updateUI();
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == jbtn50) {
			betMoney += 50;
			playerMoney -= 50;
			if (playerMoney < 0) { //플레이어머니가 0 이하이면 베팅할수 없으므로 원상복구
				betMoney -= 50;
				playerMoney += 50;
			}
			jlblBetMoney.setText("$"+betMoney);
			jlblPlayerMoney.setText("You have $"+playerMoney);
			jbtnDeal.setEnabled(true); //버튼 활성화
			chipclip.stop();
			chipclip.setFramePosition(0);
			chipclip.start();
		}
		if (e.getSource() == jbtn10) {
			betMoney += 10;
			playerMoney -= 10;
			if (playerMoney < 0) {
				betMoney -= 10;
				playerMoney += 10;
			}
			jlblBetMoney.setText("$"+betMoney);
			jlblPlayerMoney.setText("You have $"+playerMoney);
			jbtnDeal.setEnabled(true);
			chipclip.stop();
			chipclip.setFramePosition(0);
			chipclip.start();
		}
		if (e.getSource() == jbtn1) {
			betMoney += 1;
			playerMoney -= 1;
			if (playerMoney < 0) {
				betMoney -= 1;
				playerMoney += 1;
			}
			jlblBetMoney.setText("$"+betMoney);
			jlblPlayerMoney.setText("You have $"+playerMoney);
			jbtnDeal.setEnabled(true);
			chipclip.stop();
			chipclip.setFramePosition(0);
			chipclip.start();
		}
		if (e.getSource() == jbtnHit) {
			hitPlayer(++nCardsPlayer);
			if (player.value() > 21) {
				checkWinner();
				jbtn50.setEnabled(false);
				jbtn10.setEnabled(false);
				jbtn1.setEnabled(false);
				jbtnHit.setEnabled(false);
				jbtnStay.setEnabled(false);
				jbtnDeal.setEnabled(false);
				jbtnAgain.setEnabled(true);
			}
		}
		if (e.getSource() == jbtnStay) {
			while (dealer.value() < 17) {
				hitDealer(++nCardsDealer);
			}
			checkWinner();
			jbtnHit.setEnabled(false);
			jbtnStay.setEnabled(false);
			jbtnDeal.setEnabled(false);
			jbtnAgain.setEnabled(true);
		}
		if (e.getSource() == jbtnDeal) {
			player.reset();
			dealer.reset();
			hitPlayer(0);
			hitDealer(0);
			hitPlayer(1);
			hitDealer(1);
			nCardsPlayer =1;
			nCardsDealer =1;
			jlblPlayerPts.setText(""+player.value());
			
			jbtn50.setEnabled(false);
			jbtn10.setEnabled(false);
			jbtn1.setEnabled(false);
			jbtnDeal.setEnabled(false);
			jbtnHit.setEnabled(true);
			jbtnStay.setEnabled(true);
		}
		if (e.getSource() == jbtnAgain) {
			for (int i=0; i<dealer.inHand(); i++)
				tablePanel.remove(jlblCardsDealer[i]);
			for (int i=0; i<player.inHand(); i++)
				tablePanel.remove(jlblCardsPlayer[i]);
			jlblDealerPts.setText("");
			jlblPlayerPts.setText("");
			jlblStatus.setText("");
			
			jbtn50.setEnabled(true);
			jbtn10.setEnabled(true);
			jbtn1.setEnabled(true);
			jbtnAgain.setEnabled(false);
			tablePanel.updateUI();
			againclip.stop();
			againclip.setFramePosition(0);
			againclip.start();
		}
	}
	
	public static void main(String[] args) {
		new blackjack();
	}
}