package ma.youcode.lineperm.ui;

import ma.youcode.lineperm.service.UserService;
import ma.youcode.lineperm.service.FichierService;
import ma.youcode.lineperm.service.LogAnalyzerService;
import ma.youcode.lineperm.model.AccesLog;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ConsoleApp
{
	private boolean is_actif;
	private String user_connecte;
	private Scanner scanner = new Scanner(System.in);
	private UserService userservice = new UserService();
	private FichierService fichierservice = new FichierService();
	private Static final String LOG_FILE = "src/main/resources/acces.log";

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
		System.out.println("Commandes : signup  || login  || logout || ls || touch || cat || write || rm || chmod || stats || help  || exit");

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
				case "stats":
					traiterStats();
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
		fichierservice.changerPerm(mots[1], mots[2], droit, valeur, userservice.getCurrentUser());
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
	private void traiterStats()
	{
		LogAnalyzerService analyzer = new LogAnalyzerService("src/main/resources/acces.log");
		boolean statsActif = true;
		while (statsActif)
		{
			System.out.println("=== LogAnalyzer ===");
			System.out.println("1) Nombre total d'actions");
			System.out.println("2) Nombre d'accès refusés");
			System.out.println("3) Utilisateurs distincts");
			System.out.println("4) Actions par utilisateur");
			System.out.println("5) Top 3 des fichiers consultés");
			System.out.println("6) Accès refusés d'un utilisateur");
			System.out.println("7) Utilisateur le plus actif");
			System.out.println("8) Répartition des actions par type");
			System.out.println("0) Quitter");
			System.out.print("Choix : ");
			String choix = scanner.nextLine().trim();
			switch (choix)
			{
				case "1":
					System.out.println("Total actions : " + analyzer.nbrActions());
					break;
				case "2":
					System.out.println("Accès refusés : " + analyzer.nbrRefus());
					break;
				case "3":
					System.out.println("Utilisateurs distincts : " + analyzer.utilisateurDistinct());
					break;
				case "4":
					for (Map.Entry<String, Long> e : analyzer.utilisateurAction().entrySet())
						System.out.println(e.getKey() + " : " + e.getValue());
					break;
				case "5":
					for (Map.Entry<String, Long> e : analyzer.topFichiers())
						System.out.println(e.getKey() + " : " + e.getValue());
					break;
				case "6":
					System.out.print("Utilisateur : ");
					String u = scanner.nextLine().trim();
					List<AccesLog> refus = analyzer.accesRefus(u);
					if (refus.isEmpty())
						System.out.println("Aucun refus.");
					else
						for (AccesLog l : refus)
							System.out.println(l.getDate() + " " + l.getHeure() + " " + l.getAction() + " " + l.getFichier());
					break;
				case "7":
					analyzer.plusActif().ifPresentOrElse(
						e -> System.out.println("Plus actif : " + e.getKey() + " (" + e.getValue() + " actions)"),
						() -> System.out.println("Aucun log.")
					);
					break;
				case "8":
					for (Map.Entry<String, Long> e : analyzer.actionsType().entrySet())
						System.out.println(e.getKey() + " : " + e.getValue());
					break;
				case "0":
					statsActif = false;
					break;
				default:
					System.out.println("Choix invalide.");
			}
		}
	}

}