public class Triangle {
    private int a, b, c;

    public Triangle(int a, int b, int c){
        if ( ((a + b) > c ) && ((b + c) > a ) && ((a + c ) > b)) {
            this.a = a;
            this.b = b;
            this.c = c;
        }
        else{
            throw new ArithmeticException(
                    String.format("The triangle cannot be constructed from the numbers %d, %d, %d.", a, b, c));
        }
    }

    public boolean isIsosceles(){
        return (a == b) || (b == c) || (a == c);
    }

    public boolean isEquilateral(){
        return (a == b) && (b == c);
    }

    // 4. feladat: Derékszögű-e? (Pitagorasz-tétel, bármelyik oldal lehet az átfogó)
    public boolean isRightAngled(){
        return (a * a + b * b == c * c) || 
               (a * a + c * c == b * b) || 
               (b * b + c * c == a * a);
    }

    public int getPerimeter(){
        return a + b + c;
    }

    // 4. feladat: Terület számítása Hérón-képlettel
    public double getArea(){
        double s = getPerimeter() / 2.0;
        return Math.sqrt(s * (s - a) * (s - b) * (s - c));
    }
}