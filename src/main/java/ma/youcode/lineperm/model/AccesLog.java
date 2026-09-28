package ma.youcode.lineperm.model;

public class    AccesLog
{
    private int         id;
    private int         userId;
    private int         fichierId;
    private String      date;
    private String      heure;
    private String      utilisateur;
    private String      action;
    private String      fichier;
    private String      resultat;

    public AccesLog(int id, int userId, int fichierId, String action, String resultat, java.time.LocalDateTime dateHeure)
    {
        this(dateHeure.toLocalDate().toString(), dateHeure.toLocalTime().toString(), null, action, null, resultat);
        this.id = id;
        this.userId = userId;
        this.fichierId = fichierId;
    }

    public AccesLog(int id, int userId, int fichierId, String utilisateur, String fichier,
                    String action, String resultat, java.time.LocalDateTime dateHeure)
    {
        this(id, userId, fichierId, action, resultat, dateHeure);
        this.utilisateur = utilisateur;
        this.fichier = fichier;
    }

    public int getId()
    {
        return (id);
    }

    public int getUserId()
    {
        return (userId);
    }

    public int getFichierId()
    {
        return (fichierId);
    }

    public java.time.LocalDateTime getDateHeure()
    {
        return java.time.LocalDateTime.of(java.time.LocalDate.parse(date), java.time.LocalTime.parse(heure));
    }

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
