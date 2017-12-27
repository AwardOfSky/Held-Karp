package karp;

public class PairInteger{
    public int first;
    public int second;

    public PairInteger(int first, int second) {
        super();
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
    
    public boolean isGreater(PairInteger other) {
    	return this.first > other.first || (this.first == other.first && this.second > other.second);
    }
    
    public boolean isLesser(PairInteger other) {
    	return this.first < other.first || (this.first == other.first && this.second < other.second);
    }
    
    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }
    
    @Override
    public String toString() { 
        return "(" + first + ", " + second + ")"; 
    }
}