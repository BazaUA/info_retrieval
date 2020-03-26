import java.util.List;
import java.util.Objects;

public class TableEntry {
    private List<Integer> pointerToList;
    private int pointerToTerm;

    public TableEntry(List<Integer> pointerToList, int pointerToTerm) {
        this.pointerToList = pointerToList;
        this.pointerToTerm = pointerToTerm;
    }

    public List<Integer> getPointerToList() {
        return pointerToList;
    }

    public void setPointerToList(List<Integer> pointerToList) {
        this.pointerToList = pointerToList;
    }

    public int getPointerToTerm() {
        return pointerToTerm;
    }

    public void setPointerToTerm(int pointerToTerm) {
        this.pointerToTerm = pointerToTerm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableEntry that = (TableEntry) o;
        return pointerToTerm == that.pointerToTerm &&
                Objects.equals(pointerToList, that.pointerToList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pointerToList, pointerToTerm);
    }

    @Override
    public String toString() {
        return "TableEntry{" +
                "pointerToList='" + pointerToList + '\'' +
                ", pointerToTerm=" + pointerToTerm +
                '}';
    }
}
