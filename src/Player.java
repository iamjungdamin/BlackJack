public class Player {
	public Card[] cards = new Card[52];
	private int N = 0; //플레이어 손에 쥔 카드의 개수
	private String name;
	public Player(String name) {
		this.name = name;
	}
	public int inHand() { //손에 쥔 카드의 개수 리턴
		return N;
	}
	public void addCard(Card c) {
		cards[N++] = c;
	}
	public void reset() {
		N = 0;
	}
	public int value() { //플레이어가 가진 카드의 계산값
		int result = 0;
		int Aces = 0;
		for (int i=0; i<N; i++) {
			if (cards[i].getValue() == 1) {
				Aces ++;
				result += 11;
			}
			else result += cards[i].getValue();
		}
		while (result > 21 && Aces > 0) {
			result -= 10; //10빼서 A(11)를 1로
			Aces --;
		}
		return result;
	}
}