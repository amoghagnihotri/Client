// Amogh Agnihotri
// 7/12/17
// Testing for BartenderBot display

import javax.swing.JFrame;


public class ClientTester {
    public static void main(String[] args) {
        Client iiwa;
        iiwa = new Client("127.0.0.1");
        iiwa.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        iiwa.startRunning();
    }
}
