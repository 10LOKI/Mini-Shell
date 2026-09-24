package ma.youcode.lineperm;

import ma.youcode.lineperm.ui.ConsoleApp;

public class Main
{
    public static void main(String[] args)
    {
        new ConsoleApp().commencer();
        // LogAnalyzerService analyzer = new LogAnalyzerService("src/main/resources/acces.log");


        // ======> Stats Tests
        // System.out.println("Total actions: " + analyzer.nbrActions());
        // System.out.println("Total de refus : " + analyzer.nbrRefus());
        // System.out.println("utilisateur distincts : " + analyzer.utilisateurDistinct());
        // System.out.println("Total d'actions : " + analyzer.utilisateurAction());
        // System.out.println("topFichiers : " + analyzer.topFichiers());
        // System.out.println("topFichiers : " + analyzer.accesRefus("Ayoub"));
        // System.out.println("topFichiers : " + analyzer.plusActif());
        // System.out.println("topFichiers : " + analyzer.actionsType());

        // ===========> Database Tests
        // try {
        //     Connection conn = DBConnection.getInstance().getConnection();
        //     if (conn != null)
        //         System.out.println("DB connected successfully");
        // } catch (SQLException e) {
        //     System.out.println("DB connection failed: " + e.getMessage());
        // }


        // UserDao userdao = new UserDao();
        // User user2 = new User("oayoub","$2a$12$eImiTXuWVxfM37test_hash");

        // System.out.println("Saving user in the database :");
        // userdao.save(user2);
        // System.out.println("Execution completed.");
    }
}