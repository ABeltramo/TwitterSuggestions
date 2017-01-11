package com.abeltramo.twsuggestions;

import org.apache.lucene.document.Document;

import javax.swing.table.AbstractTableModel;

/**
 * twitter-suggestions
 * Created by ABeltramo on 11/01/17.
 */
public class ResultTable extends AbstractTableModel {
    private int _rows;
    private Document[] _docs;

    public ResultTable(int rows,Document[] result) {
        _rows = rows;
        _docs = result;
    }

    // ritorna il numero di colonne
    public int getColumnCount() { return 4; }
    // ritorna il numero di righe
    public int getRowCount() { return _rows;}
    // ritorna il contenuto di una cella
    public Object getValueAt(int row, int col)
    {
        // Inverted here!
        Document curDoc = _docs[_rows-row-1];
        switch (col) {
            case 0: return curDoc.getField("source").stringValue();
            case 1: return curDoc.getField("title").stringValue();
            case 2: return curDoc.getField("description").stringValue();
            case 3: return curDoc.getField("url").stringValue();
            default: return "";
        }
    }
    // ritorna il nome della colonna
    public String getColumnName(int col) {
        // e' il numero di colonna
        switch (col) {
            case 0: return "Source";
            case 1: return "Title";
            case 2: return "Description";
            case 3: return "Url";
            default: return "";
        }
    }
    // specifica se le celle sono editabili
    public boolean isCellEditable(int row, int col)
    {
        // nessuna cella editabile
        return false;
    }
}
