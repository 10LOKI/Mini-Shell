package ma.youcode.lineperm.ui;

import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.sql.*;
import java.util.concurrent.TimeUnit;

public class ConsolePersistenceTest
{
    private static String run(Path root, String input) throws Exception
    {
        Path output = Files.createTempFile(root, "console-", ".txt");
        Process process = new ProcessBuilder(
            Path.of(System.getProperty("java.home"), "bin", "java").toString(),
            "-Dfile.encoding=UTF-8", "-Dlineperm.db=" + root.resolve("audit.db"),
            "-Dlineperm.data=" + root.resolve("data"),
            "-cp", System.getProperty("java.class.path"), "ma.youcode.lineperm.Main")
            .redirectErrorStream(true).redirectOutput(output.toFile()).start();
        try (var stdin = process.getOutputStream())
        {
            stdin.write(input.getBytes(StandardCharsets.UTF_8));
        }
        if (!process.waitFor(30, TimeUnit.SECONDS))
        {
            process.destroyForcibly();
            throw new AssertionError("Console timeout");
        }
        String result = Files.readString(output);
        if (process.exitValue() != 0 || result.contains("Erreur :") || result.contains("Exception"))
            throw new AssertionError(result);
        return result;
    }

    private static int count(Connection c, String sql) throws SQLException
    {
        try (Statement s = c.createStatement(); ResultSet r = s.executeQuery(sql))
        {
            return r.next() ? r.getInt(1) : 0;
        }
    }

    public static void main(String[] args) throws Exception
    {
        Path root = Files.createTempDirectory("lineperm-console-test-");
        String first = run(root, String.join("\n", "signup", "alice", "secret", "login", "alice", "secret",
            "touch note.txt", "write note.txt hello database", "chmod note.txt other r true", "cat note.txt", "exit", ""));
        assert first.contains("Bienvenue alice") && first.contains("hello database") : first;
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:" + root.resolve("audit.db")))
        {
            assert count(c, "select count(*) from users") == 1;
            assert count(c, "select count(*) from files where droits = 'rwd|r--'") == 1;
            assert count(c, "select count(*) from logs") == 4;
        }
        String second = run(root, String.join("\n", "signup", "bob", "secret", "login", "bob", "wrong",
            "login", "bob", "secret", "ls", "cat note.txt", "write note.txt denied", "rm note.txt",
            "chmod note.txt prop r false", "cat absent.txt", "logout", "login", "alice", "secret",
            "chmod note.txt other r nonsense", "rm note.txt", "stats", "1", "2", "3", "4", "5", "6", "bob", "7", "8", "0", "exit", ""));
        assert second.contains("hello database") && second.contains("permission denied") : second;
        assert second.contains("Total actions : 10") : second;
        assert !Files.exists(root.resolve("data/note.txt"));
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:" + root.resolve("audit.db")))
        {
            assert count(c, "select count(*) from users") == 2;
            assert count(c, "select count(*) from files") == 0;
            assert count(c, "select count(*) from logs") == 10;
            assert count(c, "select count(*) from logs where resultat = 'REFUSE'") == 4;
            assert count(c, "select count(*) from logs where fichier_nom = 'absent.txt'") == 1;
            assert count(c, "select count(*) from logs where action = 'SUPPRESSION' and resultat = 'OK'") == 1;
            assert count(c, "select count(*) from logs where file_id is not null") == 0;
        }
        String third = run(root, "login\nalice\nsecret\ntouch note.txt\ncat note.txt\nexit\n");
        assert third.contains("Bienvenue alice");
        assert Files.readString(root.resolve("data/note.txt")).isEmpty();

        // Upgrade an existing database and preserve its audit history and IDs.
        Path legacy = Files.createTempDirectory("lineperm-migration-test-");
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:" + legacy.resolve("audit.db")); Statement s = c.createStatement())
        {
            s.execute("create table users (id integer primary key autoincrement, login text not null unique, password text not null)");
            s.execute("create table files (id integer primary key autoincrement, nom text not null, droits text not null, user_id integer not null references users(id))");
            s.execute("create table logs (id integer primary key autoincrement, user_id integer not null references users(id), file_id integer not null references files(id), action text not null, resultat text not null, quand text not null)");
            s.execute("insert into users values (1, 'legacy', 'hash')");
            s.execute("insert into files values (1, 'old.txt', 'rwd|---', 1)");
            s.execute("insert into logs values (42, 1, 1, 'LECTURE', 'OK', '2026-09-28 10:30:00')");
        }
        String migrated = run(legacy, "stats\n1\n5\n0\nexit\n");
        assert migrated.contains("Total actions : 1") && migrated.contains("old.txt") : migrated;
        run(legacy, "stats\n1\n0\nexit\n");
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:" + legacy.resolve("audit.db")); Statement s = c.createStatement())
        {
            assert count(c, "select count(*) from logs where id = 42 and fichier_nom = 'old.txt'") == 1;
            s.execute("pragma foreign_keys=on");
            s.execute("delete from files where id = 1");
            assert count(c, "select count(*) from logs where id = 42 and file_id is null") == 1;
        }
        System.out.println("PASS: console persistence across restarts, permissions, audit, stats, recreation and legacy schema migration.");
    }
}
