package sample.presentation;

public class BrokeMachineAnimation implements Animation {
    private int count = 0;
    private int widthNumPictures = 3;
    private int heightNumPictures = 2;
    private final double picWidth = 266.66;
    private final double picHeight = 320;

    @Override
    public double[] changePic() {
        double whichPicWidth = (count % 3) * picWidth;;
        double whichPicHeight = 0;
        double whichPic = count % 6;
        if (whichPic >= 3) {
            whichPicHeight = picHeight;
        }
        count++;
        double[] numbers = {whichPicWidth, whichPicHeight, picWidth, picHeight};
        return numbers;
    }
}
