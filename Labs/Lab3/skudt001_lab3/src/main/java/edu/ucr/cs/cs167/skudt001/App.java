package edu.ucr.cs.cs167.skudt001;


import java.io.IOException;
import java.util.Arrays;
import java.util.function.Function;

public class App
{
    public static void main( String[] args ) throws IOException
    {
//        if (args.length != 3) {
//            System.err.println("Error: At least three parameters expected, from, to, and base.");
//            System.exit(-1);
//        }
//        else {
            //base = num3
            //from = num2
            //to = num1
            int from = Integer.parseInt(args[0]);
//            new IsEven().apply(num1);
            int to = Integer.parseInt(args[1]);
            String[] bases = args[2].split("(v)|(,)");
            Function<Integer, Boolean>[] filters = new Function[bases.length];
            for (int i = 0; i < filters.length; i++) {
                int v = Integer.valueOf(bases[i]);
                filters[i] = new Function<Integer, Boolean>() {
                    @Override
                    public Boolean apply(Integer integer) {
                        return integer % v == 0;
                    }
                };
            }
//        Function<Integer, Boolean> filter;
        Function<Integer, Boolean> divisibleByFive = new Function<Integer, Boolean>() {
            @Override
            public Boolean apply(Integer x) {
                return x % 5 == 0;
            }
        };
//            Function<Integer, Boolean> divisibleByFive = x -> x % 5 == 0;
//        Function<Integer, Boolean> divisibleByTen = x -> x % 10 == 0;

        Function<Integer, Boolean> filterOr = combineWithOr(filters);
        Function<Integer, Boolean> filterAnd = combineWithAnd(filters);
        String parameter = args[2];
        if (parameter.contains("v")) {
            printNumbers(from, to, filterOr);
        }
        else if (parameter.contains(",")) {
            printNumbers(from, to, filterAnd);
        }
        else {
            int base = Integer.parseInt(args[2]);
            Function<Integer, Boolean> divisibleByBase = x -> x % base == 0;
            printNumbers(from, to, divisibleByBase);
        }

//            base = 0;
//            if (base == 2) {
//                filter = new IsEven();
//                printNumbers(from, to, filter);
////                printEvenNumbers(num1,num2);
//            } else if (base == 3){
//                filter = new IsDivisibleByThree();
//                printNumbers(from, to, filter);
//            }
//            else if (base == 5) {
//                divisibleByFive.apply(from);
//                printNumbers(from, to, divisibleByFive);
//            }
//            else if (base == 10) {
//                printNumbers(from, to, divisibleByTen);
//            }
//            if (num3 == 3) {
//
//                printNumbersDivisibleByThree(num1, num2);
//            }
//            printNumbers(num1, num2, filter);
//        }
    }
    public static Function<Integer, Boolean> combineWithAnd(Function<Integer, Boolean> ... filters) {
        Function<Integer, Boolean> and = new Function<Integer, Boolean>() {
            @Override
            public Boolean apply(Integer integer) {
                return Arrays.stream(filters).allMatch(i->i.apply(integer));
            }
        };
        return and;
    }
    public static Function<Integer, Boolean> combineWithOr(Function<Integer, Boolean> ... filters) {
        Function<Integer, Boolean> or = new Function<Integer, Boolean>() {
            @Override
            public Boolean apply(Integer integer) {
                return Arrays.stream(filters).anyMatch(i->i.apply(integer));
            }
        };
        return or;
    }
    public static void printEvenNumbers(int from, int to) {
        System.out.printf("Printing numbers in the range [%d,%d]\n", from, to);
        for (int i=from; i <= to; i++) {
            if (i % 2 == 0) {
                System.out.println(i);
            }
        }
    }
    public static void printNumbersDivisibleByThree(int from, int to) {
        System.out.printf("Printing numbers in the range [%d,%d]\n", from, to);
        for (int i=from; i <= to; i++) {
            if (i % 3 == 0) {
                System.out.println(i);
            }
        }
    }
    static class IsEven implements Function<Integer, Boolean> {
        @Override
        public Boolean apply(Integer x) {
            System.out.println(x % 2 == 0);
            return x % 2 == 0;
        }
    }
    static class IsDivisibleByThree implements Function<Integer, Boolean> {
        @Override
        public Boolean apply(Integer x) {
            System.out.println(x % 3 == 0);
            return x % 3 == 0;
        }
    }

    public static void printNumbers(int from, int to, Function<Integer, Boolean> filter) {
        System.out.printf("Printing numbers in the range [%d,%d]\n", from, to);
        for (int i=from; i <= to; i++) {
            if (filter.apply(i)) {
                System.out.println(i);
            }
        }
    }
}