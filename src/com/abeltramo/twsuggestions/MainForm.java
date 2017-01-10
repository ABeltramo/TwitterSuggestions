package com.abeltramo.twsuggestions;

import com.abeltramo.lucene.IndexNews;
import com.abeltramo.lucene.IndexTweet;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * twitter-suggestions
 * Created by ABeltramo on 09/01/17.
 */
public class MainForm {
    private JButton searchBtn;
    private JPanel panel1;
    private JTextField txtUser;
    private JProgressBar progressBar;
    private JLabel Status;
    private JCheckBox ChkUseCache;

    public MainForm() {
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchBtn.setEnabled(false);
                String user = txtUser.getText().replace("@","");
                boolean useCache = ChkUseCache.isSelected();
                IndexTweet itw = new IndexTweet();
                TwManager twmanager = new TwManager(useCache);

                new Thread(new Runnable() {
                    public void run() {
                        notifyUser(10,"Indexing @" + user + " tweet");
                        itw.makeIndex(twmanager.getUserPost(user),1.5f);
                        notifyUser(20,"Getting @" + user + " friend list");
                        for(String friend : twmanager.getUserFriend(user)){
                            notifyUser(30,"Getting @" + friend + " tweet");
                            itw.makeIndex(twmanager.getUserPost(friend),1.0f);
                        }
                        itw.closeWrite();

                        notifyUser(50,"Getting news");
                        NewsManager nwmanager = new NewsManager(useCache);
                        IndexNews inw = new IndexNews();
                        inw.makeIndex(nwmanager.getAllNews());
                        notifyUser(100,"Completed");
                        completedBackground();
                    }
                }).start();
            }
        });
    }

    private void completedBackground(){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                progressBar.setVisible(false);
                Status.setVisible(false);
                searchBtn.setEnabled(true);
            }
        });
    }

    private void notifyUser(int progress, String status){
        SwingUtilities.invokeLater(new Runnable() {
           public void run() {
               progressBar.setVisible(true);
               progressBar.setValue(progress);
               Status.setVisible(true);
               Status.setText(status);
           }
       });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainForm");
        frame.setContentPane(new MainForm().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void msgbox(String message,String title){
        JOptionPane.showMessageDialog(null, message,title,JOptionPane.WARNING_MESSAGE);
    }
}
