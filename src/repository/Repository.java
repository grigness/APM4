package repository;

import domain.SearchEngine;
import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Repository {
    private static final String JDBC_URL = "jdbc:sqlite:data/test_db.db";

    private static Connection conn = null;

    private List<SearchEngine> activities;

    public Repository() {
        activities = new ArrayList<>();
        openConnection();
        try {
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM SearchEngine");

            while (rs.next()) {
                activities.add(new SearchEngine(
                        rs.getString("name"),
                        rs.getString("keywords"),
                        rs.getString("content")))
                ;
            }
            rs.close();
            s.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public static Connection getConnection() {
        if (conn == null)
            openConnection();
        return conn;
    }



    private static void openConnection()
    {
        try
        {
            SQLiteDataSource ds = new SQLiteDataSource();
            ds.setUrl(JDBC_URL);
            if (conn == null || conn.isClosed())
                conn = ds.getConnection();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void closeConnection()
    {
        try
        {
            conn.close();
            conn = null;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void add(SearchEngine searchEngine) throws SQLException{

        String name= searchEngine.getName();

        SearchEngine existingDate=this.getDocuments().stream()
                .filter(a->a.getName().equals(name))
                .findFirst()
                .orElse(null);

        if(existingDate==null){
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO SearchEngine VALUES(?, ?, ?)");
            stmt.setString(1, searchEngine.getName());
            stmt.setString(2, searchEngine.getKeywords());
            stmt.setString(3, searchEngine.getContents());

            stmt.executeUpdate();
            stmt.close();

    } catch (SQLException e) {
            throw new RuntimeException(e);
        }}
        else{
            PreparedStatement stmt = conn.prepareStatement("UPDATE Activity SET keywords = ?, contents = ? WHERE name=?");
            stmt.setString(1, searchEngine.getKeywords());
            stmt.setString(2, searchEngine.getContents());

            stmt.setString(3, searchEngine.getName());

            stmt.executeUpdate();
            stmt.close();
        }
    }
    public void update(SearchEngine searchEngine) throws SQLException{
        String updatedb="Update SearchEngine set keywords=?, content=? WHERE name=?";
        PreparedStatement preparedStatement=getConnection().prepareStatement(updatedb);
        preparedStatement.setString(1,searchEngine.getKeywords());
        preparedStatement.setString(2,searchEngine.getContents());
        preparedStatement.setString(3,searchEngine.getName());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

        public List<SearchEngine> getDocuments() {
        List<SearchEngine> documents = new ArrayList<>();
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * from SearchEngine");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                SearchEngine a = new SearchEngine(
                        rs.getString("name"),
                        rs.getString("keywords"),
                        rs.getString("content"));
                documents.add(a);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return documents;
    }

    public double bestMatching(String s1, String s2){
        int minLength=Math.min(s1.length(),s2.length());
        int common=0;
        for(int i=0;i<minLength;i++){
            if(s1.charAt(i)==s2.charAt(i)){
                common++;
            }
        }
        return (double) common/ (double) Math.max(s1.length(),s2.length());
    }

    public SearchEngine findBestMath(String search){
        SearchEngine bestMatch=null;
        double maxSimilarity=-1.0;
        for(SearchEngine document: activities){
            double similarity=bestMatching(search,document.getName().toLowerCase());
            if(similarity>maxSimilarity){
                maxSimilarity=similarity;
                bestMatch=document;
            }
        }
        return bestMatch;
    }
}
