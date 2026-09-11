package ma.youcode.lineperm.ui;

import ma.youcode.lineperm.service.UserService;
import ma.youcode.lineperm.service.FichierService;
import java.util.Scanner;

public class ConsoleApp
{
	private boolean is_actif;
	private String user_connecte;
	private Scanner scanner = new Scanner(System.in);
	private UserService userservice = new UserService();
	private FichierService fichierservice = new FichierService();

	public ConsoleApp()
	{
		this.is_actif = false;
		this.user_connecte = null;
	}

	public void commencer()
	{
		System.out.println("╔══════════════════════════════════╗");
		System.out.println("║   LinePerm — by Ayoub Ouharda     ║");
		System.out.println("╚══════════════════════════════════╝");
		System.out.println("------------- Start ------------");
		System.out.println("Commandes : signup  || login  || help  || exit");

		while (!is_actif)
		{
			if (user_connecte == null)
			{
				System.out.print("linperm> ");
			}
			else
			{
				System.out.print(user_connecte + "@linperm> ");
			}

			String ligne = scanner.nextLine();
			ligne = ligne.trim();

			if (ligne.isEmpty())
			{
				continue;
			}

			String[] mots = ligne.split(" ");
			String commande = mots[0];

			switch (commande)
			{
				case "signup":
					traiterSignup();
					break;

				case "login":
					traiterLogin();
					break;

				case "logout":
					traiterLogout();
					break;
				case "ls":
					fichierservice.lister();
					break;
				case "cat":
					fichierservice.lire();
					break;
				case "exit":
					is_actif = true;
					System.out.println("Au revoir.");
					break;

				default:
					System.out.println("Commande inconnue.");
					break;
			}
		}
	}

	private void traiterSignup()
	{
		System.out.print("Login : ");
		String login = scanner.nextLine().trim();
		System.out.print("Mot de passe : ");
		String password = scanner.nextLine().trim();

		String resultat = userservice.signup(login, password);
		System.out.println(resultat);
	}

	private void traiterLogin()
	{
		System.out.print("Login : ");
		String login = scanner.nextLine().trim();
		System.out.print("Mot de passe : ");
		String password = scanner.nextLine().trim();

		String resultat = userservice.login(login, password);
		System.out.println(resultat);

		if (userservice.estConnecte())
		{
			user_connecte = login;
		}
	}
	
	private void traiterLogout()
	{
		String resultat = userservice.logout();
		System.out.println(resultat);
		user_connecte = null;
	}
	// New branch auth
	// private void	traiterCat()
	// {
	// 	System.out.println("Nom du fichier :");
	// 	String nom = scanner.nextLine().trim();
	// 	String contenu = fichierservice.lire(nom,userservice.getCurrentUser());
	// 	if (contenu != null) System.out.println(contenu);
	// }
}