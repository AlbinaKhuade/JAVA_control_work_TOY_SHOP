public class Toy {
    int id;
    String name;
    int count;
    Double frequency;

    int countInRaffle;

    public Toy(int id, String name, int count, Double frequency) {
        this.id = id;
        this.name = name;
        this.count = count;
        this.frequency = frequency;
    }

    public String toString() {
        return String.format("id - %d || игрушка - %s || количество - %d || частота выпадения - %.2f", id, name, count, frequency);
    }

    public void printInfo() {
        System.out.println(this);
    }

    public String toStringForRaffle() {
        return String.format("id - %d || игрушка - %s || количество в розыгрыше - %d || частота выпадения - %.2f", id, name, countInRaffle, frequency);
    }

    public void printInfoForRaffle() {
        System.out.printf("id - %d || игрушка - %s || количество в розыгрыше - %d || частота выпадения - %.2f\n", id, name, countInRaffle, frequency);
    }

    public void decreaseToyCount() {
        this.count -= 1;
    }

    public void decreaseToyCountInRaffle() {
        this.countInRaffle -= 1;
    }
    public boolean isEnoughCount(int num) {
        return this.count >= num;
    }

    public boolean isToyExists(int id) {
        return this.id == id;
    }


}
