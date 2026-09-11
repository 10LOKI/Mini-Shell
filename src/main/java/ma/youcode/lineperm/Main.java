package ma.youcode.lineperm;
import ma.youcode.lineperm.model.Fichier;
// import ma.youcode.lineperm.ui.ConsoleApp;
import ma.youcode.lineperm.model.User;
import ma.youcode.lineperm.service.FichierService;
public class	Main 
{
    public static void	main(String[] args) {
        // ConsoleApp app = new ConsoleApp();
        // app.commencer();
        User user = new User("ayoub", "1234");
        FichierService service = new FichierService();
        Fichier fichier = service.creer("file1" , user);
        if (fichier != null)
        {
            System.out.println("Fichier crée");
        }
        else
        {
            System.out.println("Fichier non crée");
        }
    }
}
