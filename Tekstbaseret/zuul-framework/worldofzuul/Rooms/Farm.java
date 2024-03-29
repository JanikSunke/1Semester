package worldofzuul.Rooms;

import worldofzuul.PlasticElements.*;
import worldofzuul.Room;

public class Farm extends Room {
    private Plastic[] numberOfPlastic;

    public Farm(String description) {
        super(description);
        super.setPlasticArray(generatePlasticArray());
    }

    public Plastic[] generatePlasticArray() {
        int cleaningPlastics = 3 + (int) (Math.random() * ((5 - 3) + 1));
        this.numberOfPlastic = new Plastic[cleaningPlastics];

        for (int i = 0; i < this.numberOfPlastic.length; i++) {
            this.numberOfPlastic[i] = new CleaningPlastic();
        }

        return this.numberOfPlastic;
    }
}
