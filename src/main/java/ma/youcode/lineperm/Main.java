package ma.youcode.lineperm;

import java.sql.Connection;
import java.sql.SQLException;
import ma.youcode.lineperm.db.DBConnection;

public class Main
{
    public static void main(String[] args)
    {
        // new ConsoleApp().commencer();
        // LogAnalyzerService analyzer = new LogAnalyzerService("src/main/resources/acces.log");

        // System.out.println("Total actions: " + analyzer.nbrActions());
        // System.out.println("Total de refus : " + analyzer.nbrRefus());
        // System.out.println("utilisateur distincts : " + analyzer.utilisateurDistinct());
        // System.out.println("Total d'actions : " + analyzer.utilisateurAction());
        // System.out.println("topFichiers : " + analyzer.topFichiers());
        // System.out.println("topFichiers : " + analyzer.accesRefus("Ayoub"));
        // System.out.println("topFichiers : " + analyzer.plusActif());
        // System.out.println("topFichiers : " + analyzer.actionsType());
        try {
            Connection conn = DBConnection.getInstance().getConnection();
            if (conn != null)
                System.out.println("DB connected successfully");
        } catch (SQLException e) {
            System.out.println("DB connection failed: " + e.getMessage());
        }
    }
}