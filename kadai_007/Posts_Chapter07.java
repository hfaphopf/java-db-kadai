package kadai_007;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Posts_Chapter07 {

    public static void main(String[] args) {
        
        Connection con = null;
        PreparedStatement insertstatement = null;
        Statement statement = null;
        

        // 投稿内容
        String[][] postsList = {
        	    { "1003", "2023-02-08", "昨日の夜は徹夜でした..", "13" },
        	    { "1003", "2023-02-08", "お疲れ様です!", "12" },
        	    { "1003", "2023-02-09", "今日も頑張ります!", "18" },
        	    { "1001", "2023-02-09", "無理は禁物ですよ!", "17" },
        	    { "1002", "2023-02-10", "明日から連休ですね!", "20" }
        	};

        
        try {
            // データベースに接続
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost/java_db",
                "root",
                "root"
            );

            System.out.println("データベース接続成功");

            // SQLクエリを準備
            String sql = "INSERT INTO posts(user_id, posted_at, post_content, likes) VALUES(?, ?, ?, ?);";
            insertstatement = con.prepareStatement(sql);
            
            String createTablesql = """
                         CREATE TABLE IF NOT EXISTS posts(
                         post_id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
                         user_id INT(11) NOT NULL,
                         posted_at DATE NOT NULL,
                         post_content VARCHAR(255) NOT NULL,
                         likes INT(11) DEFAULT 0
                         )
                    """;
            
            statement = con.createStatement();
            statement.execute(createTablesql);
            statement.close();
            String sql2 ="";
            int counter = 0;
            int rowCnt = 0;
            for(String[] post : postsList) {
            	int user_id = Integer.parseInt(post[0]); // user_id を取得
            	String doneat = post[1];
                String post_content = post[2];
                int likes = Integer.parseInt(post[3]);
                            
                insertstatement.setInt(1, user_id); // user_id を設定
                insertstatement.setString(2, doneat);
                insertstatement.setString(3, post_content);
                insertstatement.setInt(4, likes);                
                sql2 = sql2 + " (" + user_id + ", " + doneat + "," +post_content +"," + likes +")";
                if (postsList.length - 1 != counter) {
                	 sql2 = sql2 + ",";
                	}
                counter++;
                
             // 投稿データを追加
                System.out.println("レコード追加を実行します:" + insertstatement.toString());
            //    rowCnt = insertstatement.executeUpdate();
                System.out.println(rowCnt + "件のレコードが追加されました");
            }
            
            rowCnt = insertstatement.executeUpdate();
            System.out.println(sql2);
            String searchsql = "SELECT posted_at,likes, post_content FROM posts WHERE user_id = 1002;";
            
            //　投稿データを検索
            statement = con.createStatement();
            ResultSet result = statement.executeQuery(searchsql);
          
            
            System.out.println("ユーザーIDが1002のレコードを検索しました");
           
            // SQLクエリの実行結果を抽出
            while(result.next()) {
                Date posted_at = result.getDate("posted_at");
                String post_content = result.getString("post_content");
                int likes = result.getInt("likes");
                System.out.println(result.getRow() + "件目:投稿日時=" + posted_at + "/投稿内容=" + post_content + "/いいね数=" + likes);
            }
                
        } catch (SQLException e) {
            System.out.println("エラー発生:" + e.getMessage());    
            e.printStackTrace();
       
			// 使用したオブジェクトを解放
            
        } finally {
            if (insertstatement != null ) {
                try {insertstatement.close();} catch(SQLException ignore) {}
            }
            if (con != null) {
                try {con.close();}catch(SQLException ignore) {}  
            if( statement != null ) {
            	try { statement.close(); } catch(SQLException ignore) {}
             }
            }
        }
            
    }

}

