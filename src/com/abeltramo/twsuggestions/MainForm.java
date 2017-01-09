package com.abeltramo.twsuggestions;

import com.abeltramo.lucene.IndexNews;
import com.abeltramo.lucene.IndexTweet;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * twitter-suggestions
 * Created by ABeltramo on 09/01/17.
 */
public class MainForm {
    private JButton searchBtn;
    private JPanel panel1;
    private JTextField txtUser;

    public MainForm() {
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = txtUser.getText();
                IndexTweet itw = new IndexTweet();
                TwManager twmanager = new TwManager();

                System.out.println("* Indexing @" + user + " tweet *");
                itw.makeIndex(twmanager.getUserPost(user),1.5f);
                System.out.println("* Getting @" + user + " friends *");
                for(String friend : twmanager.getUserFriend(user)){
                    System.out.println("* Indexing @" + friend + " tweet *");
                    itw.makeIndex(twmanager.getUserPost(friend),1.0f);
                }
                itw.closeWrite();

                System.out.println("* Indexing News *");
                NewsManager nwmanager = new NewsManager();
                IndexNews inw = new IndexNews();
                inw.makeIndex(nwmanager.getAllNews());
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainForm");
        frame.setContentPane(new MainForm().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
