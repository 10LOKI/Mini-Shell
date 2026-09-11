package ma.youcode.lineperm.model;

public class    Fichier
{
    private String  nom;
    private String  proprietaire;
    private boolean read_prop;
    private boolean write_prop;
    private boolean delete_prop;
    private boolean read_other;
    private boolean write_other;
    private boolean delete_other;

    public  Fichier(String nom , String proprietaire)
    {
        this(nom, proprietaire, true, true, true, false, false, false);

    }
    public  Fichier(String nom , String proprietaire , boolean read_prop , boolean write_prop , boolean delete_prop , boolean read_other , boolean write_other , boolean delete_other)
    {
        this.nom = nom;
        this.proprietaire = proprietaire;
        this.read_prop = read_prop;
        this.write_prop = write_prop;
        this.delete_prop = delete_prop;
        this.read_other = read_other;
        this.write_other = write_other;
        this.delete_other = delete_other;
    }

    public String   get_nom()
    {
        return (nom);
    }
    public  void    set_nom(String nom)
    {
        this.nom = nom;
    }
    public String   get_proprietaire()
    {
        return (proprietaire);
    }
    public void set_proprietaire(String proprietaire)
    {
        this.proprietaire = proprietaire;
    }
    public boolean  isRead_prop()
    {
        return (read_prop);
    }
    public void setRead_prop(boolean read_prop)
    {
        this.read_prop = read_prop;
    }
    public boolean  isWrite_prop()
    {
        return (write_prop);
    }
    public void setWrite_prop(boolean write_prop)
    {
        this.write_prop = write_prop;
    }
    public boolean  isDelete_prop()
    {
        return (delete_prop);
    }
    public void setDelete_prop(boolean delete_prop)
    {
        this.delete_prop = delete_prop;
    }
        public boolean  isRead_other()
    {
        return (read_other);
    }
    public void setRead_other(boolean read_other)
    {
        this.read_other = read_other;
    }
    public boolean  isWrite_other()
    {
        return (write_other);
    }
    public void setWrite_other(boolean write_other)
    {
        this.write_other = write_other;
    }
    public boolean  isDelete_other()
    {
        return (delete_other);
    }
    public void setDelete_other(boolean delete_other)
    {
        this.delete_other = delete_other;
    }
}