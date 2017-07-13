// Amogh Agnihotri
// 7/12/17
// Testing for BartenderBot display

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class Client extends JFrame{

    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message = "";
    private String serverIP;
    private Socket connection;

    //constructor
    public Client(String host){
        super("Client ");
        serverIP = host;
        userText = new JTextField();
        userText.setEditable(false);
        userText.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        sendMessage(e.getActionCommand());
                        userText.setText("");
                    }
                }
        );
        add(userText, BorderLayout.NORTH);
        chatWindow = new JTextArea();
        add(new JScrollPane(chatWindow), BorderLayout.CENTER);
        setSize(300,150);
        setVisible(true);
    }

    //connect to server
    public void startRunning(){
        try{
            connectToServer();
            setupStreams();
            whileChatting();
        }catch(EOFException eof){
            showMessage("\n Client terminated connection");
        }catch(IOException io){
            io.printStackTrace();
        }finally{
            close();
        }
    }

    //connect to server
    private void connectToServer() throws IOException{
        showMessage("Attempting connection ... \n");
        connection = new Socket(InetAddress.getByName(serverIP), 6789);
        showMessage("Connected to: " + connection.getInetAddress().getHostName());
    }

    //setup streams to send and receive messages
    private void setupStreams() throws IOException{
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\n Stream is good to go \n");
    }

    //while chatting with server
    private void whileChatting() throws IOException{
        ableToType(true);
        do{
            try{
                message = (String) input.readObject();
                showMessage("\n " + message);
            }catch(ClassNotFoundException classNotFound){
                showMessage(" \n I can't understand ");
            }
        }while(!message.equals("SERVER - END"));
    }

    //closes connection
    private void close(){
        showMessage("\n Closing connection... ");
        ableToType(false);
        try{
            output.close();
            input.close();
            connection.close();
        }catch(IOException io){
            io.printStackTrace();
        }
    }

    //send messages to server
    private void sendMessage(String message){
        try{
            output.writeObject("CLIENT - " + message);
            output.flush();
            showMessage("\n CLIENT - " + message);
        }catch(IOException io){
            chatWindow.append("\n Something messed up... ");
        }
    }

    //shows message on the screen
    private void showMessage(final String message){
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        chatWindow.append(message);
                    }
                }
        );
    }

    //gives user permission
    private void ableToType(final Boolean tof){
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        userText.setEditable(tof);
                    }
                }
        );
    }

}
