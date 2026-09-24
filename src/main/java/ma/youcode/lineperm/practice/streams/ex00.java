package ma.youcode.lineperm.practice.streams;

import java.util.ArrayList;
import java.util.List;

public class ex00
{
    public static void main(String [] args)
    {
        List<Integer> numbers = List.of(1,2,3,4,5,6,7,8,9,10);

        // List<Integer> result = numbers.stream().filter(num -> num % 2 == 0).collect(Collectors.toList());
        // System.out.println(result);

        List<Integer> loops = new ArrayList<>();

        int i;
        i = 0;
        while(i < numbers.size())
        {
            Integer num = numbers.get(i);
            if (num % 2 == 0)
                loops.add(num);
            i ++;
        }
        System.out.println(loops);
    }
}
