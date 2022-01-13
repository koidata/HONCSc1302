public class Test {
    public static void main(String[] args) {
        myPrint(5);
    }

    public static void myPrint (int n) {
        if (n>0)
        {
            fancyPrint (n+2);
            System.out.println();
            myPrint(n-1);
            fancyPrint(n);
            System.out.println();
        }
    }
    public static void fancyPrint (int n) {
        if (n==1) {
            System.out.print("*");
        }
        else {
            System.out.print(n); 
            fancyPrint(n-1);
        }
    }
}
