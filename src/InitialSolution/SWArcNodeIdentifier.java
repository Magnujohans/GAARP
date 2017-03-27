package InitialSolution;
/**
 * Created by Magnu on 06.12.2016.
 */

public final class SWArcNodeIdentifier {

    private final Integer start, end;

    public SWArcNodeIdentifier(Integer start, Integer end) {
        this.start = start;
        this.end = end;
    }

    public Integer getStart() {
        return start;
    }

    public Integer getEnd() {
        return end;
    }

    @Override
    public int hashCode() {
        return start.hashCode() ^ end.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof SWArcNodeIdentifier) && ((SWArcNodeIdentifier) obj).getStart() == this.start
                && ((SWArcNodeIdentifier) obj).getEnd() == this.end;
    }
}