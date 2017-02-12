package com.abeltramo.twsuggestions;

import com.abeltramo.lucene.CompareIndex;
import com.abeltramo.lucene.IndexNews;
import com.abeltramo.lucene.IndexTweet;
import org.apache.lucene.document.Document;
import org.apache.lucene.store.RAMDirectory;

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
    private JCheckBox ChkFriends;
    private JTable resultTable;

    public MainForm() {
        MainForm _this = this;
        searchBtn.addActionListener(new ActionListener() {                                          // On start button click
            @Override
            public void actionPerformed(ActionEvent e) {
                searchBtn.setEnabled(false);                                                        //  *
                String user = txtUser.getText().replace("@","");                                    // Getting user
                boolean useCache = ChkUseCache.isSelected();                                        // preferences from GUI
                boolean useFriends = ChkFriends.isSelected();                                       //  *
                RAMDirectory tweetDir = new RAMDirectory();                                         //  *
                RAMDirectory friendDir = new RAMDirectory();                                        // Initialize Twitter
                IndexTweet itw = new IndexTweet(tweetDir,friendDir);                                // objects
                TwManager twmanager = new TwManager(useCache,_this);                                //  *
                RAMDirectory newsDir = new RAMDirectory();                                          //  *
                IndexNews inw = new IndexNews(newsDir);                                             // Initialize Index objects
                NewsManager nwmanager = new NewsManager(useCache);                                  //  *

                Runnable R = new Runnable() {                                                       // Run in background
                    public void run() {                                                             // to keep gui responsive
                        ArrayList<String> friends = new ArrayList<>();                              // List of friends
                        notifyUser(10,"Indexing @" + user + " tweet");
                        itw.makeIndex(twmanager.getUserPost(user),true);                      // Getting user posts from Twitter
                        if(useFriends) {                                                            // If checkbox is selected
                            notifyUser(20, "Getting @" + user + " friend list");
                            friends = twmanager.getUserFriend(user);                                // Getting list of friends from Twitter
                            for (int i = 0; i< friends.size(); i++) {                               // For each friend in list friend
                                String friend = friends.get(i);
                                notifyUser(30, "Getting "+ (i+1) +"/"+ friends.size() + " @" + friend + " tweets");
                                itw.makeIndex(twmanager.getUserPost(friend), false);          // Get post of i-friend
                            }
                        }
                        itw.closeWrite();                                                           // Close index twitter for writing

                        notifyUser(50,"Getting news");
                        inw.makeIndex(nwmanager.getAllNews());                                      // Create index for news

                        notifyUser(70,"Lucene Query");
                        CompareIndex cpi = new CompareIndex(tweetDir,friendDir,newsDir);            // COMPARE both resultTable
                        Document[] result = cpi.queryNews(cpi.getTopTwitterTerms(75,friends.size()),
                                                         10);                             // Result contain the news that match

                        notifyUser(100,"Completed");
                        completedBackground(result);                                                // Close the background task. We have finish
                    }
                };

                Thread T = new Thread(R);
                T.start();
            }
        });
    }

    private void completedBackground(Document[] result){                                            // When the background task is completed
        MainForm _this = this;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                progressBar.setVisible(false);
                Status.setVisible(false);
                searchBtn.setEnabled(true);

                resultTable.setModel(new ResultTable(10,result));
                resultTable.setVisible(true);
            }
        });
    }

    private void notifyUser(int progress, String status){                                           // Called to update the progressbar
        SwingUtilities.invokeLater(new Runnable() {
           public void run() {
               progressBar.setVisible(true);
               progressBar.setIndeterminate(false);
               progressBar.setValue(progress);
               Status.setVisible(true);
               Status.setText(status);
           }
       });
    }

    public void notifyWaiting(String status){                                                       // Called when we hit the Twitter API limit
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                progressBar.setVisible(true);
                progressBar.setIndeterminate(true);
                Status.setVisible(true);
                Status.setText(status);
            }
        });
    }

    public static void main(String[] args) {                                                        // Main: just initialize the GUI
        JFrame frame = new JFrame("Twitter suggestions");
        frame.setContentPane(new MainForm().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void createUIComponents() {
        resultTable = new JTable(new ResultTable(0,null));
    }
}
