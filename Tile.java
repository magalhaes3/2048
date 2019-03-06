class Tile {

    private int value;
    private boolean merged;

    Tile(){
        this.value = 0;
        this.merged = false;
    }

    int getValue() {
        return value;
    }

    void setValue(int value) {
        this.value = value;
    }

    boolean isMerged() {
        return merged;
    }

    void setMerged(boolean merged) {
        this.merged = merged;
    }
}
