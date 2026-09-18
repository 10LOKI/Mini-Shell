package ma.youcode.lineperm.model;

public class Fichier
{
    private String  nom;
    private String  proprietaire;
    private boolean readProp;
    private boolean writeProp;
    private boolean deleteProp;
    private boolean readOther;
    private boolean writeOther;
    private boolean deleteOther;

    public Fichier(String nom, String proprietaire)
    {
        this(nom, proprietaire, true, true, true, false, false, false);
    }

    public Fichier(String nom, String proprietaire, boolean readProp, boolean writeProp, boolean deleteProp, boolean readOther, boolean writeOther, boolean deleteOther)
    {
        this.nom = nom;
        this.proprietaire = proprietaire;
        this.readProp = readProp;
        this.writeProp = writeProp;
        this.deleteProp = deleteProp;
        this.readOther = readOther;
        this.writeOther = writeOther;
        this.deleteOther = deleteOther;
    }

    public String getNom()
    {
        return (nom);
    }
    public void setNom(String nom)
    {
        this.nom = nom;
    }
    public String getProprietaire()
    {
        return (proprietaire);
    }
    public void setProprietaire(String proprietaire)
    {
        this.proprietaire = proprietaire;
    }
    public boolean isReadProp()
    {
        return (readProp);
    }
    public void setReadProp(boolean readProp)
    {
        this.readProp = readProp;
    }
    public boolean isWriteProp()
    {
        return (writeProp);
    }
    public void setWriteProp(boolean writeProp)
    {
        this.writeProp = writeProp;
    }
    public boolean isDeleteProp()
    {
        return (deleteProp);
    }
    public void setDeleteProp(boolean deleteProp)
    {
        this.deleteProp = deleteProp;
    }
    public boolean isReadOther()
    {
        return (readOther);
    }
    public void setReadOther(boolean readOther)
    {
        this.readOther = readOther;
    }
    public boolean isWriteOther()
    {
        return (writeOther);
    }
    public void setWriteOther(boolean writeOther)
    {
        this.writeOther = writeOther;
    }
    public boolean isDeleteOther()
    {
        return (deleteOther);
    }
    public void setDeleteOther(boolean deleteOther)
    {
        this.deleteOther = deleteOther;
    }

}