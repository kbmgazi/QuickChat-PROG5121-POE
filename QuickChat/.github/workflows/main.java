public class main {
    public static void main(String[] args) {

        for (char row = 'A'; row <= 'D'; row++) {

            for (int seat = 1; seat <= 5; seat++) {
                System.out.print(row + "" + seat + " ");
            }

            System.out.println();
        }
    }
}