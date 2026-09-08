package ma.youcode.lineperm.service;
import ma.youcode.lineperm.model.User;

public class UserService
{
    Path path = Path.of("src/main/resources/users.txt");

    private Map<String,User> users = new HashMap<>();
    public void charger(String filePath)
    {
        try (Stream<String> lines = Files.lines(Path.of(filePath)))
        {
            // hna gha ndir dak lprocess
        }
    }
}