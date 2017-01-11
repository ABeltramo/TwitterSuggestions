package com.abeltramo.twsuggestions;

import com.abeltramo.lucene.CompareIndex;
import com.abeltramo.lucene.IndexNews;
import com.abeltramo.lucene.IndexTweet;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.misc.TermStats;

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
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // USER Preferences
                searchBtn.setEnabled(false);
                String user = txtUser.getText().replace("@","");
                boolean useCache = ChkUseCache.isSelected();
                boolean useFriends = ChkFriends.isSelected();
                // TWEET
                IndexTweet itw = new IndexTweet();
                TwManager twmanager = new TwManager(useCache,_this);
                // NEWS
                IndexNews inw = new IndexNews();
                NewsManager nwmanager = new NewsManager(useCache);
                // COMPARE both resultTable
                CompareIndex cpi = new CompareIndex();

                Runnable R = new Runnable() {
                    public void run() {
                        notifyUser(10,"Indexing @" + user + " tweet");
                        itw.makeIndex(twmanager.getUserPost(user),1.5f);
                        if(useFriends) {
                            notifyUser(20, "Getting @" + user + " friend list");
                            ArrayList<String> friends = twmanager.getUserFriend(user);
                            for (int i = 0; i< friends.size(); i++) {
                                String friend = friends.get(i);
                                notifyUser(30, "Getting "+ (i+1) +"/"+ friends.size() + " @" + friend + " tweets");
                                itw.makeIndex(twmanager.getUserPost(friend), 1.0f);
                            }
                        }
                        itw.closeWrite();

                        notifyUser(50,"Getting news");
                        inw.makeIndex(nwmanager.getAllNews());

                        notifyUser(70,"Lucene Query");
                        TermStats[] topTweet = cpi.getTopTwitterTerms(50);
                        Document[] result = cpi.queryNews(topTweet,10);

                        notifyUser(100,"Completed");
                        completedBackground(result);
                    }
                };

                Thread T = new Thread(R);
                T.start();
            }
        });
    }

    private void completedBackground(Document[] result){
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

    private void notifyUser(int progress, String status){
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

    public void notifyWaiting(String status){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                progressBar.setVisible(true);
                progressBar.setIndeterminate(true);
                Status.setVisible(true);
                Status.setText(status);
            }
        });
    }

    public static void main(String[] args) {
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
