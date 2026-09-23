package ma.youcode.lineperm.dao;

public interface Dao<T> {
    void    save(T t);
    T       findById(int id);
    void    delete(int id);
}