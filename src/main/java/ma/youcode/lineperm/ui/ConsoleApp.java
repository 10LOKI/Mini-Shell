package ma.youcode.lineperm.ui;

import ma.youcode.lineperm.service.UserService;
import java.util.Scanner;
public class	ConsoleApp
{
	private boolean	is_actif;
	private	String	user_connecte;
	private Scanner scanner = new Scanner(System.in);

	private	UserService userservice = new UserService();
	

	public	ConsoleApp()
	{
		this.is_actif = false;
		this.user_connecte = null;
	}

	public	void	commencer()
	{
		System.out.println("************* LinePerm Console *********    *");
		System.out.println("------------- Start ------------");
		while (!is_actif)
		{
			System.out.println("Utilisateur non connecté || 1. signup || 2. Login || 3.help || 4.exit");
			break;
		}
	}

	public void		lireligne()
	{}
	public void		output()
	{}


}
