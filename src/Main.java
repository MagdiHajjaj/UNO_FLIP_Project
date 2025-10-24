
public class Main {
    public static void main(String[] args) {

        System.out.println("Hello from Uno Flip!");
        Card c1 = new Card(CardColor.RED, CardType.TWO);
        Card c2 = new Card(CardColor.BLUE, CardType.REVERSE);
        System.out.println(c1);
        System.out.println(c2);

    }
}
