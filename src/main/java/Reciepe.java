import java.util.List;

/**
 * Created by David on 04/03/2017.
 */
public class Reciepe {
    private final String name;
    private final String image;
    private final List<String> steps;

    public Reciepe(String name, String image, List<String> steps) {

        this.name = name;
        this.image = image;
        this.steps = steps;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public List<String> getSteps() {
        return steps;
    }
}

