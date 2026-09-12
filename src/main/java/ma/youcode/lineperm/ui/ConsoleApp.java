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
		System.out.println("Commandes : signup  || login  || logout || ls || touch || cat || write || rm || chmod || help  || exit");

while (!is_actif)
		{
			if (user_connecte == null)
				System.out.print("linperm> ");
			else
				System.out.print(user_connecte + "@linperm> ");

			String ligne = scanner.nextLine().trim();
			if (ligne.isEmpty())
				continue;

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
					if (!verifierConnexion()) break;
					fichierservice.lister();
					break;
				case "touch":
					traiterTouch(mots);
					break;
				case "cat":
					traiterCat(mots);
					break;
				case "write":
					traiterWrite(mots);
					break;
				case "rm":
					traiterRm(mots);
					break;
				case "chmod":
					traiterChmod(mots);
					break;
				case "help":
					traiterHelp();
					break;
				case "exit":
					fichierservice.sauvegarder();
					is_actif = true;
					System.out.println("Au revoir.");
					break;
				default:
					System.out.println("Commande inconnue.");
					break;
			}
		}
	}

	private boolean verifierConnexion()
	{
		if (!userservice.estConnecte())
		{
			System.out.println("Vous devez être connecté.");
			return (false);
		}
		return (true);
	}

	private void traiterSignup()
	{
		System.out.print("Login : ");
		String login = scanner.nextLine().trim();
		System.out.print("Mot de passe : ");
		String password = scanner.nextLine().trim();

		String resultat = userservice.signup(login, password);
		password = null;
		System.out.println(resultat);
	}

	private void traiterLogin()
	{
		System.out.print("Login : ");
		String login = scanner.nextLine().trim();
		System.out.print("Mot de passe : ");
		String password = scanner.nextLine().trim();

		String resultat = userservice.login(login, password);
		password = null;
		if (userservice.estConnecte())
		{
			user_connecte = login;
		}
		System.out.println(resultat);
	}
	
	private void traiterLogout()
	{
		String resultat = userservice.logout();
		System.out.println(resultat);
		user_connecte = null;
	}
private void traiterTouch(String[] mots)
	{
		if (!verifierConnexion()) return;
		if (mots.length != 2)
		{
			System.out.println("Usage: touch <nom_fichier>");
			return;
		}
		fichierservice.creer(mots[1], userservice.getCurrentUser());
		fichierservice.sauvegarder();
	}

	private void traiterCat(String[] mots)
	{
		if (!verifierConnexion()) return;
		if (mots.length != 2)
		{
			System.out.println("Usage: cat <nom_fichier>");
			return;
		}
		String contenu = fichierservice.lire(mots[1], userservice.getCurrentUser());
		if (contenu != null)
			System.out.println(contenu);
	}

	private void traiterWrite(String[] mots)
	{
		if (!verifierConnexion()) return;
		if (mots.length < 3)
		{
			System.out.println("Usage: write <nom_fichier> <contenu>");
			return;
		}
		// join everything after the filename as content
		String contenu = String.join(" ", java.util.Arrays.copyOfRange(mots, 2, mots.length));
		fichierservice.ecrire(mots[1], contenu, userservice.getCurrentUser());
		fichierservice.sauvegarder();
	}

	private void traiterRm(String[] mots)
	{
		if (!verifierConnexion()) return;
		if (mots.length != 2)
		{
			System.out.println("Usage: rm <nom_fichier>");
			return;
		}
		fichierservice.supprimer(mots[1], userservice.getCurrentUser());
		fichierservice.sauvegarder();
	}

	private void traiterChmod(String[] mots)
	{
		if (!verifierConnexion()) return;
		if (mots.length != 5)
		{
			System.out.println("Usage: chmod <fichier> <prop|other> <r|w|d> <true|false>");
			return;
		}
		char droit = mots[3].charAt(0);
		boolean valeur = Boolean.parseBoolean(mots[4]);
		fichierservice.changer_perm(mots[1], mots[2], droit, valeur, userservice.getCurrentUser());
		fichierservice.sauvegarder();
	}

	private void traiterHelp()
	{
		System.out.println("signup            — créer un compte");
		System.out.println("login             — se connecter");
		System.out.println("logout            — se déconnecter");
		System.out.println("ls                — lister les fichiers");
		System.out.println("touch <fichier>   — créer un fichier");
		System.out.println("cat <fichier>     — lire un fichier");
		System.out.println("write <fichier> <contenu> — écrire dans un fichier");
		System.out.println("rm <fichier>      — supprimer un fichier");
		System.out.println("chmod <fichier> <prop|other> <r|w|d> <true|false> — changer les droits");
		System.out.println("exit              — quitter");
	}
}