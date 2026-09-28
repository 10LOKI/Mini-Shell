package ma.youcode.lineperm.dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Map;
import ma.youcode.lineperm.model.AccesLog;

// Run with assertions enabled (-ea) and the SQLite JDBC JAR on the classpath.
public class LogDaoTest
{
    public static void main(String[] args) throws Exception
    {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite::memory:"))
        {
            try (Statement stmt = connection.createStatement())
            {
                stmt.execute("create table users (id integer primary key, login text unique, password text)");
                stmt.execute("create table files (id integer primary key, nom text, droits text, user_id integer references users(id))");
                stmt.execute("create table logs (id integer primary key, user_id integer not null references users(id), file_id integer references files(id) on delete set null, action text not null, resultat text not null, quand text not null, fichier_nom text not null)");
                stmt.execute("insert into users values (1, 'alice', 'hash'), (2, 'bob', 'hash')");
                stmt.execute("insert into files values (1, 'a.txt', 'rwd|---', 1), (2, 'b.txt', 'rwd|---', 2)");
            }
            LogDao dao = new LogDao()
            {
                @Override
                protected Connection getConnection()
                {
                    return connection;
                }
            };
            assert dao.compterTotal() == 0;
            assert dao.compterRefuses() == 0;
            assert dao.userDistincts() == 0;
            assert dao.actionsByUser().isEmpty();
            assert dao.topFichiers(3).isEmpty();
            assert dao.findById(99) == null;
            LocalDateTime date = LocalDateTime.of(2026, 9, 28, 12, 34, 56, 123000000);
            dao.save(new AccesLog(0, 1, 1, "LECTURE", "OK", date));
            dao.save(new AccesLog(0, 1, 1, "ECRITURE", "REFUSE", date));
            dao.save(new AccesLog(0, 2, 2, "LECTURE", "REFUSE", date));
            AccesLog found = dao.findById(1);
            assert found.getId() == 1 && found.getUserId() == 1 && found.getFichierId() == 1;
            assert found.getUtilisateur().equals("alice") && found.getFichier().equals("a.txt");
            assert found.getAction().equals("LECTURE") && found.getResultat().equals("OK");
            assert found.getDateHeure().equals(date);
            assert dao.compterTotal() == 3 && dao.compterRefuses() == 2 && dao.userDistincts() == 2;
            assert dao.actionsByUser().equals(Map.of("alice", 2, "bob", 1));
            assert dao.topFichiers(1).equals(Map.of("a.txt", 2));
            assert dao.topFichiers(0).isEmpty();
            assert dao.refusesByUser("alice") == 1;
            assert dao.refusesByUser("missing") == 0;
            assert dao.refusesByUser("' OR 1=1 --") == 0;
            try { dao.topFichiers(-1); throw new AssertionError("Negative limit accepted"); }
            catch (IllegalArgumentException expected) { }
            try { dao.delete(1); throw new AssertionError("Deletion accepted"); }
            catch (UnsupportedOperationException expected) { }
            // Invalid references must be rejected even with SQLite foreign keys disabled.
            for (int[] ids : new int[][] {{99, 1}, {1, 99}})
            {
                try { dao.save(new AccesLog(0, ids[0], ids[1], "LECTURE", "OK", date)); throw new AssertionError("Invalid reference accepted"); }
                catch (IllegalArgumentException expected) { }
            }
            assert dao.compterTotal() == 3;
            AccesLog legacy = new AccesLog("2026-09-28", "12:34", "alice", "LECTURE", "a.txt", "OK");
            assert legacy.getDate().equals("2026-09-28") && legacy.getHeure().equals("12:34");
            assert legacy.getUtilisateur().equals("alice");
            System.out.println("PASS: LogDao CRUD, audit queries, timestamps, invalid references and legacy model.");
        }
    }
}
