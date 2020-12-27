public class Card {
	private int value, x; //value =1,2,3,...13(J Q K), x=0,1,2,3(클스하다)
	private String suit; //"Clubs", "Spades", "Hearts", "Diamonds"
	public Card() {
		//중복을 허용해서 임의의 카드 생성
		int temp;
		temp = (int)(Math.random()*52); //0~51
		value = temp %13 +1; //1~13
		x = temp/13; //0,1,2,3
	}
	public int getValue() {
		if (value > 10)
			return 10; //J Q K는 10으로 계산
		else return value;
	}
	public String getsuit() {
		switch(x) {
		case 0: return suit = "Clubs";
		case 1: return suit = "Spades";
		case 2: return suit = "Hearts";
		default: return suit = "Diamonds";
		}
	}
	public String filename() {
		return getsuit() +""+value+".png";
	}
}
