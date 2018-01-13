package karp;

public class PairInteger{
    protected int first;
    protected int second;

    public PairInteger(int first, int second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public int hashCode() {
        return 31 * first + second;	
    }
    
    @Override
    public boolean equals(Object other) {
        if (other instanceof PairInteger) {
            PairInteger otherPair = (PairInteger) other;
            return this.first == otherPair.first && this.second == otherPair.second;
        }
        return false;
    }

    @Override
    public String toString() { 
        return "(" + first + ", " + second + ")"; 
    }
}