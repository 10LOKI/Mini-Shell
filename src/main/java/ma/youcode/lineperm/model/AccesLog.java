package ma.youcode.lineperm.model;

public class    AccesLog
{
    private String      date;
    private String      heure;
    private String      utilisateur;
    private String      action;
    private String      fichier;
    private String      resultat;

    public      AccesLog(String date ,String heure ,String utilisateur ,String action ,String fichier ,String resultat)
    {
        this.date = date;
        this.heure = heure;
        this.utilisateur = utilisateur;
        this.action = action;
        this.fichier = fichier;
        this.resultat = resultat;
    }
    public String       getDate()
    {
        return (date);
    }
    public String       getHeure()
    {
        return (heure);
    }
    public String       getUtilisateur()
    {
        return (utilisateur);
    }
    public String       getAction()
    {
        return (action);
    }
    public String       getFichier()
    {
        return (fichier);
    }
    public String       getResultat()
    {
        return (resultat);
    }
}